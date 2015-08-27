/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Siarhei Luskanau
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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.androidquery.AQuery;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.ui.app.AppController;
import siarhei.luskanau.gps.tracker.free.ui.app.BaseDrawerActivity;

public abstract class SimpleAppBarFragment extends BaseAppBarFragment {

    protected AQuery aq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_app_bar_simple, container, false);
        aq = new AQuery(getActivity(), view);
        FrameLayout appBarFragmentContentFrameLayout = (FrameLayout) aq.id(R.id.appBarFragmentContentFrameLayout).getView();
        View contentView = onCreateContentView(inflater, appBarFragmentContentFrameLayout);
        if (contentView != null) {
            appBarFragmentContentFrameLayout.addView(contentView);
        }
        return view;
    }

    @Override
    protected void setupAppBar() {
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        Toolbar toolbar = (Toolbar) aq.id(R.id.appToolbar).getView();
        activity.setSupportActionBar(toolbar);
     }

    @Override
    protected void updateAppBar() {
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.updateDrawerToggle(true);
        activity.getSupportActionBar().setSubtitle(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppController.get(getActivity()).onShowHomeFragment();
                return true;
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    abstract protected View onCreateContentView(LayoutInflater inflater, ViewGroup container);

}