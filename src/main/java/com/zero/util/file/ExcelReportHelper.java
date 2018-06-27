package com.zero.util.file;

import com.zero.util.date.DateUtil;
import com.zero.util.file.annotation.CellFormat;
import com.zero.util.file.constant.SysConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author yezhaoxing
 * @description excel导出工具类
 * @since 2018/06/12
 */
public class ExcelReportHelper {

    /**
     * @description 生成excel返回输出流
     * @param response
     *            response
     * @param excelName
     *            导出的excel文件名
     * @param clazz
     *            excel列表数据的class对象
     * @param dataList
     *            数据
     */
    public static void exportExcel(HttpServletResponse response, String excelName, Class<?> clazz, List<?> dataList)
            throws Exception {
        List<String> titles = generateTitle(clazz);// 生成列标题
        List<Map<String, Object>> dataMapList = generateValue(dataList);// 生成列值
        XSSFWorkbook workbook = exportData(null, titles, dataMapList, excelName, true);
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition",
                String.format("attachment; filename=%s.xlsx", new String((excelName).getBytes("GB2312"), "iso8859-1")));
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.flush();
        os.close();
    }

    private static List<String> generateTitle(Class<?> clazz) {
        List<String> titles = new ArrayList<>(100);
        titles.add("序号");
        recursiveClass(titles, clazz);
        return titles;
    }

    private static List<Map<String, Object>> generateValue(List<?> dataList) throws Exception {
        List<Map<String, Object>> dataMapList = new ArrayList<>(dataList.size());
        for (Object obj : dataList) {
            Map<String, Object> dataMap = new LinkedHashMap<>(100);
            recursiveValue(dataMap, obj.getClass(), obj);
            dataMapList.add(dataMap);
        }
        return dataMapList;
    }

    // 递归获取子类及父类所有属性值
    private static void recursiveValue(Map<String, Object> dataMap, Class<?> aClass, Object obj) throws Exception {
        if (aClass != Object.class) {
            Class<?> superclass = aClass.getSuperclass();
            recursiveValue(dataMap, superclass, obj);
            Field[] fields = aClass.getDeclaredFields();
            int size = dataMap.size();
            int t = size;
            for (int j = size; j < fields.length + size; j++) {// 遍历
                Field field = fields[j - size];
                CellFormat cellFormat = field.getAnnotation(CellFormat.class);
                if (cellFormat != null) {
                    t++;
                    field.setAccessible(true); // 打开私有访问
                    Object value = field.get(obj); // 获取属性值
                    dataMap.put(String.valueOf(t), cellValue(field, value));
                }
            }
        }
    }

    private static Object cellValue(Field field, Object value) throws Exception {
        if (value != null) {
            if (value instanceof Date) {
                return dateCellFormat(field, value);
            } else {
                return String.valueOf(cellFormat(field, value));
            }
        } else {
            return "";
        }
    }

    private static Object cellFormat(Field field, Object value) throws Exception {
        CellFormat cellFormat = field.getAnnotation(CellFormat.class);
        if (null != cellFormat) {
            String filterClass = cellFormat.filterClass();
            String filterMethod = cellFormat.filterMethod();
            if (StringUtils.isNotEmpty(filterClass) && StringUtils.isNotEmpty(filterMethod)) {
                Class<?> clazz = Class.forName(SysConstants.CELL_FORMAT_FILTER);
                Method method = clazz.getDeclaredMethod(filterMethod, Object.class);
                value = method.invoke(clazz.newInstance(), value);
            }
        }
        return value;
    }

    private static String dateCellFormat(Field field, Object value) throws Exception {
        CellFormat cellFormat = field.getAnnotation(CellFormat.class);
        String patten;
        if (null != cellFormat && StringUtils.isNotEmpty(cellFormat.datePatten())) {
            patten = cellFormat.datePatten();
        } else {
            patten = SysConstants.DEFAULT_PATTEN;
        }
        return value == null ? "" : DateUtil.format((Date) value, patten);
    }

    // 递归获取子类及父类所有属性名称
    private static void recursiveClass(List<String> titles, Class<?> clazz) {
        if (clazz != Object.class) {
            Class<?> superClazz = clazz.getSuperclass();
            recursiveClass(titles, superClazz);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                CellFormat cellFormat = field.getAnnotation(CellFormat.class);
                if (cellFormat != null) {
                    titles.add(cellFormat.value());
                }
            }
        }
    }

    /**
     * @param workBook
     *            不需要新的sheet页就传null
     * @param titles
     *            当前sheet页列标题
     * @param tjDatas
     *            当前sheet页列值
     * @param sheetName
     *            当前sheet页标题
     */
    private static XSSFWorkbook exportData(XSSFWorkbook workBook, List<String> titles,
            List<Map<String, Object>> tjDatas, String sheetName, Boolean hasNumber) {
        if (workBook == null) {
            workBook = new XSSFWorkbook();
        }
        if (sheetName == null) {
            sheetName = "sheet1";
        }
        XSSFSheet sheet = workBook.createSheet(sheetName);
        XSSFCellStyle headStyle = getHeadStyle(workBook);
        XSSFCellStyle bodyStyle = getBodyStyle(workBook);
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell;
        for (int i = 0; i < titles.size(); i++) {
            cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(titles.get(i));
        }
        // 构建表体数据
        if (tjDatas != null && tjDatas.size() > 0) {
            for (int j = 0; j < tjDatas.size(); j++) {
                Map<String, Object> dataVo = tjDatas.get(j);
                XSSFRow bodyRow = sheet.createRow(j + 1);
                int firstNumber = 0;
                if (hasNumber) {
                    wrapperCellStr(bodyStyle, 0, bodyRow, String.valueOf(j + 1));
                    firstNumber++;
                }
                for (int i = 0; i < dataVo.size(); i++) {
                    int index = i + firstNumber;
                    int mapIndex = index;
                    if (!hasNumber) {
                        mapIndex++;
                    }
                    wrapperCellStr(bodyStyle, index, bodyRow, (String) dataVo.get(String.valueOf(mapIndex)));
                }
            }
        }
        return workBook;
    }

    private static void wrapperCellStr(XSSFCellStyle bodyStyle, int columnIndex, XSSFRow bodyRow, String value) {
        XSSFCell cell = bodyRow.createCell(columnIndex);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue(value);
    }

    /**
     * 设置表头的单元格样式
     */
    private static XSSFCellStyle getHeadStyle(XSSFWorkbook wb) {
        return getStyle(wb, true, true);
    }

    /**
     * 设置表体的单元格样式
     */
    private static XSSFCellStyle getBodyStyle(XSSFWorkbook wb) {
        return getStyle(wb, false, false);
    }

    private static XSSFCellStyle getStyle(XSSFWorkbook wb, Boolean backColor, Boolean bold) {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        if (backColor) {
            // 设置单元格的背景颜色为淡蓝色
            cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        // 设置单元格居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 创建单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        font.setBold(bold);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        return cellStyle;
    }
}
