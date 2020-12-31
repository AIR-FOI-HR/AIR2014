package foi.hr.parksmart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SharedMemory;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;

import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {
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
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
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
                    //Ova tri reda ispod ako nesto ne valja. Ja cu doraditi taj alert s porukom i svime,
                    // ti samo napravi da se provjerava

                    //Ako je upisani broj u pogresnom formatu, vrijednost broja se postavlja na +385112.
                    //Broj se uspješno postavlja u MainScreenu, ali u TextBoxu za unos sosBroja postavlja se
                    //tek nakon što se izađe iz postavki i ponovno u njih uđe.
                    postaviZadanuVrijednostSosBroja();

                    AlertDialog.Builder settingsAlert= new AlertDialog.Builder(SettingsActivity.this);
                    settingsAlert.setMessage("Format unesenog broja nije ispravan !");
                    settingsAlert.show();
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