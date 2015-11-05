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

package siarhei.luskanau.androiddatalib;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract class SimpleAppBarWithDrawerFragment extends BaseAppBarWithDrawerFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_bar_simple, container, false);
        FrameLayout appBarFragmentContentFrameLayout = (FrameLayout) view.findViewById(R.id.appBarFragmentContentFrameLayout);
        View contentView = onCreateContentView(inflater, appBarFragmentContentFrameLayout);
        if (contentView != null) {
            appBarFragmentContentFrameLayout.addView(contentView);
        }
        return view;
    }

    @Override
    public void onSetupAppBar() {
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.appToolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected CoordinatorLayout getCoordinatorLayout() {
        View view = getView();
        if (view != null) {
            return (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        }
        return null;
    }

    abstract protected View onCreateContentView(LayoutInflater inflater, ViewGroup container);

}