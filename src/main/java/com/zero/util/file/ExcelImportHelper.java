package com.zero.util.file;

import com.zero.util.file.annotation.CellFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @description 导入excel工具类
 * @author yezhaoxing
 * @since 2018/06/22
 */
public class ExcelImportHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelImportHelper.class);

    /**
     * 导入excel
     * 
     * @param clazz
     *            每一行对应的class类
     * @param inputStream
     *            输入流
     * @return 返回对应的类集合
     */
    public static <T> List<T> importExcel(Class<T> clazz, InputStream inputStream) throws Exception {
        Workbook workbook = new XSSFWorkbook(inputStream);
        List<T> list = new ArrayList<>(128);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        Map<Integer, String> titles = new HashMap<>(30);
        Map<String, String> propertyMap = generatePropertyMapList(clazz);// 获取bean属性和中文对应
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) {
                generateProperty(row, titles, propertyMap);
                continue;
            }
            Iterator<Cell> cellIterator = row.cellIterator();
            // 整行都空，就跳过
            boolean allRowIsNull = true;
            T t = clazz.newInstance();
            while (cellIterator.hasNext()) {
                Cell next = cellIterator.next();
                Object cellValue = setValue(t, clazz, titles.get(next.getColumnIndex()), next);
                if (cellValue != null) {
                    allRowIsNull = false;
                }
            }
            if (!allRowIsNull) {
                list.add(t);
            } else {
                LOG.info("导入Excel的第[{}]行整行为空", row.getRowNum());
            }
        }
        return list;
    }

    // 获取每一列对应的bean属性<列数,属性名>
    private static void generateProperty(Row row, Map<Integer, String> titles, Map<String, String> propertyMap) {
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell next = cellIterator.next();
            String value = next.getStringCellValue();
            titles.put(next.getColumnIndex(), propertyMap.get(value));
        }
    }

    // 设置属性值
    private static Object setValue(Object object, Class<?> clazz, String beanProperty, Cell next) throws Exception {
        Object cellValue = null;
        try {
            Field field = clazz.getDeclaredField(beanProperty);
            field.setAccessible(true);
            cellValue = getCellValue(next, field.getType());
            field.set(object, cellValue);
        } catch (NoSuchFieldException e) {
            setValue(object, clazz.getSuperclass(), beanProperty, next);
        }
        return cellValue;
    }

    private static Map<String, String> generatePropertyMapList(Class<?> clazz) {
        Map<String, String> propertyMap = new HashMap<>(100);
        recursiveClass(propertyMap, clazz);
        return propertyMap;
    }

    // 递归获取子类及父类所有属性名称
    private static void recursiveClass(Map<String, String> propertyMap, Class<?> clazz) {
        if (clazz != Object.class) {
            Class<?> superClazz = clazz.getSuperclass();
            recursiveClass(propertyMap, superClazz);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                CellFormat cellFormat = field.getAnnotation(CellFormat.class);
                if (cellFormat != null) {
                    String value = cellFormat.value();
                    propertyMap.put(value, field.getName());
                }
            }
        }
    }

    // 将单元格的值格式化成对应类属性
    private static Object getCellValue(Cell cell, Class<?> type) {
        CellType cellTypeEnum = cell.getCellTypeEnum();
        switch (cellTypeEnum) {
        case BLANK:
            return "";
        case BOOLEAN:
            return cell.getBooleanCellValue();
        case NUMERIC:
            if (String.class.isAssignableFrom(type)) {
                cell.setCellType(CellType.STRING);
                return trim(cell.getStringCellValue());
            } else if (Date.class.isAssignableFrom(type)) {
                return cell.getDateCellValue();
            }
            return cell.getNumericCellValue();
        case STRING:
            return trim(cell.getStringCellValue());
        case FORMULA:
            return String.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    private static String trim(String str) {
        return StringUtils.isEmpty(str) ? "" : str.trim();
    }
}
