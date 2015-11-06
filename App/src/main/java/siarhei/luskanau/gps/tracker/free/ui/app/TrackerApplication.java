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

package siarhei.luskanau.gps.tracker.free.ui.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

import com.squareup.leakcanary.LeakCanary;

import java.util.Locale;

import siarhei.luskanau.androidbroadcastlib.ProgressBroadcastController;
import siarhei.luskanau.gps.tracker.free.dao.BaseDAO;
import siarhei.luskanau.gps.tracker.free.database.LocationColumns;
import siarhei.luskanau.gps.tracker.free.service.TrackerService;
import siarhei.luskanau.gps.tracker.free.service.sync.SyncService;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.utils.bugreport.ExceptionHandler;

public class TrackerApplication extends Application {

    private ProgressBroadcastController.ProgressBroadcastReceiver progressBroadcastReceiver = new ProgressBroadcastController().createBroadcastReceiver(new InnerProgressBroadcastCallback());

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        ExceptionHandler.addExceptionHandler(this);
        updateLocale();

        progressBroadcastReceiver.registerReceiver(this);

        // Database will be created
        BaseDAO.queryCount(this, LocationColumns.TABLE_NAME);

        TrackerService.pingAntiKiller(this);
        SyncService.sendPositions(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        progressBroadcastReceiver.unregisterReceiver(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateLocale();
    }

    private void updateLocale() {
        Locale locale = null;
        AppSettings.State state = AppSettings.getAppSettingsEntity(this);
        if (state.language == null) {
            state.language = AppSettings.Language.EN;
            AppSettings.setAppSettingsEntity(this, state);
        }
        switch (state.language) {
            case EN: {
                locale = Locale.ENGLISH;
                break;
            }
            case RU: {
                locale = new Locale("ru");
                break;
            }
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }

    private class InnerProgressBroadcastCallback extends ProgressBroadcastController.ProgressBroadcastCallback {
        @Override
        public void onShowToast(Context context, CharSequence message) {
            Toast.makeText(TrackerApplication.this, message, Toast.LENGTH_LONG).show();
        }
    }

}
