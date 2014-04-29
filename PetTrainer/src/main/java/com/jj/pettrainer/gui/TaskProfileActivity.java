package com.jj.pettrainer.gui;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class TaskProfileActivity extends ActionBarActivity {

    private User user = new User();
    int task_id;
    int pet_id;
    private GetTaskProfileTask getTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pet_id = extras.getInt("PET_ID");
            task_id = extras.getInt("TASK_ID");
        }

        //Get the user info form shared storage and set to user.
        SharedPreferences mPrefs = getSharedPreferences(Global.PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("User", "");
        user = gson.fromJson(json, User.class);

        getTask = new GetTaskProfileTask() {
            @Override
            protected void onPostExecute(Task task) {
                TextView TaskTitleTextView = (TextView) findViewById(R.id.task_title);
                TaskTitleTextView.setText(task.getTitle());

                TextView TaskDescriptionTextView = (TextView) findViewById(R.id.task_description);
                TaskDescriptionTextView.setText(task.getDescription());

                TextView TaskDueDateTextView = (TextView) findViewById(R.id.task_due_date);
                TaskDueDateTextView.setText(task.getDue_date());

                TextView TaskRemindertView = (TextView) findViewById(R.id.task_reminder);
                TaskRemindertView.setText(task.getRemind_before());


            }

        };

        getTask.execute((Void) null);

    }


    public class GetTaskProfileTask extends AsyncTask<Void, Void, Task> {

        @Override
        protected Task doInBackground(Void... params) {
            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Token " + user.getToken());
                ResponseEntity<Task> response = restTemplate.exchange(Global.getTaskUrl(pet_id) + task_id, HttpMethod.GET, new HttpEntity<Object>(headers), Task.class);

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
        getMenuInflater().inflate(R.menu.task_profile, menu);
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
