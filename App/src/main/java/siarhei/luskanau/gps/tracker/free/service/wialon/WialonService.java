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

package siarhei.luskanau.gps.tracker.free.service.wialon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.wialon.core.Session;

import siarhei.luskanau.gps.tracker.free.broadcast.AppBroadcastController;
import siarhei.luskanau.gps.tracker.free.service.location.LocationService;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;

public class WialonService extends Service {

    private static final String TAG = "WialonService";
    private static final String ACTION_START_TRACKING = "ACTION_START_TRACKING";
    private static final String ACTION_STOP_TRACKING = "ACTION_STOP_TRACKING";

    private Session session;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        try {
            onHandleIntent(intent);
        } catch (Throwable e) {
            Log.d(TAG, "Failed onHandleIntent", e);
        }
        return START_STICKY;
    }

    private void onHandleIntent(Intent intent) {
        if (intent != null && ACTION_START_TRACKING.equals(intent.getAction())) {
            startTracking();
        } else if (intent != null && ACTION_STOP_TRACKING.equals(intent.getAction())) {
            stopTracking();
        }
    }

    private void startTracking() {
        AppSettings.setIsTrackerStarted(this, true);
        LocationService.updateGpsListener(this);
        AppBroadcastController.sendTrackerStartedStateBroadcast(this);
    }

    private void stopTracking() {
        AppSettings.setIsTrackerStarted(this, false);
        LocationService.updateGpsListener(this);
        AppBroadcastController.sendTrackerStartedStateBroadcast(this);
    }

}