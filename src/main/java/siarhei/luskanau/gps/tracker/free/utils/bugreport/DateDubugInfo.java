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

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateDubugInfo {

    @SerializedName("formattedTime")
    public String formattedTime;

    @SerializedName("timezoneOffset")
    public int timezoneOffset;

    @SerializedName("millisecond")
    public long millisecond;

    public static DateDubugInfo createDateDubugInfo() {
        try {
            DateDubugInfo dateDubugInfo = new DateDubugInfo();
            Date date = new Date();
            dateDubugInfo.formattedTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.ENGLISH)
                    .format(date);
            dateDubugInfo.timezoneOffset = date.getTimezoneOffset();
            dateDubugInfo.millisecond = date.getTime();
            return dateDubugInfo;
        } catch (Throwable t) {
        }
        return null;
    }
}