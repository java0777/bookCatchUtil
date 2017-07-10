package com.demo.port;

import java.util.List;
import java.util.Map;

/**
 * 书籍抓取工具接口
 */
public interface IBookUtil {
    /**
     * 根据书名，作者得到符合要求的数据信息
     *
     * @param bookName 书名
     * @param author   作者 可以为空
     * @return null未找到书
     */
    Map queryBook(String bookName, String author);

    /**
     * 根据有效书籍 获取章节抓取任务清单
     *
     * @param book 有效书籍 不能为null
     * @return
     */
    List<Map<String, Object>> getWorkReps(Map<String, Object> book);

    /**
     * 得到单项任务执行后结果
     * @param props 执行所需参数
     * @return 执行后结果
     */
    Object getItemWorkResult(Object... props);

    /**
     * 根据有效书籍，获取完整章节清单
     *
     * @param book 书籍数据信息对象  不能为null
     * @param r 书籍抓取任务线程实现类对象  不能为null
     * @return
     */
    List<Map<String, Object>> getBookTitles(Map book,ICatchThread r);
}
