package com.example.marketdecision.service;

import com.example.marketdecision.Bean.Decision;
import com.example.marketdecision.Bean.ProfitInfoCal;
import com.example.marketdecision.dao.ProfitInfoDao;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
@Service
public class DecisionService
{
    @Resource
    ProfitInfoDao profitInfoDao;

    public String getMaxDate()
    {
        return profitInfoDao.maxSaleDate();
    }

    public PageInfo<Decision> initProfitInfo(String saleDate, PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();

        List<Decision> decisions;
        try {
            decisions = makeDecision(saleDate);
        } catch (Exception e) {
            return null;
        }

        // 手动进行分页
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, decisions.size());

        // 截取需要的分页数据
        List<Decision> subList = decisions.subList(startIndex, endIndex);

        // 创建 PageInfo 对象，手动设置总记录数
        PageInfo<Decision> pageInfo = new PageInfo<>(subList);
        pageInfo.setTotal(decisions.size());

        return pageInfo;
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private List<Decision> makeDecision(String saleDate) throws Exception {
        List<ProfitInfoCal> cals = profitInfoDao.getProfitInfoCal(saleDate);

        List<Decision> decisions = new ArrayList<>();

        for (ProfitInfoCal value : cals) {
            double theoreticalSales;
            double saleNumber = Double.parseDouble(value.getSaleNumber());
            double unitPrice = Double.parseDouble(value.getSumPrice()) / Double.parseDouble(value.getSaleNumber());
            double afterUnitPrice = unitPrice;
            switch (value.getTypeName()) {
                case "花菜类":
                    theoreticalSales = huacai(unitPrice);
                    if (theoreticalSales > saleNumber)
                        afterUnitPrice = afterUnitPrice - inverseHuacai(theoreticalSales - saleNumber);
                    break;
                case "花叶类":
                    theoreticalSales = huaye(unitPrice);
                    if (theoreticalSales > saleNumber)
                        afterUnitPrice = afterUnitPrice - inverseHuaye(theoreticalSales - saleNumber);
                    break;
                case "辣椒类":
                    theoreticalSales = lajiao(unitPrice);
                    if(theoreticalSales > saleNumber)
                        afterUnitPrice = afterUnitPrice - inverseLajiao(theoreticalSales - saleNumber);
                    break;
                case "茄类":
                    theoreticalSales = qie(unitPrice);
                    if(theoreticalSales > saleNumber)
                        afterUnitPrice = afterUnitPrice - inverseQie(theoreticalSales - saleNumber);
                    break;
                case "食用菌":
                    theoreticalSales = shiyongjun(unitPrice);
                    if(theoreticalSales > saleNumber)
                        afterUnitPrice = afterUnitPrice - inverseShiyongjun(theoreticalSales - saleNumber);
                    break;
                case "水生根茎类":
                    theoreticalSales = genjing(unitPrice);
                    if(theoreticalSales > saleNumber)
                        afterUnitPrice = afterUnitPrice - inverseGenjing(theoreticalSales - saleNumber);
                    break;
            }

            // 将浮点数值舍入到小数点后两位
            unitPrice = Double.parseDouble(decimalFormat.format(unitPrice));
            afterUnitPrice = Double.parseDouble(decimalFormat.format(afterUnitPrice));


            String isSend = "";
            double profitRate;
            if(afterUnitPrice <= 0)
            {
                isSend = "15点后赠送";
                afterUnitPrice = 0;
            } else {
                for (int i = 0; i < 4; i++) {
                    if (i != 3) {
                        profitRate = ((10.0 - i) / 10 * unitPrice - ((1.0 + Double.parseDouble(value.getLoss()) / 100.0) * Double.parseDouble(value.getWholeSale()))) / Double.parseDouble(value.getWholeSale());
                        if (profitRate < 0.1) {
                            isSend = String.format("%s%s点后赠送", isSend, 15 + 2 * i);
                            break;
                        } else
                            isSend = String.format("%s%s点后%s折,", isSend, 15 + 2 * i, 9 - i);
                    } else isSend = String.format("%s21点后赠送", isSend);
                }
            }

            decisions.add(new Decision(value.getDate(), value.getGoodsCode(), value.getName(), decimalFormat.format(Double.parseDouble(value.getWholeSale())), decimalFormat.format(Double.parseDouble(value.getLoss())), decimalFormat.format(Double.parseDouble(value.getSaleNumber())),
                    String.valueOf(unitPrice), String.valueOf(afterUnitPrice), isSend));
        }

        return decisions;
    }

    private static double inverseHuacai(double y) {
        // 从 -2.6346 * x + 63.492 = y 解出 x
        return (y - 63.492) / -2.6346;
    }

    private static double inverseHuaye(double y) {
        // 从 -12.665 * x + 262 = y 解出 x
        return (y - 262) / -12.665;
    }

    private static double inverseLajiao(double y) {
        // 从 -1.3698 * x + 98.45 = y 解出 x
        return (y - 98.45) / -1.3698;
    }

    private static double inverseQie(double y) {
        // 从 -0.9659 * x + 29.96 = y 解出 x
        return (y - 29.96) / -0.9659;
    }

    private static double inverseShiyongjun(double y) {
        // 从 -6.4266 * x + 148.19 = y 解出 x
        return (y - 148.19) / -6.4266;
    }

    private static double inverseGenjing(double y) {
        // 从 -2.5321 * x + 63.663 = y 解出 x
        return (y - 63.663) / -2.5321;
    }
    private static double huacai(double x) {
        return -2.6346 * x + 63.492;
    }

    private static double huaye(double x) {
        return -12.665 * x + 262;
    }

    private static double lajiao(double x) {
        return -1.3698 * x + 98.45;
    }

    private static double qie(double x) {
        return -0.9659 * x + 29.96;
    }

    private static double shiyongjun(double x) {
        return -6.4266 * x + 148.19;
    }

    private static double genjing(double x) {
        return -2.5321 * x + 63.663;
    }




}
