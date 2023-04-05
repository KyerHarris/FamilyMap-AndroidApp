package kyer.harris.familymap.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
                        //swap to events
                        break;
                    case 1:
                        //swap to new person activity
                        break;
                }
                return false;
            }
        });

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

    public ExpandableListAdapter createActivityAdapter(){
        List<String> titles = new ArrayList<>();
        titles.add("LIFE EVENTS");
        titles.add("FAMILY");

        List<Event> lifeEvents = new ArrayList<>();
        Event birth = null;
        Event marriage = null;
        Event death = null;
        for (Map.Entry<String, Event> m: DataCache.getInstance().getEvents().entrySet()){
            if(!m.getValue().getPersonID().equals(person.getPersonID()))continue;
            if(m.getValue().getEventType().equals("birth")) birth = m.getValue();
            else if(m.getValue().getEventType().equals("marriage")) marriage = m.getValue();
            else if(m.getValue().getEventType().equals("death")) death = m.getValue();
        }
        lifeEvents.add(birth);
        lifeEvents.add(marriage);
        lifeEvents.add(death);

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
            if(m.getValue().getFatherID() == null);
            else{
                if(m.getValue().getFatherID().equals(person.getPersonID())) {
                    family.add(m.getValue());
                }
            }
            if(m.getValue().getMotherID() == null) continue;
            else{
                if(m.getValue().getMotherID().equals(person.getPersonID())){
                    family.add(m.getValue());
                }
            }
        }
        return new PersonActivityAdapter(this, titles, lifeEvents, family, person);
    }
}