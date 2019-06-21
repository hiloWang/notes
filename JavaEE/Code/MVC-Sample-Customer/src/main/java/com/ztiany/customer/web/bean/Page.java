package com.ztiany.customer.web.bean;

import java.util.List;

/**
 * 封装所有与分页有关的信息
 *
 * @param <T>
 */
public class Page<T> {

    //DAO层查出来
    private List<T> record;//分页查询的记录

    //用户传进来
    private int currentPage;//当前页码

    //可以根据总记录条数和每页显示的记录条数计算出来
    private int totalPageNum;//总页数

    //计算出来
    private int previousPage;//上一页

    //计算出来
    private int nextPage;//下一页

    //传递进来
    private int totalRecords;//总记录条数

    //传递进来
    private int pageSize = 10;//每页显示的条数

    //根据当前页码计算出来
    private int startIndex;//每页开始记录的索引

    private int startPageNum;//显示的开始页码

    private int endPageNum;//显示的结束页码

    private String url;//查询分页记录的servlet的访问地址

    //用此类：必须告知要看的页码，总记录条数
    public Page(int currentPage, int totalRecords) {

        this.currentPage = currentPage;
        this.totalRecords = totalRecords;

        //计算：totalPageNum
        totalPageNum = totalRecords % pageSize == 0 ? totalRecords / pageSize : totalRecords / pageSize + 1;
        previousPage = currentPage - 1 > 0 ? currentPage - 1 : 1;
        nextPage = currentPage + 1 > totalPageNum ? totalPageNum : currentPage + 1;

        startIndex = (currentPage - 1) * pageSize;

        //计算页码
        if (totalPageNum <= 9) {
            startPageNum = 1;
            endPageNum = totalPageNum;
        } else {//限制页码链接最多显示9个
            startPageNum = currentPage - 4;
            endPageNum = currentPage + 4;
            if (startPageNum <= 0) {
                startPageNum = 1;
                endPageNum = startPageNum + 8;
            }
            if (endPageNum > totalPageNum) {
                endPageNum = totalPageNum;
                startPageNum = endPageNum - 8;
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

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getStartPageNum() {
        return startPageNum;
    }

    public void setStartPageNum(int startPageNum) {
        this.startPageNum = startPageNum;
    }

    public int getEndPageNum() {
        return endPageNum;
    }

    public void setEndPageNum(int endPageNum) {
        this.endPageNum = endPageNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
