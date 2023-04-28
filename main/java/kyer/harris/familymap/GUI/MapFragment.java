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
import java.util.Map;

import kyer.harris.familymap.R;
import kyer.harris.familymap.backend.DataCache;
import Model.*;
import kyer.harris.familymap.backend.Settings;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private final ArrayList<Marker> markers = new ArrayList<>();
    private final ArrayList<Polyline> polylines = new ArrayList<>();
    private String eventID = null;
    private final Settings settings = new Settings();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onResume(){
        if(settings.isMaleEvents() != DataCache.getInstance().getSettings().isMaleEvents()){
            if(DataCache.getInstance().getSettings().isMaleEvents()) {
                for (int i = 0; i < markers.size(); i++) {
                    Person temp = DataCache.getInstance().getPerson(((Event) markers.get(i).getTag()).getPersonID());
                    if (temp.getGender().equals("m")) {
                        markers.get(i).setVisible(true);
                    }
                }
            }
            else{
                for (int i = 0; i < markers.size(); i++) {
                    Person temp = DataCache.getInstance().getPerson(((Event) markers.get(i).getTag()).getPersonID());
                    if (temp.getGender().equals("m")) {
                        markers.get(i).setVisible(false);
                    }
                }
            }
            settings.setMaleEvents(DataCache.getInstance().getSettings().isMaleEvents());
        }
        if(settings.isFemaleEvents() != DataCache.getInstance().getSettings().isFemaleEvents()){
            if(DataCache.getInstance().getSettings().isFemaleEvents()) {
                for (int i = 0; i < markers.size(); i++) {
                    Person temp = DataCache.getInstance().getPerson(((Event) markers.get(i).getTag()).getPersonID());
                    if (temp.getGender().equals("f")) {
                        markers.get(i).setVisible(true);
                    }
                }
            }
            else{
                for (int i = 0; i < markers.size(); i++) {
                    Person temp = DataCache.getInstance().getPerson(((Event) markers.get(i).getTag()).getPersonID());
                    if (temp.getGender().equals("f")) {
                        markers.get(i).setVisible(false);
                    }
                }
            }
            settings.setFemaleEvents(DataCache.getInstance().getSettings().isFemaleEvents());
        }
        if(settings.isMotherSide() != DataCache.getInstance().getSettings().isMotherSide()){
            if(DataCache.getInstance().getSettings().isMotherSide()) {
                for (int i = 0; i < markers.size(); i++) {
                    Person temp = DataCache.getInstance().getMaternalAncestors().get(((Event) markers.get(i).getTag()).getPersonID());
                    if (temp != null) {
                        markers.get(i).setVisible(true);
                    }
                }
            }
            else{
                for (int i = 0; i < markers.size(); i++) {
                    Person temp = DataCache.getInstance().getMaternalAncestors().get(((Event) markers.get(i).getTag()).getPersonID());
                    if (temp != null) {
                        markers.get(i).setVisible(false);
                    }
                }
            }
            settings.setMotherSide(DataCache.getInstance().getSettings().isMotherSide());
        }
        if(settings.isFatherSide() != DataCache.getInstance().getSettings().isFatherSide()){
            if(DataCache.getInstance().getSettings().isFatherSide()) {
                for (int i = 0; i < markers.size(); i++) {
                    Person temp = DataCache.getInstance().getPaternalAncestors().get(((Event) markers.get(i).getTag()).getPersonID());
                    if (temp != null) {
                        markers.get(i).setVisible(true);
                    }
                }
            }
            else{
                for (int i = 0; i < markers.size(); i++) {
                    Person temp = DataCache.getInstance().getPaternalAncestors().get(((Event) markers.get(i).getTag()).getPersonID());
                    if (temp != null) {
                        markers.get(i).setVisible(false);
                    }
                }
            }
            settings.setFatherSide(DataCache.getInstance().getSettings().isFatherSide());
        }
        if(settings.isFamilyTreeLines() != DataCache.getInstance().getSettings().isFamilyTreeLines()){
            if(DataCache.getInstance().getSettings().isFamilyTreeLines()){
                for (int i = 0; i < polylines.size(); i++) {
                    if(((String)polylines.get(i).getTag()).contains("Ancestor")){
                        polylines.get(i).setVisible(true);
                    }
                }
            }
            else{
                for (int i = 0; i < polylines.size(); i++) {
                    if(((String)polylines.get(i).getTag()).contains("Ancestor")){
                        polylines.get(i).setVisible(false);
                    }
                }
            }
            settings.setFamilyTreeLines(DataCache.getInstance().getSettings().isFamilyTreeLines());
        }
        if(settings.isLifeStoryLines() != DataCache.getInstance().getSettings().isLifeStoryLines()){
            if(DataCache.getInstance().getSettings().isLifeStoryLines()){
                for (int i = 0; i < polylines.size(); i++) {
                    if(((String)polylines.get(i).getTag()).equals("lifeStoryLine")){
                        polylines.get(i).setVisible(true);
                    }
                }
            }
            else{
                for (int i = 0; i < polylines.size(); i++) {
                    if(((String)polylines.get(i).getTag()).equals("lifeStoryLine")){
                        polylines.get(i).setVisible(false);
                    }
                }
            }
            settings.setLifeStoryLines(DataCache.getInstance().getSettings().isLifeStoryLines());
        }
        if(settings.isSpouseLines() != DataCache.getInstance().getSettings().isSpouseLines()){
            if(DataCache.getInstance().getSettings().isSpouseLines()){
                for (int i = 0; i < polylines.size(); i++) {
                    if(((String)polylines.get(i).getTag()).equals("spouseLine")){
                        polylines.get(i).setVisible(true);
                    }
                }
            }
            else{
                for (int i = 0; i < polylines.size(); i++) {
                    if(((String)polylines.get(i).getTag()).equals("spouseLine")){
                        polylines.get(i).setVisible(false);
                    }
                }
            }
            settings.setSpouseLines(DataCache.getInstance().getSettings().isSpouseLines());
        }
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
    public void onMapLoaded() {}
    private void createMap(){
        ArrayList<Event> events = DataCache.getInstance().currEvents();
        for (int i = 0; i < events.size(); i++) {
            float color;
            if (events.get(i).getEventType().equals("birth")) {
                color = BitmapDescriptorFactory.HUE_AZURE;
            } else if (events.get(i).getEventType().equals("marriage")) {
                color = BitmapDescriptorFactory.HUE_MAGENTA;
            } else if (events.get(i).getEventType().equals("death")) {
                color = BitmapDescriptorFactory.HUE_ORANGE;
            } else {
                color = BitmapDescriptorFactory.HUE_YELLOW;
            }
            LatLng temp = new LatLng(events.get(i).getLatitude(), events.get(i).getLongitude());
            MarkerOptions marker = new MarkerOptions().position(temp).icon(BitmapDescriptorFactory.defaultMarker(color));
            markers.add(map.addMarker(marker));
            markers.get(markers.size()-1).setTag(events.get(i));
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
        ArrayList<Event> events;
        events = DataCache.getInstance().getIndividualsEvents(person.getPersonID());
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
                connectAncestors(event, person.getFatherID(), 10);
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
                connectAncestors(event, person.getMotherID(), 10);
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
    private void connectAncestors(Event event, String personID, int size) {
        ArrayList<Event> events = new ArrayList<>();
        for (Map.Entry<String, Event> iter : DataCache.getInstance().getEvents().entrySet()) {
            if (!iter.getValue().getPersonID().equals(personID)) {
                continue;
            }
            events.add(iter.getValue());
        }
        events.sort((o1, o2) -> o1.getYear().compareTo(o2.getYear()));
        LatLng currEvent = new LatLng(event.getLatitude(), event.getLongitude());
        LatLng spouseBirth = new LatLng(events.get(0).getLatitude(), events.get(0).getLongitude());
        PolylineOptions polyline = new PolylineOptions().add(currEvent).add(spouseBirth).color(0xff008000).width(size);
        Polyline pLine = map.addPolyline(polyline);
        pLine.setTag("anyAncestor");
        polylines.add(pLine);
        if(DataCache.getInstance().getPeople().get(personID).getFatherID() != null){
            connectAncestors(events.get(0), DataCache.getInstance().getPeople().get(personID).getFatherID(), size - 2);
        }
        if(DataCache.getInstance().getPeople().get(personID).getMotherID() != null){
            connectAncestors(events.get(0), DataCache.getInstance().getPeople().get(personID).getMotherID(), size - 2);
        }
    }
    public void highlight(String eventID){
        this.eventID = eventID;
    }
}
