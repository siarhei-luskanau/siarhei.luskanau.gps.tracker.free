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

package siarhei.luskanau.gps.tracker.free.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

public class LocationModel {

    @SerializedName("rowId")
    public Long rowId;
    @SerializedName("deviceId")
    public String deviceId;
    @SerializedName("hasAccuracy")
    public boolean hasAccuracy;
    @SerializedName("accuracy")
    public float accuracy;
    @SerializedName("hasAltitude")
    public boolean hasAltitude;
    @SerializedName("altitude")
    public double altitude;
    @SerializedName("hasBearing")
    public boolean hasBearing;
    @SerializedName("bearing")
    public float bearing;
    @SerializedName("extras")
    public Map<String, String> extras;
    @SerializedName("satellites")
    public Integer satellites;
    @SerializedName("latitude")
    public Double latitude;
    @SerializedName("longitude")
    public Double longitude;
    @SerializedName("provider")
    public String provider;
    @SerializedName("hasSpeed")
    public boolean hasSpeed;
    @SerializedName("speed")
    public float speed;
    @SerializedName("time")
    public Date time;
    @SerializedName("timeText")
    public String timeText;
    @SerializedName("batteryLevel")
    public Integer batteryLevel;
    @SerializedName("batteryScale")
    public Integer batteryScale;
    @SerializedName("signalStrength")
    public Integer signalStrength;
    @SerializedName("mcc")
    public String mcc;
    @SerializedName("mnc")
    public String mnc;
    @SerializedName("lac")
    public Integer lac;
    @SerializedName("cid")
    public Integer cid;
    @SerializedName("operatorName")
    public String operatorName;

}
