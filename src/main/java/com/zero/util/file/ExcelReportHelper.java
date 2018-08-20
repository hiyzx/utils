package com.zero.util.file;

import com.zero.util.date.DateUtil;
import com.zero.util.file.annotation.CellFormat;
import com.zero.util.file.constant.SysConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
@Slf4j
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
        try (OutputStream os = response.getOutputStream();) {
            List<String> firstTitles = new ArrayList<>(30);
            List<String> titles = new ArrayList<>(30);
            generateTitle(firstTitles, titles, clazz);// 生成列标题
            List<Map<String, Object>> dataMapList = generateValue(dataList);// 生成列值
            List<List<String>> titleList = new ArrayList<>(5);
            if (!firstTitles.isEmpty()) {
                titleList.add(firstTitles);
            }
            if (!titles.isEmpty()) {
                titleList.add(titles);
            }
            XSSFWorkbook workbook = exportData(null, titleList, dataMapList, excelName);
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.xlsx",
                    new String((excelName).getBytes("GB2312"), "iso8859-1")));
            workbook.write(os);
            os.flush();
        }
    }

    private static void generateTitle(List<String> firstTitles, List<String> titles, Class<?> clazz) {
        recursiveClass(firstTitles, titles, clazz);
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
    private static void recursiveClass(List<String> firstTitles, List<String> titles, Class<?> clazz) {
        if (clazz != Object.class) {
            Class<?> superClazz = clazz.getSuperclass();
            recursiveClass(firstTitles, titles, superClazz);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                CellFormat cellFormat = field.getAnnotation(CellFormat.class);
                if (cellFormat != null) {
                    titles.add(cellFormat.value());
                    if (StringUtils.isNotEmpty(cellFormat.firstTitle())) {
                        firstTitles.add(cellFormat.firstTitle());
                    }
                }
            }
        }
    }

    /**
     * @param workBook
     *            不需要新的sheet页就传null
     * @param titleList
     *            当前sheet页列标题
     * @param tjDatas
     *            当前sheet页列值
     * @param sheetName
     *            当前sheet页标题
     */
    private static XSSFWorkbook exportData(XSSFWorkbook workBook, List<List<String>> titleList,
            List<Map<String, Object>> tjDatas, String sheetName) {
        if (workBook == null) {
            workBook = new XSSFWorkbook();
        }
        if (sheetName == null) {
            sheetName = "sheet1";
        }
        XSSFSheet sheet = workBook.createSheet(sheetName);
        XSSFCellStyle headStyle = getHeadStyle(workBook);
        XSSFCellStyle bodyStyle = getBodyStyle(workBook);
        for (int i = 0; i < titleList.size(); i++) {
            List<String> titles = titleList.get(i);
            // 构建表头
            XSSFRow headRow = sheet.createRow(i);
            XSSFCell cell;
            for (int j = 0; j < titles.size(); j++) {
                cell = headRow.createCell(j);
                cell.setCellStyle(headStyle);
                cell.setCellValue(titles.get(j));
            }
            // mergerVerticalColumn(sheet, i, 0, titleList.size() - 1);
            mergerAlignmentColumn(sheet, i, 0, titles.size() - 1);
        }
        // 构建表体数据
        if (tjDatas.size() > 0) {
            for (int j = 0; j < tjDatas.size(); j++) {
                Map<String, Object> dataVo = tjDatas.get(j);
                XSSFRow bodyRow = sheet.createRow(j + titleList.size());
                for (int i = 0; i < dataVo.size(); i++) {
                    wrapperCellStr(bodyStyle, i, bodyRow, (String) dataVo.get(String.valueOf(i)));
                }
            }
        }
        return workBook;
    }

    /**
     * @param sheet
     * @param colIndex
     *            合并的第几列 （下标从0开始）
     * @param startRowIndex
     *            从第几行开始合并（算上标题，从0开始算）
     * @param endRowIndex
     *            从第几行结束合并
     */
    private static void mergerVerticalColumn(XSSFSheet sheet, int colIndex, int startRowIndex, int endRowIndex) {
        breakFor: for (int i = startRowIndex; i <= endRowIndex; i++) {
            Cell cell = sheet.getRow(i).getCell(colIndex);

            for (int j = i + 1; j <= endRowIndex; j++) {
                Cell celltemp = sheet.getRow(j).getCell(colIndex);

                // 如果下一行与被比较行相等，则继续该循环，直到不等才跳出
                if (!celltemp.getStringCellValue().equals(cell.getStringCellValue())) {
                    int temp = j - 1;
                    if (temp > i) {
                        // 合并单元格
                        sheet.addMergedRegion(new CellRangeAddress(i, temp, colIndex, colIndex));

                    }
                    i = temp;
                    break;
                }
                if (j == endRowIndex) {
                    sheet.addMergedRegion(new CellRangeAddress(i, endRowIndex, colIndex, colIndex));
                    break breakFor;
                }
            }
        }
    }

    /**
     * @param sheet
     * @param rowIndex
     *            合并的第几行 （下标从0开始）
     * @param startColumnIndex
     *            从第几列开始合并（从0开始算）
     * @param endColumnIndex
     *            从第几列结束合并
     */
    private static void mergerAlignmentColumn(XSSFSheet sheet, int rowIndex, int startColumnIndex, int endColumnIndex) {
        while (startColumnIndex < endColumnIndex) {
            Cell firstCell = sheet.getRow(rowIndex).getCell(startColumnIndex);
            Integer endMergedIndex = findEndMergedIndex(sheet, rowIndex, startColumnIndex + 1,
                    firstCell.getStringCellValue());
            if (endMergedIndex > startColumnIndex) {
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, startColumnIndex, endMergedIndex));
            }
            startColumnIndex = endMergedIndex + 1;
        }
    }

    /**
     * 对比从endMergedIndex-1 开始,最后一个相同单元格值的index
     */
    private static Integer findEndMergedIndex(XSSFSheet sheet, Integer rowIndex, Integer endMergedIndex,
            String firstCellValue) {
        Cell endCell = sheet.getRow(rowIndex).getCell(endMergedIndex);
        if (endCell != null && firstCellValue.equals(endCell.getStringCellValue())) {
            // 合并单元格
            endMergedIndex++;
            return findEndMergedIndex(sheet, rowIndex, endMergedIndex, firstCellValue);
        } else {
            return endMergedIndex - 1;
        }
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
            cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
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
