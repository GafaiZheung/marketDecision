package com.example.marketdecision.dao;

import com.example.marketdecision.Bean.SaleCurrencyInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SaleCurrencyDao {
    /**
     * 返回所有列名
     * @return 列名
     */
    @Select(value = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'SaleCurrency'")
    List<String> getAllColumnNames();
    @Insert("INSERT INTO SaleCurrency (saleDate, saleTime, goodsCode, ${target}) VALUES (#{saleDate}, #{saleTime}, #{goodsCode}, #{value}) ON DUPLICATE KEY UPDATE ${target} = #{value}")
    void handleExcelColumns(@Param("saleDate") String saleDate,
                            @Param("saleTime") String saleTime,
                            @Param("goodsCode") String goodsCode,
                            @Param("target") String target,
                            @Param("value") String value);

    /**
     * 返回最新一天的销售流水明细
     * @return 销售流水列表
     */
    @Select("SELECT * " +
            "FROM SaleCurrency " +
            "WHERE SaleDate = (SELECT MAX(SaleDate) FROM SaleCurrency) ")
    List<SaleCurrencyInfo> getAllSaleCurrencies();

    /**
     * 返回最新一天的销售流水明细数量
     * @return 销售流水条数
     */
    @Select("SELECT COUNT(GoodsCode) " +
            "FROM SaleCurrency " +
            "WHERE SaleDate = (SELECT MAX(SaleDate) FROM SaleCurrency); ")
    int getAllSaleCurrenciesNum();

    /**
     * 返回指定日期的销售流水明细
     */
    @Select("select * " +
            "from SaleCurrency " +
            "where SaleDate = #{saleDate}")
    List<SaleCurrencyInfo> getSaleCurrencyByDate(@Param("saleDate") String saleDate);

    /**
     * 返回指定日期的销售流水明细数量
     */
    @Select("SELECT COUNT(GoodsCode) " +
            "FROM SaleCurrency " +
            "WHERE SaleDate = #{saleDate};")
    int getSaleCurrencyByDateNum(@Param("saleDate") String saleDate);

    /**
     * 新增销售流水明细
     */
    @Insert("INSERT INTO SaleCurrency(SaleDate, SaleTime, GoodsCode, SaleNumber, UnitPrice, SaleType, isDiscount) " +
            "VALUES (#{saleDate}, #{saleTime}, #{goodsCode}, #{saleNumber}, #{unitPrice}, #{saleType}, #{discount})")
    void insertSaleCurrency(@Param("saleDate") String saleDate, @Param("saleTime") String saleTime,
                            @Param("goodsCode") String goodsCode, @Param("saleNumber") String saleNumber,
                            @Param("unitPrice") String unitPrice, @Param("saleType") String saleType,
                            @Param("discount") String discount);

    /**
     * 修改销售流水明细中的销售数量
     */
    @Update("UPDATE SaleCurrency SET SaleNumber = #{saleNumber} WHERE GoodsCode = #{goodsCode}")
    void updateSaleNumber(@Param("saleNumber") String saleNumber, @Param("goodsCode") String goodsCode);

    /**
     * 修改销售流水明细中的销售单价
     */
    @Update("UPDATE SaleCurrency SET UnitPrice = #{unitPrice} WHERE GoodsCode = #{goodsCode}")
    void updateUnitPrice(@Param("unitPrice") String unitPrice, @Param("goodsCode") String goodsCode);

    /**
     * 修改销售流水明细中的销售日期和时间
     */
    @Update("UPDATE SaleCurrency SET SaleDate = #{saleDate}, SaleTime = #{saleTime} WHERE GoodsCode = #{goodsCode}")
    void updateSaleDateTime(@Param("saleDate") String saleDate, @Param("saleTime") String saleTime, @Param("goodsCode") String goodsCode);

    /**
     * 删除销售流水明细
     */
    @Delete("DELETE FROM SaleCurrency WHERE saleDate = #{saleDate} " +
            "and saleTime = #{saleTime} and GoodsCode = #{goodsCode}")
    void deleteSaleCurrency(@Param("goodsCode") String goodsCode,
                            @Param("saleTime") String saleTime,
                            @Param("saleDate") String saleDate);

    @Update("update saleCurrency set saleNumber = #{saleNumber}, unitPrice = #{unitPrice}, " +
            "saleType = #{saleType}, isDisCount = #{discount} " +
            "where saleDate = #{saleDate} and saleTime = #{saleTime} and goodsCode = #{goodsCode} ")
    void updateSaleCurrency(@Param("saleDate") String saleDate, @Param("saleTime") String saleTime,
                            @Param("goodsCode") String goodsCode, @Param("saleNumber") String saleNumber,
                            @Param("unitPrice") String unitPrice, @Param("saleType") String saleType,
                            @Param("discount") String discount);
}
