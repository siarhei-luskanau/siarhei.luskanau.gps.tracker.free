/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Siarhei Luskanau
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.utils.CryptUtils;

public class AboutFragment extends DialogFragment {

    public static final String TAG = "AboutFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.menu_action_about);
        builder.setIcon(R.drawable.ic_action_about);
        try {
            String message = new String(CryptUtils.getAssets(getActivity(), "about-" + Locale.getDefault().getLanguage()), "utf-8");
            builder.setMessage(Html.fromHtml(message));
        } catch (Throwable t1) {
            try {
                String message = new String(CryptUtils.getAssets(getActivity(), "about-en"), "utf-8");
                builder.setMessage(Html.fromHtml(message));
            } catch (Throwable t2) {
                builder.setMessage(null);
            }
        }

        builder.setNeutralButton(android.R.string.ok, null);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            TextView messageTextView = (TextView) getDialog().findViewById(android.R.id.message);
            if (messageTextView != null) {
                messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        } catch (Throwable t) {
            Log.e(TAG, t.toString(), t);
        }
    }

}
