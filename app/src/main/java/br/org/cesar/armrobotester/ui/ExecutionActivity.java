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
import br.org.cesar.armrobotester.fragments.InstructionFragment;

/**
 * Created by bcb on 28/08/16.
 */

public class ExecutionActivity extends AppCompatActivity {

    private static final String TAG_EXECUTION_FRAG = "execution_fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_wizard);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Execution");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextScreen();
            }
        });

    }

    @Override
    protected void onResume() {
        final InstructionFragment instructionFragment = new InstructionFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_placement, instructionFragment, TAG_EXECUTION_FRAG)
                .commit();

        super.onResume();
    }

    private void onNextScreen() {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

}
