package com.example.demo.util;

public interface IMessageUtil {

    String getMessage(String key);

    String getMessage(String key, Object... args);
}
