package kyer.harris.familymap.GUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import kyer.harris.familymap.R;

public class EventActivity extends AppCompatActivity {
    private GoogleMap map;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        MapFragment fragment = new MapFragment();

        fragmentManager.beginTransaction().add(R.id.fragmentFrameLayout, fragment).commit();

        eventID = getIntent().getStringExtra("eventid");
        fragment.highlight(eventID);
    }
}