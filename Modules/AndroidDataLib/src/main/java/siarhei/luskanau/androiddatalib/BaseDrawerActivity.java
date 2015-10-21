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

package siarhei.luskanau.androiddatalib;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.FrameLayout;

public abstract class BaseDrawerActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.baseDrawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        setupNavigationView(navigationView);
    }

    public void closeDrawers() {
        drawerLayout.closeDrawers();
    }

    public void openDrawer() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    public boolean isDrawerOpen() {
        if (drawerLayout != null && navigationView != null) {
            return drawerLayout.isDrawerOpen(navigationView);
        }
        return false;
    }

    public void setCheckedNavigationMenuItem(int menuItemId) {
        if (navigationView != null) {
            navigationView.setCheckedItem(menuItemId);
        }
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void onShowDrawerFragment(BaseDrawerFragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentManager.beginTransaction().replace(R.id.appContentFrameLayout, fragment, tag).commit();
        }
    }

    public FrameLayout getAppContentFrameLayout() {
        return (FrameLayout) findViewById(R.id.appContentFrameLayout);
    }

    protected abstract void setupNavigationView(NavigationView navigationView);

}