package com.ed.demo.i18ndemo.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.ed.demo.i18ndemo.entity.LanguageText;
import com.ed.demo.i18ndemo.entity.LanguageTextExcelTo;
import com.ed.demo.i18ndemo.enums.LanguageEnum;
import com.ed.demo.i18ndemo.listener.LanguageTextListener;
import com.ed.demo.i18ndemo.mapper.LanguageTextMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author ed
 * @date 2022/7/27
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Controller
@RequestMapping(value = "/i18n")
public class I18NController {

    @Autowired
    private LanguageTextMapper languageTextMapper;

    @ResponseBody
    @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST)
    public void uploadExcel(@RequestPart MultipartFile file) throws IOException {
        InputStream fileInputStream = file.getInputStream();
//        File file = new File("src/main/resources/static/i18n_demo.xlsx");
        EasyExcel.read(fileInputStream, LanguageTextExcelTo.class, new LanguageTextListener(languageTextMapper)).sheet().doRead();
    }

    @ResponseBody
    @RequestMapping(value = "/getLangs", method = RequestMethod.GET)
    public JSONObject getLangs() {
        JSONObject responseJson = new JSONObject();
        List<LanguageText> languageTextList = languageTextMapper.selectList(null);
        if (CollectionUtils.isNotEmpty(languageTextList)) {
            // 遍历已支持的语种，封装语言文本
            List<LanguageEnum> languageEnumList = Arrays.asList(LanguageEnum.values());
            for (LanguageEnum languageEnum : languageEnumList) {
                JSONObject langJsonObject = new JSONObject();
                responseJson.put(languageEnum.getDesc(), langJsonObject);
            }
            languageTextList.forEach(languageText -> {
                JSONObject langsJsonObject = JSONObject.parseObject(languageText.getLangs());
                for (LanguageEnum languageEnum : languageEnumList) {
                    JSONObject langJsonObject = responseJson.getJSONObject(languageEnum.getDesc());
                    langJsonObject.put(languageText.getKey(), langsJsonObject.getString(languageEnum.getDesc()));
                }
            });
        }
        return responseJson;
    }

}
