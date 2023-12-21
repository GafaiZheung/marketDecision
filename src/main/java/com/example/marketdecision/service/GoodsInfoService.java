package com.example.marketdecision.service;

import com.example.marketdecision.Bean.ExcelRecvReturnData;
import com.example.marketdecision.Bean.GoodsInfo;
import com.example.marketdecision.Bean.GoodsTypesInfo;
import com.example.marketdecision.Bean.Status;
import com.example.marketdecision.dao.GoodsInfoDao;

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

@SuppressWarnings("ResultOfMethodCallIgnored")
@Service
public class GoodsInfoService {

    @Resource
    private GoodsInfoDao goodsInfoDao;

    /**
     * 返回所有商品信息（分页）
     *
     * @param pageRequest 分页请求
     * @return 商品信息分页列表
     */
    public Page<GoodsInfo> getAllGoodsInfo(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNumber(), pageRequest.getPageSize());

        // 调用DAO方法
        List<GoodsInfo> goodsInfos = goodsInfoDao.getAllGoods();

        // 封装成Spring Data JPA的Page对象
        return new PageImpl<>(goodsInfos, pageRequest, ((com.github.pagehelper.Page<?>) goodsInfos).getTotal());
    }

    /**
     * 返回商品信息数量
     *
     * @return 商品信息数量
     */
    public int getAllGoodsInfoNum() {
        return goodsInfoDao.getAllGoodsNum();
    }

    /**
     * 返回商品类别信息（分页）
     *
     * @param pageRequest 分页请求
     * @return 商品类别信息分页列表
     */
    public Page<GoodsTypesInfo> getAllGoodsTypesInfo(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNumber(), pageRequest.getPageSize());

        // 调用DAO方法
        List<GoodsTypesInfo> goodsInfos = goodsInfoDao.getAllTypes();

        // 封装成Spring Data JPA的Page对象
        return new PageImpl<>(goodsInfos, pageRequest, ((com.github.pagehelper.Page<?>) goodsInfos).getTotal());
    }

    /**
     * 按类别返回商品信息（分页）
     *
     * @param goodsType   商品类别
     * @param pageRequest 分页请求
     * @return 商品信息分页列表
     */
    public Page<GoodsInfo> getGoodsInfoByType(String goodsType, PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNumber(), pageRequest.getPageSize());

        // 调用DAO方法
        List<GoodsInfo> goodsInfos = goodsInfoDao.getAllGoodsByTypicalType(goodsType);

        // 封装成Spring Data JPA的Page对象
        return new PageImpl<>(goodsInfos, pageRequest, ((com.github.pagehelper.Page<?>) goodsInfos).getTotal());
    }

    /**
     * 新增商品信息
     *
     * @param goodsCode 商品代码
     * @param name      商品名称
     * @param typeCode  类别代码
     */
    public void insertGoodsInfo(String goodsCode, String name, String typeCode) {
        goodsInfoDao.insertGoodsInfo(goodsCode, name, typeCode);
    }

    /**
     * 修改品名
     *
     * @param goodsCode 商品代码
     * @param name      商品名称
     */
    public void alterGoodsName(String goodsCode, String name) {
        goodsInfoDao.updateGoodsName(name, goodsCode);
    }

    /**
     * 修改类别
     *
     * @param goodsCode 商品代码
     * @param typeCode  类别代码
     */
    public void alterGoodsType(String goodsCode, String typeCode) {
        goodsInfoDao.updateGoodsType(typeCode, goodsCode);
    }

    /**
     * 删除商品信息
     *
     * @param goodsCode 商品代码
     */
    public void deleteGoodsInfo(String goodsCode) {
        goodsInfoDao.deleteGoodsInfo(goodsCode);
    }

    /**
     * 模糊查找商品信息（分页）
     *
     * @param value       搜索关键字
     * @param pageRequest 分页请求
     * @return 商品信息分页列表
     */
    public Page<GoodsInfo> searchGoodsInfo(String value, PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNumber(), pageRequest.getPageSize());

        // 调用DAO方法
        List<GoodsInfo> goodsInfos = goodsInfoDao.searchGoodsInfo(value);

        // 封装成Spring Data JPA的Page对象
        return new PageImpl<>(goodsInfos, pageRequest, ((com.github.pagehelper.Page<?>) goodsInfos).getTotal());
    }

    /**
     * 获取模糊查找商品信息的数量
     *
     * @param value 搜索关键字
     * @return 商品信息数量
     */
    public int searchGoodsInfoNum(String value) {
        return goodsInfoDao.searchGoodsInfoNum(value);
    }

    /**
     * 根据商品代码获取商品信息
     *
     * @param goodsCode 商品代码
     * @return 商品信息
     */
    public GoodsInfo getGoodsInfoByCode(String goodsCode) {
        return goodsInfoDao.getGoodsInfoByCode(goodsCode);
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

            return new ExcelRecvReturnData(1, goodsInfoDao.getAllColumnNames(),
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
                String goodsCode = rowData.get("GoodsCode");
                if(goodsCode == null)
                    return new Status(0, "未指定主码");
                if(goodsCode.length() > 0)
                {
                    for (Map.Entry<String, String> entry : rowData.entrySet()) {
                        if (!entry.getKey().equals("GoodsCode"))
                            goodsInfoDao.handleExcelColumns(entry.getKey(), entry.getValue(), goodsCode);
                    }
                    goodsInfoDao.handleExcelColumns("isDel", "0", goodsCode);
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

