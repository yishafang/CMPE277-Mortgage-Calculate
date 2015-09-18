package com.example.yishafang.mortgagecalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;


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

    // Monthly interest rate
    double i;
    // Loan amount
    double p;
    // Tax rate
    double t;
    // Total months
    int n;
    int totalYears;
    double totalHomeValue;

    boolean isInvalidInterest;

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

                if (!isInvalidInterest) {
                    resultPart.setVisibility(View.VISIBLE);

                    totalTaxPaid.setText(String.valueOf(calculateTax()));
                    totalTaxPaid.setKeyListener(null);

                    totalInterestPaid.setText(String.valueOf(calculateInterest()));
                    totalInterestPaid.setKeyListener(null);

                    monthlyPayment.setText(String.valueOf(calculateMonthly()));
                    monthlyPayment.setKeyListener(null);

                    payOffDate.setText("我还没算出来。。。");
                    payOffDate.setKeyListener(null);
                }

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

    //========= Private Methods =================

    private void getValues() {
        // Home value
        if (homeValue.getText().toString().equals(null) ||
                homeValue.getText().toString().equals("")) {
            Toast.makeText(this, "Home value is empty!", Toast.LENGTH_LONG).show();
        } else {
            totalHomeValue = Double.valueOf(homeValue.getText().toString());
        }

        // Down payment
        double totalDownPayment;
        if (downPayment.getText().toString().equals(null) ||
                downPayment.getText().toString().equals("")) {
            Toast.makeText(this, "Down payment is empty!", Toast.LENGTH_LONG).show();
            totalDownPayment = 0.0;
        } else {
            totalDownPayment = Double.valueOf(downPayment.getText().toString());
        }

        // Convert interest rate to monthly rate
        if (interestRate.getText().toString().equals(null) ||
                interestRate.getText().toString().equals("")) {
            //Toast.makeText(this, "Interest rate is empty!", Toast.LENGTH_LONG).show();
            showAlertDialog();
        } else {
            isInvalidInterest = false;

            double interest = Double.valueOf(interestRate.getText().toString());
            i = interest / (100 * 12);
            //double i = 0.00416667;
            Log.i("interest_annual", interestRate.getText().toString());
        }

        // Convert terms from years to months
        totalYears = Integer.valueOf(terms.getSelectedItem().toString());
        n = totalYears * 12;
        //int n = 180;
        Log.i("total_years", terms.getSelectedItem().toString());

        // Tax rate
        if (taxRate.getText().toString().equals(null) ||
                taxRate.getText().toString().equals("")) {
            Toast.makeText(this, "Tax rate is empty!", Toast.LENGTH_LONG).show();
        } else {
            double tax = Double.valueOf(taxRate.getText().toString());
            t = tax / 100;
        }

        // Loan amount
        p = totalHomeValue - totalDownPayment;
        //double p = 100000;

    }

    private double calculateTax() {
        double totalTax = totalHomeValue * totalYears * t;
        return totalTax;
    }

    private double calculateInterest() {

        return p * (1 + i);
    }

    private double calculateMonthly() {
        // Calculate the term (1+i)^n
        double power = power((1 + i), n);

        // Calculate the monthly tax
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

    private void showAlertDialog() {
        isInvalidInterest = true;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set title
        alertDialogBuilder.setTitle("Interest Rate is Empty!");

        // Set dialog message
        alertDialogBuilder.setMessage("Interest rate shouldn't be empty. Please fill out the interest rate. Click OK to close.").
                setCancelable(false).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // just close the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // Create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show it
        alertDialog.show();
    }
}
