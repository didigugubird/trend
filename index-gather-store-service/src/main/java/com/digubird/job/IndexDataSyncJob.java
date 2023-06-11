package com.digubird.job;

import cn.hutool.core.date.DateUtil;
import com.digubird.pojo.Index;
import com.digubird.service.IndexDataService;
import com.digubird.service.IndexService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndexDataSyncJob {
    private final IndexService indexService;

    private final IndexDataService indexDataService;

    public IndexDataSyncJob(IndexService indexService, IndexDataService indexDataService) {
        this.indexService = indexService;
        this.indexDataService = indexDataService;
    }

    @Scheduled(cron = "0 0 4 * * ?")
    protected void executeInternal() {
        System.out.println("定时启动：" + DateUtil.now());
        List<Index> indexes = indexService.fresh();
        for (Index index : indexes) {
            indexDataService.fresh(index.getCode());
        }
        System.out.println("定时结束：" + DateUtil.now());
    }
}
