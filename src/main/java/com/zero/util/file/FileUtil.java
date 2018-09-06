package com.zero.util.file;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author yezhaoxing
 * @since 2018/6/14
 * @description 文件工具类
 */
@Slf4j
public class FileUtil {

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
            log.error(e.getMessage(), e);
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
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
            log.error(e.getMessage(), e);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
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
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                File f = new File(file.getPath());
                f.deleteOnExit();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
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
            log.error(e.getMessage(), e);
        }
    }
}
