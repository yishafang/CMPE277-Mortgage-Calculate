package com.example.yishafang.mortgagecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;


public class MainActivity extends Activity {

    EditText homeValue;
    EditText downPayment;
    EditText interestRate;
    EditText taxRate;
    Spinner terms;

    Button calculateButton;

    EditText totalTaxPaid;
    EditText totalInterestPaid;
    EditText monthlyPayment;
    EditText payOffDate;

    double i;
    double p;
    double t;
    int n;
    int totalYears;
    double totalHomeValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeValue = (EditText) findViewById(R.id.home_value_field);
        downPayment = (EditText) findViewById(R.id.down_payment_field);
        interestRate = (EditText) findViewById(R.id.apr_field);
        taxRate = (EditText) findViewById(R.id.tax_rate_field);
        terms = (Spinner) findViewById(R.id.terms_spinner);

        totalTaxPaid = (EditText) findViewById(R.id.total_tax_paid_result);
        totalInterestPaid = (EditText) findViewById(R.id.total_interest_paid_result);
        monthlyPayment = (EditText) findViewById(R.id.monthly_payment_result);
        payOffDate = (EditText) findViewById(R.id.pay_off_date_result);

        final RelativeLayout resultPart = (RelativeLayout) findViewById(R.id.result_part);

        calculateButton = (Button) findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValues();

                resultPart.setVisibility(View.VISIBLE);

                totalTaxPaid.setText(String.valueOf(calculateTax()));
                totalTaxPaid.setKeyListener(null);

                totalInterestPaid.setText("我还没算出来。。。");
                totalInterestPaid.setKeyListener(null);

                monthlyPayment.setText(String.valueOf(calculateMonthly()));
                monthlyPayment.setKeyListener(null);



            }
        });

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

    private void getValues() {
        // Convert interest rate to monthly rate
        double interest = Double.valueOf(interestRate.getText().toString());
        i = interest / (100 * 12);
        //double i = 0.00416667;
        Log.i("interest_annual", interestRate.getText().toString());

        // Convert terms from years to months
        totalYears = Integer.valueOf(terms.getSelectedItem().toString());
        n = totalYears * 12;
        //int n = 180;
        Log.i("total_years", terms.getSelectedItem().toString());

        // Home value
        totalHomeValue = Double.valueOf(homeValue.getText().toString());

        // Down payment
        double totalDownPayment = Double.valueOf(downPayment.getText().toString());

        // Tax rate
        double tax = Double.valueOf(taxRate.getText().toString());
        t = tax / 100;

        // Loan amount
        p = totalHomeValue - totalDownPayment;
        //double p = 100000;

    }

    private double calculateTax() {
        double totalTax = totalHomeValue * totalYears * t;
        return totalTax;
    }

    private double calculateInterest() {

        return 0.0;
    }

    private double calculateMonthly() {
        // Calculate the term (1+i)^n
        double power = power((1 + i), n);

        double monthlyTax = totalHomeValue * t * totalYears / n;

        double monthly_payment = p * ((i * power) / (power - 1)) + monthlyTax;
        return monthly_payment;
    }

    // calculate the x^n
    private double power(double x, int n) {
        if(n == 0)  //terminate condition
            return 1.0;

        double half = power(x, n/2);

        if(n%2 == 0)
            return half * half;
        else if(n > 0)
            return half * half * x;
        else
            return half * half / x;
    }
}
