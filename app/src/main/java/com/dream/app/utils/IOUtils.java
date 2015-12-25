package com.dream.app.utils;

import com.socks.library.KLog;

import java.io.Closeable;
import java.io.IOException;

/**
 * ============================================================
 * 描 述 :
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/16.
 * ============================================================
 */
public class IOUtils {
    /**
     * 关闭流
     */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                KLog.e(e);
            }
        }
        return true;
    }
}
