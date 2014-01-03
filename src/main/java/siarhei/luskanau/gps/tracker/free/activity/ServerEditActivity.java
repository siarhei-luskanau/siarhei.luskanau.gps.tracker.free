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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.fragment.dialog.CheckServerDialogFragment;
import siarhei.luskanau.gps.tracker.free.settings.ServerEntity;

public class ServerEditActivity extends BaseActivity {

    private static final String SERVER_ENTITY = "SERVER_ENTITY";
    private ServerEntity serverEntity;

    public static void startServerEditActivity(Context context) {
        context.startActivity(new Intent(context, ServerEditActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_server);

        if (savedInstanceState != null && savedInstanceState.containsKey(SERVER_ENTITY)) {
            serverEntity = AppConstants.GSON.fromJson(savedInstanceState.getString(SERVER_ENTITY), ServerEntity.class);
        } else {
            serverEntity = new ServerEntity();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SERVER_ENTITY, AppConstants.GSON.toJson(serverEntity));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_server_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_accept: {
                finish();
                return true;
            }
            case R.id.menu_action_check: {
                if (getSupportFragmentManager().findFragmentByTag(CheckServerDialogFragment.TAG) == null) {
                    CheckServerDialogFragment.newInstance(serverEntity).show(getSupportFragmentManager(), CheckServerDialogFragment.TAG);
                }
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

}