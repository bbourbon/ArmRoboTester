package br.org.cesar.armrobotester;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import br.org.cesar.armrobotester.fragments.SensorActivityFragment;

public class SensorActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.setRecordFloatButton();
    }

    private void record() {

        Fragment fragmentSensor = this.getSupportFragmentManager().findFragmentById(R.id.fragment_sensor);

        if (null != fragmentSensor && fragmentSensor instanceof SensorActivityFragment) {
            SensorActivityFragment sensorActivityFragment = (SensorActivityFragment) fragmentSensor;

            sensorActivityFragment.initSensors();

            sensorActivityFragment.record();

            String reportName = sensorActivityFragment.getFileName();
            Toast.makeText(this, reportName, Toast.LENGTH_SHORT).show();
        }

        this.setStopFloatButton();
    }

    private void stopRecord() {

        Fragment fragmentSensor = this.getSupportFragmentManager().findFragmentById(R.id.fragment_sensor);

        if (null != fragmentSensor && fragmentSensor instanceof SensorActivityFragment) {
            SensorActivityFragment sensorActivityFragment = (SensorActivityFragment) fragmentSensor;

            sensorActivityFragment.stopRecord();

        }

        this.setRecordFloatButton();
    }

    private void setStopFloatButton() {
        if (null != this.fab) {
            fab.setImageResource(R.drawable.ic_float_stop);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SensorActivity.this.stopRecord();
                    Snackbar.make(view, "Stopping record Sensors...", Snackbar.LENGTH_LONG)
                            .show();

                }
            });
        }

    }

    private void setRecordFloatButton() {

        if (null != this.fab) {
            fab.setImageResource(R.drawable.ic_float_record);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SensorActivity.this.record();
                    Snackbar.make(view, "Recording Sensors...", Snackbar.LENGTH_LONG)
//                            .setAction("Action", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    SensorActivity.this.record();
//                                }
//                            })
                            .show();

                }
            });
        }

    }

}
