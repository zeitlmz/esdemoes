package com.ytzl.demoes.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * (Students)实体类
 *
 * @author lmz
 * @since 2020-08-29 11:03:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "rsdoct", type = "rsbean")
public class Students extends Model<Students> implements Serializable {
    /**
     * 标识
     */
    private Long id;
    /**
     * 学生姓名
     */
    private String name;
    /**
     * 学生详细信息
     */
    private String desc;

}
