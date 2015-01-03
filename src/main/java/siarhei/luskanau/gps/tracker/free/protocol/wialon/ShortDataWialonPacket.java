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

public class ShortDataWialonPacket extends BaseWialonPacket {

    public String date = NA;
    public String time = NA;
    public String lat1 = NA;
    public String lat2 = NA;
    public String lon1 = NA;
    public String lon2 = NA;
    public String speed = NA;
    public String course = NA;
    public String height = NA;
    public String sats = NA;

    @Override
    public String getType() {
        return "SD";
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append(date).append(MESSAGE_SEPARATOR).append(time).append(MESSAGE_SEPARATOR);
        builder.append(lat1).append(MESSAGE_SEPARATOR).append(lat2).append(MESSAGE_SEPARATOR);
        builder.append(lon1).append(MESSAGE_SEPARATOR).append(lon2).append(MESSAGE_SEPARATOR);
        builder.append(speed).append(MESSAGE_SEPARATOR);
        builder.append(course).append(MESSAGE_SEPARATOR);
        builder.append(height).append(MESSAGE_SEPARATOR);
        builder.append(sats);
        return builder.toString();
    }

}