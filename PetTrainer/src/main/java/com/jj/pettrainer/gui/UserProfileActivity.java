package com.jj.pettrainer.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import com.jj.pettrainer.R;
import com.jj.pettrainer.gui.Generic.Global;
import com.jj.pettrainer.gui.Models.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class UserProfileActivity extends ActionBarActivity {

    private User user = new User();
    private GetUserProfileTask getUserProfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Get the user info form shared storage and set to user.
        SharedPreferences mPrefs = getSharedPreferences(Global.PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("User", "");
        user = gson.fromJson(json, User.class);

        getUserProfile = new GetUserProfileTask() {
            @Override
            protected void onPostExecute(User user) {

                TextView UserUsernameTextView = (TextView) findViewById(R.id.user_username);
                TextView UserFirstNameTextView = (TextView) findViewById(R.id.user_first_name);
                TextView UserLastNameTextView = (TextView) findViewById(R.id.user_last_name);
                TextView UserEmailTextView = (TextView) findViewById(R.id.user_email);

                UserUsernameTextView.setText(user.getUsername());
                UserFirstNameTextView.setText(user.getFirstName());
                UserLastNameTextView.setText(user.getLastName());
                UserEmailTextView.setText(user.getEmail());

            }

        };

        getUserProfile.execute((Void) null);

        Button logOutBtn = (Button) findViewById(R.id.user_logout_btn);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mPrefs = getSharedPreferences(Global.PREFS_NAME, MODE_PRIVATE);
                mPrefs.edit().remove("User");

                Intent goToLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goToLoginIntent);

            }
        });


    }

    /**
     * Performs the async task of getting a users profile from the API server.
     */
    public class GetUserProfileTask extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... params) {

            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
                HttpHeaders headers = new HttpHeaders();

                headers.set("Authorization", "Token " + user.getToken());
                ResponseEntity<User> response = restTemplate.exchange(Global.API_GET_USER_PROFILE + user.getId(),
                        HttpMethod.GET, new HttpEntity<Object>(headers), User.class);

                return response.getBody();

            } catch (RestClientException e) {
                System.out.println(e);
                return null;
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
