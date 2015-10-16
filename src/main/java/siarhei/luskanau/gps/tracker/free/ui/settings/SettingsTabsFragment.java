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

package siarhei.luskanau.gps.tracker.free.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.ui.SimpleAppBarFragment;
import siarhei.luskanau.gps.tracker.free.ui.app.BaseDrawerActivity;

public class SettingsTabsFragment extends SimpleAppBarFragment {

    public static final String TAG = "SettingsTabsFragment";

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_settings_tabs, container, false);
    }

    @Override
    protected void updateAppBar() {
        super.updateAppBar();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.updateDrawerToggle(false);
        activity.getSupportActionBar().setSubtitle(R.string.menu_drawer_settings);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SettingsFragmentPagerAdapter pagerAdapter = new SettingsFragmentPagerAdapter(getChildFragmentManager(), getContext());
        ViewPager viewPager = (ViewPager) aq.id(R.id.settingsViewPager).getView();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());

        TabLayout tabLayout = (TabLayout) aq.id(R.id.settingsTabLayout).getView();
        tabLayout.setupWithViewPager(viewPager);
    }

    private static class SettingsFragmentPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[];
        private Fragment tabFragmentClasses[];

        public SettingsFragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
            super(fragmentManager);
            tabTitles = new String[]{
                    context.getString(R.string.fragment_settings_tabs_general),
                    context.getString(R.string.fragment_settings_tabs_internet_battery),
                    context.getString(R.string.fragment_settings_tabs_location)
            };
            tabFragmentClasses = new Fragment[]{
                    new GeneralSettingsFragment(),
                    new InternetSettingsFragment(),
                    PageFragment.newInstance(3)
            };
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragmentClasses[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    public static class PageFragment extends Fragment {
        public static final String ARG_PAGE = "ARG_PAGE";

        private int mPage;

        public static PageFragment newInstance(int page) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPage = getArguments().getInt(ARG_PAGE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            TextView textView = new TextView(getContext());
            textView.setText("Fragment #" + mPage);
            return textView;
        }

    }

}