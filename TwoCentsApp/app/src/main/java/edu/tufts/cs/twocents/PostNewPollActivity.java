package edu.tufts.cs.twocents;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostNewPollActivity extends AppCompatActivity {
    private static final String TAG = "PostNewPollActivity";
    private EditText poll_text;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_poll);
        poll_text = (EditText) findViewById(R.id.poll_text);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        progress = new ProgressDialog(this);
    }

    public void cancel(View view) {

        close();
    }

    public void send(View view) {
        progress.setTitle("Posting");
        progress.setCancelable(false);
        progress.show();

        Map<String, String> params = new HashMap<>();
        params.put("question", poll_text.getText().toString());

        ApiHandler apiHandler = new ApiHandler(getApplicationContext()) {
            @Override
            public void onCompleted(JSONObject response) {
                Log.v(TAG, response.toString());
                progress.dismiss();
                close();
            }

            @Override
            public void onError() {
                progress.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to post poll", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        apiHandler.makeRequest(ApiMethods.CREATE_NEW_POLL, params, null);
    }

    private void close() {
        super.onBackPressed();
    }

}