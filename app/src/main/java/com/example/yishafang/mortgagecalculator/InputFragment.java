package com.example.yishafang.mortgagecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author yishafang on 9/21/15.
 */
public class InputFragment extends Fragment {

    EditText homeValue;
    EditText downPayment;
    EditText interestRate;
    EditText taxRate;
    Spinner terms;

    Button calculateButton;
    Button resetButton;

    RelativeLayout resultPart;
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

    boolean isInvalidInput;

    boolean isDualPane;

    /**
     * Default constructor required by framework.
     */
    public InputFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, null);

        homeValue = (EditText) v.findViewById(R.id.home_value_field);
        downPayment = (EditText) v.findViewById(R.id.down_payment_field);
        interestRate = (EditText) v.findViewById(R.id.apr_field);
        taxRate = (EditText) v.findViewById(R.id.tax_rate_field);

        // Set spinner value and custom style
        ArrayList<Integer> list = new ArrayList<>();
        list.add(15);
        list.add(20);
        list.add(25);
        list.add(30);
        list.add(40);

        terms = (Spinner) v.findViewById(R.id.terms_spinner);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, list);
        terms.setAdapter(adapter);


        isDualPane = ((MainActivity) getActivity()).mIsDualPane;

        if (!isDualPane) {
            totalTaxPaid = (EditText) v.findViewById(R.id.total_tax_paid_result);
            totalInterestPaid = (EditText) v.findViewById(R.id.total_interest_paid_result);
            monthlyPayment = (EditText) v.findViewById(R.id.monthly_payment_result);
            payOffDate = (EditText) v.findViewById(R.id.pay_off_date_result);

            resultPart = (RelativeLayout) v.findViewById(R.id.result_part);
        }

        resetButton = (Button) v.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeValue.setText("");
                downPayment.setText("");
                interestRate.setText("");
                taxRate.setText("");

                isDualPane = ((MainActivity) getActivity()).mIsDualPane;
                if (isDualPane) {
                    ResultFragment resultFragment = ((MainActivity) getActivity()).mResultFragment;
                    resultFragment.totalTaxPaid.setText("");
                    resultFragment.totalInterestPaid.setText("");
                    resultFragment.monthlyPayment.setText("");
                    resultFragment.payOffDate.setText("");
                } else {
                    resultPart.setVisibility(View.INVISIBLE);
                }

            }
        });

        calculateButton = (Button) v.findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValues();

                isDualPane = ((MainActivity) getActivity()).mIsDualPane;
                if (isDualPane && !isInvalidInput) {
                    ResultFragment resultFragment = ((MainActivity) getActivity()).mResultFragment;
                    resultFragment.displayResult();
                } else {
                    if (!isInvalidInput) {
                        resultPart.setVisibility(View.VISIBLE);

                        totalTaxPaid.setText(new DecimalFormat("#,###.00").format(calculateTax()));
                        totalTaxPaid.setKeyListener(null);

                        totalInterestPaid.setText(new DecimalFormat("#,###.00").format(calculateInterest()));
                        totalInterestPaid.setKeyListener(null);

                        monthlyPayment.setText(new DecimalFormat("#,###.00").format(calculateMonthly()));
                        monthlyPayment.setKeyListener(null);

                        payOffDate.setText(calculatePayOffDate());
                        payOffDate.setKeyListener(null);
                    }
                }

            }
        });

        return v;
    }

    public void getValues() {
        // Home value
        if (homeValue.getText().toString().equals(null) ||
                homeValue.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Home value is empty!", Toast.LENGTH_LONG).show();
            String title = "Home Value is Empty!";
            String message = "Home value shouldn't be empty. Please fill out the Home value.";
            showAlertDialog(title, message);
        } else {
            totalHomeValue = Double.valueOf(homeValue.getText().toString());

            if (totalHomeValue == 0) {
                String title = "Home Value is 0!";
                String message = "Home value shouldn't be 0. Please fill out the Home value.";
                showAlertDialog(title, message);
            } else {
                isInvalidInput = false;
            }
        }

        // Down payment
        double totalDownPayment;
        if (downPayment.getText().toString().equals(null) ||
                downPayment.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Down payment is empty!", Toast.LENGTH_LONG).show();
            totalDownPayment = 0.0;
        } else {
            totalDownPayment = Double.valueOf(downPayment.getText().toString());
        }

        // Convert interest rate to monthly rate
        if (interestRate.getText().toString().equals(null) ||
                interestRate.getText().toString().equals("")) {
            String title = "Interest Rate is Empty!";
            String message = "Interest rate shouldn't be empty. Please fill out the interest rate.";
            showAlertDialog(title, message);
        } else {
            double interest = Double.valueOf(interestRate.getText().toString());

            if (interest == 0) {
                String title = "Interest Rate is 0!";
                String message = "Interest rate shouldn't be 0. Please fill out the interest rate.";
                showAlertDialog(title, message);
            } else {
                isInvalidInput = false;
                i = interest / (100 * 12);
                Log.i("interest_annual", interestRate.getText().toString());
            }
        }

        // Convert terms from years to months
        totalYears = Integer.valueOf(terms.getSelectedItem().toString());
        n = totalYears * 12;
        Log.i("total_years", terms.getSelectedItem().toString());

        // Tax rate
        if (taxRate.getText().toString().equals(null) ||
                taxRate.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Tax rate is empty!", Toast.LENGTH_LONG).show();
        } else {
            double tax = Double.valueOf(taxRate.getText().toString());
            t = tax / 100;
        }

        // Loan amount
        p = totalHomeValue - totalDownPayment;

    }

    public double calculateTax() {
        double totalTax = totalHomeValue * totalYears * t;
        return totalTax;
    }

    public double calculateInterest() {
        // Calculate the term (1+i)^n
        double power = power((1 + i), n);
        double monthlyPay = p * ((i * power) / (power - 1));
        double totalInterest = monthlyPay * n - p;
        return totalInterest;
    }

    public double calculateMonthly() {
        // Calculate the term (1+i)^n
        double power = power((1 + i), n);

        // Calculate the monthly tax
        double monthlyTax = totalHomeValue * t * totalYears / n;

        double monthly_payment = p * ((i * power) / (power - 1)) + monthlyTax;
        return monthly_payment;
    }

    public String calculatePayOffDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; //  month starts at 0

        String payOffMonth =  new DateFormatSymbols().getMonths()[month-2];
        String payOffYear = String.valueOf(year + totalYears);

        return payOffMonth + " " + payOffYear;
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

    private void showAlertDialog(String title, String message) {
        isInvalidInput = true;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // Set title
        alertDialogBuilder.setTitle(title);

        // Set dialog message
        alertDialogBuilder.setMessage(message + " Click OK to close.").
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
