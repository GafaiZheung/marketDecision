package com.example.marketdecision.dao;

import com.example.marketdecision.Bean.GoodsInfo;
import com.example.marketdecision.Bean.GoodsTypesInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface GoodsInfoDao
{

    //查看所有商品信息
    @Select("select GoodsCode, Name, Goods.TypeCode, GoodsType.TypeName from Goods, GoodsType " +
            "where Goods.TypeCode = GoodsType.TypeCode and isDel = '0' ")
    List<GoodsInfo> getAllGoods();

    //返回所有商品信息条数
    @Select("select count(GoodsCode) from Goods where isDel = '0' ")
    int getAllGoodsNum();

    //根据类别查看商品信息
    @Select("select GoodsCode, Name, Goods.TypeCode, GoodsType.TypeName from Goods, GoodsType " +
            "where Goods.TypeCode = GoodsType.TypeCode and TypeCode = #{typeCode}")
    List<GoodsInfo> getAllGoodsByTypicalType(@Param("typeCode") String typeCode);

    // 根据类别查看商品信息数量
    @Select("select count(GoodsCode) from Goods, GoodsType " +
            "where Goods.TypeCode = GoodsType.TypeCode and TypeCode = #{typeCode}")
    int getAllGoodsByTypicalTypeNum(@Param("typeCode") String typeCode);

    //返回所有商品类别
    @Select("select TypeCode, TypeName from GoodsType")
    List<GoodsTypesInfo> getAllTypes();

    //返回所有商品类别数量
    @Select("select count(TypeCode) from GoodsType")
    int getAllTypesNum();

    //新增商品信息
    @Insert("insert into Goods(GoodsCode, Name, TypeCode, isDel) Values (#{goodsCode}, #{name}, #{typeCode}, '0')")
    void insertGoodsInfo(@Param("goodsCode") String goodsCode, @Param("name") String name, @Param("typeCode") String typeCode);

    //更改品名
    @Update("update Goods set Name = #{name} where GoodsCode = #{goodsCode}")
    void updateGoodsName(@Param("name") String name, @Param("goodsCode") String goodsCode);

    //更改商品类别
    @Update("update Goods set TypeCode = #{typeCode} where GoodsCode = #{goodsCode}")
    void updateGoodsType(@Param("typeCode") String typeCode, @Param("goodsCode") String goodsCode);

    //删除商品信息
    @Update("update Goods set isDel = '1' where GoodsCode = #{goodsCode}")
    void deleteGoodsInfo(@Param("goodsCode") String goodsCode);

    /**
     * 模糊查找商品名
     * @param name
     * @return 商品信息List
     */
//    @Select("select GoodsCode, Name, Goods.TypeCode, GoodsType.TypeName from Goods, GoodsType " +
//            "where Name like CONCAT('%', #{name}, '%') and Goods.TypeCode = GoodsType.TypeCode")
    @Select("SELECT GoodsCode, Name, Goods.TypeCode, GoodsType.TypeName " +
            "FROM Goods, GoodsType " +
            "WHERE " +
            "CONCAT(Goods.GoodsCode, Goods.Name, Goods.TypeCode, GoodsType.TypeName) LIKE CONCAT('%', #{value}, '%') " +
            "AND Goods.TypeCode = GoodsType.TypeCode AND isDel = '0' ")
    List<GoodsInfo> searchGoodsInfo(@Param("value") String name);
    /**
     *
     * @param value
     * @return 数量
     */
    @Select("select count(GoodsCode)" +
            "FROM Goods, GoodsType " +
            "WHERE " +
            "CONCAT(Goods.GoodsCode, Goods.Name, Goods.TypeCode, GoodsType.TypeName) LIKE CONCAT('%', #{value}, '%') " +
            "AND Goods.TypeCode = GoodsType.TypeCode")
    int searchGoodsInfoNum(@Param("value") String value);

    /**
     * 根据商品编码返回商品名称
     * @param goodsCode
     * @return
     */
    @Select("select GoodsCode, Name, Goods.TypeCode, GoodsType.TypeName from Goods, GoodsType " +
            "where GoodsCode = #{GoodsCode} and Goods.TypeCode = GoodsType.TypeCode ")
    GoodsInfo getGoodsInfoByCode(@Param("GoodsCode") String goodsCode);

    /**
     * 插入或更新商品信息
     * @param goodsCode 商品编码
     * @param name 商品名称
     * @param typeCode 商品类别编码
     */
    @Insert("insert into Goods(GoodsCode, Name, TypeCode, isDel) values (#{goodsCode}, #{name}, #{typeCode}, '0') " +
            "on duplicate key update Name = values(Name), TypeCode = values(TypeCode)")
    void insertOrUpdateGoodsInfo(@Param("goodsCode") String goodsCode, @Param("name") String name, @Param("typeCode") String typeCode);

    /**
     * 获取序列最大值
     * @return 最大值
     */
    @Select("SELECT MAX(CONVERT(GoodsCode, SIGNED)) FROM Goods;")
    long getMaxGoodsCode();

    /**
     * 返回所有列名
     * @return 列名
     */
    @Select(value = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Goods'")
    List<String> getAllColumnNames();

    @Insert("INSERT INTO Goods (GoodsCode, ${target}) VALUES (#{goodsCode}, #{value}) " +
            "ON DUPLICATE KEY UPDATE ${target} = #{value}")
    void handleExcelColumns(@Param("target") String target,
                            @Param("value") String value,
                            @Param("goodsCode") String goodsCode);

}
