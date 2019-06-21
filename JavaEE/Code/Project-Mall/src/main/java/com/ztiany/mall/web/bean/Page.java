package com.ztiany.mall.web.bean;

import java.util.List;

/**
 * 封装所有与分页有关的信息
 *
 * @param <T>
 */
public class Page<T> {

    //用户传进来
    private final int currentPage;//当前页码，从1开始
    //传递进来
    private final int totalRecords;//总记录条数
    //传递进来
    private final int pageSize;//每页显示的条数

    //可以根据总记录条数和每页显示的记录条数计算出来
    private int totalPageNum;//总页数
    //DAO层查出来
    private List<T> record;//分页查询的记录
    //计算出来
    private int previousPage;//上一页
    //计算出来
    private int nextPage;//下一页
    //根据当前页码计算出来
    private int startIndex;//每页开始记录的索引
    private int startPageNum;//显示的开始页码
    private int endPageNum;//显示的结束页码
    private String url;//查询分页记录的servlet的访问地址

    private int maxPageShow = 9;

    //用此类：必须告知要看的页码，总记录条数
    public Page(int currentPage, int totalRecords) {
        this(currentPage, totalRecords, 10);
    }

    //用此类：必须告知要看的页码，总记录条数
    public Page(int currentPage, int totalRecords, int pageSize) {
        this.currentPage = currentPage;
        this.totalRecords = totalRecords;
        if (pageSize == 0) {
            throw new IllegalArgumentException("page size can not be 0");
        }
        this.pageSize = pageSize;

        //计算：totalPageNum
        totalPageNum = totalRecords % pageSize == 0 ? totalRecords / pageSize : totalRecords / pageSize + 1;

        previousPage = currentPage - 1 > 0 ? currentPage - 1 : 1;
        nextPage = currentPage + 1 > totalPageNum ? totalPageNum : currentPage + 1;

        startIndex = (currentPage - 1) * pageSize;

        calc();
    }

    private void calc() {

        //计算页码
        if (totalPageNum <= maxPageShow) {
            startPageNum = 1;
            endPageNum = totalPageNum;
        } else {//限制页码链接最多显示maxPageShow个

            startPageNum = currentPage - (maxPageShow / 2);
            endPageNum = currentPage + (maxPageShow / 2);

            if (startPageNum <= 0) {
                startPageNum = 1;
                endPageNum = startPageNum + (maxPageShow - 1);
            }
            if (endPageNum > totalPageNum) {
                endPageNum = totalPageNum;
                startPageNum = endPageNum - (maxPageShow - 1);
            }
        }
    }

    public List<T> getRecord() {
        return record;
    }

    public void setRecord(List<T> record) {
        this.record = record;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getStartPageNum() {
        return startPageNum;
    }

    public int getEndPageNum() {
        return endPageNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMaxPageShow(int maxPageShow) {
        if (maxPageShow < 9) {
            maxPageShow = 9;
        }
        this.maxPageShow = maxPageShow;
        calc();
    }
}
