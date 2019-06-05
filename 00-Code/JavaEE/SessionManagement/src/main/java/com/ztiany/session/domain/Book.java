package com.ztiany.session.domain;

import java.io.Serializable;

public class Book implements Serializable {

    private static final long serialVersionUID = 43232L;

    private int id;//唯一标识一本书
    private String name;
    private String author;
    private int price;
    private String description;

    public Book() {
    }

    public Book(int id, String name, String author, int price, String description) {
        super();
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", name=" + name + ", author=" + author
                + ", price=" + price + ", description=" + description + "]";
    }

}
