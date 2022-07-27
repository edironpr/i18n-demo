package com.ed.demo.i18ndemo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ed.demo.i18ndemo.entity.LanguageText;
import com.ed.demo.i18ndemo.entity.LanguageTextExcelTo;
import com.ed.demo.i18ndemo.enums.LanguageEnum;
import com.ed.demo.i18ndemo.enums.LanguageTextTypeEnum;
import com.ed.demo.i18ndemo.mapper.LanguageTextMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author ed
 * @date 2022/7/27
 */
public class LanguageTextListener implements ReadListener<LanguageTextExcelTo> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<LanguageText> insertCachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    private List<LanguageText> updateCachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private final LanguageTextMapper languageTextMapper;

    private String secondLang;

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     */
    public LanguageTextListener(LanguageTextMapper languageTextMapper) {
        this.languageTextMapper = languageTextMapper;
    }

    /**
     * 这里会一行行的返回头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        System.out.println("解析到一条头数据: " + JSON.toJSONString(headMap));
        // 如果想转成成 Map<Integer,String>
        // 方案1： 不要implements ReadListener 而是 extends AnalysisEventListener
        // 方案2： 调用 ConverterUtils.convertToStringMap(headMap, context) 自动会转换

        // 获取到第二语言
        String langValue = headMap.get(2).getStringValue();
        secondLang = LanguageEnum.parseValue(langValue).getDesc();

    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param languageTextExcelTo    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(LanguageTextExcelTo languageTextExcelTo, AnalysisContext context) {
        System.out.println("解析到一条数据: " + JSON.toJSONString(languageTextExcelTo));

        String key = languageTextExcelTo.getKey();
        // 先查询库中是否已存在该key的文本
        LambdaQueryWrapper<LanguageText> queryWrapper = new QueryWrapper<LanguageText>().lambda()
                .eq(LanguageText::getKey, key)
                .eq(LanguageText::getModel, "app")
                .eq(LanguageText::getType, LanguageTextTypeEnum.Frontend.getValue());
        LanguageText exist = languageTextMapper.selectOne(queryWrapper);
        if (exist != null) {
            // 已存在则更新langs
            String langs = exist.getLangs();
            JSONObject langsJsonObject = JSONObject.parseObject(langs);
            if (StringUtils.isNotEmpty(secondLang)) {
                langsJsonObject.put(secondLang, languageTextExcelTo.getSecondLang());
            }
            exist.setLangs(langsJsonObject.toJSONString());

            updateCachedDataList.add(exist);
            // 达到BATCH_COUNT了，需要去更新一次数据库，防止几万条数据在内存，容易OOM
            if (updateCachedDataList.size() > BATCH_COUNT) {
                updateData();
            }

        } else {
            // 不存在则插入新的文本记录
            LanguageText languageText = new LanguageText();
            languageText.setType(LanguageTextTypeEnum.Frontend.getValue());
            languageText.setModel("app");
            languageText.setKey(key);

            // 构建多语言json对象
            JSONObject langsJsonObject = new JSONObject();
            langsJsonObject.put(LanguageEnum.Chinese.getDesc(), languageTextExcelTo.getCn());
            if (StringUtils.isNotEmpty(secondLang)) {
                langsJsonObject.put(secondLang, languageTextExcelTo.getSecondLang());
            }
            languageText.setLangs(langsJsonObject.toJSONString());

            insertCachedDataList.add(languageText);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止几万条数据在内存，容易OOM
            if (insertCachedDataList.size() >= BATCH_COUNT) {
                saveData();
            }
        }

    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        if (CollectionUtils.isNotEmpty(insertCachedDataList)) {
            saveData();
        }
        if (CollectionUtils.isNotEmpty(updateCachedDataList)) {
            updateData();
        }
        System.out.println("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        System.out.println(insertCachedDataList.size() + "条待插入数据");
        languageTextMapper.batchInsert(insertCachedDataList);
        // 存储完成清理 list
        insertCachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        System.out.println("存储数据库成功！");
    }

    private void updateData() {
        System.out.println(updateCachedDataList.size() + "条待更新数据");
        languageTextMapper.batchUpdate(updateCachedDataList);
        // 存储完成清理 list
        updateCachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        System.out.println("更新数据库成功！");
    }
}
