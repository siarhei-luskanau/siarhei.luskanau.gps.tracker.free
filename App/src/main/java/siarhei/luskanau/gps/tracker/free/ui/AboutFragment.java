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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import siarhei.luskanau.androiddatalib.SimpleAppBarWithDrawerFragment;
import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class AboutFragment extends SimpleAppBarWithDrawerFragment {

    public static final String TAG = "AboutFragment";
    private static final String LINKEDIN_URL = "https://www.linkedin.com/pub/siarhei-luskanau/50/84b/23a";
    private static final String GITHUB_URL = "https://github.com/siarhei-luskanau/siarhei.luskanau.gps.tracker.free";
    private static final String COM_LINKEDIN_ANDROID = "com.linkedin.android";

    public static AboutFragment newInstance(int menuItemId) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        setMenuItemId(args, menuItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.menu_drawer_about);
        try {
            String message = new String(Utils.getBytes(getResources().openRawResource(R.raw.about)), "utf-8");
            TextView messageTextView = (TextView) getView().findViewById(R.id.aboutTextView);
            messageTextView.setText(Html.fromHtml(message));
            messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Throwable t) {
            Log.e(TAG, t.toString(), t);
        }

        getView().findViewById(R.id.emailTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"siarhei.luskanau@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Free GPS Tracker");
                getContext().startActivity(intent);
            }
        });

        getView().findViewById(R.id.linkedinTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage(COM_LINKEDIN_ANDROID);
                    intent.setData(Uri.parse("linkedin://profile/AAIAAArNr44BDpR6PxbNkmzBRgKrvXQX3ltAibc"));
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage(), e);
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(LINKEDIN_URL)));
                }
            }
        });

        getView().findViewById(R.id.githubTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(GITHUB_URL)));
            }
        });
    }

}