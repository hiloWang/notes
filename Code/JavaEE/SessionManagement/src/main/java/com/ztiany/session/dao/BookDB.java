package com.ztiany.session.dao;

import com.ztiany.session.domain.Book;

import java.util.HashMap;
import java.util.Map;

public class BookDB {

    private static Map<Integer, Book> books = new HashMap<Integer, Book>();

    static {
        books.put(1, new Book(1, "葵花宝典", "岳不群", 5, "欲练此功，必须练好基本功"));
        books.put(2, new Book(2, "辟邪剑法", "独孤求败", 5, "欲练此功，必须练好基本功"));
        books.put(3, new Book(3, "欲女心经", "小龙女", 5, "欲练此功，必须清纯"));
        books.put(4, new Book(4, "JavaWeb", "张孝祥", 5, "经典书籍"));
        books.put(5, new Book(5, "金瓶梅", "潘金莲", 5, "古典爱情"));
    }

    public static Map<Integer, Book> getAllBooks() {
        return books;
    }

    public static Book getBookById(int bookId) {
        return books.get(bookId);
    }
}
