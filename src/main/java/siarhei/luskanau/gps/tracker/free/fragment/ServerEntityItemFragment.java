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

package siarhei.luskanau.gps.tracker.free.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.activity.ServersActivity;
import siarhei.luskanau.gps.tracker.free.entity.ServerEntity;
import siarhei.luskanau.gps.tracker.free.fragment.dialog.ConfirmServerDialogFragment;

public class ServerEntityItemFragment extends Fragment {

    public static final String TAG = "ServerEntityItemFragment";
    private static final String POSITION_ARG = "POSITION_ARG";
    private AQuery aq;

    public static ServerEntityItemFragment newInstance(int position) {
        ServerEntityItemFragment fragment = new ServerEntityItemFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_ARG, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server_item, container, false);
        aq = new AQuery(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        aq.id(R.id.serverItemLinearLayout).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments().containsKey(POSITION_ARG)) {
                    int position = getArguments().getInt(POSITION_ARG);
                    ServersActivity serversActivity = (ServersActivity) getActivity();
                    ServerEntity serverEntity = serversActivity.getServerEntity(position);
                    if (getFragmentManager().findFragmentByTag(ConfirmServerDialogFragment.TAG) == null) {
                        ConfirmServerDialogFragment.newInstance(serverEntity).show(getFragmentManager(), ConfirmServerDialogFragment.TAG);
                    }
                }
            }
        });

        if (getArguments().containsKey(POSITION_ARG)) {
            int position = getArguments().getInt(POSITION_ARG);
            ServersActivity serversActivity = (ServersActivity) getActivity();
            ServerEntity serverEntity = serversActivity.getServerEntity(position);
            if (serverEntity != null) {
                updateServerEntity(serverEntity);
            }
        }
    }

    public void updateServerEntity(ServerEntity serverEntity) {
        aq.id(R.id.serverNameTextView).text(serverEntity.name);
        aq.id(R.id.serverSiteUrlTextView).text(serverEntity.site_url);
        aq.id(R.id.serverTypeTextView).text(serverEntity.server_type);
        aq.id(R.id.serverAddressTextView).text(serverEntity.server_address);
        aq.id(R.id.serverPortTextView).text(String.valueOf(serverEntity.server_port));
        if (serverEntity.custom) {
            aq.id(R.id.editServerImageButton).visible();
        } else {
            aq.id(R.id.editServerImageButton).gone();
        }
    }

}
