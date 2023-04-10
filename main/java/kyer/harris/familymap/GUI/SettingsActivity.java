package kyer.harris.familymap.GUI;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kyer.harris.familymap.R;
import kyer.harris.familymap.backend.DataCache;

public class SettingsActivity extends AppCompatActivity {
    private SwitchMaterial lifeStoryLines;
    private SwitchMaterial familyTreeLines;
    private SwitchMaterial spouseLines;
    private SwitchMaterial fatherSide;
    private SwitchMaterial motherSide;
    private SwitchMaterial maleEvents;
    private SwitchMaterial femaleEvents;
    private LinearLayout logout;


    private AppBarConfiguration appBarConfiguration;
    //https://www.geeksforgeeks.org/how-to-add-and-customize-back-button-of-action-bar-in-android/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Family Map: Settings");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        lifeStoryLines = findViewById(R.id.settings_life_story);
        familyTreeLines = findViewById(R.id.settings_family_tree);
        spouseLines = findViewById(R.id.settings_spouse_lines);
        fatherSide = findViewById(R.id.settings_father_side);
        motherSide = findViewById(R.id.settings_mother_side);
        maleEvents = findViewById(R.id.settings_male_events);
        femaleEvents = findViewById(R.id.settings_female_events);
        logout = findViewById(R.id.settings_logout);

        lifeStoryLines.setChecked(DataCache.getInstance().getSettings().isLifeStoryLines());
        familyTreeLines.setChecked(DataCache.getInstance().getSettings().isFamilyTreeLines());
        spouseLines.setChecked(DataCache.getInstance().getSettings().isSpouseLines());
        fatherSide.setChecked(DataCache.getInstance().getSettings().isFatherSide());
        motherSide.setChecked(DataCache.getInstance().getSettings().isMotherSide());
        maleEvents.setChecked(DataCache.getInstance().getSettings().isMaleEvents());
        femaleEvents.setChecked(DataCache.getInstance().getSettings().isFemaleEvents());
        lifeStoryLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().getSettings().setLifeStoryLines(isChecked);
            }
        });
        familyTreeLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().getSettings().setFamilyTreeLines(isChecked);
            }
        });
        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().getSettings().setSpouseLines(isChecked);
            }
        });
        fatherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().getSettings().setFatherSide(isChecked);
            }
        });
        motherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().getSettings().setMotherSide(isChecked);
            }
        });
        maleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().getSettings().setMaleEvents(isChecked);
            }
        });
        femaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().getSettings().setFemaleEvents(isChecked);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DataCache.getInstance().reset();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}