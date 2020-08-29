package com.ytzl.demoes.service;

import com.ytzl.demoes.entity.Students;

import java.util.List;

/**
 * @author zeitlmz
 * @date 2020/8/29 11:22
 */
public interface EsService {
    void saveAll();

    Iterable<Students> findAll();

    List<Students> improveSearch(String text);
}
