package com.demo.k17.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Administrator on 2017/6/27.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Connection con = Jsoup.connect("http://www.17k.com/chapter/2401966/28020958.html");
        Document doc = con.get();
        Elements content = doc.select("#chapterContentWapper");
        System.out.println(content);
    }
}
