package kyer.harris.familymap.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kyer.harris.familymap.R;

import Model.*;
import kyer.harris.familymap.backend.DataCache;
import kyer.harris.familymap.backend.PersonActivityAdapter;


public class PersonActivity extends AppCompatActivity {
    Person person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Family Map: Person Details");

        setContentView(R.layout.activity_person);
        TextView firstName = findViewById(R.id.personFirstName);
        TextView lastName = findViewById(R.id.personLastName);
        TextView gender = findViewById(R.id.personGender);

        String personId = getIntent().getStringExtra("personid");
        person = DataCache.getInstance().getPerson(personId);

        ExpandableListView listView = findViewById(R.id.eventList);
        ExpandableListAdapter adapter = createActivityAdapter();
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (groupPosition){
                    case 0:
                        startActivity(new Intent(PersonActivity.this, EventActivity.class).putExtra("eventid", ((Event)adapter.getChild(groupPosition, childPosition)).getEventID()));
                        break;
                    case 1:
                        startActivity(new Intent(PersonActivity.this, PersonActivity.class).putExtra("personid", ((Person)adapter.getChild(groupPosition, childPosition)).getPersonID()));
                        break;
                }
                return false;
            }
        });
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        gender.setText(person.getGender());
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_back, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        Intent intent = new Intent(PersonActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }
    public ExpandableListAdapter createActivityAdapter(){
        List<String> titles = new ArrayList<>();
        titles.add("LIFE EVENTS");
        titles.add("FAMILY");
        List<Event> lifeEvents = new ArrayList<>();
        if(!DataCache.getInstance().getSettings().isMotherSide() && DataCache.getInstance().getMaternalAncestors().get(person.getPersonID()) != null){}
        else if(!DataCache.getInstance().getSettings().isFatherSide() && DataCache.getInstance().getPaternalAncestors().get(person.getPersonID()) != null){}
        else{
            if ((DataCache.getInstance().getSettings().isMaleEvents() && person.getGender().equals("m")) ||
                    (DataCache.getInstance().getSettings().isFemaleEvents() && person.getGender().equals("f"))) {
                lifeEvents = DataCache.getInstance().getIndividualsEvents(person.getPersonID());
            }
        }
        List<Person> family = new ArrayList<>();
        if(person.getSpouseID() != null){
            family.add(DataCache.getInstance().getPerson(person.getSpouseID()));
        }
        if(person.getMotherID() != null){
            family.add(DataCache.getInstance().getPerson(person.getMotherID()));
        }
        if(person.getFatherID() != null){
            family.add(DataCache.getInstance().getPerson(person.getFatherID()));
        }
        for (Map.Entry<String, Person> m: DataCache.getInstance().getPeople().entrySet()) {
            if(m.getValue().getFatherID() != null){
                if(m.getValue().getFatherID().equals(person.getPersonID())) {
                    family.add(m.getValue());
                }
            }
            if(m.getValue().getMotherID() != null){
                if(m.getValue().getMotherID().equals(person.getPersonID())){
                    family.add(m.getValue());
                }
            }
        }
        return new PersonActivityAdapter(this, titles, lifeEvents, family, person);
    }
}