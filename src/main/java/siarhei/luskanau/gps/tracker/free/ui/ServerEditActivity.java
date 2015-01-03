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

package siarhei.luskanau.gps.tracker.free.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androidquery.AQuery;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.ui.dialog.CheckServerDialogFragment;
import siarhei.luskanau.gps.tracker.free.ui.progress.BaseProgressActivity;

public class ServerEditActivity extends BaseProgressActivity {

    private static final String SERVER_ENTITY = "SERVER_ENTITY";
    private AQuery aq = new AQuery(this);
    private ServerEntity serverEntity;

    public static void startServerEditActivity(Context context, ServerEntity serverEntity) {
        Intent intent = new Intent(context, ServerEditActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (serverEntity != null) {
            intent.putExtra(SERVER_ENTITY, AppConstants.GSON.toJson(serverEntity));
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_server);

        if (savedInstanceState != null && savedInstanceState.containsKey(SERVER_ENTITY)) {
            serverEntity = AppConstants.GSON.fromJson(savedInstanceState.getString(SERVER_ENTITY), ServerEntity.class);
        } else if (getIntent() != null && getIntent().hasExtra(SERVER_ENTITY)) {
            serverEntity = AppConstants.GSON.fromJson(getIntent().getStringExtra(SERVER_ENTITY), ServerEntity.class);
        }
        if (serverEntity == null) {
            serverEntity = new ServerEntity();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SERVER_ENTITY, AppConstants.GSON.toJson(serverEntity));
    }

    @Override
    protected void onResume() {
        super.onResume();

        aq.id(R.id.customServerAddressEditText).text(serverEntity.server_address);
        if (serverEntity.server_port > 0) {
            aq.id(R.id.customServerPortEditText).text(String.valueOf(serverEntity.server_port));
        }

        switch (serverEntity.serverType) {
            case socket: {
                aq.id(R.id.serverTypeSpinner).getSpinner().setSelection(0);
                break;
            }
            case json_form: {
                aq.id(R.id.serverTypeSpinner).getSpinner().setSelection(1);
                break;
            }
            case rest: {
                aq.id(R.id.serverTypeSpinner).getSpinner().setSelection(2);
                break;
            }
        }
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

    private ServerEntity createServer() {
        ServerEntity serverEntity = new ServerEntity();

        String serverType = aq.id(R.id.serverTypeSpinner).getSpinner().getSelectedItem().toString();
        if (serverType.equals(getString(R.string.server_type_socket))) {
            serverEntity.serverType = ServerEntity.ServerType.socket;
        } else if (serverType.equals(getString(R.string.server_type_json_body))) {
            serverEntity.serverType = ServerEntity.ServerType.rest;
        } else if (serverType.equals(getString(R.string.server_type_json_form))) {
            serverEntity.serverType = ServerEntity.ServerType.json_form;
        }

        serverEntity.name = aq.id(R.id.customServerNameEditText).getText().toString();
        serverEntity.server_address = aq.id(R.id.customServerAddressEditText).getText().toString();
        try {
            serverEntity.server_port = Integer.parseInt(aq.id(R.id.customServerPortEditText).getText()
                    .toString());
        } catch (Exception e) {
            serverEntity.server_port = 0;
        }
        return serverEntity;
    }

}