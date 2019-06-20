package com.ztiany.principle.isp;


public interface IBookSearcher {

    void searchByAuthor(String author) ;
    void searchByTitle(String title) ;
    void searchByCategory(String category) ;

}
