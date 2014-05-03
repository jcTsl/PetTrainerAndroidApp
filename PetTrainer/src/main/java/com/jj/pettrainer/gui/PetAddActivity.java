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
import android.widget.EditText;
import com.google.gson.Gson;
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

import java.util.HashMap;
import java.util.Map;

public class PetAddActivity extends ActionBarActivity {

    private User user = new User();
    private SavePetTask savePet = null;
    private EditText petName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_add);

        SharedPreferences mPrefs = getSharedPreferences(Global.PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("User", "");
        user = gson.fromJson(json, User.class);

        petName = (EditText) findViewById(R.id.pet_title);
        final Button savePetBtn = (Button) findViewById(R.id.pet_add_btn);

        savePetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savePet = new SavePetTask();
                savePet.execute((Void) null);

            }
        });

    }

    /**
     * Perform async task of sending new pet data to the API server to create a new pet.
     */
    public class SavePetTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Token " + user.getToken());

                Map<String, String> body = new HashMap<String, String>();

                body.put("title", petName.getText().toString());

                HttpEntity<?> httpEntity = new HttpEntity<Object>(body, headers);
                ResponseEntity<Pet> response = restTemplate.exchange(Global.API_ADD_PET, HttpMethod.POST, httpEntity, Pet.class);
                System.out.println(response.getBody());

                Intent goToPetListActivity = new Intent(getApplicationContext(), PetListActivity.class);
                startActivity(goToPetListActivity);

                return true;
            } catch (RestClientException e) {
                System.out.println(e);
                return false;
            }


        }
    }

    private class NewPetContainer {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pet_add, menu);
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
