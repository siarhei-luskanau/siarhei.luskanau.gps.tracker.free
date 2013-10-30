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

package siarhei.luskanau.gps.tracker.free.location;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import java.util.HashMap;

import siarhei.luskanau.gps.shared.LocationPacket;
import siarhei.luskanau.gps.tracker.free.AppConstants;

public class LocationUtils {

    private static final IntentFilter BATTERY_INTENT_FILTER = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    private static final int BAD_VALUE_INT = -1;

    public static void setLocation(LocationPacket locationEntity, Location location) {
        if (location.hasAccuracy()) {
            locationEntity.hasAccuracy = true;
            locationEntity.accuracy = location.getAccuracy();
        }
        if (location.hasAltitude()) {
            locationEntity.hasAltitude = true;
            locationEntity.altitude = location.getAltitude();
        }
        if (location.hasBearing()) {
            locationEntity.hasBearing = true;
            locationEntity.bearing = location.getBearing();
        }
        Bundle locationExtras = location.getExtras();
        if (locationExtras != null && !locationExtras.isEmpty()) {
            locationEntity.extras = new HashMap<String, String>();
            for (String key : locationExtras.keySet()) {
                locationEntity.extras.put(key, String.valueOf(locationExtras.get(key)));
            }
            try {
                if (locationEntity.satellites == null || locationEntity.satellites == 0) {
                    if (locationEntity.extras.containsKey(AppConstants.SATELLITES)) {
                        locationEntity.satellites = Integer.valueOf(locationEntity.extras.get(AppConstants.SATELLITES));
                    }
                }
            } catch (Exception e) {
                locationEntity.satellites = null;
            }
        }
        if (locationEntity.satellites != null && locationEntity.satellites == 0) {
            locationEntity.satellites = null;
        }
        locationEntity.latitude = location.getLatitude();
        locationEntity.longitude = location.getLongitude();
        locationEntity.provider = location.getProvider();
        if (location.hasSpeed()) {
            locationEntity.hasSpeed = true;
            locationEntity.speed = location.getSpeed();
        }
    }

    public static void setBatteryInfo(LocationPacket locationEntity, Context context) {
        try {
            Bundle batteryExtras = context.getApplicationContext().registerReceiver(null, BATTERY_INTENT_FILTER).getExtras();
            int level = batteryExtras.getInt("level", -1);
            int scale = batteryExtras.getInt("scale", -1);
            if (level >= 0 && level >= 0) {
                locationEntity.batteryLevel = level;
                locationEntity.batteryScale = scale;
            }
        } catch (Exception e) {
            // nothing to do
        }
    }

    public static void setGsmInfo(LocationPacket locationEntity, Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            CellLocation cellLocation = telephonyManager.getCellLocation();
            if (cellLocation instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                String mccmnc = telephonyManager.getNetworkOperator();
                if (mccmnc != null && mccmnc.length() >= 4) {
                    locationEntity.mcc = mccmnc.substring(0, 3);
                    locationEntity.mnc = mccmnc.substring(3);
                }

                int cid = gsmCellLocation.getCid();
                if (cid != BAD_VALUE_INT) {
                    locationEntity.cid = cid & 0xffff;
                }
                int lac = gsmCellLocation.getLac();
                if (lac != BAD_VALUE_INT) {
                    locationEntity.lac = lac & 0xffff;
                }

                locationEntity.operatorName = telephonyManager.getNetworkOperatorName();
            }
        } catch (Exception e) {
            // nothing to do
        }
    }

}
