package com.zero.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HtmlToPdfInterceptor extends Thread {
    private InputStream is;

    private static final Logger LOG = LoggerFactory.getLogger(HtmlToPdfInterceptor.class);

    public HtmlToPdfInterceptor(InputStream is) {
        this.is = is;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                LOG.info(line);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}