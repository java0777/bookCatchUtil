package com.demo.k17;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.port.IBookUtil;
import com.demo.port.ICatchThread;
import com.demo.util.BookWriterUtil;
import com.demo.util.CatchUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 17移动端抓取工具类
 */
public class MobileUtil  implements IBookUtil {
    /**
     * 手机客户端信息
     */
    private  final String agent = "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

    /**
     * 线程池
     */
    private  ExecutorService pool = Executors.newFixedThreadPool(50);


    /**
     * 抓取得到书清单
     *
     * @param bookName 书名
     * @return
     */
    private  List<Map<String, String>> queryBookList(String bookName) {
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
    private  Map<String, String> getBook(String bookName, String author, List<Map<String, String>> bookList) {
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
    public  Map queryBook(String bookName, String author) {
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
    private  JSONObject getData(Object book_id, String page) {
        Map params = new HashMap();
        params.put("bookId", book_id.toString());
        String url = "http://h5.17k.com/h5/book/ajaxBookList.k?callback=Q.Jsonp1499135900645425&jsonp=Q.Jsonp1499135900645425&orderType=0";
        params.put("page", page);
        Document doc = CatchUtil.getTextDoc(url, params, agent);
        String temp = doc.body().text().replaceFirst("Q.Jsonp1499135900645425\\(", "");
        temp = temp.substring(0, temp.length() - 1);
        JSONObject json = JSON.parseObject(temp);
        return json;
    }


    public  List<Map<String, Object>> getWorkReps(Map<String, Object> book) {
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


    public  Object getItemWorkResult(Object ... props ) {
        String cookie = "GUID=1fbd985b-0054-964f-dd06-932ec40e5457; " +
                "UM_distinctid=15cac5fe0de9a-09cde7ba9-1d17204b-348f0-15cac5fe0e01f3; " +
                "c_csc=alading; " +
                "c_u=21311l21517l49l51l49l48l48l57l48l48l50l49l49l57l48l49%2632119127%266503eefe5792ff9a%261497542147608; " +
                "smssalading=1; hjstat_uv=969554463176535234|680522; " +
                "info_cookie_last_time=1499146866618; c_i=0&true&0&0&0; " +
                "his_chapters=\"[{\\\"bookId\\\":1726918,\\\"chapterId\\\":29109696,\\\"readTimes\\\":1499146863868},{\\\"bookId\\\":466630,\\\"chapterId\\\":15163023,\\\"readTimes\\\":1499125403798}]\"; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221fbd985b-0054-964f-dd06-932ec40e5457%22%2C%22props%22%3A%7B%22%24latest_referrer%22%3A%22%22%2C%22%24latest_referrer_host%22%3A%22%22%7D%2C%22%24device_id%22%3A%2215cac5fe1c454-0351632a5-1d17204b-215280-15cac5fe1c599%22%7D";

        String url = "http://app.17k.com/chapter/" +
                props[0] + "/" + props[1];
        Map params = new HashMap();
        params.put("callback", "Q.Jsonp149914810730091");
        params.put("jsonp", "Q.Jsonp149914810730091");
        params.put("from", "h5");
        Document doc = CatchUtil.getTextDoc(url, params, agent, cookie);
        String text = doc.body().text();
        String temp = text.replaceFirst("/\\*\\*/Q.Jsonp149914810730091\\(", "");
        temp = temp.substring(0, temp.length() - 2);
        return temp;
    }

    /**
     * 根据书名，作者名获取书籍相关信息
     *
     * @param book 数据信息对象  不能为null
     * @return
     */
    public  List<Map<String, Object>> getBookTitles(Map book, ICatchThread r) {
        long start = System.currentTimeMillis();
        Object bookName = book.get("book_name");
        r.initWorks(book);  //加载任务
        int runCount = r.getWorksCount(); //得到任务总数
        for (int i = 1; i <= runCount; i++) {
            pool.execute(r);//申请空闲线程
        }
        System.out.println(bookName+":任务分配完毕");
        pool.shutdown();
        while (r.getWorkResultCount() != runCount) {//判断任务完成条件
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<Map<String, Object>> workResult = r.getWorkResults();//得到任务完成后结果
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
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(bookName+":采集完成");
        return workResult;
    }

    public static void main(String[] args) {
        //抓取实现工具类
        MobileUtil mobileUtil = new MobileUtil();
        Map book = mobileUtil.queryBook("罪恶之城", null);
        if(book!=null){
            //章节抓取实现工具类
            BookCatchThread r = new BookCatchThread(mobileUtil);

            List<Map<String, Object>> bookTitles = mobileUtil.getBookTitles(book,r);
            //书籍写人文件工具类
            BookWriterUtil.writerAllBook("D:\\allBook\\","罪恶之城", bookTitles, "章(.*?)[ 　](.*)" );
        }else{
            System.out.println("书籍不存在");
        }
    }
}
