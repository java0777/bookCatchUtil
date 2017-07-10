package com.demo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/7/10.
 */
public class BookWriterUtil {
    /**
     * 根据全本小说txt文件名，章节内容对象，章节调整正则，生成全本小说内容txt文件
     *
     * @param bookFileName txt文件名
     * @param workResult   章节内容对象
     * @param titleRegex   章节调整正则
     */
    public  static void writerAllBook(String  allBookTxtPath,String bookFileName, List<Map<String, Object>> workResult, String titleRegex) {
        try {
            File path = new File(allBookTxtPath);
            if (!path.exists()){
                path.mkdirs();
            }
            String bookFile=allBookTxtPath + bookFileName + ".txt";
            File file = new File(bookFile);
            if (file.exists()){
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(bookFile, true);
            for (Map item : workResult) {
                String titleName = item.get("name").toString().trim();
                titleName = getTitle(titleName, titleRegex);
                out.write(titleName.getBytes());
                String content = item.get("content").toString().trim();
                String[] lines = content.split(" ");
                for (String line : lines) {
                    line += System.getProperty("line.separator");
                    out.write(line.getBytes());
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得调整后的章节名
     *
     * @param titleName  调整前章节名
     * @param titleRegex 调整方案正则
     *                   注：第一个捕获组为章节编号，第二捕获组为章节说明
     *                   捕获组就是正则中的()
     * @return
     */
    public static String getTitle(String titleName, String titleRegex) {
        Pattern regex = null;
        if (titleRegex != null) {
            regex=Pattern.compile(titleRegex);
        }
        if (regex == null) {
            titleName += System.getProperty("line.separator");
            return titleName;
        }
        if (regex.matcher(titleName).matches()) {
            Matcher matcher = regex.matcher(titleName);
            if (matcher.find()) {
                String start = matcher.group(1);
                String end = matcher.group(2);
                String title = "第" + start + "章 " + end + System.getProperty("line.separator");
                return title;
            }
        }
        titleName += System.getProperty("line.separator");
        return titleName;
    }
}
