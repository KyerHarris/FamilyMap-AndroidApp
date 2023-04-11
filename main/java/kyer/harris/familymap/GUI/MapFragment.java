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

import com.google.android.gms.maps.CameraUpdate;
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
import kyer.harris.familymap.backend.Settings;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private ArrayList<Marker> markers = new ArrayList<>();
    private ArrayList<Polyline> polylines = new ArrayList<>();
    private String eventID = null;
    private Settings settings = DataCache.getInstance().getSettings();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onResume(){
        if(settings.isMaleEvents() != DataCache.getInstance().getSettings().isMaleEvents()){

            settings.setMaleEvents(DataCache.getInstance().getSettings().isMaleEvents());
        }
        if(settings.isFemaleEvents() != DataCache.getInstance().getSettings().isFemaleEvents()){

            settings.setFemaleEvents(DataCache.getInstance().getSettings().isFemaleEvents());
        }
        if(settings.isMotherSide() != DataCache.getInstance().getSettings().isMotherSide()){

            settings.setMotherSide(DataCache.getInstance().getSettings().isMotherSide());
        }
        if(settings.isFatherSide() != DataCache.getInstance().getSettings().isFatherSide()){

            settings.setFatherSide(DataCache.getInstance().getSettings().isFatherSide());
        }
        if(settings.isFamilyTreeLines() != DataCache.getInstance().getSettings().isFamilyTreeLines()){

            settings.setFamilyTreeLines(DataCache.getInstance().getSettings().isFamilyTreeLines());
        }
        if(settings.isLifeStoryLines() != DataCache.getInstance().getSettings().isLifeStoryLines()){

            settings.setLifeStoryLines(DataCache.getInstance().getSettings().isLifeStoryLines());
        }
        /*for (int i = 0; i < polylines.size(); i++) {
            polylines.get(i).getTag();
        }*/
        //TODO: change lines on settings change
        super.onResume();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(eventID == null) {
            inflater.inflate(R.menu.menu_map, menu);
        }
        else{
            inflater.inflate(R.menu.menu_back, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_search_bar:
                startActivity(new Intent(this.getActivity(), SearchActivity.class));
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
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Family Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        createMap();
        if(eventID != null){
            for (int i = 0; i < markers.size(); i++) {
                if (((Event)markers.get(i).getTag()).getEventID().equals(eventID)){
                    markerClick(markers.get(i));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(markers.get(i).getPosition());
                    map.animateCamera(cameraUpdate);
                }
            }
        }
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

    public void eventMap(Person person, Event event){

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
                    MarkerOptions marker = new MarkerOptions().position(temp).icon(BitmapDescriptorFactory.defaultMarker(color));
                    markers.add(map.addMarker(marker));
                    markers.get(markers.size()-1).setTag(entryA.getValue());
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
            MarkerOptions marker = new MarkerOptions().position(temp).icon(BitmapDescriptorFactory.defaultMarker(color));
            markers.add(map.addMarker(marker));
            markers.get(markers.size()-1).setTag(e.getValue());
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
                return markerClick(marker);
            }
        });
    }
    private boolean markerClick(Marker marker){
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
        return false;
    }
    private void lifeStoryLines(Person person){
        PolylineOptions polyline = new PolylineOptions().width(5).color(0xffff0000);
        ArrayList<Event> events = new ArrayList<>();
        for (Map.Entry<String, Event> entryB: DataCache.getInstance().getEvents().entrySet()) {
            if(!person.getPersonID().equals(entryB.getValue().getPersonID())){continue;}
            events.add(entryB.getValue());
        }
        events.sort((o1, o2) -> o1.getYear().compareTo(o2.getYear()));
        for (int i = 0; i < events.size(); i++) {
            polyline.add(new LatLng(events.get(i).getLatitude(), events.get(i).getLongitude()));
        }
        Polyline pLine = map.addPolyline(polyline);
        pLine.setTag("lifeStoryLine");
        polylines.add(pLine);
    }
    private void connectSpouse(Person person, Event event){
        for (Map.Entry<String, Event> iter: DataCache.getInstance().getEvents().entrySet()) {
            if(!iter.getValue().getPersonID().equals(person.getSpouseID())){continue;}
            if(!iter.getValue().getEventType().equals("birth")){continue;}
            LatLng currEvent = new LatLng(event.getLatitude(), event.getLongitude());
            LatLng spouseBirth = new LatLng(iter.getValue().getLatitude(), iter.getValue().getLongitude());
            PolylineOptions polyline = new PolylineOptions().add(currEvent).add(spouseBirth).color(0xffffb6c1);
            Polyline pLine = map.addPolyline(polyline);
            pLine.setTag("spouseLine");
            polylines.add(pLine);
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
            Polyline pLine = map.addPolyline(polyline);
            pLine.setTag("femaleAncestor");
            polylines.add(pLine);
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
            Polyline pLine = map.addPolyline(polyline);
            pLine.setTag("maleAncestor");
            polylines.add(pLine);
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
            Polyline pLine = map.addPolyline(polyline);
            pLine.setTag("anyAncestor");
            polylines.add(pLine);
            if(DataCache.getInstance().getPeople().get(personID).getFatherID() != null){
                connectAncestors(iter.getValue(), DataCache.getInstance().getPeople().get(personID).getFatherID());
            }
            if(DataCache.getInstance().getPeople().get(personID).getMotherID() != null){
                connectAncestors(iter.getValue(), DataCache.getInstance().getPeople().get(personID).getMotherID());
            }
            break;
        }
    }

    public void highlight(String eventID){
        this.eventID = eventID;
    }
}
