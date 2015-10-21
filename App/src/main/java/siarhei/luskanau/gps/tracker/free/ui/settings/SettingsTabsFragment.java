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
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import siarhei.luskanau.androiddatalib.SimpleAppBarWithDrawerFragment;
import siarhei.luskanau.gps.tracker.free.R;

public class SettingsTabsFragment extends SimpleAppBarWithDrawerFragment {

    public static final String TAG = "SettingsTabsFragment";

    public static SettingsTabsFragment newInstance(int menuItemId) {
        SettingsTabsFragment fragment = new SettingsTabsFragment();
        Bundle args = new Bundle();
        setMenuItemId(args, menuItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_settings_tabs, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.menu_drawer_settings);

        SettingsFragmentPagerAdapter pagerAdapter = new SettingsFragmentPagerAdapter(getChildFragmentManager(), getContext());
        ViewPager viewPager = (ViewPager) getView().findViewById(R.id.settingsViewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());

        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.settingsTabLayout);
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
                    new LocationSettingsFragment()
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

}