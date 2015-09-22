package com.example.yishafang.mortgagecalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.text.DecimalFormat;

/**
 * @author yishafang on 9/21/15.
 */
public class ResultFragment extends Fragment {

    RelativeLayout layout;

    EditText totalTaxPaid;
    EditText totalInterestPaid;
    EditText monthlyPayment;
    EditText payOffDate;

    public ResultFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.result_part, null);

        totalTaxPaid = (EditText) v.findViewById(R.id.total_tax_paid_result);
        totalInterestPaid = (EditText) v.findViewById(R.id.total_interest_paid_result);
        monthlyPayment = (EditText) v.findViewById(R.id.monthly_payment_result);
        payOffDate = (EditText) v.findViewById(R.id.pay_off_date_result);

        return v;
    }

    public void displayResult() {
        InputFragment inputFragment = ((MainActivity) getActivity()).mInputFragment;

        totalTaxPaid.setText(new DecimalFormat("#,###.00").format(inputFragment.calculateTax()));
        totalTaxPaid.setKeyListener(null);

        totalInterestPaid.setText(new DecimalFormat("#,###.00").format(inputFragment.calculateInterest()));
        totalInterestPaid.setKeyListener(null);

        monthlyPayment.setText(new DecimalFormat("#,###.00").format(inputFragment.calculateMonthly()));
        monthlyPayment.setKeyListener(null);

        payOffDate.setText(inputFragment.calculatePayOffDate());
        payOffDate.setKeyListener(null);

//        Bundle args = getArguments();
//        if (args != null) {
//
//            totalTaxPaid.setText(String.valueOf(args.get("KEY_TAX")));
//            totalInterestPaid.setText(String.valueOf(args.get("KEY_INTEREST")));
//            monthlyPayment.setText(String.valueOf(args.get("KEY_MONTHLY")));
//            payOffDate.setText(String.valueOf(args.get("KEY_DATE")));
//        }
    }
}
