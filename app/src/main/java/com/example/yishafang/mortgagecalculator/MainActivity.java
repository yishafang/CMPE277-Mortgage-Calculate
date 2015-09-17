package com.example.yishafang.mortgagecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends Activity {

    EditText homeValue;
    EditText downPayment;
    EditText interestRate;
    EditText taxRate;
    Spinner terms;

    Button calculateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeValue = (EditText) findViewById(R.id.home_value_field);
        downPayment = (EditText) findViewById(R.id.down_payment_field);
        interestRate = (EditText) findViewById(R.id.apr_field);
        taxRate = (EditText) findViewById(R.id.tax_rate_field);

        terms = (Spinner) findViewById(R.id.terms_spinner);

        calculateButton = (Button) findViewById(R.id.calculate_button);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
