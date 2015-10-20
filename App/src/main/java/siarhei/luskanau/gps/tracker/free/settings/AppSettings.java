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

package siarhei.luskanau.gps.tracker.free.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.annotations.SerializedName;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.model.LocationModel;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;

public class AppSettings {

    private static final String LAST_LOCATION_PACKET = "LAST_LOCATION_PACKET";
    private static final String NTP_DIFFERENT_TIME = "NTP_DIFFERENT_TIME";
    private static final String NTP_SYNCHRONIZATION_TIME = "NTP_SYNCHRONIZATION_TIME";
    private static final String APP_SETTINGS_ENTITY = "APP_SETTINGS_ENTITY";
    private static final String SERVER_ENTITY = "SERVER_ENTITY";
    private static final String PACKET_COUNTER = "PACKET_COUNTER";
    private static State cachedAppSettingsState;
    private static ServerEntity cachedServerEntity;

    public static LocationModel getLastLocationPacket(Context context) {
        return AppConstants.GSON.fromJson(getPreferences(context).getString(LAST_LOCATION_PACKET, null), LocationModel.class);
    }

    public static void setLastLocationPacket(Context context, LocationModel value) {
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

    public static State getAppSettingsEntity(Context context) {
        if (cachedAppSettingsState != null) {
            return cachedAppSettingsState;
        }
        cachedAppSettingsState = AppConstants.GSON.fromJson(getPreferences(context).getString(APP_SETTINGS_ENTITY, null), State.class);
        if (cachedAppSettingsState == null) {
            cachedAppSettingsState = new State();
            setAppSettingsEntity(context, cachedAppSettingsState);
        }
        return cachedAppSettingsState;
    }

    public static void setAppSettingsEntity(Context context, State value) {
        cachedAppSettingsState = value;
        getPreferences(context).edit().putString(APP_SETTINGS_ENTITY, AppConstants.GSON.toJson(cachedAppSettingsState)).commit();
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

    public static void setIsTrackerStarted(Context context, boolean value) {
        cachedAppSettingsState.isTrackerStarted = value;
        setAppSettingsEntity(context, cachedAppSettingsState);
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

    public enum InternetType {
        ANY_TYPE,
        WIFI_TYPE,
        OFFLINE_TYPE
    }

    public enum Language {
        EN,
        RU
    }

    public static class SendToServerInterval {
        public static final int IMMEDIATELY = 0;
        public static final int MINUTES_1 = 60000;
        public static final int MINUTES_10 = 600000;
        public static final int MINUTES_60 = 3600000;
        public static final int MANUAL = -1;
    }

    public static class FilterTimeInterval {
        public static final int IMMEDIATELY = 0;
        public static final int MINUTES_1 = 60000;
        public static final int MINUTES_10 = 600000;
        public static final int MINUTES_60 = 3600000;
    }

    public static class FilterGpsLocations {
        public static final int DONT_USE = -1;
        public static final int USE = 0;
        public static final int FILTER_10_M = 10;
        public static final int FILTER_100_M = 100;
        public static final int FILTER_1000_M = 1000;
    }

    public static class FilterNetworkLocations {
        public static final int DONT_USE = -1;
        public static final int USE = 0;
        public static final int FILTER_100_M = 100;
        public static final int FILTER_500_M = 500;
        public static final int FILTER_5000_M = 5000;
    }

    public static class State {
        @SerializedName("EulaAccepted")
        public boolean isEulaAccepted = false;

        @SerializedName("isTrackerStarted")
        public boolean isTrackerStarted = false;

        @SerializedName("autoStart")
        public boolean autoStart = false;

        @SerializedName("isShowNotification")
        public boolean isShowNotification = true;

        @SerializedName("internetSettingsEntity")
        public InternetSettingsEntity internetSettingsEntity = new InternetSettingsEntity();

        @SerializedName("batterySettings")
        public BatterySettingsEntity batterySettings = new BatterySettingsEntity();

        @SerializedName("locationSettings")
        public LocationSettingsEntity locationSettings = new LocationSettingsEntity();

        @SerializedName("language")
        public Language language;
    }

    public static class BatterySettingsEntity {
        @SerializedName("stopIfBatteryLow")
        public boolean stopIfBatteryLow = true;

        @SerializedName("startIfBatteryOk")
        public boolean startIfBatteryOk = true;

        @SerializedName("stopIfChargerDisconnected")
        public boolean stopIfChargerDisconnected = false;

        @SerializedName("startIfChargerConnected")
        public boolean startIfChargerConnected = false;
    }

    public static class InternetSettingsEntity {
        @SerializedName("internetType")
        public InternetType internetType = InternetType.ANY_TYPE;
        @SerializedName("sendToServerInterval")
        public int sendToServerInterval = SendToServerInterval.IMMEDIATELY;
    }

    public static class LocationSettingsEntity {
        @SerializedName("isUseGsmCellInfo")
        public boolean isUseGsmCellInfo = false;

        @SerializedName("timeFilter")
        public int timeFilter = FilterTimeInterval.MINUTES_1;

        @SerializedName("filterGpsLocations")
        public int filterGpsLocations = FilterGpsLocations.DONT_USE;

        @SerializedName("filterNetworkLocations")
        public int filterNetworkLocations = FilterNetworkLocations.FILTER_100_M;
    }

}