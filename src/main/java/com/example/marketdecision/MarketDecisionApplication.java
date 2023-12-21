package com.example.marketdecision;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MapperScan("com.example.marketdecision.dbc")
@SpringBootApplication
public class MarketDecisionApplication {

    @RequestMapping("/")
    String home()
    {
        return "fuck you!!!";
    }

    public static void main(String[] args) {
        SpringApplication.run(MarketDecisionApplication.class, args);
    }

}
