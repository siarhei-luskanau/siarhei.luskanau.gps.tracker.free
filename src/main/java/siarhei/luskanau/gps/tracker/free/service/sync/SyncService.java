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

package siarhei.luskanau.gps.tracker.free.service.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import siarhei.luskanau.gps.tracker.free.service.sync.tracking.SendPositionsCallable;

public class SyncService extends Service {

    private static final String TAG = "SyncService";
    private static final String ACTION_SEND_POSITIONS = "ACTION_SEND_POSITIONS";
    private static final String EXTRA_TASK = "EXTRA_TASK";
    private static LinkedList<Task> queue = new LinkedList<Task>();
    private ThreadPoolExecutor sendPositionsExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

    public static void sendPositions(Context context) {
        context.startService(new Intent(context, SyncService.class).setAction(ACTION_SEND_POSITIONS));
    }

    public static <T extends Task> void startTask(Context context, T task) {
        Log.d(TAG, "Add task " + task.getClass().getSimpleName());
        context.startService(new Intent(context, SyncService.class).putExtra(EXTRA_TASK, task));
    }

    public static void delaySendPositions(Context context, long delayFromCurrent) {
        Intent intent = new Intent(context, SyncService.class).setAction(ACTION_SEND_POSITIONS);
        delayFromCurrent = Math.max(delayFromCurrent, 2000);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayFromCurrent, pendingIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sendPositionsExecutor.setKeepAliveTime(3 * 60 * 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && ACTION_SEND_POSITIONS.equals(intent.getAction())) {
            if (sendPositionsExecutor.getQueue().size() <= 3) {
                sendPositionsExecutor.submit(new SendPositionsCallable(this));
            } else {
                Log.w(TAG, "Too many tasks in executor Queue");
            }
        } else if (intent != null && intent.hasExtra(EXTRA_TASK)) {
            try {
                addTask((Task) intent.getSerializableExtra(EXTRA_TASK));
            } catch (Throwable e) {
                Log.e(TAG, "Failed onHandleIntent", e);
            }
        }
        return START_STICKY;
    }

    private void addTask(Task task) {
        if (task != null) {
            synchronized (queue) {
                if (task.getTaskTag() != null) {
                    for (Task forEachTask : queue) {
                        if (task.getTaskTag().equals(forEachTask.getTaskTag())) {
                            Log.w(TAG, "Skip task " + task.getTaskTag() + ". Already is in queue");
                            return;
                        }
                    }
                    queue.push(task);
                } else {
                    Log.w(TAG, "Task " + task.getClass().getSimpleName() + " should have not null TaskTag");
                }
            }
            if (executor.getQueue().size() <= 3) {
                executor.submit(new TaskCallable());
            }
        }
    }

    public abstract static class Task implements Serializable {
        abstract public void doTask(Context context) throws Exception;

        public String getTaskTag() {
            return getClass().getName();
        }
    }

    private class TaskCallable implements Callable<Object> {
        @Override
        public Object call() throws Exception {
            try {
                for (; ; ) {
                    Task task;
                    synchronized (queue) {
                        task = queue.peek();
                    }
                    if (task != null) {
                        long startTime = System.currentTimeMillis();
                        Log.d(TAG, "Run task " + task.getClass().getSimpleName());
                        try {
                            task.doTask(getApplicationContext());
                        } catch (Throwable e) {
                            Log.e(TAG, "Failed task", e);
                        }
                        Log.d(TAG, "Finish task " + task.getClass().getSimpleName() + " in " + (System.currentTimeMillis() - startTime) + " ms");
                        synchronized (queue) {
                            queue.remove(task);
                        }
                    } else {
                        break;
                    }
                }
            } catch (Throwable e) {
                Log.e(TAG, "Failed TaskCallable", e);
            }
            return null;
        }
    }

}
