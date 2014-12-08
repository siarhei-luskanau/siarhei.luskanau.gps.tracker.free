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

package siarhei.luskanau.gps.tracker.free.service.location;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.util.Date;
import java.util.TimeZone;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.broadcast.AppBroadcastController;
import siarhei.luskanau.gps.tracker.free.dao.LocationDAO;
import siarhei.luskanau.gps.tracker.free.service.sync.SyncService;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.shared.LocationPacket;
import siarhei.luskanau.gps.tracker.free.utils.PhoneStateUtils;

public class LocationsController {

    private String deviceId;
    private long timeFilter;
    private long distanceFilter;
    private Location previousLocation;

    public LocationsController(String deviceId, long timeFilter, long distanceFilter) {
        this.deviceId = deviceId;
        this.timeFilter = timeFilter;
        this.distanceFilter = distanceFilter;
    }

    public static void saveLocation(Context context, String deviceId, Location location, long timeMillis) {
        try {
            LocationPacket locationEntity = new LocationPacket();
            locationEntity.deviceId = deviceId;
            locationEntity.time = new Date(timeMillis);
            AppConstants.DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
            locationEntity.timeText = AppConstants.DATE_FORMAT.format(locationEntity.time);
            if (location != null) {
                LocationUtils.setLocation(locationEntity, location);
            }
            LocationUtils.setBatteryInfo(locationEntity, context);
            locationEntity.signalStrength = PhoneStateUtils.signalStrength;
            if (AppSettings.getAppSettingsEntity(context).locationSettings.isUseGsmCellInfo) {
                LocationUtils.setGsmInfo(locationEntity, context);
            }

            LocationDAO.insertOrUpdateLocationPacket(context, locationEntity);
            AppBroadcastController.sendLastPositionIsUpdatedBroadcast(context);
            SyncService.sendPositions(context);
        } catch (Exception e) {
            Log.e(context.getPackageName(), "LocationReceiver.saveLocation", e);
        }
    }

    public void onLocationChanged(Context context, Location location, long timeMillis) {
        location.setTime(timeMillis);
        boolean isNeedSave = true;

        if (timeFilter > 0 || distanceFilter > 0) {
            if (previousLocation != null) {
                isNeedSave = false;
                if (timeFilter > 0) {
                    if (location.getTime() - previousLocation.getTime() >= timeFilter) {
                        isNeedSave = true;
                    }
                }
                if (distanceFilter > 0) {
                    if (location.distanceTo(previousLocation) >= distanceFilter) {
                        isNeedSave = true;
                    }
                }
            } else {
                isNeedSave = true;
            }
        }

        if (isNeedSave) {
            saveLocation(context, deviceId, location, timeMillis);
            previousLocation = location;
        }
    }

}
