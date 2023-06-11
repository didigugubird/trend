package com.digubird.web;

import com.digubird.pojo.IndexData;
import com.digubird.service.IndexDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexDataController {
    final
    IndexDataService indexDataService;

    public IndexDataController(IndexDataService indexDataService) {
        this.indexDataService = indexDataService;
    }

    @GetMapping("/getIndexData/{code}")
    public List<IndexData> get(@PathVariable("code") String code) {
        return indexDataService.get(code);
    }

    @GetMapping("/removeIndexData/{code}")
    public String remove(@PathVariable("code") String code) {
        indexDataService.remove(code);
        return "remove index_data successfully";
    }

    @GetMapping("/freshIndexData/{code}")
    public String fresh(@PathVariable("code") String code) {
        indexDataService.fresh(code);
        return "index_data fresh successfully";
    }
}
