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

package siarhei.luskanau.gps.tracker.free.utils.bugreport;

import android.content.Context;

import com.google.gson.Gson;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final boolean isDebugMode = true;
    private static final Gson GSON = new Gson();
    private Thread.UncaughtExceptionHandler previousHandler;
    private Context context;

    private ExceptionHandler(Context context, Thread.UncaughtExceptionHandler previousHandler) {
        this.context = context;
        this.previousHandler = previousHandler;
    }

    static public void addExceptionHandler(Context context) {
        if (!isDebugMode) {
            return;
        }
        Thread.UncaughtExceptionHandler previousHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (previousHandler instanceof ExceptionHandler) {
            return;
        }
        ExceptionHandler exceptionHandler = new ExceptionHandler(context, previousHandler);
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
    }

    public static void handleException(Context context, Throwable throwable, String message) {
        try {
            StringBuilder builder = new StringBuilder();

            if (message != null) {
                if (builder.length() > 0) {
                    builder.append("\n\n");
                }
                builder.append(message);
            }

            if (throwable != null) {
                if (builder.length() > 0) {
                    builder.append("\n\n");
                }
                builder.append(ThrowableDubugInfo.createThrowableDubugInfo(throwable));
            }

            AllDebugInfo allDebugInfo = AllDebugInfo.createAllDebugInfo(context);
            if (allDebugInfo != null) {
                if (builder.length() > 0) {
                    builder.append("\n\n");
                }
                builder.append(GSON.toJson(allDebugInfo));
            }

            String[] to = new String[]{"siarhei.luskanau@gmail.com"};
            String[] cc = null;
            String subject = "Free-gps.net Tracker : Bug report";
            SendMailer.send(context, to, cc, subject, builder.toString(), null);
        } catch (Throwable t) {
            // empty
        }
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    handleException(context, throwable, null);
                    if (previousHandler != null) {
                        previousHandler.uncaughtException(thread, throwable);
                    }
                } catch (Exception e) {
                    // empty
                }
            }
        }).start();
    }

    public void handleMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    handleException(context, null, message);
                } catch (Exception e) {
                    // empty
                }
            }
        }).start();
    }

}
