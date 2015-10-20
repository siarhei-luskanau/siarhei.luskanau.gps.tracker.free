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

package siarhei.luskanau.gps.tracker.free.utils;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

public abstract class PhoneStateUtils {

    public static int signalStrength;

    private static PhoneStateListener phoneStateListener = new InnerPhoneStateListener();

    public static void startListen(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public static void stopListen(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        } catch (Exception e) {
            Log.e(context.getPackageName(), "PhoneStateManager.stopListen", e);
        }
    }

    private static class InnerPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.isGsm()) {
                int asu = signalStrength.getGsmSignalStrength();
                if (asu == 0 || asu == 99) {
                    PhoneStateUtils.signalStrength = 0;
                } else if (asu < 5) {
                    PhoneStateUtils.signalStrength = 2;
                } else if (asu >= 5 && asu < 8) {
                    PhoneStateUtils.signalStrength = 3;
                } else if (asu >= 8 && asu < 12) {
                    PhoneStateUtils.signalStrength = 4;
                } else if (asu >= 12 && asu < 14) {
                    PhoneStateUtils.signalStrength = 5;
                } else if (asu >= 14) {
                    PhoneStateUtils.signalStrength = 6;
                }
            } else {
                int cdmaDbm = signalStrength.getCdmaDbm();
                if (cdmaDbm >= -89) {
                    PhoneStateUtils.signalStrength = 6;
                } else if (cdmaDbm >= -97) {
                    PhoneStateUtils.signalStrength = 5;
                } else if (cdmaDbm >= -103) {
                    PhoneStateUtils.signalStrength = 4;
                } else if (cdmaDbm >= -107) {
                    PhoneStateUtils.signalStrength = 3;
                } else if (cdmaDbm >= -109) {
                    PhoneStateUtils.signalStrength = 2;
                } else {
                    PhoneStateUtils.signalStrength = 0;
                }
            }
        }
    }

}
