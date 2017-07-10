package com.demo.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

/**
 * 自定义抓取工具类
 */
public class CatchUtil {

    /**
     * get方式抓取txt内容网址
     *
     * @param url    网址
     * @param params 参数
     * @return
     */
    public static Document getTextDoc(String url, Map<String, String> params) {
        Connection con = Jsoup.connect(url);
        if (params != null) {
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
     *
     * @param url
     * @param params
     * @param agent  客户端信息
     * @return
     */
    public static Document getTextDoc(String url, Map<String, String> params,String agent) {
        Connection con = Jsoup.connect(url);
        if (params != null) {
            con.data(params);
        }
        if(agent!=null){
            con.userAgent(agent);
        }
        Document doc = null;
        try {
            doc = con.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static Document getTextDoc(String url, Map<String, String> params,String agent,String cookie) {
        Connection con = Jsoup.connect(url);
        if (params != null) {
            con.data(params);
        }
        if (cookie!=null){
            con.header("cookie",cookie);
        }
        if(agent!=null){
            con.userAgent(agent);
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
     *
     * @param url    网址
     * @param params 参数
     * @return
     */
    public static Document getPostDoc(String url, Map<String, String> params) {
        Connection con = Jsoup.connect(url);
        if (params != null) {
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
     * 根据doc对象获取列表文本内容 每行后换行符分割
     * @param doc 待解析doc对象
     * @param select  标签选择字符串
     * @return  换行拼接内容
     */
    public  static StringBuilder getText(Document doc,String select) {
        StringBuilder result=new StringBuilder();
        Elements list = doc.select(select);
        for(Element item:list) {
            String text = item.text().trim()+"\r\n";
            result.append(text);
        }
        return result;
    }

}
