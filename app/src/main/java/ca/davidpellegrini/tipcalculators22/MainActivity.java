package ca.davidpellegrini.tipcalculators22;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, TextWatcher {

    private float tipPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tipPercent = 0.2f;
        TextView tipPercentTV = findViewById(R.id.tipPercent);
        tipPercentTV.setText(tipPercent * 100 + "");

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
    public void onClick(View v) {

        int id = v.getId();
        if(id == R.id.decButton){
            tipPercent -= 0.05f;
        }
        else if(id == R.id.incButton){
            tipPercent += 0.05f;
        }

        updateSceen();

        TextView tipPercentTV = findViewById(R.id.tipPercent);
        tipPercentTV.setText(Math.round(tipPercent * 100) + "");

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

        TextView totalAmountTV = findViewById(R.id.totalAmountTextView);
        float totalAmount = billAmount + (billAmount*tipPercent);
        totalAmountTV.setText("$" + totalAmount);
    }
}