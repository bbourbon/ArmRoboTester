package br.org.cesar.armrobotester.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.model.SensorData;

/**
 * Created by bcb on 03/08/16.
 */
public class SensorDataRecyclerViewAdapter extends
        RecyclerView.Adapter<SensorDataRecyclerViewAdapter.SensorDataViewHolder> {

    ArrayList<SensorData> mSensorDataList;

    public SensorDataRecyclerViewAdapter() {
        mSensorDataList = new ArrayList<>();
    }

    @Override
    public SensorDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_sensor_status_item, parent, false);
        return new SensorDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SensorDataViewHolder holder, int position) {
        SensorData sensorData = mSensorDataList.get(position);
        holder.mText = sensorData.toString();
    }

    @Override
    public int getItemCount() {
        return (mSensorDataList != null) ? mSensorDataList.size() : 0;
    }

    public class SensorDataViewHolder extends RecyclerView.ViewHolder {
        public String mText;

        public SensorDataViewHolder(View itemView) {
            super(itemView);
            mText = "";
        }
    }
}
