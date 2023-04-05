package kyer.harris.familymap.GUI;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import androidx.appcompat.app.ActionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kyer.harris.familymap.R;
import kyer.harris.familymap.backend.DataCache;
import Model.*;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private ArrayList<Polyline> polylines = new ArrayList<>();
    //https://stackoverflow.com/questions/39316800/add-onoptionsitemselected-calling-in-fragment
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onResume(){
        super.onResume();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_search_bar:
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this.getActivity(), SettingsActivity.class));
                return true;
            default:
                break;
        }
        return false;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);
        // calling the action bar
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        // showing the back button in action bar



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        createMap();
        /*
        Spouse Lines
        A line is drawn linking the selected event to the birth event of the person’s spouse (i.e., the person associated with the selected event). If there is no birth event recorded for the spouse, the earliest available event for the spouse is used instead. If the person has no recorded spouse, or the recorded spouse has no events, no line is drawn.

        Family Tree Lines
        Lines linking the selected event to the person’s ancestors (i.e., the person associated with the selected event) are drawn as follows:
        A line is drawn between the selected event and the birth event of the selected person’s father. If the person’s father does not have a birth event, the earliest available event for the father is used instead. If the person has no recorded father, or the recorded father has no events, no line is drawn.
        A line is drawn between the selected event and the birth event of the selected person’s mother. The same logic that applies to the father also applies to the mother.
        Lines are drawn recursively from parents’ birth events to grandparents’ birth events, from grandparents’ birth events to great grandparents’ birth events, etc. including all available generations. In all cases, if a person’s birth event does not exist, the earliest event in the person’s life should be used instead of their birth event. As lines are drawn recursively up the family tree, they should become progressively and noticeably thinner.
        Life Story Line
        Lines are drawn connecting each event in a person’s life story (i.e., the person associated with the selected event), ordered chronologically. (See the Person Activity section for information on how events are ordered chronologically.)
         */
    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }
    private ArrayList<Person> personArray(HashMap<String, Person> mapA, HashMap<String, Person> mapB) {
        ArrayList<Person> persons = new ArrayList<>();
        for (Map.Entry<String, Person> entryA : mapA.entrySet()) {
            for (Map.Entry<String, Person> entryB : mapB.entrySet()) {
                if (entryA.getKey().equals(entryB.getKey())) {
                    persons.add(entryA.getValue());
                }
            }
        }
        return persons;
    }
    private ArrayList<Person> personArray(HashMap<String, Person> mapA){
        ArrayList<Person> persons = new ArrayList<>();
        for (Map.Entry<String, Person> entryA : mapA.entrySet()) {
            persons.add(entryA.getValue());

        }
        return persons;
    }


    private void populateMap(HashMap<String, Event> mapA, HashMap<String, Event> mapB){
        for (Map.Entry<String, Event> entryA : mapA.entrySet()) {
            for (Map.Entry<String, Event> entryB: mapB.entrySet()) {
                if(entryA.getKey().equals(entryB.getKey())){
                    Float color;
                    if (entryA.getValue().getEventType().equals("birth")) {
                        color = BitmapDescriptorFactory.HUE_AZURE;
                    } else if (entryA.getValue().getEventType().equals("marriage")) {
                        color = BitmapDescriptorFactory.HUE_MAGENTA;
                    } else if (entryA.getValue().getEventType().equals("death")) {
                        color = BitmapDescriptorFactory.HUE_ORANGE;
                    } else {
                        color = BitmapDescriptorFactory.HUE_YELLOW;
                    }
                    LatLng temp = new LatLng(entryA.getValue().getLatitude(), entryA.getValue().getLongitude());
                    map.addMarker(new MarkerOptions().position(temp)
                                    .icon(BitmapDescriptorFactory.defaultMarker(color)))
                            .setTag(entryA.getValue());
                    continue;
                }
            }
        }
    }
    private void populateMap(HashMap<String, Event> mapA){
        for (Map.Entry<String, Event> e : mapA.entrySet()) {
            Float color;
            if (e.getValue().getEventType().equals("birth")) {
                color = BitmapDescriptorFactory.HUE_AZURE;
            } else if (e.getValue().getEventType().equals("marriage")) {
                color = BitmapDescriptorFactory.HUE_MAGENTA;
            } else if (e.getValue().getEventType().equals("death")) {
                color = BitmapDescriptorFactory.HUE_ORANGE;
            } else {
                color = BitmapDescriptorFactory.HUE_YELLOW;
            }
            LatLng temp = new LatLng(e.getValue().getLatitude(), e.getValue().getLongitude());
            map.addMarker(new MarkerOptions().position(temp)
                            .icon(BitmapDescriptorFactory.defaultMarker(color)))
                    .setTag(e.getValue());
        }
    }
    private void createMap(){
        //Both sides of tree
        if (DataCache.getInstance().getSettings().isFatherSide() && DataCache.getInstance().getSettings().isMotherSide()) {
            if(DataCache.getInstance().getSettings().isMaleEvents() && DataCache.getInstance().getSettings().isFemaleEvents()) {
                populateMap(DataCache.getInstance().getEvents());
            }
            //all sides just male
            else if(DataCache.getInstance().getSettings().isMaleEvents() && !DataCache.getInstance().getSettings().isFemaleEvents()){
                populateMap(DataCache.getInstance().getMaleEvents());
            }
            //all sides just female
            else if(!DataCache.getInstance().getSettings().isMaleEvents() && DataCache.getInstance().getSettings().isFemaleEvents()){
                populateMap(DataCache.getInstance().getFemaleEvents());
            }
        }
        //just father's side of tree
        else if (!DataCache.getInstance().getSettings().isMotherSide() && DataCache.getInstance().getSettings().isFatherSide()) {
            //father's side male & female
            if (DataCache.getInstance().getSettings().isMaleEvents() && DataCache.getInstance().getSettings().isFemaleEvents()) {
                populateMap(DataCache.getInstance().getPaternalEvents());
            }
            //father's side just male
            else if (DataCache.getInstance().getSettings().isMaleEvents() && !DataCache.getInstance().getSettings().isFemaleEvents()) {
                populateMap(DataCache.getInstance().getPaternalEvents(), DataCache.getInstance().getMaleEvents());
            }
            //father's side just female
            else if (!DataCache.getInstance().getSettings().isMaleEvents() && DataCache.getInstance().getSettings().isFemaleEvents()) {
                populateMap(DataCache.getInstance().getPaternalEvents(), DataCache.getInstance().getFemaleEvents());
            }
        }
        //mother's side of tree
        else if (DataCache.getInstance().getSettings().isMotherSide() && !DataCache.getInstance().getSettings().isFatherSide()) {
            //mother's side male & female
            if (DataCache.getInstance().getSettings().isMaleEvents() && DataCache.getInstance().getSettings().isFemaleEvents()) {
                populateMap(DataCache.getInstance().getMaternalEvents());

            }
            //mother's side just male
            else if (DataCache.getInstance().getSettings().isMaleEvents() && !DataCache.getInstance().getSettings().isFemaleEvents()) {
                populateMap(DataCache.getInstance().getMaternalEvents(), DataCache.getInstance().getMaleEvents());
            }
            //mother's side just female
            else if (!DataCache.getInstance().getSettings().isMaleEvents() && DataCache.getInstance().getSettings().isFemaleEvents()) {
                populateMap(DataCache.getInstance().getMaternalEvents(), DataCache.getInstance().getFemaleEvents());
            }
        }
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LinearLayout textbox = getView().findViewById(R.id.eventInfo);
                Event event = (Event)marker.getTag();

                textbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), PersonActivity.class).putExtra("personid", event.getPersonID()));

                    }
                });
                for (int i = 0; i < polylines.size(); i++) {
                    polylines.get(i).remove();
                }
                polylines.clear();
                final TextView mapUser = getView().findViewById(R.id.mapText_username);
                final TextView mapBio = getView().findViewById(R.id.mapText_bio);
                Person person = DataCache.getInstance().getPeople().get(event.getPersonID());
                mapUser.setText(person.getFirstName() + " " + person.getLastName());
                mapBio.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
                if(person.getSpouseID() != null && DataCache.getInstance().getSettings().isSpouseLines()
                        && DataCache.getInstance().getSettings().isFemaleEvents() && DataCache.getInstance().getSettings().isMaleEvents()){
                    connectSpouse(person, event);
                }
                if(DataCache.getInstance().getSettings().isFamilyTreeLines()) {
                    familyTreeLines(person, event);
                }
                if(DataCache.getInstance().getSettings().isLifeStoryLines()){
                    lifeStoryLines(person);
                }

                //bring event line down here
                return false;
            }
            private void lifeStoryLines(Person person){
                PolylineOptions polyline = new PolylineOptions();
                LatLng birth = null;
                LatLng death = null;
                LatLng marriage = null;
                for (Map.Entry<String, Event> entryB: DataCache.getInstance().getEvents().entrySet()) {
                    if(!person.getPersonID().equals(entryB.getValue().getPersonID())){continue;}
                    if(entryB.getValue().getEventType().equals("birth")){
                        birth = new LatLng(entryB.getValue().getLatitude(), entryB.getValue().getLongitude());
                    }
                    else if(entryB.getValue().getEventType().equals("death")){
                        death = new LatLng(entryB.getValue().getLatitude(), entryB.getValue().getLongitude());
                    }
                    else if(entryB.getValue().getEventType().equals("marriage")){
                        marriage = new LatLng(entryB.getValue().getLatitude(), entryB.getValue().getLongitude());
                    }
                    if(marriage != null && birth != null && death != null){
                        polyline.width(5).color(0xffff0000).add(birth);
                        polyline.add(marriage);
                        polyline.add(death);
                        break;
                    }
                }
                polylines.add(map.addPolyline(polyline));
            }
            private void connectSpouse(Person person, Event event){
                for (Map.Entry<String, Event> iter: DataCache.getInstance().getEvents().entrySet()) {
                    if(!iter.getValue().getPersonID().equals(person.getSpouseID())){continue;}
                    if(!iter.getValue().getEventType().equals("birth")){continue;}
                    LatLng currEvent = new LatLng(event.getLatitude(), event.getLongitude());
                    LatLng spouseBirth = new LatLng(iter.getValue().getLatitude(), iter.getValue().getLongitude());
                    PolylineOptions polyline = new PolylineOptions().add(currEvent).add(spouseBirth).color(0xffffb6c1);
                    polylines.add(map.addPolyline(polyline));
                    break;
                }
            }
            private void familyTreeLines(Person person, Event event){
                //father's side
                if(person.getFatherID() != null && DataCache.getInstance().getSettings().isFatherSide()) {
                    //male and female ancestors
                    if(DataCache.getInstance().getSettings().isMaleEvents() && DataCache.getInstance().getSettings().isFemaleEvents()) {
                        connectAncestors(event, person.getFatherID());
                    }
                    //female ancestors
                    else if(!DataCache.getInstance().getSettings().isMaleEvents() && DataCache.getInstance().getSettings().isFemaleEvents()){
                        connectFemaleAncestors(event, person.getFatherID());
                    }
                    //male ancestors
                    else if(DataCache.getInstance().getSettings().isMaleEvents() && !DataCache.getInstance().getSettings().isFemaleEvents()){
                        connectMaleAncestors(event, person.getFatherID());
                    }
                }
                //mother's side
                if(person.getMotherID() != null && DataCache.getInstance().getSettings().isMotherSide()){
                    //male and female ancestors
                    if(DataCache.getInstance().getSettings().isMaleEvents() && DataCache.getInstance().getSettings().isFemaleEvents()) {
                        connectAncestors(event, person.getMotherID());
                    }
                    //female ancestors
                    else if(!DataCache.getInstance().getSettings().isMaleEvents() && DataCache.getInstance().getSettings().isFemaleEvents()){
                        connectFemaleAncestors(event, person.getMotherID());
                    }
                    //male ancestors
                    else if(DataCache.getInstance().getSettings().isMaleEvents() && !DataCache.getInstance().getSettings().isFemaleEvents()){
                        connectMaleAncestors(event, person.getMotherID());
                    }
                }
            }
            private void connectFemaleAncestors(Event event, String personID) {
                for (Map.Entry<String, Event> iter : DataCache.getInstance().getEvents().entrySet()) {
                    if (!iter.getValue().getPersonID().equals(personID)) {
                        continue;
                    }
                    if (!iter.getValue().getEventType().equals("birth")) {
                        continue;
                    }
                    LatLng currEvent = new LatLng(event.getLatitude(), event.getLongitude());
                    LatLng spouseBirth = new LatLng(iter.getValue().getLatitude(), iter.getValue().getLongitude());
                    PolylineOptions polyline = new PolylineOptions().add(currEvent).add(spouseBirth).color(0xff008000);
                    polylines.add(map.addPolyline(polyline));
                    if(DataCache.getInstance().getPeople().get(personID).getMotherID() != null){
                        connectFemaleAncestors(iter.getValue(), DataCache.getInstance().getPeople().get(personID).getMotherID());
                    }
                    break;
                }
            }
            private void connectMaleAncestors(Event event, String personID) {
                for (Map.Entry<String, Event> iter : DataCache.getInstance().getEvents().entrySet()) {
                    if (!iter.getValue().getPersonID().equals(personID)) {
                        continue;
                    }
                    if (!iter.getValue().getEventType().equals("birth")) {
                        continue;
                    }
                    LatLng currEvent = new LatLng(event.getLatitude(), event.getLongitude());
                    LatLng spouseBirth = new LatLng(iter.getValue().getLatitude(), iter.getValue().getLongitude());
                    PolylineOptions polyline = new PolylineOptions().add(currEvent).add(spouseBirth).color(0xff008000);
                    polylines.add(map.addPolyline(polyline));
                    if(DataCache.getInstance().getPeople().get(personID).getFatherID() != null){
                        connectMaleAncestors(iter.getValue(), DataCache.getInstance().getPeople().get(personID).getFatherID());
                    }
                    break;
                }
            }
            private void connectAncestors(Event event, String personID) {
                for (Map.Entry<String, Event> iter : DataCache.getInstance().getEvents().entrySet()) {
                    if (!iter.getValue().getPersonID().equals(personID)) {
                        continue;
                    }
                    if (!iter.getValue().getEventType().equals("birth")) {
                        continue;
                    }
                    LatLng currEvent = new LatLng(event.getLatitude(), event.getLongitude());
                    LatLng spouseBirth = new LatLng(iter.getValue().getLatitude(), iter.getValue().getLongitude());
                    PolylineOptions polyline = new PolylineOptions().add(currEvent).add(spouseBirth).color(0xff008000);
                    polylines.add(map.addPolyline(polyline));
                    if(DataCache.getInstance().getPeople().get(personID).getFatherID() != null){
                        connectAncestors(iter.getValue(), DataCache.getInstance().getPeople().get(personID).getFatherID());
                    }
                    if(DataCache.getInstance().getPeople().get(personID).getMotherID() != null){
                        connectAncestors(iter.getValue(), DataCache.getInstance().getPeople().get(personID).getMotherID());
                    }
                    break;
                }
            }
        });
    }
}
