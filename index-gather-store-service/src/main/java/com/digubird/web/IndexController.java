package com.digubird.web;

import com.digubird.pojo.Index;
import com.digubird.service.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {
    final
    IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping("/getCodes")
    public List<Index> get() {
        return indexService.get();
    }

    @GetMapping("/freshCodes")
    public String fresh() {
        indexService.fresh();
        return "fresh code successfully";
    }

    @GetMapping("/removeCodes")
    public String remove() {
        indexService.remove();
        return "Cache removed!";
    }
}
