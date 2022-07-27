package com.ed.demo.i18ndemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ed.demo.i18ndemo.entity.LanguageText;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LanguageTextMapper extends BaseMapper<LanguageText> {
    void batchInsert(@Param("list") List<LanguageText> insertCachedDataList);

    void batchUpdate(@Param("list") List<LanguageText> updateCachedDataList);
}
