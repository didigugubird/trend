package com.digubird.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.digubird.pojo.IndexData;
import com.digubird.pojo.Profit;
import com.digubird.pojo.Trade;
import com.digubird.pojo.AnnualProfit;
import com.digubird.service.BackTestService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.util.*;

@RestController
public class BackTestController {
    final BackTestService backTestService;
    public BackTestController (BackTestService backTestService){
        this.backTestService = backTestService;
    }

    @GetMapping("/simulate/{code}/{ma}/{buyThreshold}/{sellThreshold}/{serviceCharge}/{startDate}/{endDate}")
    @CrossOrigin
    public Map<String,Object> backTest(
            @PathVariable("code") String code
            ,@PathVariable("ma") int ma
            ,@PathVariable("buyThreshold") float buyThreshold
            ,@PathVariable("sellThreshold") float sellThreshold
            ,@PathVariable("serviceCharge") float serviceCharge
            ,@PathVariable("startDate") String strStartDate
            ,@PathVariable("endDate") String strEndDate
    ) {
        List<IndexData> allIndexData = backTestService.listIndexData(code);

        String indexStartDate = allIndexData.get(0).getDate();
        String indexEndDate = allIndexData.get(allIndexData.size()-1).getDate();

        allIndexData = filterByDateRange(allIndexData,strStartDate, strEndDate);

        Map<String,?> simulateResult= backTestService.simulate(ma, sellThreshold, buyThreshold,serviceCharge, allIndexData);
        List<Profit> profits = (List<Profit>) simulateResult.get("profits");
        List<Trade> trades = (List<Trade>) simulateResult.get("trades");

        float years = backTestService.getYear(allIndexData);
        float indexIncomeTotal = (allIndexData.get(allIndexData.size()-1).getClosePoint() - allIndexData.get(0).getClosePoint()) / allIndexData.get(0).getClosePoint();
        float indexIncomeAnnual = (float) Math.pow(1+indexIncomeTotal, 1/years) - 1;
        float trendIncomeTotal = (profits.get(profits.size()-1).getValue() - profits.get(0).getValue()) / profits.get(0).getValue();
        float trendIncomeAnnual = (float) Math.pow(1+trendIncomeTotal, 1/years) - 1;

        int winCount = (Integer) simulateResult.get("winCount");
        int lossCount = (Integer) simulateResult.get("lossCount");
        float avgWinRate = (Float) simulateResult.get("avgWinRate");
        float avgLossRate = (Float) simulateResult.get("avgLossRate");

        List<AnnualProfit> annualProfits = (List<AnnualProfit>) simulateResult.get("annualProfits");

        Map<String,Object> result = new HashMap<>();
        result.put("indexData", allIndexData);
        result.put("indexStartDate", indexStartDate);
        result.put("indexEndDate", indexEndDate);
        result.put("profits", profits);
        result.put("trades", trades);
        result.put("years", years);
        result.put("indexIncomeTotal", indexIncomeTotal);
        result.put("indexIncomeAnnual", indexIncomeAnnual);
        result.put("trendIncomeTotal", trendIncomeTotal);
        result.put("trendIncomeAnnual", trendIncomeAnnual);

        result.put("winCount", winCount);
        result.put("lossCount", lossCount);
        result.put("avgWinRate", avgWinRate);
        result.put("avgLossRate", avgLossRate);

        result.put("annualProfits", annualProfits);

        return result;
    }
    private List<IndexData> filterByDateRange(List<IndexData> allIndexData, String strStartDate, String strEndDate) {
        if(StrUtil.isBlankOrUndefined(strStartDate) || StrUtil.isBlankOrUndefined(strEndDate) )
            return allIndexData;

        List<IndexData> result = new ArrayList<>();
        Date startDate = DateUtil.parse(strStartDate);
        Date endDate = DateUtil.parse(strEndDate);

        for (IndexData indexData : allIndexData) {
            Date date =DateUtil.parse(indexData.getDate());
            if(
                    date.getTime()>=startDate.getTime() &&
                            date.getTime()<=endDate.getTime()
            ) {
                result.add(indexData);
            }
        }
        return result;
    }
}
