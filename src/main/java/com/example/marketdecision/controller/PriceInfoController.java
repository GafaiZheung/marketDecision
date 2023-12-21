package com.example.marketdecision.controller;

import com.example.marketdecision.Bean.*;
import com.example.marketdecision.dao.PriceInfoDao;
import com.example.marketdecision.service.PriceInfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 批发价信息页面业务逻辑层
 */
@RestController
@RequestMapping("/priceInfo")
public class PriceInfoController {

    @Resource
    private PriceInfoService priceInfoService;
    @Resource
    private PriceInfoDao priceInfoDao;

    /**
     * 获取最新一天的批发价信息（分页）
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 批发价信息和操作状态
     */
    @RequestMapping(value = "getAllPriceInfo", method = RequestMethod.GET)
    public PriceInfoReturnData getAllPriceInfo(@RequestParam int pageNum, @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<PriceInfo> priceInfoPage = priceInfoService.getAllPriceInfo(pageRequest);

        List<PriceInfo> priceInfoList = priceInfoPage.getContent();

        int total = priceInfoDao.getAllPriceInfoNum();

        if (!priceInfoList.isEmpty())
            return new PriceInfoReturnData(1, total, priceInfoList); // Success with data
        else
            return new PriceInfoReturnData(404, total, priceInfoList); // No data found
    }

    /**
     * 获取指定日期的批发价信息（分页）
     *
     * @param date     指定日期
     * @param pageNum  页码
     * @param pageSize 每页记录数
     * @return 批发价信息和操作状态
     */
    @RequestMapping(value = "getPriceInfoByDate", method = RequestMethod.GET)
    public PriceInfoReturnData getPriceInfoByDate(@RequestParam String date, @RequestParam int pageNum, @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<PriceInfo> priceInfoPage = priceInfoService.getPriceInfoByDate(date, pageRequest);

        List<PriceInfo> priceInfoList = priceInfoPage.getContent();

        int total = priceInfoDao.getPriceInfoByDateNum(date);

        if (!priceInfoList.isEmpty())
            return new PriceInfoReturnData(1, total, priceInfoList); // Success with data
        else
            return new PriceInfoReturnData(404, total, priceInfoList); // No data found
    }

    /**
     * 新增批发价信息
     *
     * @param goodsCode 商品编码
     * @param wholesale 批发价
     * @param date      日期
     * @return 添加操作的状态信息
     */
    @RequestMapping(value = "insertPriceInfo", method = RequestMethod.PUT)
    public Status insertPriceInfo(@RequestParam String goodsCode, @RequestParam String wholesale, @RequestParam String date) {
        try {
            priceInfoService.insertPriceInfo(goodsCode, wholesale, date);
            return new Status(1, "添加成功"); // Insert success
        } catch (Exception e) {
            return new Status(404, "添加失败，商品不存在"); // Insert failure, goods does not exist
        }
    }

    /**
     * 修改批发价格
     *
     * @param goodsCode 商品编码
     * @param wholesale 新的批发价
     * @param date      日期
     * @return 更新操作的状态信息
     */
    @RequestMapping(value = "updateWholesalePrice", method = RequestMethod.PUT)
    public Status updateWholesalePrice(@RequestParam String goodsCode, @RequestParam String wholesale, @RequestParam String date) {
        try {
            priceInfoService.updateWholesalePrice(goodsCode, wholesale, date);
            return new Status(1, "修改成功"); // Update success
        } catch (Exception e) {
            return new Status(404, "修改失败，商品不存在"); // Update failure, goods does not exist
        }
    }

    /**
     * 修改日期
     *
     * @param goodsCode 商品编码
     * @param date      新的日期
     * @return 更新操作的状态信息
     */
    @RequestMapping(value = "updatePriceInfoDate", method = RequestMethod.PUT)
    public Status updatePriceInfoDate(@RequestParam String goodsCode, @RequestParam String date) {
        try {
            priceInfoService.updatePriceInfoDate(goodsCode, date);
            return new Status(1, "update success"); // Update success
        } catch (Exception e) {
            return new Status(404, "update failure"); // Update failure
        }
    }

    /**
     * 删除批发价信息
     *
     * @param goodsCode 商品编码
     * @param date      日期
     * @return 删除操作的状态信息
     */
    @RequestMapping(value = "deletePriceInfo", method = RequestMethod.DELETE)
    public Status deletePriceInfo(@RequestParam String goodsCode, @RequestParam String date) {
        try {
            priceInfoService.deletePriceInfo(goodsCode, date);
            return new Status(1, "删除成功"); // Delete success
        } catch (Exception e) {
            return new Status(404, "删除失败，商品不存在"); // Delete failure, goods does not exist
        }
    }

    /**
     * 上传批发价信息的Excel文件并处理
     *
     * @param file 上传的Excel文件
     * @return Excel文件处理结果
     */
    @RequestMapping(value = "uploadPriceInfoExcel", method = RequestMethod.POST)
    public ExcelRecvReturnData handleUploadPriceInfoExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return new ExcelRecvReturnData(0, null, null, null); // Empty file
        try {
            byte[] bytes = file.getBytes();
            return priceInfoService.handleUploadExcel(file);
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
    @RequestMapping(value = "handlePriceInfoExcelData", method = RequestMethod.POST)
    public Status handlePriceInfoExcelData(@RequestBody ExcelDataRequest request) {
        String filePath = request.getFilePath();
        List<List<String>> columnMappings = request.getColumnMappings();

        // 调用服务层处理数据
        return priceInfoService.handleUploadData(filePath, columnMappings);
    }

}
