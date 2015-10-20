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

public class WialonApiConstants {

    public static final class packetTypes {
        public static final class login {
            public static final String L = "L";
            public static final String AL = "AL";
            public static final String AL_1_SUCCESS = "1";
            public static final String AL_0_REJECT = "0";
            public static final String AL_01_ERROR_PASSWORD = "01";
        }

        public static final class shortData {
            public static final String SD = "SD";
            public static final String ASD = "ASD";
            public static final String ASD_STRUCTURE_ERROR = "-1";
            public static final String ASD_0_INCORRECT_TIME = "0";
            public static final String ASD_1_SUCCESS = "1";
            public static final String ASD_10_ERROR_COORDINATES = "10";
            public static final String ASD_11_ERROR_HEIGHT_SPEED_COURSE = "11";
            public static final String ASD_12_ERROR_SATELLITES = "12";
        }
    }

}