/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Siarhei Luskanau
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package siarhei.luskanau.gps.tracker.free.dao;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.settings.ServerEntity;

public class ServerEntityDAO {

    private static final String TAG = "ServerEntityDAO";

    public static List<ServerEntity> getServerEntities(Context context) {
        List<ServerEntity> list = new ArrayList<ServerEntity>();
        try {
            String json = new String(getAssets(context, "servers.json"), "utf-8");
            List<ServerEntity> serverBeans = AppConstants.GSON.fromJson(json, ServerEntity.COLLECTION_TYPE);
            if (serverBeans != null) {
                list.addAll(serverBeans);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return list;
    }

    private static byte[] getAssets(Context context, String filename) throws Exception {
        InputStream inputStream = context.getAssets().open(filename);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            for (; ; ) {
                int length = inputStream.read(buffer);
                if (length == -1) {
                    break;
                } else {
                    if (length > 0) {
                        byteArrayOutputStream.write(buffer, 0, length);
                    } else {
                        try {
                            Thread.sleep(300);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

}