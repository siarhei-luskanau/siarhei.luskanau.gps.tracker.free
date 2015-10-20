/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Siarhei Luskanau
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

package siarhei.luskanau.gps.tracker.free.service.sync.tracking;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.Callable;

import siarhei.luskanau.gps.tracker.free.dao.LocationDAO;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.service.sync.SyncService;
import siarhei.luskanau.gps.tracker.free.service.sync.SyncSettings;
import siarhei.luskanau.gps.tracker.free.service.sync.tracking.json.SendJsonForm;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class SendPositionsCallable implements Callable<Object> {

    private static final String TAG = "SendPositionsCallable";
    private Context context;

    public SendPositionsCallable(Context context) {
        this.context = context;
    }

    @Override
    public Object call() throws Exception {
        try {
            for (; ; ) {
                if (!Utils.isOnline(context)) {
                    throw new IOException("not online");
                }

                boolean isAllowSendToServer = true;
                AppSettings.State appSettingsState = AppSettings.getAppSettingsEntity(context);
                if (!SyncSettings.isSendToServerImmediately(context)) {
                    // check send to server interval
                    if (appSettingsState.internetSettingsEntity.sendToServerInterval > 0) {
                        long delay = SyncSettings.getSendToServerTimeMs(context) + appSettingsState.internetSettingsEntity.sendToServerInterval - System.currentTimeMillis();
                        if (delay >= 0) {
                            SyncService.delaySendPositions(context, delay);
                            return null;
                        }
                    }

                    if (appSettingsState.internetSettingsEntity.sendToServerInterval == -1) {
                        isAllowSendToServer = false;
                    }
                    // check use Internet
                    if (appSettingsState.internetSettingsEntity.internetType == AppSettings.InternetType.OFFLINE_TYPE) {
                        isAllowSendToServer = false;
                    }
                    // check use Wi-Fi only
                    if (appSettingsState.internetSettingsEntity.internetType == AppSettings.InternetType.WIFI_TYPE
                            && !Utils.isWifiNetworkType(context)) {
                        isAllowSendToServer = false;
                    }
                }
                if (isAllowSendToServer) {
                    sendPositions();
                    SyncSettings.setSendToServerImmediately(context, false);
                    // update send to server time
                    if (appSettingsState.internetSettingsEntity.sendToServerInterval > 0) {
                        SyncSettings.setSendToServerTimeMs(context, System.currentTimeMillis());
                    }
                }
                SyncSettings.clearBackOff(context);
                if (!LocationDAO.hasLocations(context)) {
                    return null;
                }
            }
        } catch (Exception e) {
            boolean needBackOff = true;
            if (SyncSettings.getSyncBackOffCount(context) > 0 && SyncSettings.getSyncBackOffTimeMs(context) - System.currentTimeMillis() > 900) {
                needBackOff = false;
            }
            if (needBackOff) {
                Log.w(TAG, e.toString());
                long delay = SyncSettings.incBackoff(context);
                SyncService.delaySendPositions(context, delay);
            }
        }
        return null;
    }

    private void sendPositions() throws Exception {
        ServerEntity serverEntity = AppSettings.getServerEntity(context);
        switch (serverEntity.serverType) {
            case socket: {
                new SendSocketTask().doTask(context);
                break;
            }
            case rest: {
                SendRestTask.doTask(context);
                break;
            }
            case json_form: {
                SendJsonForm.sendLocationsForm(context);
                break;
            }
        }
        //LocationDAO.deleteLocations(context, LocationDAO.queryNextLocations(context, 10));
    }

}