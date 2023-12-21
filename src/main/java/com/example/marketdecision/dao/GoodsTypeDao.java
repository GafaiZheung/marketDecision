package com.example.marketdecision.dao;

import com.example.marketdecision.Bean.GoodsTypesInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsTypeDao {

    /**
     * 获取所有商品类别
     *
     * @return
     */
    @Select("select * from GoodsType")
    List<GoodsTypesInfo> getAllGoodsTypesInfo();

    /**
     * 返回所有商品类别数量
     */
    @Select("select count(TypeCode) from GoodsType")
    int getAllGoodsTypesInfoNum();

    /**
     * 更改商品类别名
     */
    @Update("update GoodsType set TypeName = #{typeName} where TypeCode = #{typeCode}")
    void updateGoodsType(@Param("typeName") String typeName, @Param("typeCode") String typeCode);

    /**
     * 插入新商品类别
     */
    @Insert("insert into GoodsType(TypeCode, TypeName) Values (#{typeCode}, #{typeName})")
    void insertGoodsType(@Param("typeCode") String typeCode, @Param("typeName") String typeName);

    /**
     * 删除商品类别
     */
    @Delete("delete from GoodsType where TypeCode = #{typeCode}")
    void deleteGoodsType(@Param("typeCode") String typeCode);

    /**
     * 根据关键字模糊查询商品类别
     *
     * @param keyword 查询关键字
     * @return 匹配的商品类别列表
     */
    @Select("select * from GoodsType where TypeCode like CONCAT('%', #{keyword}, '%') or TypeName like CONCAT('%', #{keyword}, '%')")
    List<GoodsTypesInfo> searchGoodsTypeByKeyword(@Param("keyword") String keyword);

}
