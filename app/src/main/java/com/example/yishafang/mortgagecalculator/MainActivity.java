package com.example.yishafang.mortgagecalculator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;


/**
 * Main activity.
 * The configurations are divided in two large groups: single-pane layouts and dual-pane layouts.
 *
 * In single-pane mode, this activity shows input fields using a {@link InputFragment}.
 *
 * In dual-pane mode, this activity shows a {@link InputFragment} on the left side and an
 * {@link ResultFragment} on the right side. When the user clicks calculate button on the left, the
 * corresponding results are shown on the right.
 *
 */

public class MainActivity extends FragmentActivity {

    // Whether or not we are in dual-pane mode
    boolean mIsDualPane = false;

    // The fragment where the input fields are displayed
    InputFragment mInputFragment;

    // The fragment where the result is displayed (null if absent)
    ResultFragment mResultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // Determine whether we are in single-pane or dual-pane mode by testing the visibility
        // of the results fields view.
        View resultFieldsView = findViewById(R.id.result_fields);
        mIsDualPane = resultFieldsView != null && resultFieldsView.getVisibility() == View.VISIBLE;

        // find our fragments
        mInputFragment = (InputFragment) getSupportFragmentManager().findFragmentById(R.id.input_fields);
        mResultFragment = (ResultFragment) getSupportFragmentManager().findFragmentById(R.id.result_fields);

    }
}
