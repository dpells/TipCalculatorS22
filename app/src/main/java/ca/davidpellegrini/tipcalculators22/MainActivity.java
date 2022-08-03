package ca.davidpellegrini.tipcalculators22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SettingsFragment settingsFragment = (SettingsFragment)getFragmentManager()
                .findFragmentById(R.id.settings_fragment);
        if(settingsFragment == null)
            getMenuInflater().inflate(R.menu.options_menu, menu);
        else
            getMenuInflater().inflate(R.menu.options_menu_twopane, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.menu_about){
            Toast.makeText(
                    this, "TipCalc For JAV1001", Toast.LENGTH_SHORT
            ).show();
            return true;
        }
        else if(itemId == R.id.menu_settings){
            startActivity(new Intent(
                    getApplicationContext(),
                    SettingsActivity.class
            ));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}