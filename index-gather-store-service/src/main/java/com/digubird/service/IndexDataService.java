package com.digubird.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.digubird.pojo.IndexData;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "index_data")
public class IndexDataService {
    final
    RestTemplate restTemplate;
    private List<IndexData> indexDatas;

    public IndexDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(key = "'code_'+#p0")
    public List<IndexData> get(String code) {
        return CollUtil.toList();
    }

    @CacheEvict(key = "'code_'+#p0")
    public void remove(String code) {
    }

    @HystrixCommand(fallbackMethod = "third_part_not_connected")
    @CachePut(key = "'code_'+#p0", unless = "#result[0].date=='0'")
    public List<IndexData> fresh(String code) {
        return fetch_IndexData_from_third_part(code);
    }

    private List<IndexData> fetch_IndexData_from_third_part(String code) {
        List<Map> temp = restTemplate.getForObject("http://127.0.0.1:8762/indexes/" + code + ".json", List.class);
        return mapIndexData(temp);
    }

    private List<IndexData> mapIndexData(List<Map> temp) {
        indexDatas = new ArrayList<>();

        for (Map map : temp) {
            String date = map.get("date").toString();
            Float closePoint = Convert.toFloat(map.get("closePoint"));
            IndexData indexData = new IndexData();
            indexData.setDate(date);
            indexData.setClosePoint(closePoint);
            indexDatas.add(indexData);
        }
        return indexDatas;
    }

    private List<IndexData> third_part_not_connected(String code) {
        System.out.printf("third_part not connected()");
        IndexData indexData = new IndexData();
        indexData.setDate("0");
        indexData.setClosePoint(0);
        return CollUtil.toList(indexData);
    }


}
