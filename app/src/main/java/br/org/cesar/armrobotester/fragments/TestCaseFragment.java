package br.org.cesar.armrobotester.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.content.TestManager;
import br.org.cesar.armrobotester.model.Motion;
import br.org.cesar.armrobotester.model.TestCase;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TestCaseFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "ARM_ROBOT_TEST";
    ArrayAdapter<CharSequence> mAdapter1;
    ArrayAdapter<CharSequence> mAdapter2;
    ArrayAdapter<CharSequence> mAdapter3;
    private Spinner mSpinnerMotion1;
    private Spinner mSpinnerMotion2;
    private Spinner mSpinnerMotion3;
    private Button mButtonAdd;
    private Button mButtonCancel;
    private EditText mEditTextName;
    private EditText mEditTextId;

    public TestCaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_test_case, container, false);

        mEditTextName = (EditText) view.findViewById(R.id.edit_test_case_name);
        mEditTextId = (EditText) view.findViewById(R.id.edit_test_case_id);

        mSpinnerMotion1 = (Spinner) view.findViewById(R.id.spinner_test_case_motion_1);
        mSpinnerMotion2 = (Spinner) view.findViewById(R.id.spinner_test_case_motion_2);
        mSpinnerMotion3 = (Spinner) view.findViewById(R.id.spinner_test_case_motion_3);

        initSpinners();

        mButtonAdd = (Button) view.findViewById(R.id.button_test_case_add);
        mButtonCancel = (Button) view.findViewById(R.id.button_test_case_cancel);

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestCaseFragment.this.onAddClicked();
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestCaseFragment.this.onCancelClicked();
            }
        });


        return view;
    }

    private void initSpinners() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        mAdapter1 = ArrayAdapter.createFromResource(this.getContext(),
                R.array.list_test, android.R.layout.simple_spinner_item);
        mAdapter2 = ArrayAdapter.createFromResource(this.getContext(),
                R.array.list_test, android.R.layout.simple_spinner_item);
        mAdapter3 = ArrayAdapter.createFromResource(this.getContext(),
                R.array.list_test, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        mAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mSpinnerMotion1.setAdapter(mAdapter1);
        mSpinnerMotion2.setAdapter(mAdapter2);
        mSpinnerMotion3.setAdapter(mAdapter3);

        mSpinnerMotion1.setOnItemSelectedListener(this);
        mSpinnerMotion2.setOnItemSelectedListener(this);
        mSpinnerMotion3.setOnItemSelectedListener(this);
    }

    private void onCancelClicked() {
        //TODO: Clean fields, back to previous fragment
        this.getFragmentManager().popBackStack();
    }

    private void onAddClicked() {
        //TODO: Read fields info, create Test Case, add to TestManager, Clean fields, Toast success
        List<Motion> motionList = new ArrayList<>(3);
        String name = mEditTextName.getText().toString();
        String id = mEditTextId.getText().toString();

        try {
            CharSequence motion1 = (CharSequence) mSpinnerMotion1.getSelectedItem();
            if (!TextUtils.isEmpty(motion1)) {
                motionList.add(new Motion(Motion.Type.Position, Motion.MIN_POSITION_VALUE));
            }
            CharSequence motion2 = (CharSequence) mSpinnerMotion1.getSelectedItem();
            if (!TextUtils.isEmpty(motion2)) {
                motionList.add(new Motion(Motion.Type.Position, Motion.MIN_POSITION_VALUE));
            }
            CharSequence motion3 = (CharSequence) mSpinnerMotion1.getSelectedItem();
            if (!TextUtils.isEmpty(motion3)) {
                motionList.add(new Motion(Motion.Type.Rotation, Motion.MAX_ROTATION_VALUE));
            }
        } catch (NullPointerException | ClassCastException ex) {
            Log.d(TAG, "Fail to get motions: " + ex.getLocalizedMessage());
        }

        //Create Test Case
        TestCase tc = new TestCase();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(id)
                && TextUtils.isDigitsOnly(id) && motionList.size() > 0) {

            tc.setId(Integer.parseInt(id));
            tc.setName(name);
            tc.setMotionList(motionList);

            // Add to Test Manager
            TestManager.getInstance(getContext()).addTest(tc);

            // Clean Fields
            mEditTextName.setText("");
            mEditTextId.setText("");

            mSpinnerMotion1.setSelection(0);
            mSpinnerMotion2.setSelection(0);
            mSpinnerMotion3.setSelection(0);

            // Notify User Success
            Toast.makeText(getContext(), "TC" + id + ": " + name + " added!", Toast.LENGTH_SHORT)
                    .show();

        } else {
            // Notify User Failed
            Toast.makeText(getContext(), "One or more fields are incorrect!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void createTestCase() {
        TestCase testCase = null;
        String name = "";
        int id = -1;
        List<Motion> motions = new ArrayList<>(3);

        testCase = new TestCase();
        testCase.setName(name);
        testCase.setId(id);
        testCase.setMotionList(motions);

        TestManager.getInstance(getContext()).addTest(testCase);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mSpinnerMotion1 && mAdapter1 != null) {
            CharSequence motion1 = mAdapter1.getItem(position);
            Log.d(TAG, "Selected 1: " + motion1);
        } else if (parent == mSpinnerMotion2 && mAdapter2 != null) {
            CharSequence motion2 = mAdapter2.getItem(position);
            Log.d(TAG, "Selected 2: " + motion2);
        } else if (parent == mSpinnerMotion3 && mAdapter3 != null) {
            CharSequence motion3 = mAdapter3.getItem(position);
            Log.d(TAG, "Selected 3: " + motion3);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent == mSpinnerMotion1) {
            Log.d(TAG, "Nothing Selected 1. ");
        } else if (parent == mSpinnerMotion2) {
            Log.d(TAG, "Nothing Selected 2. ");
        } else if (parent == mSpinnerMotion3) {
            Log.d(TAG, "Nothing Selected 3. ");
        }
    }
}
