package com.demo.k17.util;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

/**
 * 17k网站 书籍抓取工具类
 */
public interface IBookUtil {

    /**
     * get方式抓取txt内容网址
     *
     * @param url    网址
     * @param params 参数
     * @return doc对象
     */
    Document getTextDoc(String url, Map<String, String> params);

    /**
     * post方式抓取txt内容网址
     *
     * @param url    网址
     * @param params 参数
     * @return doc对象
     */
    Document getPostDoc(String url, Map<String, String> params);

    /**
     * 根据章节集合内容网址 获取整张页面内容
     * @param  url 章节集合内容网址
     * @return 抓取获得的doc对象
     */
    Document getTitlesDoc(String url);

    /**
     * 根据抓取到的doc对象，解析出章节相关内容
     *
     * @param doc doc对象
     * @return key为中文章节名，value为对应的url
     */
    List<Map<String, String>> getAllTitles(Document doc);

    /**
     * 根据章节文字内容网址 获取整张页面内容
     * @param  url 章节文字内容网址
     * @return 抓取获得的doc对象
     */
    Document getContentDoc(String url);

    /**
     * 根据章节文字内容doc对象 获取有效数据
     * @param titleContentDoc
     * @return  文字信息的缓存对象
     */
    StringBuilder  getTitleContent(Document titleContentDoc);

}
