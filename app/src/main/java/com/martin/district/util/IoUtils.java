package com.martin.district.util;

import android.content.Context;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description:
 * @author: Created by martin on 16-7-1.
 */
public class IoUtils {

    private static final String TAG = IoUtils.class.getSimpleName();

    private static final String DB_NAME = "address.db";
    public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 KB

    private IoUtils() {
    }

    public static void releaseAddressDb(Context context) {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = context.getAssets().open(DB_NAME);
            os = new FileOutputStream(new File(getCacheDirectory(context, false), DB_NAME));
            copyStream(is, os, DEFAULT_BUFFER_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSilently(os);
            closeSilently(is);
        }
    }

    private static void copyStream(InputStream is, OutputStream os, int bufferSize) throws IOException {
        int count;
        final byte[] bytes = new byte[bufferSize];
        while ((count = is.read(bytes, 0, bytes.length)) != -1) {
            os.write(bytes, 0, count);
        }
        os.flush();
    }

    private static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/databases/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    public static boolean isExistsDb(Context context) {
        return new File("/data/data/" + context.getPackageName() + "/databases/" + DB_NAME).exists();
    }
}
