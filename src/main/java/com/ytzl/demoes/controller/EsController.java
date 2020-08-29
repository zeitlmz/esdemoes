package com.ytzl.demoes.controller;

import com.ytzl.demoes.entity.Students;
import com.ytzl.demoes.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zeitlmz
 * @date 2020/8/29 11:50
 */
@RestController
@RequestMapping("/esDocument")
public class EsController {
    @Autowired
    private EsService esService;

    /**
     * 将数据库内的信息保存到es
     * @author zeitlmz
     * @date 2020/8/29 15:02
     */
    @RequestMapping("/saveAll")
    public void saveAll() {
        esService.saveAll();
    }

    /**
     * 查询所有信息
     * @author zeitlmz
     * @date 2020/8/29 15:01
     */
    @RequestMapping("/findAll")
    public Iterable<Students> findAll() {
        return esService.findAll();
    }

    /**
     * 按条件搜索信息有分页和高亮
     * @author zeitlmz
     * @date 2020/8/29 15:01
     * @param text 搜索条件
     */
    @GetMapping("/search")
    public List<Students> search(@RequestParam("text") String text) {
        return esService.improveSearch(text);
    }
}
