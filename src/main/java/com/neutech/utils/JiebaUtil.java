package com.neutech.utils;

import com.huaban.analysis.jieba.JiebaSegmenter;

import java.util.List;

public class JiebaUtil {
    public static List<String> getSingleWorld(String  string){
        // 匹配分词模板
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> strings = segmenter.sentenceProcess(string);
        return strings;
    }
}
