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

package siarhei.luskanau.gps.tracker.free.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.broadcast.AppBroadcastController;
import siarhei.luskanau.gps.tracker.free.service.location.LocationService;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class TrackerService extends Service {

    private static final String TAG = "TrackerService";
    private static final String ACTION_START_TRACKING = "ACTION_START_TRACKING";
    private static final String ACTION_STOP_TRACKING = "ACTION_STOP_TRACKING";
    private static final String ACTION_ANTI_KILLER = "ACTION_ANTI_KILLER";

    public static void startTracking(Context context) {
        context.startService(new Intent(context, TrackerService.class).setAction(ACTION_START_TRACKING));
    }

    public static void stopTracking(Context context) {
        context.startService(new Intent(context, TrackerService.class).setAction(ACTION_STOP_TRACKING));
    }

    public static void pingAntiKiller(Context context) {
        context.startService(new Intent(context, LocationService.class).setAction(ACTION_ANTI_KILLER));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
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

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    private void onHandleIntent(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_START_TRACKING: {
                    startTracking();
                    break;
                }
                case ACTION_STOP_TRACKING: {
                    stopTracking();
                    break;
                }
                case ACTION_ANTI_KILLER: {
                    Log.d(TAG, ACTION_ANTI_KILLER);
                    if (AppSettings.getAppSettingsEntity(this).isTrackerStarted) {
                        LocationService.updateGpsListener(this);
                        //TODO sendTask
                    } else {
                        cancelAntiKillerPing(this);
                    }
                    break;
                }
            }
        }
        showNotification();
    }

    private void startTracking() {
        startAntiKillerPing(this);
        AppSettings.setIsTrackerStarted(this, true);
        LocationService.updateGpsListener(this);
        AppBroadcastController.sendTrackerStartedStateBroadcast(this);
    }

    private void stopTracking() {
        cancelAntiKillerPing(this);
        AppSettings.setIsTrackerStarted(this, false);
        LocationService.updateGpsListener(this);
        AppBroadcastController.sendTrackerStartedStateBroadcast(this);
    }

    private void showNotification() {
        AppSettings.State appSettingsState = AppSettings.getAppSettingsEntity(this);
        if (appSettingsState.isTrackerStarted && appSettingsState.isShowNotification) {
            startForeground(R.id.tracker_service_notification_id, Utils.createNotification(this, getString(R.string.service_tracker_running)));
        } else {
            stopForeground(true);
        }
    }

    private void startAntiKillerPing(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 30 * 1000, getAntiKillerPendingIntent(context));
    }

    private void cancelAntiKillerPing(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getAntiKillerPendingIntent(context));
    }

    private PendingIntent getAntiKillerPendingIntent(Context context) {
        return PendingIntent.getService(context, 0, new Intent(context, LocationService.class).setAction(ACTION_ANTI_KILLER), PendingIntent.FLAG_UPDATE_CURRENT);
    }

}