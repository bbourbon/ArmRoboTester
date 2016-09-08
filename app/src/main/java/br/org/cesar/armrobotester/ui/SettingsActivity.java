package br.org.cesar.armrobotester.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.fragments.SettingsFragment;

/**
 * Created by bcb on 29/08/16.
 */
public class SettingsActivity extends AppCompatActivity {
    private static final String TAG_SETTINGS_FRAG = "settings_fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_wizard);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        final SettingsFragment settingsFragment = new SettingsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_placement, settingsFragment, TAG_SETTINGS_FRAG)
                .commit();

        super.onResume();
    }

}
