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
    Document getTextDoc(String url,Map<String, String> params);

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
     * @param   titleContentDoc
     * @return  文字信息的缓存对象
     */
    StringBuilder  getTitleContent(Document titleContentDoc);

    /**
     * 根据作者，书籍名，调用起点或17k查询接口，得到解析后的章节内容
     * 1.通过接口判断是否有该本书
     * 没有 返回null
     * 有 一步步嵌套解析得到所得章节内容
     * Map  key 章节名  value 解析后的章节内容
     * @param author 作者
     * @param bookName   书籍名
     * @return  null没有此书
     */
    Map<String,StringBuilder> getAllTitles(String author,String bookName);
}
