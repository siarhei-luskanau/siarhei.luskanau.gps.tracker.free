package siarhei.luskanau.gps.tracker.free.ui.progress;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.androidquery.AQuery;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.broadcast.ProgressBroadcastController;

public abstract class BaseProgressActivity extends AppCompatActivity {

    private ProgressBroadcastController.ProgressBroadcastReceiver progressBroadcastReceiver = new ProgressBroadcastController().createBroadcastReceiver(new InnerProgressBroadcastCallback());
    private AQuery aq = new AQuery(this);

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
            Snackbar.make(aq.id(R.id.contentFrameLayout).getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

}
