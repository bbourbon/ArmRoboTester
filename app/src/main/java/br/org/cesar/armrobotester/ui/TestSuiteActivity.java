package br.org.cesar.armrobotester.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import br.org.cesar.armrobotester.R;

/**
 * Created by bcb on 28/08/16.
 */

public class TestSuiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_wizard);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Test Suite");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextScreen();
            }
        });

        Button buttonAction = (Button) findViewById(R.id.button_action);
        buttonAction.setText("Add Test Case");
        buttonAction.setVisibility(View.VISIBLE);
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionButtonClicked();
            }
        });

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

}
