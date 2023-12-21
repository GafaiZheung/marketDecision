package com.example.marketdecision.service;

import com.example.marketdecision.Bean.ExcelRecvReturnData;
import com.example.marketdecision.Bean.GoodsTypesInfo;
import com.example.marketdecision.Bean.Status;
import com.example.marketdecision.dao.GoodsTypeDao;
import com.github.pagehelper.PageHelper;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 商品类别服务类
 */
@Service
public class GoodsTypeService {

    @Resource
    private GoodsTypeDao goodsTypeDao;

    /**
     * 返回所有商品类别信息（分页）
     *
     * @param pageRequest 分页请求对象
     * @return 商品类别信息的分页数据
     */
    public Page<GoodsTypesInfo> getAllGoodsTypesInfo(PageRequest pageRequest) {
        // 使用PageHelper设置分页参数，这里使用Spring Data JPA的PageRequest
        PageHelper.startPage(pageRequest.getPageNumber(), pageRequest.getPageSize());

        // 调用DAO方法获取商品类别信息列表
        List<GoodsTypesInfo> goodsTypesInfos = goodsTypeDao.getAllGoodsTypesInfo();

        // 封装成Spring Data JPA的Page对象
        return new PageImpl<>(goodsTypesInfos, pageRequest, ((com.github.pagehelper.Page<?>) goodsTypesInfos).getTotal());
    }

    /**
     * 新增商品类别
     *
     * @param typeCode 商品类别编码
     * @param typeName 商品类别名称
     */
    public void insertGoodsType(String typeCode, String typeName) {
        goodsTypeDao.insertGoodsType(typeCode, typeName);
    }

    /**
     * 删除商品类别
     *
     * @param typeCode 商品类别编码
     */
    public void deleteGoodsType(String typeCode) {
        goodsTypeDao.deleteGoodsType(typeCode);
    }

    /**
     * 更改商品类别名
     *
     * @param typeCode 商品类别编码
     * @param typeName 新的商品类别名称
     */
    public void updateGoodsType(String typeCode, String typeName) {
        goodsTypeDao.updateGoodsType(typeName, typeCode);
    }

    /**
     * 处理上传的 Excel 文件
     *
     * @param file 上传的 Excel 文件
     * @return Excel 文件处理结果
     * @throws Exception 处理异常
     */
    public ExcelRecvReturnData handleUploadExcel(MultipartFile file) throws Exception {
        if (isExcelFile(file)) {
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();

            // 生成随机文件名
            String randomFileName = generateRandomFileName();

            // 构建目标文件对象
            File targetFile = new File("./tempRecv", randomFileName + getFileExtension(originalFilename));

            // 创建目标目录（如果不存在）
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            // 保存文件
            try (FileOutputStream fileOutputStream = new FileOutputStream(targetFile)) {
                fileOutputStream.write(file.getBytes());
            }

            // 处理返回值
            return new ExcelRecvReturnData(1, null, null, targetFile.getPath());

        } else {
            return new ExcelRecvReturnData(0, null, null, null);
        }
    }

    /**
     * 处理上传的 Excel 数据
     *
     * @param filePath   Excel 文件路径
     * @param relations  列映射关系
     * @return 处理结果
     */
    public Status handleUploadData(String filePath, List<List<String>> relations) {
        File file = new File(filePath);

        if (!file.exists()) {
            return new Status(500, "服务器内部错误");
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            Workbook workbook;
            if (filePath.toLowerCase().endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                workbook = WorkbookFactory.create(fis);
            }

            Sheet sheet = workbook.getSheetAt(0); // 假设你想从第一个sheet中读取

            // 获取列名和它们的索引映射
            Map<String, Integer> columnIndexMap = getColumnIndexMap(sheet.getRow(0));

            // 取出 relations 中包含的列
            Map<List<String>, Integer> relationsColumnIndexMap = new HashMap<>();
            for (List<String> relation : relations) {
                String columnName = relation.get(0);
                Integer columnIndex = columnIndexMap.get(columnName);
                if (columnIndex != null) {
                    relationsColumnIndexMap.put(relation, columnIndex);
                }
            }

            // 获取迭代器，从第二行开始
            Iterator<Row> iterator = sheet.iterator();
            if (iterator.hasNext()) {
                iterator.next(); // 跳过第一行
            }
            while (iterator.hasNext()) {
                Row row = iterator.next();
                // 存储每行中对应的列值
                Map<String, String> rowData = new HashMap<>();

                // 遍历 relations 中包含的列
                for (Map.Entry<List<String>, Integer> entry : relationsColumnIndexMap.entrySet()) {
                    String columnName = entry.getKey().get(0);
                    Integer columnIndex = entry.getValue();

                    // 获取列值并放入 rowData
                    Cell cell = row.getCell(columnIndex);
                    String cellValue = getCellValueAsString(cell);
                    rowData.put(entry.getKey().get(1), cellValue);
                }

                // 在这里可以对 rowData 进行处理
                // 这里仅作示例，可以根据实际需求调用其他服务处理数据
                System.out.println("Row Data: " + rowData);
            }

            return new Status(1, "更新成功");

        } catch (IOException | EncryptedDocumentException e) {
            return new Status(500, "服务器内部错误");
        }
    }

    private Map<String, Integer> getColumnIndexMap(Row headerRow) {
        Map<String, Integer> columnIndexMap = new HashMap<>();

        for (Cell cell : headerRow) {
            columnIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }

        return columnIndexMap;
    }

    private boolean isExcelFile(MultipartFile file) {
        try {
            // 使用 Apache POI 来尝试解析文件
            Workbook workbook;
            try {
                workbook = WorkbookFactory.create(file.getInputStream());
            } catch (Exception ex) {
                // 如果 WorkbookFactory 无法解析，尝试使用 HSSFWorkbook
                workbook = new HSSFWorkbook(file.getInputStream());
            }
            // 如果成功解析，说明是 Excel 文件
            return true;
        } catch (Exception e) {
            // 解析失败，不是 Excel 文件
            return false;
        }
    }

    private String getCellValueAsString(Cell cell) {
        DataFormatter dataFormatter = new DataFormatter();
        return dataFormatter.formatCellValue(cell);
    }

    private String generateRandomFileName() {
        // 使用 Random 生成 32 位数
        Random random = new Random();
        long randomLong = random.nextLong();

        // 将 32 位数转换成字符串并进行 BASE64 编码
        String base64Encoded = Base64.getEncoder().encodeToString(String.valueOf(randomLong).getBytes());

        // 去除可能的特殊字符，保留字母和数字
        return base64Encoded.replaceAll("[^a-zA-Z0-9]", "");
    }

    private String getFileExtension(String fileName) {
        // 获取文件扩展名，包括点号（.）
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex != -1 ? fileName.substring(lastDotIndex) : "";
    }
}
