package br.org.cesar.armrobotester.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import br.org.cesar.armrobotester.R;

/**
 * Created by bcb on 29/08/16.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_wizard);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Settings");

        Button buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setVisibility(View.GONE);
    }
}
