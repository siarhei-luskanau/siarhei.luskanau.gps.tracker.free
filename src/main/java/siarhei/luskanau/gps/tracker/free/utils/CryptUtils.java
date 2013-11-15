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

package siarhei.luskanau.gps.tracker.free.utils;

public class CryptUtils {

//    private static PublicCrypt publicCrypt;

//    private static PublicCrypt getPublicCrypt(Context context) throws Exception {
//        if (publicCrypt == null) {
//            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
//            byte[] signatureByteArray = packageInfo.signatures[0].toByteArray();
//            publicCrypt = new PublicCrypt(signatureByteArray);
//        }
//        return publicCrypt;
//    }

//    public static byte[] getAssets(Context context, String filename) throws Exception {
//        CRC32 crc32 = new CRC32();
//        crc32.update(filename.getBytes());
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        InputStream inputStream = context.getAssets().open(String.valueOf(crc32.getValue()));
//        byte[] buffer = new byte[1024];
//        for (; ; ) {
//            int length = inputStream.read(buffer);
//            if (length == -1) {
//                break;
//            } else {
//                if (length > 0) {
//                    byteArrayOutputStream.write(buffer, 0, length);
//                }
//            }
//        }
//        inputStream.close();
//        byte[] bytes = getPublicCrypt(context).decrypt(byteArrayOutputStream.toByteArray());
//        return bytes;
//    }

}
