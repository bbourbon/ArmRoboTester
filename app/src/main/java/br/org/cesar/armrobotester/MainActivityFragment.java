package br.org.cesar.armrobotester;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    Button mButtonTest;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_main, container, false);

        mButtonTest = (Button) layoutView.findViewById(R.id.button_test);

        mButtonTest.setOnClickListener(this);

        return layoutView;
    }


    private void launchDeviceConnection() {
        Intent testSelectionIntent = new Intent(getContext(), DeviceConnectionActivity.class);
        MainActivityFragment.this.getContext().startActivity(testSelectionIntent);

    }

    @Override
    public void onClick(View v) {
        if (v != null && v.equals(mButtonTest)) {
            launchDeviceConnection();
        }
    }
}
