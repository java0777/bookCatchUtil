package com.demo.port;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/10.
 */
public interface ICatchThread extends Runnable{
    /**
     * 任务初始化
     *
     * @param book 待生成任务的书籍
     */
    void initWorks(Map book);

    /**
     * 得到生成后任务总数
     * @return
     */
    int getWorksCount();

    /**
     * 申请得到一个任务  需线程安全
     * @return 得到一个任务项
     */
    Map<String, Object> getItemWork();

    /**
     * 将处理好的任务结果 保存  需线程安全
     * @param workResult
     */
    void putWorkResult(Map<String, Object> workResult);

    /**
     * 得到已保存的任务结果总数
     * @return
     */
    int getWorkResultCount();

    /**
     * 得到任务处理后的结果集合
     * @return
     */
    List<Map<String, Object>> getWorkResults();
}
