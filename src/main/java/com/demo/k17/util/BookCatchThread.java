package com.demo.k17.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 手机抓取多线程对象
 */
public class BookCatchThread implements Runnable {

    private final List<Map<String, Object>> repository = new ArrayList<>();
    private final List<Map<String, Object>> workResult = new ArrayList<>();

    /**
     * 任务初始化
     *
     * @param book
     */
    public void initWorks(Map book) {
        if (book == null) {
            return;
        }
        List<Map<String, Object>> titles = MobileUtil.getTitles(book);
        for (Map item : titles) {
            item.put("bookId", book.get("book_id"));
        }
        repository.addAll(titles);
    }

    /**
     * 线程池要执行任务的次数
     *
     * @return
     */
    public int getRunCount() {
        return repository.size();
    }

    /**
     * 得到一个任务内容
     *
     * @return null 代表任务已经处理完
     */
    public synchronized Map<String, Object> getItemWork() {
        int size = repository.size();
        if (size != 0) {
            return repository.remove(0);
        }
        return null;
    }

    /**
     * 存放工作结果
     *
     * @param title
     */
    public synchronized void returnWorkResult(Map<String, Object> title) {
        workResult.add(title);
    }

    /**
     * 得到当前结果数量
     *
     * @return
     */
    public int getResultCount() {
        return workResult.size();
    }

    /**
     * 得到工作结果集
     *
     * @return
     */
    public List<Map<String, Object>> getWorkResult() {
        return workResult;
    }

    @Override
    public void run() {
        Map<String, Object> workItem = getItemWork();
        Object bookId = null;
        Object id = null;
        String titleJsonStr = null;
        try {
            if (workItem != null) {
                bookId = workItem.get("bookId");
                id = workItem.get("id");
                titleJsonStr = MobileUtil.getTitleJsonStr(bookId, id);
                JSONObject json = JSON.parseObject(titleJsonStr);
                JSONObject data = json.getJSONObject("data");
                Object name = data.get("name");
                Object content = data.get("content");
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("content", content);
                map.put("titleId", id);
                returnWorkResult(map);
            }
        } catch (Exception e) {
            System.out.println("出错了，章节：" + workItem.get("name"));
            titleJsonStr = titleJsonStr.replaceAll("\\?\\\"", "");
            JSONObject json = JSON.parseObject(titleJsonStr);
            JSONObject data = json.getJSONObject("data");
            Object name = data.get("name");
            Object content = data.get("content");
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("content", content);
            map.put("titleId", id);
            returnWorkResult(map);
        }
    }


   /* public void main(String[] args) {
        long start = System.currentTimeMillis();
        String bookName = "罪恶之城";
        String author = null;
        Map book = MobileUtil.queryBook(bookName, author);
        if (book == null) {
            return;
        }
        BookCatchThread r = new BookCatchThread();
        //加载任务
        r.initWorks(book);
        int runCount = r.getRunCount();
        //创建任务池
        ExecutorService pool = Executors.newFixedThreadPool(50);
        for (int i = 1; i <= runCount; i++) {
            pool.execute(r);//预加载任务
        }
        System.out.println("任务分配完毕");
        pool.shutdown();
        while (r.getResultCount() != runCount) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<Map<String, Object>> workResult = r.getWorkResult();
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
        });
        System.out.println("采集完成");
        MobileUtil.getAllBook(bookName, workResult, "章(.*)[　 ](.*)");
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println("新书录入完毕");
    }*/


}
