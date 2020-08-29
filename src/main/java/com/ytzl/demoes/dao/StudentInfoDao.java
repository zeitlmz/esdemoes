package com.ytzl.demoes.dao;

import com.ytzl.demoes.entity.Students;

import java.util.List;

public interface StudentInfoDao{
    List<Students> selectAll();
}
