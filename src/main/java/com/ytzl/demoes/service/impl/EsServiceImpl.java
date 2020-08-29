package com.ytzl.demoes.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ytzl.demoes.dao.EsDao;
import com.ytzl.demoes.dao.StudentInfoDao;
import com.ytzl.demoes.entity.Students;
import com.ytzl.demoes.service.EsService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zeitlmz
 * @date 2020/8/29 11:22
 */
@Service("esService")
public class EsServiceImpl implements EsService {
    @Resource
    private EsDao esDao;
    @Resource
    private StudentInfoDao studentInfoDao;
    @Resource
    private RestHighLevelClient client;

    @Override
    public void saveAll() {
        List<Students> students = studentInfoDao.selectAll();
        Long id = 1L;
        for (Students student : students) {
            student.setId(id);
            id++;
        }
        esDao.saveAll(students);
    }

    @Override
    public Iterable<Students> findAll() {
        return esDao.findAll();
    }

    @Override
    public List<Students> improveSearch(String text) {
        SearchRequest searchRequest = new SearchRequest("rsdoct");
        searchRequest.types("rsbean");
        //构造资源搜索对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //多条件搜索的字段
        MultiMatchQueryBuilder matchQueryBuilder = new MultiMatchQueryBuilder(text, "name", "desc").
                field("name", 10).
                field("desc", 5);
        //添加条件到布尔查询
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(matchQueryBuilder);
        searchSourceBuilder.query(matchQueryBuilder);
        searchSourceBuilder.size(100);
        searchSourceBuilder.from(0);
        //高亮查询
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<em><span style='color:red'>");
        highlightBuilder.postTags("</span></em>");
        //配置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("desc"));
        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest);
            return obtain(searchResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Students> obtain(SearchResponse searchResponse) {
        //搜索结果
        SearchHits searchResponseHits = searchResponse.getHits();
        //获取搜索结果条数
        long totalHits = searchResponseHits.getTotalHits();
        //匹配度高的结果
        SearchHit[] searchHits = searchResponseHits.getHits();
        //新建存储转换成实体类的集合
        ArrayList<Students> allInfoBean = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            //使用java提供的方法，把获取到的资源用字符串的形式放回
            String content = hit.getSourceAsString();
            //使用FastJSON工具将json格式字符串转换成指定类型的实体类
            Students student = JSONObject.parseObject(content, Students.class);
            //将转换好的实体类放进集合
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null) {
                HighlightField nameField = highlightFields.get("name");
                HighlightField descField = highlightFields.get("desc");
                if (nameField != null) {
                    Text[] nameFieldFragments = nameField.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text nameFieldFragment : nameFieldFragments) {
                        stringBuffer.append(nameFieldFragment);
                    }
                    String name = stringBuffer.toString();
                    student.setName(name);
                }

                if (descField != null) {
                    Text[] descFieldFragments = descField.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text descFieldFragment : descFieldFragments) {
                        stringBuffer.append(descFieldFragment);
                    }
                    String desc = stringBuffer.toString();
                    student.setDesc(desc);
                }
            }
            allInfoBean.add(student);
        }
        return allInfoBean;
    }
}
