package com.demo.k17.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.demo.k17.util.MobileUtil.*;

/**
 * Created by Administrator on 2017/8/4.
 */
public class WorksFactory {
    private static final List<Map<String, Object>> repository = new ArrayList<>();
    private static final List<Map<String, Object>> workResult = new ArrayList<>();

    public static void initWorks(Map book) {
        if (book == null) {
            return;
        }
        List<Map<String, Object>> titles = getTitles(book);
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
    public static int getRunCount() {
        return repository.size();
    }

    /**
     * 得到一个任务内容
     *
     * @return null 代表任务已经处理完
     */
    public synchronized static Map<String, Object> getItemWork() {
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
    public synchronized static void returnWorkResult(Map<String, Object> title) {
        workResult.add(title);
    }

    /**
     * 得到当前结果数量
     *
     * @return
     */
    public static int getResultCount() {
        return workResult.size();
    }

    /**
     * 得到工作结果集
     * @return
     */
    public static List<Map<String, Object>> getWorkResult() {
        return workResult;
    }
}
