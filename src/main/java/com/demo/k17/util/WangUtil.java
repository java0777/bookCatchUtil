package com.demo.k17.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by Administrator on 2017/7/3.
 */
public class WangUtil {

    /**
     * 根据书名，作者，查询书籍信息
     * @param bookNameData 书名
     * @param authorData 作者
     * @return null查无此书
     * bookName 书名
     * author 作者
     * type 类型
     * marks 标签字符串
     *info 书籍内容介绍
     * url 章节地址
     */
    public  static Map<String,String> getBookInfo(String bookNameData,String authorData){
        HashMap<String, String> data = new HashMap<>();
        data.put("c.st", "1");
        data.put("c.q", bookNameData);
        Document doc = CatchUtil.getTextDoc("http://search.17k.com/search.xhtml", data);
        Elements select = doc.select(".textlist");
        boolean flag = false;//true 有符合要求的数
        Map<String, String> bookMap = new HashMap<>();
        for (Element e : select) {
            Elements bookInfo = e.select(".textmiddle a");
            String bookName = bookInfo.get(0).text().trim();
            String author = e.select(".ls:contains(作者：) a").text().trim();
            String type = e.select("span:contains(类别：) a").text().trim();
            String marks = e.select(".bq10:contains(标签：) a").text().trim();
            Element infoE = e.select("li:contains(简介：) a").get(0);
            String info = infoE.text().trim();
            String url = infoE.attr("href").trim();
            if ((authorData == null || authorData.equals(author)) && bookNameData.equals(bookName)) {
                bookMap.put("bookName", bookName);
                bookMap.put("author", author);
                bookMap.put("type", type);
                bookMap.put("marks", marks);
                bookMap.put("info", info);
                bookMap.put("url", url);
                flag = true;
                break;
            }
        }

        if(flag){//找到
            return bookMap;
        }else{
            return null;
        }
    }

    /**
     * 根据抓取到的章节列表doc对象，解析出章节列表
     * @param doc doc对象
     * @return
     * key 章节名
     * value 章节地址
     */
    public static Map<String, String> getAllTitles(Document doc) {
        Map<String, String> map = new LinkedHashMap<>();
        //查询节点，获取章节信息
        Elements content = doc.select("div.Main.List dl.Volume dd a");
        for (Element e:content){
            String key = e.text();//章节名称
            String value = "http://www.17k.com"+e.attr("href");//章节地址
            map.put(key,value);
        }
        return map;
    }

    /**
     * 根据章节文字内容doc对象 获取有效数据
     * @param titleContentDoc
     * @return 文字信息的缓存对象
     */
    public static StringBuilder getTitleContent(Document titleContentDoc) {
        Elements content = titleContentDoc.select("div#chapterContentWapper");
        String text = content.text();
        StringBuilder stringBuilder=new StringBuilder();
        StringBuilder builder = stringBuilder.append(text);
        return builder;
    }



    public static void main(String[] args) {
        Map<String, String> bookInfo = getBookInfo("罪恶之城", null);
        if(bookInfo!=null){
            String url = bookInfo.get("url");
            Document titlesDoc = CatchUtil.getTextDoc(url, null);
            Map<String, String> allTitles = getAllTitles(titlesDoc);
            Set<Map.Entry<String, String>> entries = allTitles.entrySet();
            for(Map.Entry<String, String> entry:entries){
                System.out.println(entry.getKey());
                Document contentDoc = CatchUtil.getTextDoc(entry.getValue(), null);
                StringBuilder content = getTitleContent(contentDoc);
                System.out.println(content);
            }
        }else {
            System.out.println("没有该书");
        }
    }
}
