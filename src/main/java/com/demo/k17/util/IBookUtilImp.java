package com.demo.k17.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hs1 on 2017/6/30.
 */
public class IBookUtilImp implements IBookUtil {
    /**
     *get方式抓取txt内容网址
     * @param url    网址
     * @param params 参数
     * @return
     */
    public Document getTextDoc(String url, Map<String, String> params) {
        Connection con = Jsoup.connect(url);
        if (params!=null){
            con.data(params);
        }
        Document doc = null;
        try {
            doc = con.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * post方式抓取txt内容网址
     * @param url    网址
     * @param params 参数
     * @return
     */
    public Document getPostDoc(String url, Map<String, String> params){
        Connection con = Jsoup.connect(url);
        if (params!=null){
            con.data(params);
        }
        Document doc = null;
        try {
            doc = con.post();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

    /**
     * 根据章节集合内容网址 获取整张页面内容
     * @return
     */
    public Document getTitlesDoc(String url) {
        Document doc = getPostDoc(url,null);
        return doc;
    }




    /**
     * 根据抓取到的doc对象，解析出章节相关内容
     * @param doc doc对象
     * @return
     */
    public List<Map<String, String>> getAllTitles(Document doc) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        //查询节点，获取章节信息
        Elements content = doc.select("div.Main.List dl.Volume dd a");
        for (Element e:content){
            String key = e.text();//章节名称
            String value = e.attr("href");//章节地址
            map.put(key,value);
        }
        return (List<Map<String, String>>) map;
    }


    /**
     * 根据章节文字内容网址 获取整张页面内容
     * @return 抓取获得的doc对象
     */
    public Document getContentDoc(String url) {
        Document doc =getTitlesDoc(url);
        return doc;
    }


    /**
     * 根据章节文字内容doc对象 获取有效数据
     * @param titleContentDoc
     * @return 文字信息的缓存对象
     */
    public StringBuilder getTitleContent(Document titleContentDoc) {
        Elements content = titleContentDoc.select("div#chapterContentWapper");
        String text = content.text();
        StringBuilder stringBuilder=new StringBuilder();
        StringBuilder builder = stringBuilder.append(text);
        return builder;
    }

    /**
     根据作者，书籍名，调用起点或17k查询接口，得到解析后的章节内容
     * 1.通过接口判断是否有该本书
     * 没有 返回null
     * 有 一步步嵌套解析得到所得章节内容
     * Map  key 章节名  value 解析后的章节内容
     * @param author 作者
     * @param bookName   书籍名
     * @return null没有此书
     */
    public Map<String, StringBuilder> getAllTitles(String author, String bookName) {
        Map map=new LinkedHashMap();
        map.put(author,bookName);
        Document postDoc = getPostDoc(null, map);
        return null;
    }
}
