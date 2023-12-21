package com.example.marketdecision.controller;

import com.example.marketdecision.Bean.Decision;
import com.example.marketdecision.Bean.DecisionsReturnData;

import com.example.marketdecision.service.DecisionService;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/decision")
public class DecisionsController
{
    @Resource
    private DecisionService decisionService;

    @RequestMapping(value = "getDecision", method = RequestMethod.GET)
    public DecisionsReturnData getDecision(@RequestParam int pageNum, @RequestParam int pageSize) {
        try{
            PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
            PageInfo<Decision> decisionPageInfo = decisionService.initProfitInfo(decisionService.getMaxDate(), pageRequest);

            List<Decision> decisions = decisionPageInfo.getList();
            int total = (int) decisionPageInfo.getTotal();

            if (!decisions.isEmpty()) {
                return new DecisionsReturnData(1, total, decisions);
            } else {
                return new DecisionsReturnData(404, total, decisions);
            }
        }catch (Exception e)
        {
            return new DecisionsReturnData(404, 0, null);
        }
    }

    @RequestMapping(value = "getDecisionByDate", method = RequestMethod.GET)
    public DecisionsReturnData getDecisionByDate(@RequestParam String date, @RequestParam int pageNum, @RequestParam int pageSize)
    {
        try{
            PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
            PageInfo<Decision> decisionPageInfo = decisionService.initProfitInfo(date, pageRequest);

            List<Decision> decisions = decisionPageInfo.getList();
            int total = (int) decisionPageInfo.getTotal();

            if (!decisions.isEmpty()) {
                return new DecisionsReturnData(1, total, decisions);
            } else {
                return new DecisionsReturnData(404, total, decisions);
            }
        }catch (Exception e)
        {
            return new DecisionsReturnData(404, 0, null);
        }
    }
}
