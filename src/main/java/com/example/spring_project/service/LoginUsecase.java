package com.example.spring_project.service;

import com.example.spring_project.common.methods.TimeCalculator;
import com.example.spring_project.domain.entity.User;
import com.example.spring_project.domain.repository.GoogleOauthRepository;
import com.example.spring_project.domain.repository.GoogleRepository;
import com.example.spring_project.domain.repository.SessionRepository;
import com.example.spring_project.domain.repository.UserRepository;
import com.example.spring_project.infrastructure.googleApi.response.GoogleGetUserInfoResponse;
import com.example.spring_project.infrastructure.googleApi.response.GoogleOauthResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

  public String login(String authCode)
    throws UnsupportedEncodingException {
      // アクセストークン、リフレッシュトークン、有効期限を取得する

      GoogleOauthResponse googleOauthResponse = googleOauthRepository.GetAccessToken(authCode);
      String accessToken = googleOauthResponse.getAccessToken();
      String refreshToken = googleOauthResponse.getRefreshToken();
      String expiresIn = googleOauthResponse.getExpiresIn();
      String expires = TimeCalculator.getTimeAfterSeconds(expiresIn);

      // TODO : responseの型定義を行う。
      // ジャンプしないとわからないのは不親切。

      // ユーザー情報の取得
      GoogleGetUserInfoResponse userInfo =  googleRepository.GetUserInfo(accessToken);
      String email = userInfo.getEmail();
      email = email.replaceAll("\"", "");
      String name = userInfo.getName();
      name = name.replaceAll("\"", "");
      ArrayList<User> user = userRepository.SelectByEmail(email);
      // DBになければ登録
      if (user.isEmpty()) {
        user.add(new User(email, name));
        userRepository.RegisterUser(user.get(0));
      }

      // redisにユーザー情報、セッション情報を登録する。
      String sessionId = sessionRepository.GenerateSession(
        user.get(0),
        accessToken,
        refreshToken,
        expires
      );
      return sessionId;
    }
}
