package com.demo.k17.util;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 小说章节目录内容解析
 */
public class Test {
    public static void main(String[] args) throws IOException {
        //
        /*Connection con = Jsoup.connect("http://www.17k.com/list/2477255.html");
        Document doc = con.get();
        System.out.println(bookHref(doc));;*/
        bookText();
    }
    //Main List

    /**
     *
     * @throws IOException
     */
    //解析小说章节内容
    static void bookText()throws IOException{
        Connection con = Jsoup.connect("http://www.17k.com/chapter/2477255/28942975.html");
        Document doc = con.get();
        Elements content = doc.select("div#chapterContentWapper");
        String text = content.text();
        System.out.println(text);
        String [] s=text.split("\\s+?");
        for (String ss:s) {
            System.out.println(ss);
        }
    }

    /**
     *
     * @param doc
     * @return
     * @throws IOException
     */
    //传网页连接，解析章节链接(value)和章节目录（key）
    static Map bookHref(Document doc)throws IOException {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        //查询节点，获取章节信息
        Elements content = doc.select("div.Main.List dl.Volume dd a");
        for (Element e:content){
            String key = e.text();//章节名称
            String value = e.attr("href");//章节地址
            map.put(key,value);
        }
        return map;
    }

}

