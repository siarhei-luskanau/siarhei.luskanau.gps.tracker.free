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

package siarhei.luskanau.gps.tracker.free.service.sync.tracking;

import android.content.Context;
import android.util.Log;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

import siarhei.luskanau.gps.tracker.free.broadcast.AppBroadcastController;
import siarhei.luskanau.gps.tracker.free.dao.LocationDAO;
import siarhei.luskanau.gps.tracker.free.model.LocationModel;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.service.sync.tracking.adapter.Tr151ModLocationAdapter;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;

public class SendSocketTask {

    private static Socket socket;
    private static OutputStream outputStream;
    private Tr151ModLocationAdapter packetAdapter = new Tr151ModLocationAdapter();

    public void doTask(Context context) throws Exception {
        ServerEntity serverEntity = AppSettings.getServerEntity(context);
        for (; ; ) {
            List<LocationModel> locationEntities = LocationDAO.queryNextLocations(context, 100);
            if (locationEntities != null && locationEntities.size() > 0) {
                openSocket(serverEntity.server_address, serverEntity.server_port);
                for (LocationModel locationEntity : locationEntities) {
                    outputStream.write(packetAdapter.tr151ModPacketToString(packetAdapter.fromLocationModel(locationEntity)).getBytes());
                    outputStream.flush();
                }
                LocationDAO.deleteLocations(context, locationEntities);
                incPacketCounter(context, locationEntities.size());
                Log.d(context.getPackageName(), "Packet is send: " + locationEntities.size());
            } else {
                break;
            }
            closeSocket();
        }
    }

    private void incPacketCounter(Context context, int count) {
        AppSettings.setPacketCounter(context, AppSettings.getPacketCounter(context) + count);
        AppBroadcastController.sendLastPositionIsUpdatedBroadcast(context);
    }

    private void openSocket(String address, int port) throws Exception {
        if (outputStream == null) {
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(address), port);
            socket = new Socket();
            socket.setSoTimeout(60 * 1000);
            socket.connect(socketAddress, 5 * 1000);
            outputStream = socket.getOutputStream();
        }
    }

    private void closeSocket() {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
        }
        outputStream = null;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        }
        socket = null;
    }

}
