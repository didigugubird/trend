package com.digubird.web;

import com.digubird.config.IPConfiguration;
import com.digubird.pojo.IndexData;
import com.digubird.service.IndexDataService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexDataController {
    final
    IndexDataService indexDataService;
    final IPConfiguration ipConfiguration;

    public IndexDataController(IndexDataService indexDataService, IPConfiguration ipConfiguration) {
        this.indexDataService = indexDataService;
        this.ipConfiguration = ipConfiguration;
    }

    @GetMapping("/data/{code}")
    @CrossOrigin
    public List<IndexData> get(@PathVariable("code") String code)  {
        return indexDataService.get(code);
    }
}
