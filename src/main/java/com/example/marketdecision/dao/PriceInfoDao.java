package com.example.marketdecision.dao;

import com.example.marketdecision.Bean.PriceInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PriceInfoDao {
    /**
     * 返回所有列名
     * @return 列名
     */
    @Select(value = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'PriceInfo'")
    List<String> getAllColumnNames();
    @Insert("INSERT INTO PriceInfo (GoodsCode, WholeSale, Date) VALUES (#{goodsCode}, #{wholesale}, #{date})")
    void handleExcelColumns(@Param("goodsCode") String goodsCode,
                            @Param("wholesale") String wholesale,
                            @Param("date") String date);

    /**
     * 返回所有批发价信息
     */
    @Select("SELECT GoodsCode, Date, ROUND(AVG(Wholesale), 2) AS Wholesale " +
            "FROM priceInfo p1 " +
            "WHERE EXISTS (SELECT 1 FROM (SELECT DISTINCT Date FROM priceInfo ORDER BY Date DESC LIMIT 3) AS recent_dates WHERE p1.Date = recent_dates.Date) " +
            "GROUP BY GoodsCode, Date " +
            "ORDER BY Date DESC")
    List<PriceInfo> getAllPriceInfo();




    /**
     * 返回所有批发价信息数量
     */
    @Select("SELECT COUNT(GoodsCode) " +
            "FROM priceInfo " +
            "WHERE Date IN (SELECT recent_dates.Date FROM (SELECT DISTINCT Date FROM priceInfo ORDER BY Date DESC LIMIT 3) AS recent_dates)")
    int getAllPriceInfoNum();



    /**
     * 返回指定日期的批发价信息
     */
    @Select("SELECT GoodsCode, #{date} AS SpecifiedDate, ROUND(AVG(Wholesale), 2) AS AveragePrice " +
            "FROM priceInfo " +
            "WHERE Date = #{date} " +
            "GROUP BY GoodsCode ")
    List<PriceInfo> getPriceInfoByDate(@Param("date") String date);

    /**
     * 返回指定日期的批发价信息数量
     */
    @Select("SELECT COUNT(GoodsCode) FROM priceInfo " +
            "WHERE Date = #{date}")
    int getPriceInfoByDateNum(@Param("date") String date);

    /**
     * 新增批发价信息
     */
    @Insert("insert into PriceInfo(GoodsCode, Wholesale, Date) Values (#{goodsCode}, #{wholesale}, #{date})")
    void insertPriceInfo(@Param("goodsCode") String goodsCode, @Param("wholesale") String wholesale, @Param("date") String date);

    /**
     * 修改批发价格
     */
    @Update("update PriceInfo set Wholesale = #{wholesale} where GoodsCode = #{goodsCode} and Date = #{date}")
    void updateWholesalePrice(@Param("wholesale") String wholesale, @Param("goodsCode") String goodsCode, @Param("date") String date);

    /**
     * 修改日期
     */
    @Update("update PriceInfo set Date = #{date} where GoodsCode = #{goodsCode}")
    void updatePriceInfoDate(@Param("goodsCode") String goodsCode, @Param("date") String date);

    /**
     * 删除批发价信息
     */
    @Delete("delete from PriceInfo where GoodsCode = #{goodsCode} and Date = #{date}")
    void deletePriceInfo(@Param("goodsCode") String goodsCode, @Param("date") String date);
}
