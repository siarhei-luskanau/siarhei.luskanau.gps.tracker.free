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
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import siarhei.luskanau.gps.shared.LocationPacket;
import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.location.LocationService;

public class AppSettings {

    private static final String LAST_LOCATION_PACKET = "LAST_LOCATION_PACKET";
    private static final String NTP_DIFFERENT_TIME = "NTP_DIFFERENT_TIME";
    private static final String NTP_SYNCHRONIZATION_TIME = "NTP_SYNCHRONIZATION_TIME";
    private static final String APP_SETTINGS_ENTITY = "APP_SETTINGS_ENTITY";
    private static final String SERVER_ENTITY = "SERVER_ENTITY";
    private static final String PACKET_COUNTER = "PACKET_COUNTER";
    private static AppSettingsEntity cachedAppSettingsEntity;
    private static ServerEntity cachedServerEntity;

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

    public static AppSettingsEntity getAppSettingsEntity(Context context) {
        if (cachedAppSettingsEntity != null) {
            return cachedAppSettingsEntity;
        }
        cachedAppSettingsEntity = AppConstants.GSON.fromJson(getPreferences(context).getString(APP_SETTINGS_ENTITY, null), AppSettingsEntity.class);
        if (cachedAppSettingsEntity == null) {
            cachedAppSettingsEntity = new AppSettingsEntity();
            setAppSettingsEntity(context, cachedAppSettingsEntity);
        }
        return cachedAppSettingsEntity;
    }

    public static void setAppSettingsEntity(Context context, AppSettingsEntity value) {
        cachedAppSettingsEntity = value;
        getPreferences(context).edit().putString(APP_SETTINGS_ENTITY, AppConstants.GSON.toJson(cachedAppSettingsEntity)).commit();
    }

    public static int getPacketCounter(Context context) {
        return getPreferences(context).getInt(PACKET_COUNTER, 0);
    }

    public static void setPacketCounter(Context context, int value) {
        getPreferences(context).edit().putInt(PACKET_COUNTER, value).commit();
    }

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void clear(Context context) {
        getPreferences(context).edit().clear().commit();
    }

    @SuppressWarnings("deprecation")
    public static boolean isTrackerStarted(Context context) {
        return getAppSettingsEntity(context).isTrackerStarted;
    }

    @SuppressWarnings("deprecation")
    public static void setTrackerStarted(Context context, boolean value) {
        cachedAppSettingsEntity.isTrackerStarted = value;
        setAppSettingsEntity(context, cachedAppSettingsEntity);
        LocationService.updateGpsListener(context);
        context.sendBroadcast(new Intent(AppConstants.ACTION_TRACKER_STARTED_CHANGED));
    }

    public static ServerEntity getServerEntity(Context context) {
        if (cachedServerEntity == null) {
            cachedServerEntity = AppConstants.GSON.fromJson(getPreferences(context).getString(SERVER_ENTITY, null), ServerEntity.class);
        }

        return cachedServerEntity;
    }

    public static void setServerEntity(Context context, ServerEntity value) {
        cachedServerEntity = value;
        getPreferences(context).edit().putString(SERVER_ENTITY, AppConstants.GSON.toJson(value)).commit();
    }

}