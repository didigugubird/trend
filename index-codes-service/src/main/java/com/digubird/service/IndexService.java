package com.digubird.service;

import cn.hutool.core.collection.CollUtil;
import com.digubird.pojo.Index;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "indexes")
public class IndexService {
    @Cacheable(key = "'all_codes'")
    public List<Index> get() {
        Index index = new Index();
        index.setName("无效指数代码");
        index.setCode("000000");
        return CollUtil.toList(index);
    }
}
