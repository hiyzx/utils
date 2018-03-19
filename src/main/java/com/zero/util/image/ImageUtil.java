package com.zero.util.image;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

/**
 * 
 * ClassName: ImageUtil 
 * @Description: 图片工具类
 * @author angelo
 * @date 2016-5-9
 */
public class ImageUtil {

	/**
	 * 
	 * @Description: 下载图片
	 * @remark
	 * @param strUrl 图片的url地址
	 * @param fileName 保存图片的路径
	 * @author angelo
	 * @throws Exception 
	 * @date 2016-5-9
	 */
	public static void downloadImage(String strUrl, String savePath, String fileName) throws Exception {
		URL url = null;
		InputStream in = null;
		FileOutputStream out = null;
		try {
			File file = new File(savePath);
			if (file.exists() == false) {
				file.mkdirs();
			}

			url = new URL(strUrl);
			in = new DataInputStream(url.openStream());
			out = new FileOutputStream(new File(Paths.get(savePath, fileName).toString()));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
