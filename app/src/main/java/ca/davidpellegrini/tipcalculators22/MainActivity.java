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

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, TextWatcher {

    private float tipPercent;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(
                this,
                R.xml.preferences,
                false
        );
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean save = prefs.getBoolean("save_values_pref", false);

        String rounding = prefs.getString("rounding_pref", "default");
        Toast.makeText(this, rounding, Toast.LENGTH_SHORT).show();



        //easterEgg.setOnClickListener(this);

        EditText billAmountEditText = findViewById(R.id.billAmountEditText);
        billAmountEditText.addTextChangedListener(this);
        Log.wtf("output","Hello World!");
        Log.v("BillAmountString", billAmountEditText.getText().toString());

        Button incButton = findViewById(R.id.incButton);
        Button decButton = findViewById(R.id.decButton);
        incButton.setOnClickListener(this);
        decButton.setOnClickListener(this);

        RadioGroup numPeopleGroup = findViewById(R.id.numPeopleRadioGroup);
        numPeopleGroup.setOnCheckedChangeListener(this);


        String[] peopleArray = {
                "1 Person",
                "2 People",
                "3 People",
                "4 People"
        };

        ArrayAdapter<String> peopleAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                peopleArray
        );

        Spinner numPeopleSpinner = findViewById(R.id.spinner);
        numPeopleSpinner.setAdapter(peopleAdapter);
        numPeopleSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    protected void onResume(){
        super.onResume();

        EditText billAmountET = findViewById(R.id.billAmountEditText);
        billAmountET.setText(prefs.getString("billAmountString", ""));

        tipPercent = prefs.getFloat("tipPercent", 0.2f);
        Log.d("TipPercent", Float.toString(tipPercent));
        TextView tipPercentTV = findViewById(R.id.tipPercent);
        tipPercentTV.setText(tipPercent * 100 + "");

        int numPeople = prefs.getInt("numPeople", 2);
        RadioGroup numPeopleRG = findViewById(R.id.numPeopleRadioGroup);
        if(numPeople == 1){
            numPeopleRG.check(R.id.onePersonRB);
        }
        else if(numPeople == 2){
            numPeopleRG.check(R.id.twoPersonRB);
        }
        else if(numPeople == 3){
            numPeopleRG.check(R.id.threePersonRB);
        }
        else if(numPeople == 4){
            numPeopleRG.check(R.id.fourPersonRB);
        }

        updateSceen();
    }

    protected void onPause(){
        saveData();
        Log.d("Pause", "App is paused");
        super.onPause();
    }

    protected void onStop(){
        saveData();
        super.onStop();
    }
    protected void onDestroy(){
        saveData();
        super.onDestroy();
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

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == R.id.decButton){
            tipPercent -= 0.05f;
        }
        else if(id == R.id.incButton){
            tipPercent += 0.05f;
        }

        updateSceen();

        Spinner sp = findViewById(R.id.spinner);
        Log.v("SpinnerSelected", sp.getSelectedItem().toString());

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.v("CheckedID", String.valueOf(checkedId));
        RadioButton rb = findViewById(checkedId);
        Log.v("CheckedText", rb.getText().toString());

        Log.v("GroupChecked", String.valueOf(group.getCheckedRadioButtonId()));
        if(group.getCheckedRadioButtonId() == R.id.onePersonRB){
            findViewById(R.id.textView10).setVisibility(View.GONE);
            findViewById(R.id.totalPerPersonTextView).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.textView10).setVisibility(View.VISIBLE);
            findViewById(R.id.totalPerPersonTextView).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(view != null) {
            Log.v("SpinnerItem", ((TextView) view).getText().toString());
            if(((TextView)view).getText().toString().equals("1 Person")) {
                findViewById(R.id.textView10).setVisibility(View.GONE);
                findViewById(R.id.totalPerPersonTextView).setVisibility(View.GONE);
            }
            else{
                findViewById(R.id.textView10).setVisibility(View.VISIBLE);
                findViewById(R.id.totalPerPersonTextView).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // don't care
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateSceen();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // don't care
    }



    public void updateSceen(){
        TextView tipAmount = findViewById(R.id.tipAmountTextView);
        EditText billAmountET = findViewById(R.id.billAmountEditText);
        String billAmountString = billAmountET.getText().toString();
        float billAmount = billAmountString.isEmpty()
                ? 0
                : Float.parseFloat(billAmountString);
        tipAmount.setText("$"+(billAmount*tipPercent));

        TextView tipPercentTV = findViewById(R.id.tipPercent);
        tipPercentTV.setText((tipPercent * 100) + "%");

//        try{
//            billAmount = Float.parseFloat(billAmountString);
//            tipAmount.setText("$"+(billAmount*tipPercent));
//        }
//        catch(NumberFormatException nfe){
//            billAmount = 0;
//        }

        TextView totalAmountTV = findViewById(R.id.totalAmountTextView);
        float totalAmount = billAmount + (billAmount*tipPercent);
        totalAmountTV.setText("$" + totalAmount);

        saveData();
    }

    private void saveData(){
        SharedPreferences.Editor editor = prefs.edit();

        if(prefs.getBoolean("save_values_pref", false)){
            // save EditText    string
            // tipPercent       float
            // numPeople        int


            // name: FirstLast, phone: 1234567890, email: david@cc.ca; name: SecontLast, phone: 0987654321, email: david@personal.ca

            EditText billAmountET = findViewById(R.id.billAmountEditText);
            RadioGroup numPeopleRB = findViewById(R.id.numPeopleRadioGroup);
            editor.putString("billAmountString", billAmountET.getText().toString());
            editor.putFloat("tipPercent", tipPercent);
            int numPeople = 0, numID = numPeopleRB.getCheckedRadioButtonId();
            if(numID == R.id.onePersonRB){
                numPeople = 1;
            }
            else if(numID == R.id.twoPersonRB){
                numPeople = 2;
            }
            else if(numID == R.id.threePersonRB){
                numPeople = 3;
            }
            else if(numID == R.id.fourPersonRB){
                numPeople = 4;
            }
            editor.putInt("numPeople", numPeople);
        }
        else{
            editor.clear();
            editor.putBoolean("save_values_pref", false);
        }

        editor.apply();
    }
}