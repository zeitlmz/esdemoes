package com.ytzl.demoes.dao;

import com.ytzl.demoes.entity.Students;
import org.springframework.data.repository.CrudRepository;

/**
 * @author zeitlmz
 * @date 2020/8/29 11:22
 */
public interface EsDao extends CrudRepository<Students,Long>{
}
