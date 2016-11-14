package br.org.cesar.armrobotester.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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
public class TestCaseFragment extends Fragment {

    private static final String TAG = "ARM_ROBOT_TEST";

    private Button mButtonAdd;
    private Button mButtonCancel;

    private EditText mEditTextName;
    private EditText mEditTextId;

    private EditText mEditTextValue1;
    private EditText mEditTextValue2;
    private EditText mEditTextValue3;

    private CheckBox mCheckShoulder1;
    private CheckBox mCheckElbow1;
    private CheckBox mCheckWrist1;

    private CheckBox mCheckShoulder2;
    private CheckBox mCheckElbow2;
    private CheckBox mCheckWrist2;

    private CheckBox mCheckShoulder3;
    private CheckBox mCheckElbow3;
    private CheckBox mCheckWrist3;

    private OnTestCaseListener mOnTestCaseListener;

    private TestCase mTestCase;
    private String mTestSuiteName;

    public TestCaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTestSuiteName = getActivity().getIntent().getStringExtra(TestSuiteFragment.ARG_SUITE);
        if (mTestSuiteName == null && savedInstanceState != null) {
            mTestSuiteName = savedInstanceState.getString(TestSuiteFragment.ARG_SUITE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_test_case, container, false);

        mEditTextName = (EditText) view.findViewById(R.id.edit_test_case_name);
        mEditTextId = (EditText) view.findViewById(R.id.edit_test_case_id);

        mEditTextValue1 = (EditText) view.findViewById(R.id.edit_test_case_motion_1);
        mEditTextValue2 = (EditText) view.findViewById(R.id.edit_test_case_motion_2);
        mEditTextValue3 = (EditText) view.findViewById(R.id.edit_test_case_motion_3);

        mCheckShoulder1 = (CheckBox) view.findViewById(R.id.check_shoulder_1);
        mCheckElbow1 = (CheckBox) view.findViewById(R.id.check_elbow_1);
        mCheckWrist1 = (CheckBox) view.findViewById(R.id.check_wrist_1);

        mCheckShoulder2 = (CheckBox) view.findViewById(R.id.check_shoulder_2);
        mCheckElbow2 = (CheckBox) view.findViewById(R.id.check_elbow_2);
        mCheckWrist2 = (CheckBox) view.findViewById(R.id.check_wrist_2);

        mCheckShoulder3 = (CheckBox) view.findViewById(R.id.check_shoulder_3);
        mCheckElbow3 = (CheckBox) view.findViewById(R.id.check_elbow_3);
        mCheckWrist3 = (CheckBox) view.findViewById(R.id.check_wrist_3);

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

    public void setOnTestCaseListener(OnTestCaseListener onTestCaseListener) {
        mOnTestCaseListener = onTestCaseListener;
    }

    private void onCancelClicked() {
        if (mOnTestCaseListener != null) {
            mOnTestCaseListener.onCancel();
        }
    }

    private void onAddClicked() {
        addTestCase();
        if (mOnTestCaseListener != null) {
            mOnTestCaseListener.onAdd();
        }
    }

    public void addTestCase() {
        TestCase testCase = getTestCase();
        if (mTestCase != null && testCase != null) {
            TestManager.getInstance(getContext()).removeTest(mTestCase);
            TestManager.getInstance(getContext()).addTest(mTestSuiteName, testCase);
            mTestCase = testCase;

        } else if (testCase != null) {
            TestManager.getInstance(getContext()).addTest(mTestSuiteName, testCase);
            reset();
        }
    }

    public void loadTestCase(TestCase testCase) {
        if (testCase == null) return;
        mTestCase = testCase;

        //TODO: Load mTestSuite on views
        mButtonAdd.setText("Update");
    }


    public TestCase getTestCase() {
        TestCase testCase = new TestCase();

        // Name
        try {
            String name = mEditTextName.getText().toString();
            testCase.setName(name);

            // ID
            int id = Integer.parseInt(mEditTextId.getText().toString());
            testCase.setId(id);

            // Motions
            List<Motion> motionList = new ArrayList<>(3);

            // Motion 1
            boolean shoulder1 = mCheckShoulder1.isChecked();
            boolean elbow1 = mCheckElbow1.isChecked();
            boolean wrist1 = mCheckWrist1.isChecked();
            String value1 = mEditTextValue1.getText().toString();
            Motion m1 = getMotion(shoulder1, elbow1, wrist1, value1);
            if (m1 != null) motionList.add(m1);

            // Motion 2
            boolean shoulder2 = mCheckShoulder2.isChecked();
            boolean elbow2 = mCheckElbow2.isChecked();
            boolean wrist2 = mCheckWrist2.isChecked();
            String value2 = mEditTextValue2.getText().toString();
            Motion m2 = getMotion(shoulder2, elbow2, wrist2, value2);
            if (m2 != null) motionList.add(m2);

            // Motion 3
            boolean shoulder3 = mCheckShoulder3.isChecked();
            boolean elbow3 = mCheckShoulder3.isChecked();
            boolean wrist3 = mCheckShoulder3.isChecked();
            String value3 = mEditTextValue3.getText().toString();
            Motion m3 = getMotion(shoulder3, elbow3, wrist3, value3);
            if (m3 != null) motionList.add(m3);

            if (motionList.size() > 0) {
                testCase.setMotionList(motionList);
            } else {
                testCase = null;
            }

        } catch (Exception ex) {
            Log.e(TAG, "Exception: " + ex.getLocalizedMessage());
            testCase = null;
        }

        return testCase;
    }

    public void reset() {

        // Test case identification
        mEditTextName.getEditableText().clear();
        mEditTextId.getEditableText().clear();

        // Test case motions
        mEditTextValue1.getEditableText().clear();
        mEditTextValue2.getEditableText().clear();
        mEditTextValue3.getEditableText().clear();

        // un-check
        mCheckShoulder1.setChecked(false);
        mCheckElbow1.setChecked(false);
        mCheckWrist1.setChecked(false);

        mCheckShoulder2.setChecked(false);
        mCheckElbow2.setChecked(false);
        mCheckWrist2.setChecked(false);

        mCheckShoulder3.setChecked(false);
        mCheckElbow3.setChecked(false);
        mCheckWrist3.setChecked(false);

    }

    public void remove() {
        if (mTestCase != null) {
            //TODO: Add Alert Dialog
            TestManager.getInstance(getContext()).removeTest(mTestCase);
            mTestCase = null;
            reset();
        }
    }

    private Motion getMotion(boolean shoulder, boolean elbow, boolean wrist, String value) {
        Motion motion = null;

        try {
            boolean o = shoulder;
            boolean c = elbow;
            boolean p = wrist;

            int v = 0;
            if (!TextUtils.isEmpty(value)) v = Integer.parseInt(value);

            // TODO: Check limits for the value
            motion = new Motion(o, c, p, v);
            Log.d(TAG, "Motion: " + motion.toString());
        } catch (Exception ex) {
            motion = null;
        }

        return motion;
    }


    /**
     *
     */
    public interface OnTestCaseListener {
        void onCancel();

        void onAdd();
    }
}
