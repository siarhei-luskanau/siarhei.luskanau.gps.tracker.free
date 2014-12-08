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

package siarhei.luskanau.gps.tracker.free.service.sync;

import android.content.Context;
import android.content.SharedPreferences;

public class SyncSettings {

    private static final String SHARED_PREFERENCES_NAME = "siarhei.luskanau.gps.tracker.free.SyncSettings";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void clear(Context context) {
        getPreferences(context).edit().clear().commit();
    }

    private static final String SYNC_BACK_OFF_TIME_MS = "SYNC_BACK_OFF_TIME_MS";

    public static long getSyncBackOffTimeMs(Context context) {
        return getPreferences(context).getLong(SYNC_BACK_OFF_TIME_MS, 0);
    }

    public static void setSyncBackOffTimeMs(Context context, long value) {
        getPreferences(context).edit().putLong(SYNC_BACK_OFF_TIME_MS, value).commit();
    }

    private static final String SYNC_BACK_OFF_COUNT = "SYNC_BACK_OFF_COUNT";

    public static int getSyncBackOffCount(Context context) {
        return getPreferences(context).getInt(SYNC_BACK_OFF_COUNT, 0);
    }

    private static void setSyncBackOffCount(Context context, int value) {
        getPreferences(context).edit().putInt(SYNC_BACK_OFF_COUNT, value).commit();
    }

    public static long incBackoff(Context context) {
        int count = getSyncBackOffCount(context);
        if (count >= 10) {
            count = 0;
        }
        count++;
        long delay = (long) Math.pow(2, count);
        delay = delay * 1000;
        setSyncBackOffTimeMs(context, System.currentTimeMillis() + delay);
        setSyncBackOffCount(context, count);
        return delay;
    }

    public static void clearBackOff(Context context) {
        SharedPreferences preferences = getPreferences(context);
        if (preferences.contains(SYNC_BACK_OFF_COUNT) && preferences.contains(SYNC_BACK_OFF_TIME_MS)) {
            preferences.edit().remove(SYNC_BACK_OFF_COUNT).remove(SYNC_BACK_OFF_TIME_MS).commit();
        }
    }

    private static final String SEND_TO_SERVER_TIME_MS = "SEND_TO_SERVER_TIME_MS";

    public static long getSendToServerTimeMs(Context context) {
        return getPreferences(context).getLong(SEND_TO_SERVER_TIME_MS, 0);
    }

    public static void setSendToServerTimeMs(Context context, long value) {
        getPreferences(context).edit().putLong(SEND_TO_SERVER_TIME_MS, value).commit();
    }

    private static final String SEND_TO_SERVER_IMMEDIATELY = "SEND_TO_SERVER_IMMEDIATELY";

    public static boolean isSendToServerImmediately(Context context) {
        return getPreferences(context).getBoolean(SEND_TO_SERVER_IMMEDIATELY, false);
    }

    public static void setSendToServerImmediately(Context context, boolean value) {
        getPreferences(context).edit().putBoolean(SEND_TO_SERVER_IMMEDIATELY, value).commit();
    }

}
