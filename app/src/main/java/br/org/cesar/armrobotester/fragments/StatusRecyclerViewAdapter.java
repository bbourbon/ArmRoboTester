package br.org.cesar.armrobotester.fragments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.org.cesar.armrobotester.MainNaviActivity;
import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.model.ArmSensor;

/**
 * Created by bcb on 20/07/16.
 */
public class StatusRecyclerViewAdapter extends
        RecyclerView.Adapter<StatusRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ArmSensor> mValues;

    public StatusRecyclerViewAdapter(ArrayList<ArmSensor> listArmSensors) {
        mValues = listArmSensors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_sensor_status_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArmSensor sensor = mValues.get(position);
        Log.d(MainNaviActivity.TAG, "ArmSensor: " + sensor.toString());
        Log.d(MainNaviActivity.TAG, "Sensors: " + mValues.size());

        holder.mId.setText("ID: " + sensor.getId());
        holder.mSpeed.setText("Speed: " + sensor.getSpeed() + " RPM");
        holder.mTemp.setText("Temperature: " + sensor.getTemperature() + " ˚C");
        holder.mPos.setText("Position: " + sensor.getPosition());
        holder.mTorque.setText("Torque: " + sensor.getTorque() + " N");

    }

    @Override
    public int getItemCount() {
        return (null != mValues) ? mValues.size() : 0;
    }

    public void updateList(ArrayList<ArmSensor> armSensorArrayList) {
        mValues = armSensorArrayList;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mId;
        public final TextView mSpeed;
        public final TextView mTemp;
        public final TextView mPos;
        public final TextView mTorque;

        public ViewHolder(View itemView) {
            super(itemView);
            mId = (TextView) itemView.findViewById(R.id.text_sensor_id);
            mSpeed = (TextView) itemView.findViewById(R.id.text_sensor_speed);
            mTemp = (TextView) itemView.findViewById(R.id.text_sensor_temp);
            mPos = (TextView) itemView.findViewById(R.id.text_sensor_pos);
            mTorque = (TextView) itemView.findViewById(R.id.text_sensor_torque);
        }
    }
}
