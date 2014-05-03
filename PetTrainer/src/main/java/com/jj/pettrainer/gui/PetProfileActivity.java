package com.jj.pettrainer.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.jj.pettrainer.R;
import com.jj.pettrainer.gui.Generic.Global;
import com.jj.pettrainer.gui.Models.Pet;
import com.jj.pettrainer.gui.Models.Task;
import com.jj.pettrainer.gui.Models.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class PetProfileActivity extends ActionBarActivity {

    private User user = new User();
    private GetPetProfileTask getPet = null;
    private GetPetTasksTask getPetTasks = null;
    int pet_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pet_id = extras.getInt("PET_ID");
        }

        final ListView petTasklistView = (ListView) findViewById(R.id.petTaskListView);

        //Get the user info form shared storage and set to user.
        SharedPreferences mPrefs = getSharedPreferences(Global.PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("User", "");
        user = gson.fromJson(json, User.class);

        getPet = new GetPetProfileTask() {
            @Override
            protected void onPostExecute(Pet pet) {
                TextView PetTitleTextView = (TextView) findViewById(R.id.pet_title);
                System.out.println("PET:" + pet.getTitle());
                PetTitleTextView.setText(pet.getTitle());
            }

        };

        getPet.execute((Void) null);


        final PetProfileActivity that = this;


        getPetTasks = new GetPetTasksTask() {
            @Override
            protected void onPostExecute(Task[] tasks) {
                ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(that, android.R.layout.simple_list_item_1, tasks);
                petTasklistView.setAdapter(adapter);

                petTasklistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int itemPosition = position;

                        Task task = (Task) petTasklistView.getItemAtPosition(position);

                        Intent goToTaskProfileActivity = new Intent(getApplicationContext(), TaskProfileActivity.class);
                        goToTaskProfileActivity.putExtra("TASK_ID", task.getId());
                        goToTaskProfileActivity.putExtra("PET_ID", task.getPet());
                        startActivity(goToTaskProfileActivity);

                    }
                });

            }

        };

        getPetTasks.execute((Void) null);

    }

    /**
     * Perform the async task of getting all the pets profile from the API servers
     */
    public class GetPetProfileTask extends AsyncTask<Void, Void, Pet> {

        @Override
        protected Pet doInBackground(Void... params) {
            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Token " + user.getToken());
                ResponseEntity<Pet> response = restTemplate.exchange(Global.API_GET_PET_PROFILE + pet_id, HttpMethod.GET, new HttpEntity<Object>(headers), Pet.class);

                System.out.println(response.getBody());

                return response.getBody();

            } catch (RestClientException e) {
                System.out.println(e);
                return null;
            }
        }

    }

    public class GetPetTasksTask extends AsyncTask<Void, Void, Task[]> {
        @Override
        protected Task[] doInBackground(Void... params) {
            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Token " + user.getToken());
                ResponseEntity<Task[]> response = restTemplate.exchange(Global.getTaskUrl(pet_id), HttpMethod.GET, new HttpEntity<Object>(headers), Task[].class);
                System.out.println(response.getBody());

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
        getMenuInflater().inflate(R.menu.pet_profile, menu);
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
