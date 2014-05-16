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

package siarhei.luskanau.gps.tracker.free.shared;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Tr151ModLocationPacketAdapter implements BaseLocationPacketAdapter<Tr151ModPacket> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyy", Locale.ENGLISH);
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HHmmss", Locale.ENGLISH);
    private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("ddMMyyHHmmss", Locale.ENGLISH);
    private static final DecimalFormat LONGITUDE_FORMAT = new DecimalFormat("00000.0000");
    private static final DecimalFormat LATITUDE_FORMAT = new DecimalFormat("0000.0000");
    private static final DecimalFormat ALTITUDE_FORMAT = new DecimalFormat("0.00");
    private static final DecimalFormat SPEED_FORMAT = new DecimalFormat("0.00");
    private static final DecimalFormat BEARING_FORMAT = new DecimalFormat("0");

    public Tr151ModLocationPacketAdapter() {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    public Tr151ModPacket fromLocationPacket(LocationPacket locationPacket) {
        if (locationPacket != null) {
            Tr151ModPacket tr151ModPacket = new Tr151ModPacket();
            tr151ModPacket.imei = locationPacket.deviceId;
            if (locationPacket.time != null) {
                tr151ModPacket.date = DATE_FORMAT.format(locationPacket.time);
                tr151ModPacket.time = TIME_FORMAT.format(locationPacket.time);
            }
            tr151ModPacket.longitude = longitudeToString(locationPacket.longitude);
            tr151ModPacket.latitude = latitudeToString(locationPacket.latitude);

            if (locationPacket.hasAltitude) {
                tr151ModPacket.altitude = locationPacket.altitude;
            }

            if (locationPacket.hasSpeed) {
                tr151ModPacket.speed = (double) locationPacket.speed;
            }

            if (locationPacket.hasBearing) {
                tr151ModPacket.heading = (int) locationPacket.bearing;
            }

            tr151ModPacket.satellites = locationPacket.satellites;
            tr151ModPacket.signalStrength = locationPacket.signalStrength;
            tr151ModPacket.battery = getBattery(locationPacket);
            tr151ModPacket.mcc = locationPacket.mcc;
            tr151ModPacket.mnc = locationPacket.mnc;
            tr151ModPacket.lac = locationPacket.lac;
            tr151ModPacket.cid = locationPacket.cid;
            return tr151ModPacket;
        }
        return null;
    }

    @Override
    public LocationPacket toLocationPacket(Tr151ModPacket source) {
        if (source != null) {
            LocationPacket locationPacket = new LocationPacket();
            locationPacket.deviceId = source.imei;

            try {
                if (source.date != null && source.time != null) {
                    locationPacket.time = DATETIME_FORMAT.parse(source.date + source.time);
                }
            } catch (ParseException e) {
                locationPacket.time = null;
                e.printStackTrace();
            }

            locationPacket.longitude = stringToLongitude(source.longitude);
            locationPacket.latitude = stringToLatitude(source.latitude);

            locationPacket.hasAltitude = source.altitude != null;
            if (locationPacket.hasAltitude) {
                locationPacket.altitude = source.altitude;
            }

            locationPacket.hasSpeed = source.speed != null;
            if (locationPacket.hasSpeed) {
                locationPacket.speed = source.speed.floatValue();
            }

            locationPacket.hasBearing = source.heading != null;
            if (locationPacket.hasBearing) {
                locationPacket.bearing = source.heading;
            }

            locationPacket.satellites = source.satellites;
            locationPacket.signalStrength = source.signalStrength;

            if (source.battery != null) {
                locationPacket.batteryScale = 100;
                locationPacket.batteryLevel = (source.battery * locationPacket.batteryScale) / 7;
            }

            locationPacket.mcc = source.mcc;
            locationPacket.mnc = source.mnc;
            locationPacket.lac = source.lac;
            locationPacket.cid = source.cid;
            return locationPacket;
        }
        return null;
    }

    private String longitudeToString(Double longitude) {
        if (longitude == null) {
            return null;
        }
        String ewString;
        if (longitude >= 0) {
            ewString = "E";
        } else {
            ewString = "W";
            longitude = Math.abs(longitude);
        }
        int longitude1 = longitude.intValue();
        double longitude2 = longitude - longitude1;
        longitude2 = (longitude2 / 100) * 60;
        longitude = (longitude1 + longitude2) * 100;
        return ewString + LONGITUDE_FORMAT.format(longitude).replace(',', '.');
    }

    private Double degreesToDecimal(String degreesString) {
        int pointPoition = degreesString.indexOf('.');
        String s1 = degreesString.substring(0, pointPoition);
        String s2 = degreesString.substring(pointPoition + 1);
        s2 = s1.substring(s1.length() - 2, s1.length()) + s2;
        s1 = s1.substring(0, s1.length() - 2);
        return Double.parseDouble(s1) + 10 * Double.parseDouble("0." + s2) / 6;
    }

    private Double stringToLongitude(String longitudeString) {
        if (longitudeString == null) {
            return null;
        }
        longitudeString = longitudeString.toUpperCase(Locale.ENGLISH);
        if (longitudeString.startsWith("E")) {
            longitudeString = longitudeString.substring(1);
            Double longitude = degreesToDecimal(longitudeString);
            return round(longitude, 6);
        }
        if (longitudeString.startsWith("W")) {
            longitudeString = longitudeString.substring(1);
            Double longitude = -1 * degreesToDecimal(longitudeString);
            return round(longitude, 6);
        }
        return null;
    }

    private String latitudeToString(Double latitude) {
        if (latitude == null) {
            return null;
        }
        String nsString;
        if (latitude >= 0) {
            nsString = "N";
        } else {
            nsString = "S";
            latitude = Math.abs(latitude);
        }
        int latitude1 = latitude.intValue();
        double latitude2 = latitude - latitude1;
        latitude2 = (latitude2 / 100) * 60;
        latitude = (latitude1 + latitude2) * 100;
        return nsString + LATITUDE_FORMAT.format(latitude).replace(',', '.');
    }

    private Double stringToLatitude(String latitudeString) {
        if (latitudeString == null) {
            return null;
        }
        latitudeString = latitudeString.toUpperCase(Locale.ENGLISH);
        if (latitudeString.startsWith("N")) {
            latitudeString = latitudeString.substring(1);
            Double latitude = degreesToDecimal(latitudeString);
            return round(latitude, 6);
        }
        if (latitudeString.startsWith("S")) {
            latitudeString = latitudeString.substring(1);
            Double latitude = -1 * degreesToDecimal(latitudeString);
            return round(latitude, 6);
        }
        return null;
    }

    private Integer getBattery(LocationPacket locationEntity) {
        if (locationEntity.batteryLevel != null && locationEntity.batteryScale != null) {
            if (locationEntity.batteryLevel >= 0 && locationEntity.batteryScale > 0) {
                return Math.round(7 * locationEntity.batteryLevel / locationEntity.batteryScale);
            } else {
                return 0;
            }
        }
        return null;
    }

    public String tr151ModPacketToString(Tr151ModPacket tr151ModPacket) {
        StringBuilder builder = new StringBuilder();
        builder.append("$").append(tr151ModPacket.imei).append(",");
        builder.append(tr151ModPacket.status).append(",");
        builder.append(tr151ModPacket.gpsFix).append(",");

        if (tr151ModPacket.date != null) {
            builder.append(tr151ModPacket.date).append(",");
            builder.append(tr151ModPacket.time).append(",");
        } else {
            builder.append(",").append(",");
        }

        if (tr151ModPacket.longitude != null) {
            builder.append(tr151ModPacket.longitude).append(",");
        } else {
            builder.append(",");
        }

        if (tr151ModPacket.latitude != null) {
            builder.append(tr151ModPacket.latitude).append(",");
        } else {
            builder.append(",");
        }

        if (tr151ModPacket.altitude != null) {
            builder.append(ALTITUDE_FORMAT.format(tr151ModPacket.altitude).replace(',', '.')).append(",");
        } else {
            builder.append(",");
        }

        if (tr151ModPacket.speed != null) {
            builder.append(SPEED_FORMAT.format(tr151ModPacket.speed * 1.943844).replace(',', '.'))
                    .append(",");
        } else {
            builder.append(",");
        }

        if (tr151ModPacket.heading != null) {
            builder.append(BEARING_FORMAT.format(tr151ModPacket.heading).replace(',', '.')).append(",");
        } else {
            builder.append(",");
        }

        if (tr151ModPacket.latitude != null && tr151ModPacket.longitude != null) {
            if (tr151ModPacket.satellites != null && tr151ModPacket.satellites >= 4) {
                builder.append(tr151ModPacket.satellites).append(",");
            } else {
                builder.append(25).append(",");
            }
        } else {
            builder.append(",");
        }

        if (tr151ModPacket.signalStrength != null) {
            builder.append(tr151ModPacket.signalStrength).append(",");
        } else {
            builder.append(",");
        }

        if (tr151ModPacket.battery != null) {
            builder.append(tr151ModPacket.battery).append(",");
        } else {
            builder.append(",");
        }

        if (tr151ModPacket.mcc != null) {
            builder.append(tr151ModPacket.mcc).append(",");
        } else {
            builder.append(",");
        }

        if (tr151ModPacket.mnc != null) {
            builder.append(tr151ModPacket.mnc).append(",");
        } else {
            builder.append(",");
        }

        if (tr151ModPacket.lac != null) {
            builder.append(tr151ModPacket.lac).append(",");
        } else {
            builder.append(",");
        }

        if (tr151ModPacket.cid != null) {
            builder.append(tr151ModPacket.cid).append("!");
        } else {
            builder.append("!");
        }
        return builder.toString();
    }

    public Tr151ModPacket stringToTr151ModPacket(String tr151ModPacketString) {
        if (tr151ModPacketString == null) {
            return null;
        }
        String sub = tr151ModPacketString.substring(tr151ModPacketString.indexOf('$') + 1,
                tr151ModPacketString.indexOf('!'));
        if (sub == null) {
            return null;
        }
        String[] tokens = sub.split(",");
        if (tokens == null) {
            return null;
        }

        Tr151ModPacket tr151ModPacket = new Tr151ModPacket();
        if (0 < tokens.length) {
            tr151ModPacket.imei = tokens[0];
        }
        if (1 < tokens.length && tokens[1] != null && !"".equals(tokens[1])) {
            tr151ModPacket.status = Integer.parseInt(tokens[1]);
        }
        if (2 < tokens.length) {
            tr151ModPacket.gpsFix = tokens[2];
        }
        if (3 < tokens.length) {
            tr151ModPacket.date = tokens[3];
        }
        if (4 < tokens.length) {
            tr151ModPacket.time = tokens[4];
        }
        if (5 < tokens.length) {
            tr151ModPacket.longitude = tokens[5];
        }
        if (6 < tokens.length) {
            tr151ModPacket.latitude = tokens[6];
        }
        if (7 < tokens.length && tokens[7] != null && !"".equals(tokens[7])) {
            tr151ModPacket.altitude = Double.parseDouble(tokens[7]);
        }
        if (8 < tokens.length && tokens[8] != null && !"".equals(tokens[8])) {
            tr151ModPacket.speed = Double.parseDouble(tokens[8]);
        }
        if (9 < tokens.length && tokens[9] != null && !"".equals(tokens[9])) {
            tr151ModPacket.heading = Integer.parseInt(tokens[9]);
        }
        if (10 < tokens.length && tokens[10] != null && !"".equals(tokens[10])) {
            tr151ModPacket.satellites = Integer.parseInt(tokens[10]);
        }
        if (11 < tokens.length && tokens[11] != null && !"".equals(tokens[11])) {
            tr151ModPacket.signalStrength = Integer.parseInt(tokens[11]);
        }
        if (12 < tokens.length && tokens[12] != null && !"".equals(tokens[12])) {
            tr151ModPacket.battery = Integer.parseInt(tokens[12]);
        }
        if (13 < tokens.length) {
            tr151ModPacket.mcc = tokens[13];
        }
        if (14 < tokens.length) {
            tr151ModPacket.mnc = tokens[14];
        }
        if (15 < tokens.length && tokens[15] != null && !"".equals(tokens[15])) {
            tr151ModPacket.lac = Integer.parseInt(tokens[15]);
        }
        if (16 < tokens.length && tokens[16] != null && !"".equals(tokens[16])) {
            tr151ModPacket.cid = Integer.parseInt(tokens[16]);
        }
        return tr151ModPacket;
    }

    private double round(double value, int scale) {
        return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
