package com.example.spring_project.service;

import com.example.spring_project.common.methods.TimeCalculator;
import com.example.spring_project.domain.entity.Color;
import com.example.spring_project.domain.entity.Project;
import com.example.spring_project.domain.entity.User;
import com.example.spring_project.domain.repository.ColorRepository;
import com.example.spring_project.domain.repository.GoogleCalendarCalendarRepository;
import com.example.spring_project.domain.repository.GoogleCalendarColorsRepository;
import com.example.spring_project.domain.repository.GoogleCalendarGetCalendarListRepository;
import com.example.spring_project.domain.repository.GoogleOauthRepository;
import com.example.spring_project.domain.repository.GoogleRepository;
import com.example.spring_project.domain.repository.ProjectRepository;
import com.example.spring_project.domain.repository.SessionRepository;
import com.example.spring_project.domain.repository.UserRepository;
import com.example.spring_project.infrastructure.googleApi.request.GoogleCalendarAddCalendarRequest;
import com.example.spring_project.infrastructure.googleApi.response.GoogleGetUserInfoResponse;
import com.example.spring_project.infrastructure.googleApi.response.GoogleOauthResponse;
import com.example.spring_project.infrastructure.rdb.mapper.ProjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginUsecase {

  // login処理を行う。
  // アクセストークンをもとにユーザー情報(name,email)を取得する。
  // DBに同じemailを持つユーザーがいるかを確認する。
  // いなければユーザー登録を行う。いればユーザー登録はスキップする。
  // sessionID(ulid)を作成する。
  // sessionIDをキー、ユーザー情報とアクセストークン、リフレッシュトークン、アクセストークンの有効期限の情報をredisに登録する。
  // 最後にsessionIDを返す。
  // 基本的にセッション管理は共通処理部分で行う。例外として、ログイン処理を行う際はその共通処理をスキップする。
  // 共通処理で行うセッション管理は、sessionIDをキーとしてredisからアクセストークン、リフレッシュトークン、有効期限を受け取る。
  // 現在時刻が有効期限を過ぎていればリフレッシュする。過ぎていなければリフレッシュをスキップする。
  // 最終的にアクセストークンを返す。
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private SessionRepository sessionRepository;
  @Autowired
  private GoogleOauthRepository googleOauthRepository;
  @Autowired
  private GoogleRepository googleRepository;
  @Autowired
  private GoogleCalendarGetCalendarListRepository googleCalendarGetCalendarListRepository;
  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private GoogleCalendarColorsRepository googleCalendarColorsRepository;
  @Autowired
  private ColorRepository colorRepository;
  @Autowired
  private ProjectMapper projectMapper;
  @Autowired
  private GoogleCalendarCalendarRepository googleCalendarCalendarRepository;
  private static final String GENERAL = "General";

  public String login(String authCode)
      throws UnsupportedEncodingException {
    System.out.println("login");
    // アクセストークン、リフレッシュトークン、有効期限を取得する
    GoogleOauthResponse googleOauthResponse = googleOauthRepository.GetAccessToken(authCode);
    String accessToken = googleOauthResponse.getAccessToken();
    String refreshToken = googleOauthResponse.getRefreshToken();
    String expiresIn = googleOauthResponse.getExpiresIn();
    String expires = TimeCalculator.getTimeAfterSeconds(expiresIn);

    // ユーザー情報の取得
    GoogleGetUserInfoResponse userInfo = googleRepository.GetUserInfo(accessToken);
    String email = userInfo.getEmail().replaceAll("\"", "");
    String name = userInfo.getName();
    name = name.replaceAll("\"", "");
    ArrayList<User> user = userRepository.SelectByEmail(email);
    log.info("user");
    log.info(user.toString());
    List<Project> calendarList = googleCalendarGetCalendarListRepository.getCalendarList(email, accessToken);
    List<Project> mainCalendarList = calendarList
        .stream()
        .filter(item -> item.getId().equals(email))
        .toList();
    var mainCalendar = new Project("", "", "", "", email, null, null);
    if (!mainCalendarList.isEmpty()) {
      mainCalendar = mainCalendarList.get(0);
    }
    List<Color> googleCalendarColorList = googleCalendarColorsRepository.getGoogleCalendarColors(email, accessToken);
    log.info("mainCalendar");
    log.info(mainCalendar.toString());
    log.info("googleCalendarColorList");
    log.info(googleCalendarColorList.toString());
    // DBになければ登録
    if (user.isEmpty()) {
      log.info("add new user");
      User newUser = new User(email, name);
      user.add(newUser);
      userRepository.RegisterUser(newUser);
      try {
        log.info("upsertColorList");
        colorRepository.upsertColorList(googleCalendarColorList);
      } catch (Exception error) {
        log.error(error.toString());
      }
      try {
        if (!mainCalendar.getId().equals("")) {
          log.info("insert project");
          projectMapper.insertProject(mainCalendar.getId(), GENERAL, mainCalendar.getColor_id(), mainCalendar.getMemo(),
              email);
        }
      } catch (Exception error) {
        log.error(error.toString());
      }
    } else {
      // TODO : colorsテーブルの更新、projectテーブルの更新を行うように書き換える。
      log.info("projectBeforeUpdate");
      Project projectBeforeUpdate = projectRepository.selectByNameAndEmail(GENERAL, email);
      projectRepository.updateProject(GENERAL, GENERAL, mainCalendar.getColor_id(), projectBeforeUpdate.getMemo(),
          email);
    }
    googleCalendarCalendarRepository.addNewCalendar(email, accessToken, "あたらしいかれんだー1", "めも");
    // redisにユーザー情報、セッション情報を登録する。
    String sessionId = sessionRepository.GenerateSession(
        user.get(0),
        accessToken,
        refreshToken,
        expires);
    return sessionId;
  }
}
