package com.example.spring_project.infrastructure.rdb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.spring_project.domain.entity.Color;

@Mapper
public interface ColorMapper {
    public List<Color> selectByEmail(@Param("user_email") String email);

    public void upsertColorList(@Param("color_list") List<Color> colorList);

    public void deleteColorList(@Param("color_kist") List<Color> colorList);
}
