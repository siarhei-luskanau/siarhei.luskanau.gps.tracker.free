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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Iterator;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.utils.PhoneStateUtils;
import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class LocationService extends Service {

    private static final String ACTION_UPDATE_GPS_LISTENER = "ACTION_UPDATE_GPS_LISTENER";
    private static final String ACTION_SAVE_INVALID_LOCATION = "ACTION_SAVE_INVALID_LOCATION";
    private LocationManager locationManager;
    private InnerLocationListener locationListener;
    private LocationsController gpsLocationsController;
    private LocationsController networkLocationsController;
    private long ntpDifferentTime;
    private String deviceId;

    public static void updateGpsListener(Context context) {
        context.startService(new Intent(context, LocationService.class).setAction(ACTION_UPDATE_GPS_LISTENER));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (ACTION_SAVE_INVALID_LOCATION.equals(intent.getAction())) {
                if (AppSettings.getAppSettingsEntity(this).isTrackerStarted && locationListener != null) {
                    locationListener.onLocationChanged(null);
                }
            } else if (ACTION_UPDATE_GPS_LISTENER.equals(intent.getAction())) {
                stopListenLocation();
                if (AppSettings.getAppSettingsEntity(this).isTrackerStarted) {
                    startListenLocation();
                } else {
                    stopSelf();
                }
            }
        } else {
            if (AppSettings.getAppSettingsEntity(this).isTrackerStarted) {
                startListenLocation();
            } else {
                stopListenLocation();
                stopSelf();
            }
        }
        return START_STICKY;
    }

    private void startListenLocation() {
        if (locationListener == null) {
            deviceId = Utils.getDeviceId(this);
            ntpDifferentTime = AppSettings.getNtpDifferentTime(this);
            locationListener = new InnerLocationListener();
            PhoneStateUtils.startListen(this);

            AppSettings.State appSettingsState = AppSettings.getAppSettingsEntity(this);
            gpsLocationsController = new LocationsController(deviceId, appSettingsState.locationSettings.timeFilter, appSettingsState.locationSettings.filterGpsLocations);
            if (appSettingsState.locationSettings.filterGpsLocations != AppSettings.FilterGpsLocations.DONT_USE) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                } catch (Exception e) {
                    Log.e(getPackageName(), e.getMessage(), e);
                }
            }
            networkLocationsController = new LocationsController(deviceId, appSettingsState.locationSettings.timeFilter, appSettingsState.locationSettings.filterNetworkLocations);

            if (appSettingsState.locationSettings.filterNetworkLocations != AppSettings.FilterNetworkLocations.DONT_USE) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                } catch (Exception e) {
                    Log.e(getPackageName(), e.getMessage(), e);
                }
            }

            if (appSettingsState.locationSettings.isUseGsmCellInfo
                    && appSettingsState.locationSettings.filterGpsLocations != AppSettings.FilterGpsLocations.DONT_USE
                    && appSettingsState.locationSettings.filterNetworkLocations != AppSettings.FilterNetworkLocations.DONT_USE) {
                startSaveInvalidPing(this, appSettingsState.locationSettings.timeFilter);
            }
        }
    }

    private void stopListenLocation() {
        if (locationListener != null) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.removeUpdates(locationListener);
            locationListener = null;
            cancelSaveInvalidPing(this);
            PhoneStateUtils.stopListen(this);
            stopForeground(true);
        }
    }

    private void startSaveInvalidPing(Context context, long pingInterval) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pingInterval, getSaveInvalidPendingIntent(context));
    }

    private void cancelSaveInvalidPing(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getSaveInvalidPendingIntent(context));
    }

    private PendingIntent getSaveInvalidPendingIntent(Context context) {
        return PendingIntent.getService(context, 0, new Intent(context, LocationService.class).setAction(ACTION_SAVE_INVALID_LOCATION), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private class InnerLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location == null || location.getProvider() == null) {
                LocationsController.saveLocation(LocationService.this, deviceId, location, System.currentTimeMillis() - ntpDifferentTime);
            } else if (LocationManager.GPS_PROVIDER.equals(location.getProvider())) {
                updateSatellites(location);
                gpsLocationsController.onLocationChanged(LocationService.this, location, System.currentTimeMillis() - ntpDifferentTime);
            } else if (LocationManager.NETWORK_PROVIDER.equals(location.getProvider())) {
                networkLocationsController.onLocationChanged(LocationService.this, location, System.currentTimeMillis() - ntpDifferentTime);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        private void updateSatellites(Location location) {
            if (location.getExtras() != null && location.getExtras().containsKey(AppConstants.SATELLITES)) {
                return;
            }
            try {
                GpsStatus localGpsStatus = locationManager.getGpsStatus(null);
                int satellites = 0;
                Iterator<GpsSatellite> iterator = localGpsStatus.getSatellites().iterator();
                for (; iterator.hasNext(); ) {
                    if (iterator.next().usedInFix()) {
                        satellites++;
                    }
                }
                if (satellites > 0) {
                    location.getExtras().putInt(AppConstants.SATELLITES, satellites);
                }
            } catch (Exception e) {
                Log.w(getPackageName(), e);
            }
        }
    }

}
