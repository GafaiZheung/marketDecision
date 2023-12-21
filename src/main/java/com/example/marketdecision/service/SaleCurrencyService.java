package com.example.marketdecision.service;

import com.example.marketdecision.Bean.ExcelRecvReturnData;
import com.example.marketdecision.Bean.SaleCurrencyInfo;
import com.example.marketdecision.Bean.Status;
import com.example.marketdecision.dao.SaleCurrencyDao;
import com.github.pagehelper.PageHelper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * 销售流水服务类
 */
@Service
public class SaleCurrencyService {

    @Resource
    private SaleCurrencyDao saleCurrencyDao;

    /**
     * 获取所有销售流水明细（分页）
     *
     * @param pageRequest 分页请求
     * @return 销售流水明细分页列表
     */
    public Page<SaleCurrencyInfo> getAllSaleCurrencies(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNumber(), pageRequest.getPageSize());

        // 调用DAO方法
        List<SaleCurrencyInfo> saleCurrencyInfos = saleCurrencyDao.getAllSaleCurrencies();

        // 封装成Spring Data JPA的Page对象
        return new PageImpl<>(saleCurrencyInfos, pageRequest, ((com.github.pagehelper.Page<?>) saleCurrencyInfos).getTotal());
    }

    /**
     * 新增销售流水明细
     *
     * @param saleDate   销售日期
     * @param saleTime   销售时间
     * @param goodsCode  商品代码
     * @param saleNumber 销售数量
     * @param unitPrice  销售单价
     * @param saleType   销售类型
     * @param discount   折扣
     */
    public void insertSaleCurrency(String saleDate, String saleTime, String goodsCode, String saleNumber,
                                   String unitPrice, String saleType, String discount) {
        saleCurrencyDao.insertSaleCurrency(saleDate, saleTime, goodsCode, saleNumber, unitPrice, saleType, discount);
    }

    /**
     * 修改销售流水明细中的销售数量
     *
     * @param saleNumber 销售数量
     * @param goodsCode  商品代码
     */
    public void updateSaleNumber(String saleNumber, String goodsCode) {
        saleCurrencyDao.updateSaleNumber(saleNumber, goodsCode);
    }

    /**
     * 修改销售流水明细中的销售单价
     *
     * @param unitPrice 销售单价
     * @param goodsCode 商品代码
     */
    public void updateUnitPrice(String unitPrice, String goodsCode) {
        saleCurrencyDao.updateUnitPrice(unitPrice, goodsCode);
    }

    /**
     * 修改销售流水明细中的销售日期和时间
     *
     * @param saleDate 销售日期
     * @param saleTime 销售时间
     * @param goodsCode 商品代码
     */
    public void updateSaleDateTime(String saleDate, String saleTime, String goodsCode) {
        saleCurrencyDao.updateSaleDateTime(saleDate, saleTime, goodsCode);
    }

    /**
     * 删除销售流水明细
     *
     * @param saleDate  销售日期
     * @param saleTime  销售时间
     * @param goodsCode 商品代码
     */
    public void deleteSaleCurrency(String saleDate, String saleTime, String goodsCode) {
        saleCurrencyDao.deleteSaleCurrency(goodsCode, saleTime, saleDate);
    }

    /**
     * 返回指定日期的销售流水明细（分页）
     *
     * @param saleDate    销售日期
     * @param pageRequest 分页请求
     * @return 销售流水明细列表
     */
    public Page<SaleCurrencyInfo> getSaleCurrencyByDate(String saleDate, PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNumber(), pageRequest.getPageSize());

        // 调用DAO方法
        List<SaleCurrencyInfo> saleCurrencyInfos = saleCurrencyDao.getSaleCurrencyByDate(saleDate);

        // 封装成Spring Data JPA的Page对象
        return new PageImpl<>(saleCurrencyInfos, pageRequest, ((com.github.pagehelper.Page<?>) saleCurrencyInfos).getTotal());
    }

    /**
     * 更新销售流水明细
     *
     * @param saleDate   销售日期
     * @param saleTime   销售时间
     * @param goodsCode  商品代码
     * @param saleNumber 销售数量
     * @param unitPrice  销售单价
     * @param saleType   销售类型
     * @param discount   折扣
     * @return 更新结果状态
     */
    public Status updateSaleCurrency(String saleDate, String saleTime, String goodsCode, String saleNumber,
                                     String unitPrice, String saleType, String discount) {
        try {
            if ("退货".equals(saleType)) {
                double saleNumberValue = Double.parseDouble(saleNumber);
                String formattedSaleNumber = String.format("%.2f", saleNumberValue);
                saleNumber = String.valueOf(-Double.parseDouble(formattedSaleNumber));
            } else if ("销售".equals(saleType)) {
                double saleNumberValue = Double.parseDouble(saleNumber);
                String formattedSaleNumber = String.format("%.2f", saleNumberValue);
                saleNumber = String.valueOf(Math.abs(Double.parseDouble(formattedSaleNumber)));
            }
            saleCurrencyDao.updateSaleCurrency(saleDate, saleTime, goodsCode, saleNumber, unitPrice, saleType, discount);
            return new Status(1, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Status(0, "更新失败，不满足完整性约束");
        }
    }

    /**
     * 处理上传的 Excel 文件
     *
     * @param file 上传的 Excel 文件
     * @return 处理结果
     * @throws Exception 处理异常
     */
    public ExcelRecvReturnData handleUploadExcel(MultipartFile file) throws Exception {
        if (isExcelFile(file)) {
            String originalFilename = file.getOriginalFilename();
            String randomFileName = generateRandomFileName();
            File targetFile = new File("./tempRecv", randomFileName + getFileExtension(originalFilename));
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(targetFile)) {
                fileOutputStream.write(file.getBytes());
            }

            return new ExcelRecvReturnData(1, saleCurrencyDao.getAllColumnNames(),
                    extractColumnNames(file), targetFile.getPath());
        } else {
            return new ExcelRecvReturnData(0, null, null, null);
        }
    }

    /**
     * 生成随机文件名
     *
     * @return 随机文件名
     */
    private String generateRandomFileName() {
        Random random = new Random();
        long randomLong = random.nextLong();
        String base64Encoded = Base64.getEncoder().encodeToString(String.valueOf(randomLong).getBytes());
        return base64Encoded.replaceAll("[^a-zA-Z0-9]", "");
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex != -1 ? fileName.substring(lastDotIndex) : "";
    }

    /**
     * 判断文件是否为 Excel 文件
     *
     * @param file 文件
     * @return 是否为 Excel 文件
     */
    private boolean isExcelFile(MultipartFile file) {
        try {
            Workbook workbook;
            try {
                workbook = new XSSFWorkbook(file.getInputStream());
            } catch (Exception ex) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从 Excel 文件中提取列名
     *
     * @param multiPartFile Excel 文件
     * @return 列名列表
     * @throws IOException 读取文件异常
     */
    private List<String> extractColumnNames(MultipartFile multiPartFile) throws IOException {
        List<String> columnNames = new ArrayList<>();

        try (InputStream inputStream = multiPartFile.getInputStream()) {
            Workbook workbook;
            if (multiPartFile.getOriginalFilename().toLowerCase().endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                workbook = WorkbookFactory.create(inputStream);
            }

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Cell> cellIterator = sheet.getRow(0).cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String columnName = getCellValueAsString(cell);
                columnNames.add(columnName);
            }
        }

        return columnNames;
    }

    /**
     * 获取单元格的字符串值
     *
     * @param cell 单元格
     * @return 单元格的字符串值
     */
    private String getCellValueAsString(Cell cell) {
        DataFormatter dataFormatter = new DataFormatter();
        return dataFormatter.formatCellValue(cell);
    }

    /**
     * 处理上传的 Excel 数据
     *
     * @param filePath  文件路径
     * @param relations 列关系列表
     * @return 处理结果
     */
    public Status handleUploadData(String filePath, List<List<String>> relations) {
        File file = new File(filePath);

        if (!file.exists())
            return new Status(500, "服务器内部错误");

        try (FileInputStream fis = new FileInputStream(file)) {
            Workbook workbook;
            if (filePath.toLowerCase().endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                workbook = WorkbookFactory.create(fis);
            }

            Sheet sheet = workbook.getSheetAt(0);

            // 获取列名和它们的索引映射
            Map<String, Integer> columnIndexMap = getColumnIndexMap(sheet.getRow(0));

            // 取出relations中包含的列
            Map<List<String>, Integer> relationsColumnIndexMap = new HashMap<>();
            for (List<String> relation : relations) {
                String columnName = relation.get(0);
                Integer columnIndex = columnIndexMap.get(columnName);
                if (columnIndex != null) {
                    relationsColumnIndexMap.put(relation, columnIndex);
                }
            }

            Iterator<Row> iterator = sheet.iterator();
            if (iterator.hasNext()) {
                iterator.next(); // 跳过第一行
            }
            while (iterator.hasNext()) {
                Row row = iterator.next();
                Map<String, String> rowData = new HashMap<>();

                for (Map.Entry<List<String>, Integer> entry : relationsColumnIndexMap.entrySet()) {
                    String columnName = entry.getKey().get(0);
                    Integer columnIndex = entry.getValue();
                    Cell cell = row.getCell(columnIndex);
                    String cellValue = getCellValueAsString(cell);
                    rowData.put(entry.getKey().get(1), cellValue);
                }

                // 在这里可以对rowData进行处理
                String saleDate = rowData.get("saleDate");
                String saleTime = rowData.get("saleTime");
                String goodsCode = rowData.get("goodsCode");
                if(goodsCode == null || saleDate == null || saleTime == null)
                    return new Status(0, "未指定主码");

                if(saleDate.length() > 0 && saleTime.length() > 0 &&  goodsCode.length() > 0)
                    for (Map.Entry<String, String> entry : rowData.entrySet()) {
                        if (!entry.getKey().equals("goodsCode") && !entry.getKey().equals("saleTime") && !entry.getKey().equals("saleDate"))
                            saleCurrencyDao.handleExcelColumns(saleDate, saleTime, goodsCode, entry.getKey(), entry.getValue());
                    }
            }

            file.delete();
            return new Status(1, "更新成功");

        } catch (IOException e) {
            return new Status(500, "服务器内部错误");
        }
    }

    /**
     * 获取列名和它们的索引映射
     *
     * @param headerRow 表头行
     * @return 列名和索引的映射
     */
    private Map<String, Integer> getColumnIndexMap(Row headerRow) {
        Map<String, Integer> columnIndexMap = new HashMap<>();

        for (Cell cell : headerRow) {
            columnIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
        return columnIndexMap;
    }
}
