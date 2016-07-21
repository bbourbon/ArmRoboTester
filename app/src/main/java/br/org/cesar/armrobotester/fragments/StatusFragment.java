package br.org.cesar.armrobotester.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.model.ArmSensor;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment implements View.OnClickListener {

    private static final long REFRESH_INTERVAL_MS = 1000;
    protected ArrayList<ArmSensor> mListArmSensors;
    protected StatusRecyclerViewAdapter mStatusRecyclerViewAdapter;
    protected Handler mHandler;

    public StatusFragment() {
        // Required empty public constructor

        mListArmSensors = new ArrayList<>();
        mHandler = new Handler();
        loadSensorStatus();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mStatusRecyclerViewAdapter =
                    new StatusRecyclerViewAdapter(mListArmSensors);
            recyclerView.setAdapter(mStatusRecyclerViewAdapter);

        }
        autoRefresh(REFRESH_INTERVAL_MS);
        return view;
    }

    protected void autoRefresh(final long interval) {

        if (null == this.getContext()) return;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        boolean isAutoRefreshStatusEnabled = sp.getBoolean("auto_refresh_status", false);

        if (null != mHandler && isAutoRefreshStatusEnabled) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    StatusFragment.this.autoRefresh(interval);
                }
            }, interval);
        }

        refresh();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loadSensorStatus();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void refresh() {
        loadSensorStatus();
    }

    protected void loadSensorStatus() {
        // TODO: Request Sensor Data via Bluetooth to Arduino Controller
        final Random r = new Random();

        ArmSensor armSensor1 = new ArmSensor(1, 0, r.nextInt(75), 39, 10);
        ArmSensor armSensor2 = new ArmSensor(2, 0, r.nextInt(75), 10, 5);
        ArmSensor armSensor3 = new ArmSensor(3, 0, r.nextInt(75), 39, 8);
        ArmSensor armSensor4 = new ArmSensor(4, 60, r.nextInt(75), 39, 30);

        if (null != mListArmSensors) {
            mListArmSensors.clear();

            mListArmSensors.add(armSensor1);
            mListArmSensors.add(armSensor2);
            mListArmSensors.add(armSensor3);
            mListArmSensors.add(armSensor4);

            if (null != mStatusRecyclerViewAdapter) {
                mStatusRecyclerViewAdapter.updateList(mListArmSensors);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof FloatingActionButton) {
            refresh();
        }
    }
}
