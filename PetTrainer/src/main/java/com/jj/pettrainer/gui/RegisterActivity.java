package com.jj.pettrainer.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.jj.pettrainer.R;
import com.jj.pettrainer.gui.Generic.Global;
import com.jj.pettrainer.gui.Models.User;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class RegisterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {
        private EditText userNameView, firstNameView, lastNameView, emailView, passwordView;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_register, container, false);

            Button registerButton = (Button) rootView.findViewById(R.id.register_button);
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerUser();
                }
            });

            userNameView = (EditText) rootView.findViewById(R.id.user_username);
            firstNameView = (EditText) rootView.findViewById(R.id.user_first_name);
            lastNameView = (EditText) rootView.findViewById(R.id.user_last_name);
            emailView = (EditText) rootView.findViewById(R.id.email);
            passwordView = (EditText) rootView.findViewById(R.id.password);

            return rootView;
        }

        private void registerUser() {
            User user = new User(
                    userNameView.getText().toString(),
                    firstNameView.getText().toString(),
                    lastNameView.getText().toString(),
                    emailView.getText().toString(),
                    passwordView.getText().toString()
            );

            UserRegisterTask task = new UserRegisterTask(user);
            task.execute((Void) null);

        }
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
        private final User user;

        UserRegisterTask(User user) {
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
                User result = restTemplate.postForObject(Global.API_REGISTER, user, User.class);

                SharedPreferences mPrefs = getSharedPreferences(Global.PREFS_NAME, MODE_PRIVATE);

                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putString("User", result.toJson());
                prefsEditor.commit();

                Intent goToPetListActivity = new Intent(getApplicationContext(), PetListActivity.class);
                startActivity(goToPetListActivity);

            } catch (RestClientException e) {
                System.out.println(e);
                return false;
            }
            return true;
        }
    }

}
