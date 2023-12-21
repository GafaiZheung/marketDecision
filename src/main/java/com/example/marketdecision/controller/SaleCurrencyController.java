package com.example.marketdecision.controller;

import com.example.marketdecision.Bean.*;
import com.example.marketdecision.dao.SaleCurrencyDao;
import com.example.marketdecision.service.SaleCurrencyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/saleCurrency")
public class SaleCurrencyController {

    @Resource
    private SaleCurrencyService saleCurrencyService;
    @Resource
    private SaleCurrencyDao saleCurrencyDao;

    /**
     * 获取所有销售流水明细（分页）
     * 对应前端getAllSaleCurrencies请求
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 销售流水明细分页列表
     */
    @RequestMapping(value = "getAllSaleCurrencies", method = RequestMethod.GET)
    public SaleCurrencyInfoReturnData getAllSaleCurrencies(@RequestParam int pageNum, @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<SaleCurrencyInfo> saleCurrencyInfoPage = saleCurrencyService.getAllSaleCurrencies(pageRequest);

        List<SaleCurrencyInfo> saleCurrencyInfos = saleCurrencyInfoPage.getContent();

        int total = saleCurrencyDao.getAllSaleCurrenciesNum();

        if (!saleCurrencyInfos.isEmpty())
            return new SaleCurrencyInfoReturnData(1, total, saleCurrencyInfos);
        else
            return new SaleCurrencyInfoReturnData(404, total, saleCurrencyInfos);
    }

    /**
     * 新增销售流水明细
     * 对应前端insertSaleCurrency请求
     *
     * @param saleDate   销售日期
     * @param saleTime   销售时间
     * @param goodsCode  商品代码
     * @param saleNumber 销售数量
     * @param unitPrice  销售单价
     * @param saleType   销售类型
     * @param discount   折扣
     * @return 状态信息
     */
    @RequestMapping(value = "insertSaleCurrency", method = RequestMethod.PUT)
    public Status insertSaleCurrency(String saleDate, String saleTime, String goodsCode, String saleNumber,
                                     String unitPrice, String saleType, String discount) {
        try {
            saleCurrencyService.insertSaleCurrency(saleDate, saleTime, goodsCode, saleNumber, unitPrice, saleType, discount);
            return new Status(1, "insert success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Status(404, "insert failure");
        }
    }

    /**
     * 修改销售流水明细中的销售数量
     * 对应前端updateSaleNumber请求
     *
     * @param saleNumber 销售数量
     * @param goodsCode  商品代码
     * @return 状态信息
     */
    @RequestMapping(value = "updateSaleNumber", method = RequestMethod.PUT)
    public Status updateSaleNumber(String saleNumber, String goodsCode) {
        try {
            saleCurrencyService.updateSaleNumber(saleNumber, goodsCode);
            return new Status(1, "update success");
        } catch (Exception e) {
            return new Status(404, "update failure");
        }
    }

    /**
     * 修改销售流水明细中的销售单价
     * 对应前端updateUnitPrice请求
     *
     * @param unitPrice 销售单价
     * @param goodsCode 商品代码
     * @return 状态信息
     */
    @RequestMapping(value = "updateUnitPrice", method = RequestMethod.PUT)
    public Status updateUnitPrice(String unitPrice, String goodsCode) {
        try {
            saleCurrencyService.updateUnitPrice(unitPrice, goodsCode);
            return new Status(1, "update success");
        } catch (Exception e) {
            return new Status(404, "update failure");
        }
    }

    /**
     * 修改销售流水明细中的销售日期和时间
     * 对应前端updateSaleDateTime请求
     *
     * @param saleDate 销售日期
     * @param saleTime 销售时间
     * @param goodsCode 商品代码
     * @return 状态信息
     */
    @RequestMapping(value = "updateSaleDateTime", method = RequestMethod.PUT)
    public Status updateSaleDateTime(String saleDate, String saleTime, String goodsCode) {
        try {
            saleCurrencyService.updateSaleDateTime(saleDate, saleTime, goodsCode);
            return new Status(1, "update success");
        } catch (Exception e) {
            return new Status(404, "update failure");
        }
    }

    /**
     * 删除销售流水明细
     * 对应前端deleteSaleCurrency请求
     *
     * @param goodsCode 商品代码
     * @return 状态信息
     */
    @RequestMapping(value = "deleteSaleCurrency", method = RequestMethod.DELETE)
    public Status deleteSaleCurrency(String goodsCode, String saleDate, String saleTime) {
        try {
            System.out.println(saleDate+saleTime+goodsCode);
            saleCurrencyService.deleteSaleCurrency(saleDate, saleTime, goodsCode);
            return new Status(1, "delete success");
        } catch (Exception e) {
            return new Status(404, "delete failure");
        }
    }

    /**
     * 返回指定日期的销售流水明细
     * 对应前端getSaleCurrencyByDate请求
     *
     * @param saleDate 销售日期
     * @return 销售流水明细列表
     */
    @RequestMapping(value = "getSaleCurrencyByDate", method = RequestMethod.GET)
    public SaleCurrencyInfoReturnData getSaleCurrencyByDate(String saleDate, int pageNum, int pageSize)
    {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);

        Page<SaleCurrencyInfo> saleCurrencyInfos = saleCurrencyService.getSaleCurrencyByDate(saleDate, pageRequest);

        List<SaleCurrencyInfo> saleCurrencyInfoList = saleCurrencyInfos.getContent();

        int total = saleCurrencyDao.getSaleCurrencyByDateNum(saleDate);

        if (!saleCurrencyInfos.isEmpty())
            return new SaleCurrencyInfoReturnData(1, total, saleCurrencyInfoList);
        else
            return new SaleCurrencyInfoReturnData(404, total, saleCurrencyInfoList);
    }

    @RequestMapping(value = "updateSaleCurrency", method = RequestMethod.PUT)
    public Status updateSaleCurrency(String saleDate, String saleTime, String goodsCode, String saleNumber,
                                     String unitPrice, String saleType, String discount)
    {return saleCurrencyService.updateSaleCurrency(saleDate, saleTime, goodsCode, saleNumber, unitPrice, saleType, discount);}

    @RequestMapping(value = "uploadSaleCurrencyExcel", method = RequestMethod.POST)
    public ExcelRecvReturnData handleUploadSaleCurrencyExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return new ExcelRecvReturnData(0, null, null, null); // Empty file
        try {
            byte[] bytes = file.getBytes();
            return saleCurrencyService.handleUploadExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
            return new ExcelRecvReturnData(500, null, null, null); // Internal server error
        }
    }

    /**
     * 处理前端传递的Excel数据
     * 对应前端handleSaleCurrencyExcelData请求
     * @param request 包含Excel文件路径和列映射信息的请求对象
     * @return Excel数据处理结果
     */
    @RequestMapping(value = "handleSaleCurrencyExcelData", method = RequestMethod.POST)
    public Status handleSaleCurrencyExcelData(@RequestBody ExcelDataRequest request) {
        String filePath = request.getFilePath();
        List<List<String>> columnMappings = request.getColumnMappings();

        // 调用服务层处理数据
        return saleCurrencyService.handleUploadData(filePath, columnMappings);
    }
}
