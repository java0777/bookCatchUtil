package com.demo.k17.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 17移动端抓取工具类
 */
public class MobileUtil {
    /**
     * 手机客户端信息
     */
    private static final String AGENT = "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
    /**
     * 完本内容存放位置
     */
    private static final String ALL_BOOK_TXT_PATH = "D:\\allBook\\";


    /**
     * unicode 字符串转普通字符串
     *
     * @param unicode unicode字符串
     * @return
     */
    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }

    /**
     * 抓取得到书清单
     *
     * @param bookName 书名
     * @return
     */
    private static List<Map<String, String>> queryBookList(String bookName) {
        String url = "http://api.17k.com/v2/book/search";
        Map params = new HashMap();
        params.put("jsonp", "Q.Jsonp1499136123632890");
        params.put("callback", "Q.Jsonp1499136123632890");
        params.put("app_key", "1351550300");
        params.put("sort_type", "0");
        params.put("page", "1");
        params.put("num", "10");
        params.put("key", bookName);
        Document doc = CatchUtil.getTextDoc(url, params);
        Element body = doc.body();
        String content = body.text();
        String temp = content.replaceFirst("/\\*\\*/Q.Jsonp1499136123632890\\(", "");
        temp = temp.substring(0, temp.length() - 2);
        JSONObject json = JSON.parseObject(temp);
        List<Map<String, String>> data = json.getObject("data", List.class);
        return data;
    }

    /**
     * 过滤书清单 保留有效数据
     *
     * @param bookName 书名
     * @param author   作者 可以为空
     * @param bookList 待过滤书清单
     * @return null未找到书
     */
    private static Map<String, String> getBook(String bookName, String author, List<Map<String, String>> bookList) {
        for (Map<String, String> book : bookList) {
            String book_name = book.get("book_name");
            String author_name = book.get("author_name");
            if ((author == null || author.equals(author_name)) && bookName.equals(book_name)) {
                return book;
            }
        }
        return null;
    }

    /**
     * 根据书名，作者得到符合要求的数据信息
     *
     * @param bookName 书名
     * @param author   作者 可以为空
     * @return null未找到书
     */
    public static Map queryBook(String bookName, String author) {
        List<Map<String, String>> bookList = queryBookList(bookName);
        Map<String, String> book = getBook(bookName, author, bookList);
        return book;
    }

    /**
     * 得到当前章节列表
     *
     * @param book_id 书本唯一识别码
     * @param page    章节页面
     * @return
     */
    private static JSONObject getData(Object book_id, String page) {
        Map params = new HashMap();
        params.put("bookId", book_id.toString());
        String url = "http://h5.17k.com/h5/book/ajaxBookList.k?callback=Q.Jsonp1499135900645425&jsonp=Q.Jsonp1499135900645425&orderType=0";
        params.put("page", page);
        Document doc = CatchUtil.getTextDoc(url, params, AGENT);
        String temp = doc.body().text().replaceFirst("Q.Jsonp1499135900645425\\(", "");
        temp = temp.substring(0, temp.length() - 1);
        JSONObject json = JSON.parseObject(temp);
        return json;
    }

    /**
     * 得到章节清单
     *
     * @param book
     * @return
     */
    public static List<Map<String, Object>> getTitles(Map<String, Object> book) {
        Object book_id = book.get("book_id");
        List<Map<String, Object>> result = new ArrayList<>();
        JSONObject json = getData(book_id.toString(), "1");
        List<Map<String, Object>> data = json.getObject("datas", List.class);
        result.addAll(data);
        Integer pages = json.getInteger("totalPage");
        for (int page = 2; page <= pages; page++) {
            JSONObject jsonTemp = getData(book_id, String.valueOf(page));
            List<Map<String, Object>> temp = jsonTemp.getObject("datas", List.class);
            result.addAll(temp);
        }
        return result;
    }

    /**
     * 根据书本编号，章节名称获取内容
     *
     * @param bookId
     * @param titleId
     * @return
     */
    public static String getTitleJsonStr(Object bookId, Object titleId) {
        String cookie = "GUID=1fbd985b-0054-964f-dd06-932ec40e5457; " +
                "UM_distinctid=15cac5fe0de9a-09cde7ba9-1d17204b-348f0-15cac5fe0e01f3; " +
                "c_csc=alading; " +
                "c_u=21311l21517l49l51l49l48l48l57l48l48l50l49l49l57l48l49%2632119127%266503eefe5792ff9a%261497542147608; " +
                "smssalading=1; hjstat_uv=969554463176535234|680522; " +
                "info_cookie_last_time=1499146866618; c_i=0&true&0&0&0; " +
                "his_chapters=\"[{\\\"bookId\\\":1726918,\\\"chapterId\\\":29109696,\\\"readTimes\\\":1499146863868},{\\\"bookId\\\":466630,\\\"chapterId\\\":15163023,\\\"readTimes\\\":1499125403798}]\"; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221fbd985b-0054-964f-dd06-932ec40e5457%22%2C%22props%22%3A%7B%22%24latest_referrer%22%3A%22%22%2C%22%24latest_referrer_host%22%3A%22%22%7D%2C%22%24device_id%22%3A%2215cac5fe1c454-0351632a5-1d17204b-215280-15cac5fe1c599%22%7D";

        String url = "http://app.17k.com/chapter/" +
                bookId + "/" + titleId;
        Map params = new HashMap();
        params.put("callback", "Q.Jsonp149914810730091");
        params.put("jsonp", "Q.Jsonp149914810730091");
        params.put("from", "h5");
        Document doc = CatchUtil.getTextDoc(url, params, AGENT, cookie);
        String text = doc.body().text();
        String temp = text.replaceFirst("/\\*\\*/Q.Jsonp149914810730091\\(", "");
        temp = temp.substring(0, temp.length() - 2);
        return temp;
    }

    /**
     * 根据全本小说txt文件名，章节内容对象，章节调整正则，生成全本小说内容txt文件
     *
     * @param bookFileName txt文件名
     * @param workResult   章节内容对象
     * @param titleRegex   章节调整正则
     */
    public static void getAllBook(String bookFileName, List<Map<String, Object>> workResult, String titleRegex) {
        try {
            FileOutputStream out = new FileOutputStream(ALL_BOOK_TXT_PATH + bookFileName + ".txt", true);
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
    private static String getTitle(String titleName, String titleRegex) {
        Pattern regex = null;
        if (titleRegex != null) {
            Pattern.compile(titleRegex);
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
                String title = "第" + start + "章" + end + System.getProperty("line.separator");
                return title;
            }
        }
        titleName += System.getProperty("line.separator");
        return titleName;
    }

    /**
     * 根据书名，作者名获取书籍相关信息
     * @param bookName  书名
     * @param author  作者
     * @return
     */
    public static List<Map<String, Object>> getBookTitles(String bookName, String author,String titleRegex) {
        long start = System.currentTimeMillis();
        Map book = MobileUtil.queryBook(bookName, author);//查询书本
        if (book == null) {//判断书本查询结果
            System.out.println(bookName + ":不存在");
            return null;
        }
        BookCatchThread r = new BookCatchThread();//创建内容抓取线程
        r.initWorks(book);  //加载任务
        int runCount = r.getRunCount(); //得到任务总数
        ExecutorService pool = Executors.newFixedThreadPool(50);
        for (int i = 1; i <= runCount; i++) {
            pool.execute(r);//申请空闲线程
        }
        System.out.println("任务分配完毕");
        pool.shutdown();
        while (r.getResultCount() != runCount) {//判断任务完成条件
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<Map<String, Object>> workResult = r.getWorkResult();//得到任务完成后结果
        Collections.sort(workResult, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if (o1 != null && o2 != null) {
                    Integer id1 = (Integer) o1.get("titleId");
                    Integer id2 = (Integer) o2.get("titleId");
                    return id1.compareTo(id2);
                }
                return 0;
            }
        });//结果排序
        System.out.println("采集完成");
        MobileUtil.getAllBook(bookName, workResult, titleRegex);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println("新书录入完毕");
        return workResult;
    }

    public static void main(String[] args) {
        getBookTitles("罪恶之城",null,"章(.*)[　 ](.*)");
        getBookTitles("尘缘",null,null);
    }
}
