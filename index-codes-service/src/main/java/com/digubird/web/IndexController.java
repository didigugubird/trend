package com.digubird.web;

import com.digubird.config.IPConfiguration;
import com.digubird.pojo.Index;
import com.digubird.service.IndexService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {
    final
    IndexService indexService;
    final IPConfiguration ipConfiguration;

    public IndexController(IndexService indexService, IPConfiguration ipConfiguration) {
        this.indexService = indexService;
        this.ipConfiguration = ipConfiguration;
    }

    @GetMapping("/codes")
    @CrossOrigin
    public List<Index> codes() throws Exception {
        System.out.println("current instance's port is " + ipConfiguration.getPort());
        return indexService.get();
    }
}
