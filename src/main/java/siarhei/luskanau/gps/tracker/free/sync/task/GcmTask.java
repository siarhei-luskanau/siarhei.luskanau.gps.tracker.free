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

package siarhei.luskanau.gps.tracker.free.sync.task;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.concurrent.atomic.AtomicInteger;

import siarhei.luskanau.gps.tracker.free.gcm.GcmSettings;
import siarhei.luskanau.gps.tracker.free.sync.SyncService;

public class GcmTask extends BaseTask {

    private static final String TAG = "GcmTask";
    private static final String SENDER_ID = "437129776573";
    private static AtomicInteger atomicInteger = new AtomicInteger();

    public GcmTask(Context context) {
        super(context);
    }

    public static void sendEchoMessage(Context context) {
        try {
            if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) != ConnectionResult.SUCCESS) {
                return;
            }
            String registrationId = GcmSettings.getRegistrationId(context);
            if (registrationId != null) {
                GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(context);
                Bundle data = new Bundle();
                data.putString("my_message", "Hello World from siarhei");
                data.putString("my_action", "siarhei.luskanau.gps.tracker.free.ECHO_NOW");
                String id = Integer.toString(atomicInteger.incrementAndGet());
                googleCloudMessaging.send(SENDER_ID + "@gcm.googleapis.com", id, data);
                Log.d(TAG, "Message " + id + " is sent");
            } else {
                SyncService.ping(context);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString(), e);
        }
    }

    @Override
    public void doTask() throws Exception {
        // Check device for Play Services APK.
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) != ConnectionResult.SUCCESS) {
            return;
        }

        // Check registration
        String registrationId = GcmSettings.getRegistrationId(context);
        if (registrationId == null) {
            GcmSettings.setRegisteredOnServer(context, false);
            GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(context);
            registrationId = googleCloudMessaging.register(SENDER_ID);
            if (registrationId != null) {
                GcmSettings.setRegistrationId(context, registrationId);
                sendEchoMessage(context);
                Log.d(TAG, "registrationId: " + registrationId);
            }
        }

        if (registrationId != null && !GcmSettings.isRegisteredOnServer(context)) {
            // Send registrationId to server
            GcmSettings.setRegisteredOnServer(context, true);
        }
    }

}
