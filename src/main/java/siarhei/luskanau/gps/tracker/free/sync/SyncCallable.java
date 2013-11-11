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

package siarhei.luskanau.gps.tracker.free.sync;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.Callable;

import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.settings.AppSettingsEntity;
import siarhei.luskanau.gps.tracker.free.sync.task.GcmTask;
import siarhei.luskanau.gps.tracker.free.sync.task.SendLocationTask;
import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class SyncCallable implements Callable<Object> {

    private static final String TAG = "SyncCallable";
    private Context context;

    public SyncCallable(Context context) {
        this.context = context;
    }

    @Override
    public Object call() throws Exception {
        try {
            if (!Utils.isOnline(context)) {
                throw new IOException("not online");
            }

            new GcmTask(context).doTask();

            boolean isAllowSendToServer = true;
            AppSettingsEntity appSettingsEntity = AppSettings.getAppSettingsEntity(context);
            if (!SyncSettings.isSendToServerImmediately(context)) {
                // check send to server interval
                if (appSettingsEntity.internetSettingsEntity.sendToServerInterval > 0) {
                    long delay = SyncSettings.getSendToServerTimeMs(context)
                            + appSettingsEntity.internetSettingsEntity.sendToServerInterval
                            - System.currentTimeMillis();
                    if (delay >= 0) {
                        SyncService.delayTask(context, delay);
                        return null;
                    }
                }

                if (appSettingsEntity.internetSettingsEntity.sendToServerInterval == -1) {
                    isAllowSendToServer = false;
                }
                // check use Internet
                if (!appSettingsEntity.internetSettingsEntity.isUseInternet) {
                    isAllowSendToServer = false;
                }
                // check use Wi-Fi only
                if (appSettingsEntity.internetSettingsEntity.isUseWifiOny
                        && !Utils.isWifiNetworkType(context)) {
                    isAllowSendToServer = false;
                }
            }
            if (isAllowSendToServer) {
                new SendLocationTask(context).doTask();
                SyncSettings.setSendToServerImmediately(context, false);
                // update send to server time
                if (appSettingsEntity.internetSettingsEntity.sendToServerInterval > 0) {
                    SyncSettings.setSendToServerTimeMs(context, System.currentTimeMillis());
                }
            }

            SyncSettings.clearBackoff(context);
        } catch (Exception e) {
            boolean needBackoff = true;
            if (SyncSettings.getSyncBackoffCount(context) > 0 && SyncSettings.getSyncBackoffTimeMs(context) - System.currentTimeMillis() > 900) {
                needBackoff = false;
            }
            if (needBackoff) {
                Log.w(TAG, e.toString());
                long delay = SyncSettings.incBackoff(context);
                SyncService.delayTask(context, delay);
            }
        }
        return null;
    }

}
