package ca.davidpellegrini.tipcalculators22;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class TipCalculatorFragment extends Fragment
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener, TextWatcher {

    private EditText billAmountEditText;
    private Button incButton, decButton;
    private RadioGroup numPeopleGroup;
    private Spinner numPeopleSpinner;
    private TextView tipAmount, tipPercentTV, totalAmountTV, totalPersonLabel, totalPerPerson;
    private float tipPercent;
    private SharedPreferences prefs;

    public TipCalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(
                getActivity(),
                R.xml.preferences,
                false
        );
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean save = prefs.getBoolean("save_values_pref", false);

        String rounding = prefs.getString("rounding_pref", "default");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tip_calculator, container, false);

        tipPercent = prefs.getFloat("tipPercent", 0.2f);

        tipAmount = view.findViewById(R.id.tipAmountTextView);
        tipPercentTV = view.findViewById(R.id.tipPercent);
        totalAmountTV = view.findViewById(R.id.totalAmountTextView);
        totalPersonLabel = view.findViewById(R.id.totalPerPersonLabel);
        totalPerPerson = view.findViewById(R.id.totalPerPersonTextView);

        billAmountEditText = view.findViewById(R.id.billAmountEditText);
        billAmountEditText.addTextChangedListener(this);
        Log.wtf("output","Hello World!");
        Log.v("BillAmountString", billAmountEditText.getText().toString());

        incButton = view.findViewById(R.id.incButton);
        decButton = view.findViewById(R.id.decButton);
        incButton.setOnClickListener(this);
        decButton.setOnClickListener(this);

        numPeopleGroup = view.findViewById(R.id.numPeopleRadioGroup);
        numPeopleGroup.setOnCheckedChangeListener(this);


        String[] peopleArray = {
                "1 Person",
                "2 People",
                "3 People",
                "4 People"
        };

        ArrayAdapter<String> peopleAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                peopleArray
        );

        numPeopleSpinner = view.findViewById(R.id.spinner);
        numPeopleSpinner.setAdapter(peopleAdapter);
        numPeopleSpinner.setOnItemSelectedListener(this);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        billAmountEditText.setText(prefs.getString("billAmountString", ""));

        tipPercent = prefs.getFloat("tipPercent", 0.2f);
        Log.d("TipPercent", Float.toString(tipPercent));
        tipPercentTV.setText(tipPercent * 100 + "");

        int numPeople = prefs.getInt("numPeople", 2);
        if(numPeople == 1){
            numPeopleGroup.check(R.id.onePersonRB);
        }
        else if(numPeople == 2){
            numPeopleGroup.check(R.id.twoPersonRB);
        }
        else if(numPeople == 3){
            numPeopleGroup.check(R.id.threePersonRB);
        }
        else if(numPeople == 4){
            numPeopleGroup.check(R.id.fourPersonRB);
        }

        updateSceen();
    }

    @Override
    public void onPause() {
        saveData();
        super.onPause();
    }

    @Override
    public void onStop() {
        saveData();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        saveData();
        super.onDestroy();
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

        Log.v("SpinnerSelected", numPeopleSpinner.getSelectedItem().toString());

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        Log.v("GroupChecked", String.valueOf(group.getCheckedRadioButtonId()));
        if(group.getCheckedRadioButtonId() == R.id.onePersonRB){
            totalPersonLabel.setVisibility(View.GONE);
            totalPerPerson.setVisibility(View.GONE);
        }
        else{
            totalPersonLabel.setVisibility(View.VISIBLE);
            totalPerPerson.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(view != null) {
            Log.v("SpinnerItem", ((TextView) view).getText().toString());
            if(((TextView)view).getText().toString().equals("1 Person")) {
                totalPersonLabel.setVisibility(View.GONE);
                totalPerPerson.setVisibility(View.GONE);
            }
            else{
                totalPersonLabel.setVisibility(View.VISIBLE);
                totalPerPerson.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
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

    /**
     * This is a description.
     */
    public void updateSceen(){

        String billAmountString = billAmountEditText.getText().toString();
        float billAmount = billAmountString.isEmpty()
                ? 0
                : Float.parseFloat(billAmountString);
        tipAmount.setText("$"+(billAmount*tipPercent));

        tipPercentTV.setText((tipPercent * 100) + "%");

//        try{
//            billAmount = Float.parseFloat(billAmountString);
//            tipAmount.setText("$"+(billAmount*tipPercent));
//        }
//        catch(NumberFormatException nfe){
//            billAmount = 0;
//        }


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

            editor.putString("billAmountString", billAmountEditText.getText().toString());
            editor.putFloat("tipPercent", tipPercent);
            int numPeople = 0, numID = numPeopleGroup.getCheckedRadioButtonId();
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