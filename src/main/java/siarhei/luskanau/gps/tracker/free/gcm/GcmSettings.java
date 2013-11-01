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

package siarhei.luskanau.gps.tracker.free.gcm;

import android.content.Context;
import android.content.SharedPreferences;

import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class GcmSettings {

    private static final String SHARED_PREFERENCES_NAME = "GcmBackoffSettings";
    private static final String REGISTRATION_ID = "REGISTRATION_ID";
    private static final String REGISTERED_APP_VERSION = "REGISTERED_APP_VERSION";
    private static final String IS_REGISTERED_ON_SERVER = "IS_REGISTERED_ON_SERVER";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void clear(Context context) {
        getPreferences(context).edit().clear().commit();
    }

    public static String getRegistrationId(Context context) {
        String registrationId = getPreferences(context).getString(REGISTRATION_ID, null);
        if (registrationId == null || registrationId.trim().length() == 0) {
            return null;
        }
        int registeredAppVersion = getPreferences(context).getInt(REGISTERED_APP_VERSION, Integer.MIN_VALUE);
        if (registeredAppVersion != Utils.getVersionCode(context)) {
            return null;
        }
        return registrationId;
    }

    public static void setRegistrationId(Context context, String value) {
        getPreferences(context).edit().putString(REGISTRATION_ID, value).putInt(REGISTERED_APP_VERSION, Utils.getVersionCode(context)).commit();
    }

    public static Boolean isRegisteredOnServer(Context context) {
        return getPreferences(context).getBoolean(IS_REGISTERED_ON_SERVER, false);
    }

    public static void setRegisteredOnServer(Context context, Boolean value) {
        getPreferences(context).edit().putBoolean(IS_REGISTERED_ON_SERVER, value).commit();
    }

}
