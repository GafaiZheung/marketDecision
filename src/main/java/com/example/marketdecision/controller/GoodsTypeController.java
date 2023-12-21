package com.example.marketdecision.controller;

import com.example.marketdecision.Bean.*;
import com.example.marketdecision.service.GoodsTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/goodsType")
public class GoodsTypeController {

    @Resource
    private GoodsTypeService goodsTypeService;

    /**
     * 新增商品类别
     *
     * @param typeCode 商品类别代码
     * @param typeName 商品类别名称
     * @return 插入操作的状态信息
     */
    @RequestMapping(value = "insertGoodsType", method = RequestMethod.POST)
    public Status insertGoodsType(String typeCode, String typeName) {
        try {
            goodsTypeService.insertGoodsType(typeCode, typeName);
            return new Status(1, "插入成功"); // Insert success
        } catch (Exception e) {
            return new Status(404, "插入失败"); // Insert failure
        }
    }

    /**
     * 返回商品类别列表（分页）
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 商品类别信息和操作状态
     */
    @RequestMapping(value = "getAllGoodsType", method = RequestMethod.GET)
    public GoodsTypeInfoReturnData getAllGoodsType(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<GoodsTypesInfo> goodsTypesInfoPage = goodsTypeService.getAllGoodsTypesInfo(pageRequest);

        List<GoodsTypesInfo> goodsTypesInfos = goodsTypesInfoPage.getContent();

        if (!goodsTypesInfos.isEmpty())
            return new GoodsTypeInfoReturnData(1, 6, goodsTypesInfos); // Success with data
        else
            return new GoodsTypeInfoReturnData(404, 0, goodsTypesInfos); // No data found
    }

    /**
     * 删除指定商品类别
     *
     * @param typeCode 待删除商品类别代码
     * @return 删除操作的状态信息
     */
    @RequestMapping(value = "deleteGoodsType", method = RequestMethod.DELETE)
    public Status deleteGoodsType(String typeCode) {
        try {
            goodsTypeService.deleteGoodsType(typeCode);
            return new Status(1, "删除成功"); // Delete success
        } catch (Exception e) {
            return new Status(404, "删除失败"); // Delete failure
        }
    }

    /**
     * 修改商品类别名
     *
     * @param typeCode 待修改商品类别代码
     * @param typeName 修改后的商品类别名称
     * @return 更新操作的状态信息
     */
    @RequestMapping(value = "updateGoodsType", method = RequestMethod.PUT)
    public Status updateGoodsType(String typeCode, String typeName) {
        try {
            goodsTypeService.updateGoodsType(typeCode, typeName);
            return new Status(1, "更新成功"); // Update success
        } catch (Exception e) {
            return new Status(404, "更新失败"); // Update failure
        }
    }

    /**
     * 上传Excel文件并处理
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
            return goodsTypeService.handleUploadExcel(file);
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
        return goodsTypeService.handleUploadData(filePath, columnMappings);
    }
}
