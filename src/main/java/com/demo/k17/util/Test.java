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
        bookMenu();
         Connection con = Jsoup.connect("http://www.17k.com/chapter/2477255/28942975.html");
        Document doc = con.get();
        Elements content = doc.select("div#chapterContentWapper");
        String text = content.text();
        String [] s=text.split("\\s+?");
      for (String ss:s) {
          System.out.println(ss);
        }

    }
    //Main List

    static void bookMenu() throws IOException {
        Connection con = Jsoup.connect("http://www.17k.com/list/2477255.html");
        Document doc = con.get();
        Elements content = doc.select("div.Main.List dl.Volume dd");
        String text = content.text();
        String [] s=text.split("\\s+");
        for (String ss:s) {
            System.out.println(ss);
        }
    }
}
