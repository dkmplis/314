package ru.kata.spring.boot_security.demo.util;

import java.util.Map;

public class ValidErrorsInfo {

    private Map<String, String> errors;

    public ValidErrorsInfo(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
