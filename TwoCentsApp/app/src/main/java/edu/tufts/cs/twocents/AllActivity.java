package edu.tufts.cs.twocents;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class AllActivity extends AppCompatActivity {
    private static final String TAG = "AllActivity";

    private TextView pollTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        pollTextView = (TextView) findViewById(R.id.poll_view);
        fetchPolls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        } else if (id == R.id.action_me) {
            startActivity(new Intent(getApplicationContext(), AllActivity.class));
            return true;
        } else if (id == R.id.action_all) {
            startActivity(new Intent(getApplicationContext(), AllActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchPolls() {
        String [] getParams = {"lat=2", "lng=-5", "radius=5"};
        MakeRequest mr = new MakeRequest("getPolls", getParams) {
            @Override
            protected void onPostExecute(JSONObject result) {
                if (result != null) {
                    displayPolls(result);
                } else {
                    Log.v("TAG", "JSON Object was NULL");
                }
            }
        };
        mr.execute();
    }

    private void displayPolls(JSONObject polls) {

    }

}
