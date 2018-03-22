package com.zero.util.file;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yezhaoxing
 * @date 2018/03/22
 */
public class ExcelUtil {

    /**
     * 导出excel:标题和值要对应
     * 
     * @param titles
     *            表格各标题
     * @param columnMapList
     *            表格各类属性的集合
     */
    public static XSSFWorkbook exportExcel(List<String> titles, List<Map<String, String>> columnMapList)
            throws IOException {
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("sheet 1");
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
        if (columnMapList != null && columnMapList.size() > 0) {
            for (int j = 0; j < columnMapList.size(); j++) {
                XSSFRow bodyRow = sheet.createRow(j + 1);
                Map<String, String> columnMap = columnMapList.get(j);
                wrapperCell(bodyStyle, 0, bodyRow, String.valueOf(j + 1));
                for (int i = 0; i < columnMap.size(); i++) {
                    Integer index = i + 1;
                    wrapperCell(bodyStyle, index, bodyRow, columnMap.get(String.valueOf(index)));
                }
            }
        }
        // 设置第几列的高和宽
        sheet.setColumnWidth(1, 150 * 60);
        return workBook;
    }

    /**
     * 封装cell值
     */
    private static void wrapperCell(XSSFCellStyle bodyStyle, int columnIndex, XSSFRow bodyRow, String value) {
        XSSFCell cell = bodyRow.createCell(columnIndex);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue(value);
    }

    /**
     * 设置表头的单元格样式
     */
    private static XSSFCellStyle getHeadStyle(XSSFWorkbook wb) {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格的背景颜色为淡蓝色
        cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    /**
     * 设置表体的单元格样式
     */
    private static XSSFCellStyle getBodyStyle(XSSFWorkbook wb) {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        // font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);

        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        return cellStyle;
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
    private static Map<String, String> mergerData(XSSFSheet sheet, int colIndex, int startRowIndex, int endRowIndex) {
        Map<String, String> map = new HashMap<>();

        breakFor: for (int i = startRowIndex; i <= endRowIndex; i++) {
            Cell cell = sheet.getRow(i).getCell(colIndex);

            for (int j = i + 1; j <= endRowIndex; j++) {
                Cell celltemp = sheet.getRow(j).getCell(colIndex);

                // 如果下一行与被比较行相等，则继续该循环，直到不等才跳出
                if (!celltemp.getStringCellValue().equals(cell.getStringCellValue())) {
                    int temp = j - 1;
                    if (temp > i) {
                        // 合并单元格
                        map.put(cell.getStringCellValue(), i + 1 + "," + j);
                        sheet.addMergedRegion(new CellRangeAddress(i, temp, colIndex, colIndex));

                    }
                    i = temp;
                    break;
                }
                if (j == endRowIndex) {
                    map.put(cell.getStringCellValue(), i + 1 + "," + j);
                    sheet.addMergedRegion(new CellRangeAddress(i, endRowIndex, colIndex, colIndex));
                    break breakFor;
                }
            }
        }
        return map;
    }
}
