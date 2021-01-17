package foi.hr.parksmart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SharedMemory;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;

import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferencesMod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);


        sharedPreferencesMod = getSharedPreferences("idDarkMode",0);
        Boolean booleanMod = sharedPreferencesMod.getBoolean("keyDarkMode",false);
        if(booleanMod) {
            Log.d("SharedPrefYes", booleanMod.toString());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            SharedPreferences.Editor editora = sharedPreferencesMod.edit();
            editora.putBoolean("keyDarkMode", true);
            editora.commit();
        }
    }

    
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
    
    SharedPreferences prefs2;
    SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
        {
                String numberForSosCheck =sharedPreferences.getString("keySosNumbera","112");
                //Ovdje statiti regex nad varijablom numberForSosCheck
                String regexObrazac = "^\\+385(1[0-9]{2,10}|(2[0-3]|3[1-5]|4(0|[2-4]|[7-9])|5[1-3]|" +
                        "6([0-1]|[4-5]|9)|7(2|[4-5]))[0-9]{6}|9([1-2]|5|[7-9])[0-9]{7})$";
                if(!Pattern.matches(regexObrazac, numberForSosCheck)){
                    postaviZadanuVrijednostSosBroja();

                    AlertDialog.Builder settingsAlert= new AlertDialog.Builder(SettingsActivity.this);
                    settingsAlert.setMessage("Format unesenog broja nije ispravan !");
                    settingsAlert.show();
                }
            Boolean darkMode = sharedPreferences.getBoolean("keyDarkMode",false);
            if(darkMode){
                Log.d("Unutra",darkMode.toString());
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferences.Editor editor = sharedPreferencesMod.edit();
                editor.putBoolean("keyDarkMode",true);
                editor.commit();
            }
            else{
                Log.d("UnutraNO",darkMode.toString());
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SharedPreferences.Editor editor = sharedPreferencesMod.edit();
                editor.putBoolean("keyDarkMode",false);
                editor.commit();
            }
        }
    };

    private void postaviZadanuVrijednostSosBroja(){
        SharedPreferences sosBrojObjekt = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        SharedPreferences.Editor editor = sosBrojObjekt.edit();
        editor.putString("keySosNumbera", "+385112");
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }
}