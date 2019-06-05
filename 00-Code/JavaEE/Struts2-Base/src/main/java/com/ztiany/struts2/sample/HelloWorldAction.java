package com.ztiany.struts2.sample;

import java.io.Serializable;

public class HelloWorldAction implements Serializable {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String sayHello() {
        message = "helloworld by struts2";
        return "success";
    }

}