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

package siarhei.luskanau.gps.tracker.free.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.AQuery;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.fragment.AboutFragment;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.settings.ServerEntity;
import siarhei.luskanau.gps.tracker.free.sync.task.GcmTask;
import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class TrackerActivity extends BaseActivity {

    private static final String TAG = "TrackerActivity";
    private AQuery aq = new AQuery(this);

    public static void startTrackerActivity(Context context) {
        context.startActivity(new Intent(context, TrackerActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        aq.id(R.id.imeiTextView).text(getString(R.string.fragment_tracker_imei, Utils.getDeviceId(this)));

        aq.id(R.id.editServerImageButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServersActivity.startServersActivity(TrackerActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ServerEntity serverEntity = AppSettings.getServerEntity(this);
        if (serverEntity != null) {
            aq.id(R.id.aboutServerImageButton).visible();
            aq.id(R.id.serverNameImageButton).text(getString(R.string.fragment_tracker_server, serverEntity.name));
        } else {
            aq.id(R.id.aboutServerImageButton).gone();
            aq.id(R.id.serverNameImageButton).text(getString(R.string.fragment_tracker_server, "---"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tracker, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_settings: {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new GcmTask(TrackerActivity.this).doTask();
                            GcmTask.sendEchoMessage(TrackerActivity.this);
                        } catch (Exception e) {
                            Log.d(TAG, e.toString(), e);
                        }

                    }
                }).start();
                return true;
            }
            case R.id.menu_action_about: {
                if (getSupportFragmentManager().findFragmentByTag(AboutFragment.TAG) == null) {
                    new AboutFragment().show(getSupportFragmentManager(), AboutFragment.TAG);
                }
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

}
