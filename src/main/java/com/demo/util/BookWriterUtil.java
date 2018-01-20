package com.demo.util;

import java.io.*;
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
    public static void writerAllBook(String allBookTxtPath, String bookFileName, List<Map<String, Object>> workResult, String titleRegex) {
        try {
            File path = new File(allBookTxtPath);
            if (!path.exists()) {
                path.mkdirs();
            }
            String bookFile = allBookTxtPath + bookFileName + ".txt";
            File file = new File(bookFile);
            if (file.exists()) {
                file.delete();
            }
            BufferedWriter writer = getWriter(bookFile);
            for (Map item : workResult) {
                String titleName = item.get("name").toString().trim();
                titleName = getTitle(titleName, titleRegex);
                writer.write(titleName);
                String content = item.get("content").toString().trim();
                String[] lines = content.split(" ");
                for (String line : lines) {
                    writer.newLine();
                    writer.write(line);
                }
                writer.newLine();
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据完整文件名，创建字符编码为UTF-8的字符输出流
     *
     * @param file 完整文件名，包含路径
     * @return
     * @throws IOException
     */
    public static BufferedWriter getWriter(String file) throws IOException {
        FileOutputStream out = new FileOutputStream(file, true);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        return writer;
    }

    /**
     * 根据完整文件名，创建字符编码为UTF-8的字符输入流
     *
     * @param file 完整文件名，包含路径
     * @return
     * @throws IOException
     */
    public static BufferedReader getReader(String file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        return reader;
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
            regex = Pattern.compile(titleRegex);
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
