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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class BaseDrawerFragment extends Fragment {

    private static final String NAVIGATION_MENU_ITEM_ID_ARG = "NAVIGATION_MENU_ITEM_ID_ARG";

    public static void setMenuItemId(Bundle args, int menuItemId) {
        args.putInt(NAVIGATION_MENU_ITEM_ID_ARG, menuItemId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(getClass().getSimpleName(), "BaseDrawerFragment.onActivityCreated");
        setCheckedNavigationMenuItem();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getClass().getSimpleName(), "BaseDrawerFragment.onResume");
        setCheckedNavigationMenuItem();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(getClass().getSimpleName(), "BaseDrawerFragment.onAttach");
        setCheckedNavigationMenuItem();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(getClass().getSimpleName(), "BaseDrawerFragment.onDetach");
    }

    protected BaseDrawerActivity getBaseDrawerActivity() {
        return (BaseDrawerActivity) getActivity();
    }

    protected void setCheckedNavigationMenuItem() {
        getBaseDrawerActivity().setCheckedNavigationMenuItem(getNavigationMenuId());
    }

    protected int getNavigationMenuId() {
        if (getArguments() != null && getArguments().containsKey(NAVIGATION_MENU_ITEM_ID_ARG)) {
            return getArguments().getInt(NAVIGATION_MENU_ITEM_ID_ARG);
        }
        return 0;
    }

}