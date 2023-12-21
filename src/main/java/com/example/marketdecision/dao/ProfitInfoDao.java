package com.example.marketdecision.dao;

import com.example.marketdecision.Bean.ProfitInfoCal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProfitInfoDao
{
    /**
     * 根据日期计算利润
     *
     * @return 利润计算需要的数据包装类
     */
    @Select("select A.SaleDate, B.GoodsCode, B.Name, E.TypeName, avg(C.Wholesale) as Wholesale, " +
            "       avg(D.Attrition_Rate) as Loss, sum(A.SaleNumber * A.UnitPrice) as summary, " +
            "       sum(A.SaleNumber) as total " +
            "from SaleCurrency as A, Goods as B, PriceInfo as C, Loss as D, GoodsType as E " +
            "where A.SaleDate = #{saleDate} and A.GoodsCode = B.GoodsCode and B.GoodsCode = C.GoodsCode " +
            "        and C.GoodsCode = D.GoodsCode and A.SaleDate = C.Date and B.TypeCode = E.TypeCode " +
            "group by A.GoodsCode ")
    List<ProfitInfoCal> getProfitInfoCal(@Param("saleDate") String saleDate);

    /**
     * 根据日期计算利润数量
     */
    @Select("select count(A.GoodsCode) " +
            "from SaleCurrency as A, Goods as B, PriceInfo as C, Loss as D, GoodsType as E " +
            "where A.SaleDate = #{saleDate} and A.GoodsCode = B.GoodsCode and B.GoodsCode = C.GoodsCode " +
            "        and C.GoodsCode = D.GoodsCode and A.SaleDate = C.Date and B.TypeCode = E.TypeCode " +
            "group by A.GoodsCode ")
    int getProfitInfoCalNum(@Param("saleDate") String saleDate);

    /**
     * 返回最新一天的日期
     */
    @Select("select max(saleDate) from saleCurrency ")
    String maxSaleDate();
}
