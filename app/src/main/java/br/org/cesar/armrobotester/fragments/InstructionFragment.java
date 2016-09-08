package br.org.cesar.armrobotester.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.org.cesar.armrobotester.R;

/**
 * Created by bcb on 30/08/16.
 */
public class InstructionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instruction, container, false);

        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
