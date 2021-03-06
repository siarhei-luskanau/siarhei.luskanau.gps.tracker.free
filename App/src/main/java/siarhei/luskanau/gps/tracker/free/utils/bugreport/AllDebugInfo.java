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

package siarhei.luskanau.gps.tracker.free.utils.bugreport;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class AllDebugInfo {

    @SerializedName("package")
    public PackageDebugInfo packageDebugInfo;

    @SerializedName("date")
    public DateDubugInfo dateDubugInfo;

    @SerializedName("build")
    public Map<String, Object> buildDebugInfo;

    public static AllDebugInfo createAllDebugInfo(Context context) {
        AllDebugInfo allDebugInfo = new AllDebugInfo();
        allDebugInfo.packageDebugInfo = PackageDebugInfo.createPackageDebugInfo(context);
        allDebugInfo.dateDubugInfo = DateDubugInfo.createDateDubugInfo();
        allDebugInfo.buildDebugInfo = BuildDebugInfo.createBuildDebugInfo();
        return allDebugInfo;
    }
}
