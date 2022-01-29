package br.com.zaratech.util;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestClass {

    public static void main(String[] args) {

        mapValues("32767", "sqlq");
    }

    public static Map<String, String> mapValues(String value, String name) {

        Map<String, String> values = new HashMap<>();
        values.put(value, name);
        log.info(values.toString());
        return values;
    }
}
