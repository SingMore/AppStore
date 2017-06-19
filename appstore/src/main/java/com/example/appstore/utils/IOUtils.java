package com.example.appstore.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by SingMore on 2017/2/6.
 */

public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
}
