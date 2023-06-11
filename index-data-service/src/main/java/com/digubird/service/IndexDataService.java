package com.digubird.service;

import cn.hutool.core.collection.CollUtil;
import com.digubird.pojo.IndexData;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "index_data")
public class IndexDataService {
    @Cacheable(key = "'code_'+#p0")
    public List<IndexData> get(String code) {
        IndexData indexData = new IndexData();
        indexData.setClosePoint(0);
        indexData.setDate("无效指数数据代码");
        return CollUtil.toList(indexData);
    }
}
