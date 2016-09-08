package br.org.cesar.armrobotester.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
public class TestCaseFragment extends Fragment {

    private static final String TAG = "ARM_ROBOT_TEST";
    private Button mButtonAdd;
    private Button mButtonCancel;
    private EditText mEditTextName;
    private EditText mEditTextId;
    private OnTestCaseListener mOnTestCaseListener;

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

        // Specify the layout to use when the list of choices appears

        // Apply the adapter to the spinner

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

    private void addTestCase() {
        // Read fields info, create Test Case, add to TestManager, Clean fields, Toast success
        List<Motion> motionList = new ArrayList<>(3);
        String name = mEditTextName.getText().toString();
        String id = mEditTextId.getText().toString();


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


    public interface OnTestCaseListener {
        void onCancel();

        void onAdd();
    }
}
