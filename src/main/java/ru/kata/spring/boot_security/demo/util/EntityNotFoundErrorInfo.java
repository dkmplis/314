package ru.kata.spring.boot_security.demo.util;

public class EntityNotFoundErrorInfo {
    String message;


    public EntityNotFoundErrorInfo(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
