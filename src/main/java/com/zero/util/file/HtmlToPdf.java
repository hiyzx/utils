package com.zero.util.file;

import com.zero.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yezhaoxing
 * @since 2018/6/21
 * @description pdf生成工具类
 */
public class HtmlToPdf {
    private static final Logger LOG = LoggerFactory.getLogger(HtmlToPdfInterceptor.class);

    // 定义一个线程池
    private static ExecutorService executorService1 = Executors.newFixedThreadPool(10);
    private static ExecutorService executorService2 = Executors.newFixedThreadPool(10);

    private static final String WEB_URL = "http://localhost:8080/";

    private static final String TO_PDF_TOOL = "E:\\wkhtmltox\\bin\\wkhtmltopdf.exe";

    private static final String PA_PDF_PATH = "E:\\HTML_PDF\\";

    private static final String TAX_API_URI = "tax/";

    private static final String HEAD_HTML = "pdfFile/html/header.html?companyName=%s";

    private static final String COVER_HTML = "pdfFile/html/cover.html?companyName=%s&reportNum=%s";

    private static final String INDEX_HTML = "pdfFile/index.html?nsrsbh=%s&taxApiUrl=%s";

    public static String convert(String tdCode, String companyName) {
        String destPath = String.format("%s%s%s%s%s", PA_PDF_PATH, tdCode, File.separator, System.currentTimeMillis(),
                ".pdf");
        String reportNum = String.format("%s%s%s", "MS", DateUtil.format(new Date(), "yyyyMMddHHmmss"), "102d");
        File file = new File(destPath);
        File parent = file.getParentFile();
        // 如果PDF保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }

        StringBuilder cmd = new StringBuilder();
        cmd.append(TO_PDF_TOOL);
        cmd.append(" --page-size A4 ");
        cmd.append(" --header-line ");// 页眉下面的线
        cmd.append(" --header-html ").append(WEB_URL).append(String.format(HEAD_HTML, companyName)).append(" ");
        cmd.append(" --header-font-size 16 ");
        cmd.append(" --header-spacing 5 ");
        cmd.append(" --margin-top 20mm --margin-bottom 12mm --margin-right 10mm --margin-left 10mm ");
        cmd.append(" --footer-spacing 5 ");
        cmd.append(" --footer-font-size 6 ");
        cmd.append(" --footer-center [page]/[toPage] ");
        cmd.append(" cover ").append(WEB_URL).append(String.format(COVER_HTML, companyName, reportNum));
        cmd.append(" --no-stop-slow-scripts ");
        cmd.append(" --javascript-delay 2000 ");
        String taxApiUrl = String.format("%s%s", WEB_URL, TAX_API_URI);
        cmd.append(WEB_URL).append(String.format(INDEX_HTML, tdCode, taxApiUrl));
        cmd.append(" ");
        cmd.append(destPath);

        String cmdStr = cmd.toString();
        LOG.info("{}PDF生成的cmd:{} ", tdCode, cmdStr);
        Process exec = null;
        try {
            exec = Runtime.getRuntime().exec(cmdStr);
            final InputStream is1 = exec.getInputStream(); // 获取进程的标准输入流
            final InputStream is2 = exec.getErrorStream(); // 获取进城的错误流
            // 启动两个线程，一个线程负责读标准creating, waitFo输出流，另一个负责读标准错误流
            HtmlToPdfInterceptor interceptor1 = new HtmlToPdfInterceptor(is1);
            HtmlToPdfInterceptor interceptor2 = new HtmlToPdfInterceptor(is2);
            executorService1.execute(interceptor1);
            executorService2.execute(interceptor2);
            LOG.info("[{}]PDF creating, waitFor before", tdCode);
            exec.waitFor();
            LOG.info("[{}]PDF creating, waitFor after", tdCode);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
            throw new RuntimeException("pdf生成失败");
        } finally {
            if (null != exec) {
                exec.destroy();
            }
        }
        return destPath;
    }
}
