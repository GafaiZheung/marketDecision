package com.example.marketdecision.controller;

import com.example.marketdecision.Bean.*;
import com.example.marketdecision.dao.LossDataDao;
import com.example.marketdecision.service.LossDataService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 损耗数据页面业务逻辑层
 */
@RestController
@RequestMapping("/lossData")
public class LossDataController {

    @Resource
    LossDataService lossDataService;
    @Resource
    LossDataDao lossDataDao;

    /**
     * 删除损耗数据
     *
     * @param goodsCode 商品编码
     * @return 删除操作的状态信息
     */
    @RequestMapping(value = "deleteLossData", method = RequestMethod.DELETE)
    public Status deleteLossData(String goodsCode) {
        try {
            lossDataService.deleteLossData(goodsCode);
            return new Status(1, "删除成功"); // Delete success
        } catch (Exception e) {
            return new Status(404, "删除失败，商品不存在"); // Delete failure, goods does not exist
        }
    }

    /**
     * 插入损耗数据
     *
     * @param goodsCode     商品编码
     * @param attritionRate 损耗率
     * @return 插入操作的状态信息
     */
    @RequestMapping(value = "insertLossData", method = RequestMethod.PUT)
    public Status insertLossData(String goodsCode, String attritionRate) {
        try {
            lossDataService.insertLossData(goodsCode, attritionRate);
            return new Status(1, "添加成功"); // Insert success
        } catch (Exception e) {
            return new Status(404, "添加失败，商品不存在"); // Insert failure, goods does not exist
        }
    }

    /**
     * 修改损耗数据
     *
     * @param goodsCode     商品编码
     * @param attritionRate 新的损耗率
     * @return 更新操作的状态信息
     */
    @RequestMapping(value = "updateLossData", method = RequestMethod.PUT)
    public Status updateLossData(String goodsCode, String attritionRate) {
        try {
            lossDataService.updateLossData(goodsCode, attritionRate);
            return new Status(1, "修改成功"); // Update success
        } catch (Exception e) {
            return new Status(404, "修改失败"); // Update failure
        }
    }

    /**
     * 返回所有损耗数据（分页）
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 损耗数据信息和操作状态
     */
    @RequestMapping(value = "getAllLossData", method = RequestMethod.GET)
    public LossDataReturnData getAllLossData(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<LossData> lossDataPage = lossDataService.getAllLossData(pageRequest);

        List<LossData> lossDataList = lossDataPage.getContent();

        int total = lossDataDao.getAllLossDataNum();

        if (!lossDataList.isEmpty())
            return new LossDataReturnData(1, total, lossDataList); // Success with data
        else
            return new LossDataReturnData(404, total, lossDataList); // No data found
    }

    /**
     * 返回模糊查询损耗数据（分页）
     *
     * @param value    模糊查找的值
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 模糊查找的损耗数据信息和操作状态
     */
    @RequestMapping(value = "searchLossData", method = RequestMethod.GET)
    public LossDataReturnData searchLossData(String value, int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<LossData> lossDataPage = lossDataService.searchLossData(value, pageRequest);

        List<LossData> lossDataList = lossDataPage.getContent();

        int total = lossDataDao.searchLossDataNum(value);

        if (!lossDataList.isEmpty())
            return new LossDataReturnData(1, total, lossDataList); // Success with data
        else
            return new LossDataReturnData(404, total, lossDataList); // No data found
    }

    /**
     * 上传损耗数据的Excel文件并处理
     *
     * @param file 上传的Excel文件
     * @return Excel文件处理结果
     */
    @RequestMapping(value = "uploadLossDataExcel", method = RequestMethod.POST)
    public ExcelRecvReturnData handleUploadLossDataExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return new ExcelRecvReturnData(0, null, null, null); // Empty file
        try {
            byte[] bytes = file.getBytes();
            return lossDataService.handleUploadExcel(file);
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
    @RequestMapping(value = "handleLossDataExcelData", method = RequestMethod.POST)
    public Status handleLossDataExcelData(@RequestBody ExcelDataRequest request) {
        String filePath = request.getFilePath();
        List<List<String>> columnMappings = request.getColumnMappings();

        // 调用服务层处理数据
        return lossDataService.handleUploadData(filePath, columnMappings);
    }
}
