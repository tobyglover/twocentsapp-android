package edu.tufts.cs.twocents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateUserActivity extends AppCompatActivity {

    private EditText usernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        usernameInput = (EditText)findViewById(R.id.usernameInput);
        Button goButton = (Button) findViewById(R.id.goButton);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser();
            }
        });

        /* To restrict Space Bar in Keyboard */
        InputFilter whitespaceFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        usernameInput.setFilters(new InputFilter[] { whitespaceFilter, new InputFilter.LengthFilter(20) });
    }

    @Override
    public void onBackPressed() {}

    private void createNewUser() {
        Map<String, String> getParams = new HashMap<>();
        String username = usernameInput.getText().toString();

        if (!username.isEmpty()) {
            getParams.put("username", username);
        }

        ApiHandler apiHandler = new ApiHandler(getApplicationContext()) {
            @Override
            public void onCompleted(JSONObject response) {
                try {
                    String userKey = response.get("userKey").toString();
                    requestDone(userKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        apiHandler.makeRequest(ApiMethods.CREATE_NEW_USER, getParams, null);
    }

    private void requestDone(String userKey) {
        User user = new User(getApplicationContext());
        user.setUserKey(userKey);
        super.onBackPressed();
    }

}
