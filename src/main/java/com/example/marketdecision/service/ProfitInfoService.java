package com.example.marketdecision.service;

import com.example.marketdecision.Bean.ProfitInfo;
import com.example.marketdecision.Bean.ProfitInfoCal;
import com.example.marketdecision.Bean.SaleCurrencyInfo;
import com.example.marketdecision.dao.ProfitInfoDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfitInfoService
{
    @Resource
    private ProfitInfoDao profitInfoDao;

    public PageInfo<ProfitInfo> initProfitInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();

        // 获取所有的利润信息
        List<ProfitInfo> profitInfos = null;
        try {
            profitInfos = getProfitInfo(profitInfoDao.maxSaleDate());
        } catch (Exception e) {
            return null;
        }

        // 手动进行分页
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, profitInfos.size());

        // 截取需要的分页数据
        List<ProfitInfo> subList = profitInfos.subList(startIndex, endIndex);

        // 创建 PageInfo 对象，手动设置总记录数
        PageInfo<ProfitInfo> pageInfo = new PageInfo<>(subList);
        pageInfo.setTotal(profitInfos.size());

        return pageInfo;
    }

    public PageInfo<ProfitInfo> getProfitInfoByDate(String date, PageRequest pageRequest)
    {
        int pageNum = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();

        // 获取所有的利润信息
        List<ProfitInfo> profitInfos = null;
        try {
            profitInfos = getProfitInfo(date);
        } catch (Exception e) {
            return null;
        }

        // 手动进行分页
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, profitInfos.size());

        // 截取需要的分页数据
        List<ProfitInfo> subList = profitInfos.subList(startIndex, endIndex);

        // 创建 PageInfo 对象，手动设置总记录数
        PageInfo<ProfitInfo> pageInfo = new PageInfo<>(subList);
        pageInfo.setTotal(profitInfos.size());

        return pageInfo;
    }


    /**
     * 根据日期返回利润信息
     * @return 利润信息列表
     */
    public List<ProfitInfo> getProfitInfo(String saleDate) throws Exception
    {
        List<ProfitInfoCal> profitInfoCals = profitInfoDao.getProfitInfoCal(saleDate);

        List<ProfitInfo> profitInfos = new ArrayList<>();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        for (ProfitInfoCal cal: profitInfoCals)
        {
            String date = cal.getDate();
            String goodsCode = cal.getGoodsCode();
            String name = cal.getName();
            String wholeSale = cal.getWholeSale();
            String loss = cal.getLoss();
            String saleNumber = cal.getSaleNumber();
            String sumPrice = cal.getSumPrice();
            String typeName = cal.getTypeName();

            //计算利润
            double profitValue = Double.parseDouble(sumPrice) - Double.parseDouble(saleNumber) * (1.0 + Double.parseDouble(loss) / 100.0) * Double.parseDouble(wholeSale);
            String profit = decimalFormat.format(profitValue);

            loss = decimalFormat.format(Double.parseDouble(loss));
            saleNumber = decimalFormat.format(Double.parseDouble(saleNumber));
            sumPrice = decimalFormat.format(Double.parseDouble(sumPrice));
            wholeSale = decimalFormat.format(Double.parseDouble(wholeSale));

            ProfitInfo profitInfo = new ProfitInfo(date, goodsCode, name, typeName, wholeSale, loss, sumPrice, saleNumber, profit);
            profitInfos.add(profitInfo);
        }
        return profitInfos;

    }

}
