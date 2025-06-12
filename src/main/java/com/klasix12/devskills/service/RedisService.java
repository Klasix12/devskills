package com.klasix12.devskills.service;

public interface RedisService {
    void save(String key, String value, long duration);
    void delete(String key);
    boolean exists(String key);
}
