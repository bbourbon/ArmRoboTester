package br.org.cesar.armrobotester.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.fragments.InstructionFragment;

/**
 * Created by bcb on 28/08/16.
 */

public class ReportActivity extends AppCompatActivity {
    private static final String TAG_REPORT_FRAG = "report_fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Report");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setText("Save");
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveReport();
            }
        });

    }

    @Override
    protected void onResume() {
        final InstructionFragment instructionFragment = new InstructionFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_placement, instructionFragment, TAG_REPORT_FRAG)
                .commit();

        super.onResume();
    }

    private void onSaveReport() {
        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
    }


}
