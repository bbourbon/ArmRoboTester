package br.org.cesar.armrobotester.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.fragments.TestSuiteFragment;

/**
 * Created by bcb on 28/08/16.
 */

public class TestSuiteActivity extends AppCompatActivity implements
        TestSuiteFragment.OnListFragmentInteractionListener {

    private static final String TAG_TEST_SUITE_FRAG = "test_suite_fragment";
    private Button mButtonNext;
    private Button mButtonAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_wizard);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Test Suite");
        actionBar.setDisplayHomeAsUpEnabled(true);
        mButtonNext = (Button) findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextScreen();
            }
        });

        mButtonAction = (Button) findViewById(R.id.button_action);
        mButtonAction.setText("Add Test Case");
        mButtonAction.setVisibility(View.VISIBLE);
        mButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionButtonClicked();
            }
        });
    }

    @Override
    protected void onResume() {
        final TestSuiteFragment testSuiteFragment = new TestSuiteFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_placement, testSuiteFragment, TAG_TEST_SUITE_FRAG)
                .commit();

        super.onResume();
    }

    private void onActionButtonClicked() {
        Intent intent = new Intent(this, TestCaseActivity.class);
        intent.setAction(TestCaseActivity.ACTION_NEW_TEST);
        startActivity(intent);
    }

    private void onNextScreen() {
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onListFragmentItemCountChanged(int count) {
        mButtonNext.setEnabled(count > 0);
    }
}
