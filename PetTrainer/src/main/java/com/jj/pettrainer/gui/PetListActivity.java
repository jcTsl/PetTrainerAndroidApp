package com.jj.pettrainer.gui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.jj.pettrainer.R;
import com.jj.pettrainer.gui.Generic.Global;
import com.jj.pettrainer.gui.Models.Pet;
import com.jj.pettrainer.gui.Models.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PetListActivity extends ActionBarActivity {

    private User user = new User();
    private GetPetsTask getPets = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        final ListView listView = (ListView) findViewById(R.id.PetListView);

        //Get the user info form shared storage and set to user.
        SharedPreferences mPrefs = getSharedPreferences(Global.PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("User", "");
        user = gson.fromJson(json, User.class);

        final PetListActivity that = this;

        getPets = new GetPetsTask() {
            @Override
            protected void onPostExecute(Pet[] pets) {
                ArrayAdapter<Pet> adapter = new ArrayAdapter<Pet>(that, android.R.layout.simple_list_item_1, pets);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition = position;

                        // ListView Clicked item value
                        Pet pet = (Pet) listView.getItemAtPosition(position);

                        Intent goToPetProfileActivity = new Intent(getApplicationContext(), PetProfileActivity.class);
                        goToPetProfileActivity.putExtra("PET_ID", pet.getId());
                        startActivity(goToPetProfileActivity);

                    }

                });
            }

        };

        getPets.execute((Void) null);

    }


    public class GetPetsTask extends AsyncTask<Void, Void, Pet[]> {

        @Override
        protected Pet[] doInBackground(Void... params) {
            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Token " + user.getToken());
                ResponseEntity<Pet[]> response = restTemplate.exchange(Global.API_GET_PETS_URL, HttpMethod.GET, new HttpEntity<Object>(headers), Pet[].class);
                System.out.println(response.getBody());

                return response.getBody();

//
//

            } catch (RestClientException e) {
                System.out.println(e);
                return null;
            }
        }

    }


//    /**
//     * Shows the progress UI and hides the login form.
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    public void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pet_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_pet:

                break;
            case R.id.action_show_profile:

                Intent goToUserProfileActivity = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(goToUserProfileActivity);

                break;
            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
