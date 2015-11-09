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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import siarhei.luskanau.androiddatalib.SimpleAppBarWithUpFragment;
import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.ui.dialog.CheckServerDialogFragment;

public class ServerEditFragment extends SimpleAppBarWithUpFragment {

    public static final String TAG = "ServerEditFragment";
    private static final String SERVER_ENTITY_ARG = "SERVER_ENTITY_ARG";
    private AQuery aq;
    private ServerEntity serverEntity;

    public static ServerEditFragment newInstance(ServerEntity serverEntity) {
        ServerEditFragment fragment = new ServerEditFragment();
        Bundle args = new Bundle();
        args.putString(SERVER_ENTITY_ARG, AppConstants.GSON.toJson(serverEntity));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_edit_server, container, false);
        aq = new AQuery(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.fragment_settings_general_server);

        if (savedInstanceState != null && savedInstanceState.containsKey(SERVER_ENTITY_ARG)) {
            serverEntity = AppConstants.GSON.fromJson(savedInstanceState.getString(SERVER_ENTITY_ARG), ServerEntity.class);
        } else if (getArguments() != null && getArguments().containsKey(SERVER_ENTITY_ARG)) {
            serverEntity = AppConstants.GSON.fromJson(getArguments().getString(SERVER_ENTITY_ARG), ServerEntity.class);
        }
        if (serverEntity == null) {
            serverEntity = new ServerEntity();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SERVER_ENTITY_ARG, AppConstants.GSON.toJson(serverEntity));
    }

    @Override
    public void onResume() {
        super.onResume();

        aq.id(R.id.customServerAddressEditText).text(serverEntity.server_address);
        if (serverEntity.server_port > 0) {
            aq.id(R.id.customServerPortEditText).text(String.valueOf(serverEntity.server_port));
        }
        if (serverEntity.serverType != null) {
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
        } else {
            aq.id(R.id.serverTypeSpinner).getSpinner().setSelection(0);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_server_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_accept: {
                onUpClicked();
                return true;
            }
            case R.id.menu_action_check: {
                if (getActivity().getSupportFragmentManager().findFragmentByTag(CheckServerDialogFragment.TAG) == null) {
                    CheckServerDialogFragment.newInstance(serverEntity).show(getActivity().getSupportFragmentManager(), CheckServerDialogFragment.TAG);
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