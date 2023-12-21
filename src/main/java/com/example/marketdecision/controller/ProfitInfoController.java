package com.example.marketdecision.controller;

import com.example.marketdecision.Bean.ProfitInfo;
import com.example.marketdecision.Bean.ProfitInfoReturnData;
import com.example.marketdecision.Bean.SaleCurrencyInfo;
import com.example.marketdecision.Bean.SaleCurrencyInfoReturnData;
import com.example.marketdecision.dao.ProfitInfoDao;
import com.example.marketdecision.service.ProfitInfoService;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/profitInfo")
public class ProfitInfoController
{

    @Resource
    private ProfitInfoService profitInfoService;
    @Resource
    private ProfitInfoDao profitInfoDao;


    /**
     * 返回最新一天的利润信息
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @return 利润信息返回值包装器类
     */
    @RequestMapping(value = "initProfitInfo", method = RequestMethod.GET)
    public ProfitInfoReturnData initProfitInfo(@RequestParam int pageNum, @RequestParam int pageSize) {
        try{
            PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
            PageInfo<ProfitInfo> profitInfoPageInfo = (PageInfo<ProfitInfo>) profitInfoService.initProfitInfo(pageRequest);

            List<ProfitInfo> profitInfos = profitInfoPageInfo.getList();
            int total = (int) profitInfoPageInfo.getTotal();

            if (!profitInfos.isEmpty()) {
                return new ProfitInfoReturnData(1, total, profitInfos);
            } else {
                return new ProfitInfoReturnData(404, total, profitInfos);
            }
        }catch (Exception e)
        {
            return new ProfitInfoReturnData(404, 0, null);
        }

    }

    /**
     * 根据日期获得利润数据
     */
    @RequestMapping(value = "getProfitInfoByDate", method = RequestMethod.GET)
    public ProfitInfoReturnData getProfitInfoByDate(String saleDate, int pageNum, int pageSize)
    {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        PageInfo<ProfitInfo> profitInfoPageInfo = (PageInfo<ProfitInfo>) profitInfoService.getProfitInfoByDate(saleDate, pageRequest);

        List<ProfitInfo> profitInfos = profitInfoPageInfo.getList();
        int total = (int) profitInfoPageInfo.getTotal();

        if (!profitInfos.isEmpty()) {
            return new ProfitInfoReturnData(1, total, profitInfos);
        } else {
            return new ProfitInfoReturnData(404, total, profitInfos);
        }
    }

    /**
     * 根据日期获取利润数据的Excel文件
     */
    @RequestMapping(value = "getProfitInfoExcel", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getProfitInfoExcel(String saleDate)
    {
        if(saleDate.length() == 0)
        {
            saleDate = profitInfoDao.maxSaleDate();
        }
        List<ProfitInfo> profitInfoList = null;
        try {
            profitInfoList = profitInfoService.getProfitInfo(saleDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("profitInfo");

        // 创建表头行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("日期");
        headerRow.createCell(1).setCellValue("商品编码");
        headerRow.createCell(2).setCellValue("名称");
        headerRow.createCell(3).setCellValue("种类");
        headerRow.createCell(4).setCellValue("批发价");
        headerRow.createCell(5).setCellValue("损失(%)");
        headerRow.createCell(6).setCellValue("销售数量");
        headerRow.createCell(7).setCellValue("总价");
        headerRow.createCell(8).setCellValue("利润");

        // 填充数据行
        int rowNum = 1;
        for (ProfitInfo profitInfo : profitInfoList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(profitInfo.getDate());
            row.createCell(1).setCellValue(profitInfo.getGoodsCode());
            row.createCell(2).setCellValue(profitInfo.getName());
            row.createCell(3).setCellValue(profitInfo.getTypeName());
            row.createCell(4).setCellValue(profitInfo.getWholeSale());
            row.createCell(5).setCellValue(profitInfo.getLoss());
            row.createCell(6).setCellValue(profitInfo.getSaleNumber());
            row.createCell(7).setCellValue(profitInfo.getSumPrice());
            row.createCell(8).setCellValue(profitInfo.getProfit());
        }


        // 将Excel写入字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 设置响应头，告诉浏览器文件类型
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", saleDate + ".xlsx");

        // 返回Excel文件的字节数组
        return new ResponseEntity<>(outputStream.toByteArray(), headers, org.springframework.http.HttpStatus.OK);

    }


}
