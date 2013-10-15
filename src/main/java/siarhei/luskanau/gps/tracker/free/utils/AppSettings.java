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

package siarhei.luskanau.gps.tracker.free.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import siarhei.luskanau.gps.shared.LocationPacket;
import siarhei.luskanau.gps.tracker.free.AppConstants;

public class AppSettings {

    private static final String LAST_LOCATION_PACKET = "LAST_LOCATION_PACKET";
    private static final String NTP_DIFFERENT_TIME = "NTP_DIFFERENT_TIME";
    private static final String NTP_SYNCHRONIZATION_TIME = "NTP_SYNCHRONIZATION_TIME";

    public static LocationPacket getLastLocationPacket(Context context) {
        return AppConstants.GSON.fromJson(getPreferences(context).getString(LAST_LOCATION_PACKET, null), LocationPacket.class);
    }

    public static void setLastLocationPacket(Context context, LocationPacket value) {
        getPreferences(context).edit().putString(LAST_LOCATION_PACKET, AppConstants.GSON.toJson(value)).commit();
    }

    public static long getNtpDifferentTime(Context context) {
        return getPreferences(context).getLong(NTP_DIFFERENT_TIME, 0);
    }

    public static void setNtpDifferentTime(Context context, long value) {
        getPreferences(context).edit().putLong(NTP_DIFFERENT_TIME, value).commit();
    }

    public static long getNtpSynchronizationTime(Context context) {
        return getPreferences(context).getLong(NTP_SYNCHRONIZATION_TIME, 0);
    }

    public static void setNtpSynchronizationTime(Context context, long value) {
        getPreferences(context).edit().putLong(NTP_SYNCHRONIZATION_TIME, value).commit();
    }

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void clear(Context context) {
        getPreferences(context).edit().clear().commit();
    }

}