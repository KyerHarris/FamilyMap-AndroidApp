package kyer.harris.familymap.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ExpandableListView;
import android.widget.TextView;

import kyer.harris.familymap.R;

import Model.*;
import kyer.harris.familymap.backend.DataCache;


public class PersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ActionBar actionBar = getSupportActionBar();
        TextView firstName = findViewById(R.id.personFirstName);
        TextView lastName = findViewById(R.id.personLastName);
        TextView gender = findViewById(R.id.personGender);
        ExpandableListView events = findViewById(R.id.eventList);
        ExpandableListView family = findViewById(R.id.familyList);

        String personId = getIntent().getStringExtra("personid");
        Person person = DataCache.getInstance().getPerson(personId);

        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        gender.setText(person.getGender());
        //https://www.digitalocean.com/community/tutorials/android-expandablelistview-example-tutorial
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_back, menu);
        return true;
    }
}