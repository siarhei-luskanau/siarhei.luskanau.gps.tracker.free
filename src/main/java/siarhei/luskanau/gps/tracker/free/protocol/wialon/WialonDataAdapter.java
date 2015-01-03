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

package siarhei.luskanau.gps.tracker.free.protocol.wialon;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import siarhei.luskanau.gps.tracker.free.model.LocationModel;
import siarhei.luskanau.gps.tracker.free.protocol.BaseLocationAdapter;

public class WialonDataAdapter implements BaseLocationAdapter<ShortDataWialonPacket> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyy", Locale.ENGLISH);
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HHmmss", Locale.ENGLISH);
    private static final DecimalFormat LATITUDE_FORMAT = new DecimalFormat("0000.0000");
    private static final DecimalFormat LONGITUDE_FORMAT = new DecimalFormat("00000.0000");

    @Override
    public ShortDataWialonPacket fromLocationModel(LocationModel locationModel) {
        ShortDataWialonPacket shortDataWialonPacket = new ShortDataWialonPacket();
        if (locationModel.time != null) {
            shortDataWialonPacket.date = DATE_FORMAT.format(locationModel.time);
            shortDataWialonPacket.time = TIME_FORMAT.format(locationModel.time);
        }
        if (locationModel.latitude != null) {
            if (locationModel.latitude >= 0) {
                shortDataWialonPacket.lat2 = "N";
            } else {
                shortDataWialonPacket.lat2 = "S";
            }
            shortDataWialonPacket.lat1 = LATITUDE_FORMAT.format(toGrad(locationModel.latitude)).replace(',', '.');
        }
        if (locationModel.longitude != null) {
            if (locationModel.longitude >= 0) {
                shortDataWialonPacket.lon2 = "E";
            } else {
                shortDataWialonPacket.lon2 = "W";
            }
            shortDataWialonPacket.lon1 = LONGITUDE_FORMAT.format(toGrad(locationModel.longitude)).replace(',', '.');
        }
        if (locationModel.hasSpeed) {
            shortDataWialonPacket.speed = String.valueOf(Math.round(locationModel.speed));
        }
        if (locationModel.hasBearing) {
            shortDataWialonPacket.course = String.valueOf(Math.round(locationModel.bearing));
        }
        if (locationModel.hasAltitude) {
            shortDataWialonPacket.height = String.valueOf(Math.round(locationModel.altitude));
        }
        if (locationModel.satellites != null) {
            shortDataWialonPacket.sats = String.valueOf(locationModel.satellites);
        }
        return shortDataWialonPacket;
    }

    private double toGrad(Double dec) {
        int part1 = dec.intValue();
        double part2 = dec - part1;
        part2 = (part2 / 100) * 60;
        return (part1 + part2) * 100;
    }

    @Override
    public LocationModel toLocationModel(ShortDataWialonPacket source) {
        return null;
    }

}