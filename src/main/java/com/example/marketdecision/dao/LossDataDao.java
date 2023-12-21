package com.example.marketdecision.dao;

import com.example.marketdecision.Bean.LossData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LossDataDao {
    @Insert("INSERT INTO Loss (GoodsCode, ${target}) VALUES (#{goodsCode}, #{value}) " +
            "ON DUPLICATE KEY UPDATE ${target} = #{value}")
    void handleExcelColumns(@Param("target") String target,
                            @Param("value") String value,
                            @Param("goodsCode") String goodsCode);

    /**
     * 新增损耗数据
     */
    @Insert("insert into Loss(GoodsCode, Attrition_Rate) Values (#{goodsCode}, #{attritionRate})")
    void insertLossData(@Param("goodsCode") String goodsCode, @Param("attritionRate") String attritionRate);

    /**
     * 修改损耗数据
     */
    @Update("update Loss set Attrition_Rate = #{attritionRate} where GoodsCode = #{goodsCode}")
    void updateLossData(@Param("goodsCode") String goodsCode, @Param("attritionRate") String attritionRate);

    /**
     * 返回所有损耗数据（分页）
     */
    @Select("select Loss.GoodsCode,  Goods.Name, Attrition_Rate from Loss, Goods " +
            "where Loss.GoodsCode = Goods.GoodsCode")
    List<LossData> getAllLossData();

    /**
     * 返回所有损耗数据数量
     */
    @Select("select count(Loss.GoodsCode) from Loss, Goods " +
            "where Loss.GoodsCode = Goods.GoodsCode")
    int getAllLossDataNum();

    /**
     * 根据关键字模糊查询损耗数据（分页）
     *
     * @param keyword 查询关键字
     * @return 匹配的损耗数据列表
     */
    @Select("select Loss.GoodsCode,  Goods.Name, Attrition_Rate from Loss, Goods " +
            "where Loss.GoodsCode = Goods.GoodsCode and (Goods.Name like concat('%', #{keyword}, '%') or Loss.GoodsCode like concat('%', #{keyword}, '%'))")
    List<LossData> searchLossData(@Param("keyword") String keyword);

    /**
     * 根据关键字模糊查询损耗数据数量
     *
     * @param keyword 查询关键字
     * @return 匹配的损耗数据数量
     */
    @Select("select count(Loss.GoodsCode) from Loss, Goods " +
            "where Loss.GoodsCode = Goods.GoodsCode and (Goods.Name like concat('%', #{keyword}, '%') or Loss.GoodsCode like concat('%', #{keyword}, '%'))")
    int searchLossDataNum(@Param("keyword") String keyword);


    /**
     * 删除损耗数据
     */
    @Delete("delete from Loss where GoodsCode = #{goodsCode}")
    void deleteLossData(@Param("goodsCode") String goodsCode);

    /**
     * 返回所有列名
     * @return 列名
     */
    @Select(value = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Loss'")
    List<String> getAllColumnNames();
}
