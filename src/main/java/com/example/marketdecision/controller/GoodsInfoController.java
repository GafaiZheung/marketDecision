package com.example.marketdecision.controller;

import com.example.marketdecision.Bean.*;
import com.example.marketdecision.dao.GoodsInfoDao;
import com.example.marketdecision.service.GoodsInfoService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品信息页面业务逻辑层
 */
@RestController
@RequestMapping("/goodsInfo")
public class GoodsInfoController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsInfoController.class);

    @Resource
    private GoodsInfoService goodsInfoService;
    @Resource
    private GoodsInfoDao goodsInfoDao;

    /**
     * 查询所有商品信息
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 商品信息和操作状态
     */
    @RequestMapping(value = "/getAllGoodsInfo", method = RequestMethod.GET)
    public GoodsInfoReturnData getAllGoodsInfo(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<GoodsInfo> goodsInfoPage = goodsInfoService.getAllGoodsInfo(pageRequest);

        List<GoodsInfo> goodsInfos = goodsInfoPage.getContent();

        int total = goodsInfoService.getAllGoodsInfoNum();

        if (!goodsInfos.isEmpty())
            return new GoodsInfoReturnData(1, total, goodsInfos); // Success with data
        else
            return new GoodsInfoReturnData(404, total, goodsInfos); // No data found
    }

    /**
     * 查询所有商品类别
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 商品类别信息和操作状态
     */
    @RequestMapping(value = "getAllGoodsTypeInfo", method = RequestMethod.GET)
    public GoodsTypeInfoReturnData getAllGoodsTypeInfo(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<GoodsTypesInfo> goodsInfoPage = goodsInfoService.getAllGoodsTypesInfo(pageRequest);

        List<GoodsTypesInfo> goodsInfos = goodsInfoPage.getContent();

        int total = goodsInfoDao.getAllTypesNum();

        if (!goodsInfos.isEmpty())
            return new GoodsTypeInfoReturnData(1, total, goodsInfos); // Success with data
        else
            return new GoodsTypeInfoReturnData(404, total, goodsInfos); // No data found
    }

    /**
     * 修改商品信息
     *
     * @param goodCode  商品编码
     * @param goodsName 商品名称
     * @param typeCode  商品类别编码
     * @return 更新操作的状态信息
     */
    @RequestMapping(value = "updateGoodsInfo", method = RequestMethod.PUT)
    public Status updateGoodsInfo(String goodCode, String goodsName, String typeCode) {
        try {
            goodsInfoService.alterGoodsName(goodCode, goodsName);
            goodsInfoService.alterGoodsType(goodCode, typeCode);
            return new Status(1, "修改成功"); // Update success
        } catch (Exception e) {
            return new Status(404, "修改失败，商品编码不存在"); // Update failure, goods code does not exist
        }
    }

    /**
     * 更改品名
     *
     * @param goodsCode 商品编码
     * @param goodName  修改后的商品名称
     * @return 更新操作的状态信息
     */
    @RequestMapping(value = "updateGoodsName", method = RequestMethod.PUT)
    public Status updateGoodsName(String goodsCode, String goodName) {
        try {
            goodsInfoService.alterGoodsName(goodsCode, goodName);
            return new Status(1, "更新成功"); // Update success
        } catch (Exception e) {
            return new Status(404, "更新失败"); // Update failure
        }
    }

    /**
     * 新增商品
     *
     * @param goodsCode 商品编码
     * @param goodsName 商品名称
     * @param typeCode  商品类别编码
     * @return 插入操作的状态信息
     */
    @RequestMapping(value = "insertGoodsInfo", method = RequestMethod.POST)
    public Status insertGoodsInfo(String goodsCode, String goodsName, String typeCode) {
        if (goodsCode.length() != 0 && goodsName.length() != 0 && typeCode.length() != 0) {
            try {
                goodsInfoService.insertGoodsInfo(goodsCode, goodsName, typeCode);
                return new Status(1, "添加成功"); // Insert success
            } catch (Exception e) {
                return new Status(404, "添加失败，商品编码已存在"); // Insert failure, goods code already exists
            }
        } else
            return new Status(0, "添加失败，不允许为空"); // Insert failure, not allowed to be empty
    }

    /**
     * 修改类别
     *
     * @param goodsCode 商品编码
     * @param typeCode  修改后的商品类别编码
     * @return 更新操作的状态信息
     */
    @RequestMapping(value = "alterGoodsType", method = RequestMethod.PUT)
    public Status alterGoodsType(String goodsCode, String typeCode) {
        try {
            goodsInfoService.alterGoodsType(goodsCode, typeCode);
            return new Status(1, "更新成功"); // Update success
        } catch (Exception e) {
            return new Status(404, "更新失败"); // Update failure
        }
    }

    /**
     * 删除商品信息
     *
     * @param goodsCode 商品编码
     * @return 删除操作的状态信息
     */
    @RequestMapping(value = "deleteGoodsInfo", method = RequestMethod.DELETE)
    public Status deleteGoodsInfo(String goodsCode) {
        try {
            goodsInfoService.deleteGoodsInfo(goodsCode);
            return new Status(1, "删除成功"); // Delete success
        } catch (Exception e) {
            return new Status(404, "无法删除，商品编码不存在"); // Delete failure, goods code does not exist
        }
    }

    /**
     * 模糊查找商品信息
     *
     * @param value    模糊查找的值
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 模糊查找的商品信息和操作状态
     */
    @RequestMapping(value = "searchGoodsInfo", method = RequestMethod.GET)
    public GoodsInfoReturnData searchGoodsInfo(String value, int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<GoodsInfo> goodsInfoPage = goodsInfoService.searchGoodsInfo(value, pageRequest);

        List<GoodsInfo> goodsInfos = goodsInfoPage.getContent();

        int total = goodsInfoService.searchGoodsInfoNum(value);

        if (!goodsInfos.isEmpty())
            return new GoodsInfoReturnData(1, total, goodsInfos); // Success with data
        else


            return new GoodsInfoReturnData(404, total, goodsInfos); // No data found
    }

    /**
     * 接收前端上传的Excel文件
     *
     * @param file 上传的Excel文件
     * @return Excel文件处理结果
     */
    @RequestMapping(value = "uploadExcel", method = RequestMethod.POST)
    public ExcelRecvReturnData handleUploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return new ExcelRecvReturnData(0, null, null, null); // Empty file
        try {
            byte[] bytes = file.getBytes();
            return goodsInfoService.handleUploadExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
            return new ExcelRecvReturnData(500, null, null, null); // Internal server error
        }
    }

    /**
     * 处理前端传递的Excel数据
     *
     * @param request 包含Excel文件路径和列映射信息的请求对象
     * @return Excel数据处理结果
     */
    @RequestMapping(value = "handleExcelData", method = RequestMethod.POST)
    public Status handleExcelData(@RequestBody ExcelDataRequest request) {
        String filePath = request.getFilePath();
        List<List<String>> columnMappings = request.getColumnMappings();

        // 调用服务层处理数据
        return goodsInfoService.handleUploadData(filePath, columnMappings);
    }

    /**
     * 根据商品编码获取商品信息
     *
     * @param goodsCode 商品编码
     * @return 商品信息
     */
    @RequestMapping(value = "getGoodsInfoByCode", method = RequestMethod.GET)
    public GoodsInfo getGoodsInfoByCode(String goodsCode) {
        return goodsInfoService.getGoodsInfoByCode(goodsCode);
    }

    /**
     * 获取新的商品编码
     *
     * @return 新的商品编码
     */
    @RequestMapping(value = "getGoodsCode", method = RequestMethod.GET)
    public String getGoodsCode() {
        return String.valueOf(goodsInfoDao.getMaxGoodsCode() + 1);
    }
}