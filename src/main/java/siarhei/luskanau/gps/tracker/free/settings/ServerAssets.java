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

package siarhei.luskanau.gps.tracker.free.settings;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.utils.CryptUtils;
import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class ServerAssets {

    private static ServerAssets INSTANCE;

    private List<ServerEntity> serverBeanList;

    public static ServerAssets get(Context context) {
        if (INSTANCE == null) {
            synchronized (ServerAssets.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServerAssets(context);
                }
            }
        }
        return INSTANCE;
    }

    private ServerAssets(Context context) {
        try {
            String deviceId = Utils.getDeviceId(context);
            serverBeanList = new ArrayList<ServerEntity>();
            String json = new String(CryptUtils.getAssets(context, "servers.json"), "utf-8");
            List<ServerEntity> serverBeans = AppConstants.GSON.fromJson(json, ServerEntity.COLLECTION_TYPE);
            for (ServerEntity serverBean : serverBeans) {
                if (serverBean.deviceIds != null && !serverBean.deviceIds.contains(deviceId)) {
                    continue;
                }
                serverBeanList.add(serverBean);
            }
        } catch (Exception e) {
            serverBeanList = new ArrayList<ServerEntity>();
        }
    }

    public List<String> getServerNames() {
        List<String> serverNames = new ArrayList<String>();
        for (ServerEntity serverBean : serverBeanList) {
            serverNames.add(serverBean.name);
        }
        return serverNames;
    }

    public ServerEntity getServerEntity(String name) {
        for (ServerEntity serverBean : serverBeanList) {
            if (serverBean.name.equals(name)) {
                return serverBean;
            }
        }
        return null;
    }

}
