package br.org.cesar.armrobotester;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import br.org.cesar.armrobotester.ui.SettingsActivity;
import br.org.cesar.armrobotester.ui.TestSuiteActivity;

/**
 * Created by bcb on 28/08/16.
 */

public class WizardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_wizard);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Begin");

        Button buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextScreen();
            }
        });
    }

    private void onNextScreen() {
        Intent nextScreen = new Intent(WizardActivity.this, TestSuiteActivity.class);
        startActivity(nextScreen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wizard, menu);
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
            onSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
