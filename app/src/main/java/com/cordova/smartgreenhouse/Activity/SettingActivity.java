package com.cordova.smartgreenhouse.Activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cordova.smartgreenhouse.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class SettingActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    public static class SettingsFragment extends PreferenceFragment {
        SharedPreferences prefsOptions;
        //private int settings_prefs.;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refHome = database.getReference("setting");
        DatabaseReference refPublic, refLokal, refValuePublic, refHostname, refValueHostname, refValueLocal;
        PreferenceFragment.OnPreferenceStartFragmentCallback hostname, ippublic, iplocal;
        Preference et;

        public SettingsFragment() {

        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.setting_pref);
            SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
            et = findPreference("prefIPpublic");


            refPublic = refHome.child("ip_public");
            refValuePublic = refPublic.child("value");
            refHostname = refHome.child("hostname");
            refValueHostname = refHostname.child("value");
            refLokal = refHome.child("ip_local");
            refValueLocal = refLokal.child("value");
            update();

        }

        private void update() {

            refValueHostname.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            refValuePublic.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    et.setSummary(dataSnapshot.getValue() + "");
                    Log.d("cvc", dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            refValueLocal.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

}
