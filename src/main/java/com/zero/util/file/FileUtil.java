package com.zero.util.file;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ClassName: FileUtil
 *
 * @author angelo
 * @Description: 文件帮助工具
 * @date 2016-5-9
 */
public class FileUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    /**
     * @param filePath
     *            包含文件路径及文件名
     * @param content
     *            写入的内容
     * @Description: 向文件写入内容
     */
    public static void write(String filePath, String content) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath, false);
            fw.write(content);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * @param filePath
     *            包含文件路径及文件名
     * @Description: 读取文件的内容
     * @remark 逐个字符读取
     */
    public static String read(String filePath) {
        FileReader fr = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            fr = new FileReader(filePath);
            StringBuilder sb = new StringBuilder();
            int ch = 0;
            while ((ch = fr.read()) != -1) {
                sb.append((char) ch);
            }
            return sb.toString();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    // 文件打包下载
    public static void downLoadFiles(List<File> files, String projectName) throws Exception {
        File file = new File(projectName);// 创建临时压缩文件
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fous = new FileOutputStream(file);
        ZipOutputStream zipOut = new ZipOutputStream(fous);
        zipFile(files, zipOut);
        zipOut.close();
        fous.close();
    }

    // 把接受的全部文件打成压缩包
    private static void zipFile(List<File> files, ZipOutputStream outputStream) throws IOException {
        for (File file : files) {
            zipFile(file, outputStream);
        }
    }

    private static void downloadZip(File file, HttpServletResponse response) {
        try {
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");

            // 如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        } finally {
            try {
                File f = new File(file.getPath());
                f.deleteOnExit();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     */
    private static void zipFile(File inputFile, ZipOutputStream ouputStream) {
        try {
            if (inputFile.exists()) {
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    ouputStream.putNextEntry(entry);
                    int nNumber;// 向压缩文件中输出数据
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    bins.close();
                    IN.close();
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private static void createRow(XSSFSheet sheet, XSSFCellStyle style, Integer rowIndex, String title, String fileName,
            String fileDesc) {
        XSSFRow row = sheet.createRow(rowIndex);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue(fileName);
        cell = row.createCell(2);
        cell.setCellValue(fileDesc);
        cell.setCellStyle(style);
    }

    /**
     * 到处excel
     */
    public static XSSFWorkbook exportExcel(List<String> titles, List<Map<String, Object>> columnMapList)
            throws IOException {
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("导出excel例子");
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
                Map<String, Object> projectMap = columnMapList.get(j);
                // 类似
                wrapperCell(bodyStyle, 1, bodyRow, (String) projectMap.get("1"));
                wrapperCell(bodyStyle, 2, bodyRow, (String) projectMap.get("2"));
            }
        }
        // 设置第几列的高和宽
        sheet.setColumnWidth(1, 150 * 60);
        sheet.setColumnWidth(2, 60 * 60);
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
