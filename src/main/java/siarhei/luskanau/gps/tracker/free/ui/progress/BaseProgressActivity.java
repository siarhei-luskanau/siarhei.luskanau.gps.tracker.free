package siarhei.luskanau.gps.tracker.free.ui.progress;

import android.support.v7.app.ActionBarActivity;

import siarhei.luskanau.gps.tracker.free.broadcast.ProgressBroadcastController;

public abstract class BaseProgressActivity extends ActionBarActivity {

    private ProgressBroadcastController.ProgressBroadcastReceiver progressBroadcastReceiver = new ProgressBroadcastController().createBroadcastReceiver(new InnerProgressBroadcastCallback());

    @Override
    protected void onStart() {
        super.onStart();
        progressBroadcastReceiver.registerReceiver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressBroadcastReceiver.unregisterReceiver(this);
    }

    private class InnerProgressBroadcastCallback extends ProgressBroadcastController.ProgressBroadcastCallback {
        @Override
        public void onShowAlertDialog(CharSequence title, CharSequence message) {
            AlertDialogFragment.show(BaseProgressActivity.this, title, message);
        }
    }

}
