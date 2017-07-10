package com.demo.k17;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.port.IBookUtil;
import com.demo.port.ICatchThread;

import java.util.*;


/**
 * 手机抓取多线程对象
 */
public class BookCatchThread implements ICatchThread {
    //任务清单
    private final List<Map<String, Object>> workRep = new ArrayList<>();
    //结果清单
    private final List<Map<String, Object>> workResult = new ArrayList<>();

    private IBookUtil iBookUtil;

    public BookCatchThread(IBookUtil iBookUtil) {
        this.iBookUtil = iBookUtil;
    }

    /**
     * 任务初始化
     *
     * @param book
     */
    public void initWorks(Map book) {
        if (book == null) {
            return;
        }
        List<Map<String, Object>> titles = iBookUtil.getWorkReps(book);
        for (Map item : titles) {
            item.put("bookId", book.get("book_id"));
        }
        workRep.addAll(titles);
    }

    /**
     * 线程池要执行任务的次数
     *
     * @return
     */
    public int getWorksCount() {
        return workRep.size();
    }

    /**
     * 得到一个任务内容
     *
     * @return null 代表任务已经处理完
     */
    public synchronized Map<String, Object> getItemWork() {
        int size = workRep.size();
        if (size != 0) {
            return workRep.remove(size-1);
        }
        return null;
    }

    /**
     * 存放工作结果
     *
     * @param title
     */
    public synchronized void putWorkResult(Map<String, Object> title) {
        workResult.add(title);
    }

    /**
     * 得到当前结果数量
     *
     * @return
     */
    public int getWorkResultCount() {
        return workResult.size();
    }

    /**
     * 得到工作结果集
     *
     * @return
     */
    public List<Map<String, Object>> getWorkResults() {
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
                titleJsonStr = iBookUtil.getItemWorkResult(bookId, id).toString();
                JSONObject json = JSON.parseObject(titleJsonStr);
                JSONObject data = json.getJSONObject("data");
                Object name = data.get("name");
                Object content = data.get("content");
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("content", content);
                map.put("titleId", id);
                putWorkResult(map);
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
            putWorkResult(map);
        }
    }


}
