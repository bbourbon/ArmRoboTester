package br.org.cesar.armrobotester.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.armrobotester.R;

/**
 * Created by bcb on 14/11/16.
 */
public class ResultFragment extends Fragment implements View.OnClickListener {


    private ExpandableListView lv;

    public ResultFragment () {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootview = inflater.inflate(R.layout.result_fragment_layout, container, false );
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = (ExpandableListView) view.findViewById(R.id.expListView);
        SuiteExpListAdapter adapter = new SuiteExpListAdapter(getContext(), getMockUpResult());
        adapter.setSimpleMode(getResultMode());
        lv.setAdapter(adapter);
        lv.setGroupIndicator(null);
    }

    @Override
    public void onClick(View view) {
        //TODO: Toggle between simple to full report mode;
    }

    private boolean getResultMode() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        boolean isSimpleModeReport = sp.getBoolean("report_simple_mode", false);
        return isSimpleModeReport;
    }


    //-----
    private ArrayList<MyTestSuiteResult> getMockUpResult() {

        ArrayList<MyTestSuiteResult> testSuites = new ArrayList<>(8);

        // --- Test Suite ID_001 ---
        MyTestSuiteResult ts1 = new MyTestSuiteResult("ID_001");

        MyTestCaseResult tc1 = new MyTestCaseResult("Test 1");
        ts1.testCases.add(tc1);
        MyTestCaseResult tc2 = new MyTestCaseResult("Test 2");
        ts1.testCases.add(tc2);
        MyTestCaseResult tc3 = new MyTestCaseResult("Test 3");
        ts1.testCases.add(tc3);
        MyTestCaseResult tc4 = new MyTestCaseResult("Test 4");
        ts1.testCases.add(tc4);
        MyTestCaseResult tc5 = new MyTestCaseResult("Test 5");
        ts1.testCases.add(tc5);
        MyTestCaseResult tc6 = new MyTestCaseResult("Test 6");
        ts1.testCases.add(tc6);
        MyTestCaseResult tc7 = new MyTestCaseResult("Test 7");
        ts1.testCases.add(tc7);

        testSuites.add(ts1);

        // --- Test Suite ID_002 ---
        MyTestSuiteResult ts2 = new MyTestSuiteResult("ID_002");

        MyTestCaseResult tc8 = new MyTestCaseResult("Test 8");
        ts2.testCases.add(tc8);
        MyTestCaseResult tc9 = new MyTestCaseResult("Test 9");
        ts2.testCases.add(tc9);
        MyTestCaseResult tc10 = new MyTestCaseResult("Test 10");
        ts2.testCases.add(tc10);
        MyTestCaseResult tc11 = new MyTestCaseResult("Test 11");
        ts2.testCases.add(tc11);
        MyTestCaseResult tc12 = new MyTestCaseResult("Test 12");
        ts2.testCases.add(tc12);
        MyTestCaseResult tc13 = new MyTestCaseResult("Test 13");
        ts2.testCases.add(tc13);
        MyTestCaseResult tc14 = new MyTestCaseResult("Test 14");
        ts2.testCases.add(tc14);

        testSuites.add(ts2);

        // --- Test Suite ID_003 ---
        MyTestSuiteResult ts3 = new MyTestSuiteResult("ID_003");
        testSuites.add(ts3);

        // --- Test Suite ID_004 ---
        MyTestSuiteResult ts4 = new MyTestSuiteResult("ID_004");
        testSuites.add(ts4);

        // --- Test Suite ID_005 ---
        MyTestSuiteResult ts5 = new MyTestSuiteResult("ID_005");
        testSuites.add(ts5);

        // --- Test Suite ID_006 ---
        MyTestSuiteResult ts6 = new MyTestSuiteResult("ID_006");
        testSuites.add(ts6);

        // --- Test Suite ID_007 ---
        MyTestSuiteResult ts7 = new MyTestSuiteResult("ID_007");
        testSuites.add(ts7);

        // --- Test Suite ID_008 ---
        MyTestSuiteResult ts8 = new MyTestSuiteResult("ID_008");
        testSuites.add(ts8);


        return testSuites;

    }

    //-----

    public class MyTestSuiteResult {

        String name;
        List<MyTestCaseResult> testCases;
        public MyTestSuiteResult (String name) {
            this.name = name;
            this.testCases = new ArrayList<>(10);
        }
    }

    public class MyTestCaseResult {

        String name;
        String deviceModel;
        String armAngle;
        String deviceAngle;

        ArrayList<String> results;

        public MyTestCaseResult (String name) {
            this.name = name;
            this.deviceModel = "Moto G2";
            this.armAngle = "45";
            this.deviceAngle = "45";

            results = new ArrayList<>(7);
        }

        public ArrayList<String> getSimpleResult() {
            results.clear();

            results.add("Arm: " + this.armAngle + "˚");
            results.add(deviceModel +": " + this.deviceAngle + "˚");

            return this.results;
        }

        public ArrayList<String> getFullResult() {
            results.clear();

            // ARM and motors results;
            results.add("Robotic Arm");
            //id, speed, temperature, torque, pos
            //results.add("ID: 0000 RPM / 000 ˚C / 0000 Nm / 000˚");
            results.add("M1: 0000 RPM / 050 ˚C / 010 Nm / 000˚");
            results.add("M2: 0000 RPM / 040 ˚C / 003 Nm / 000˚");
            results.add("M3: 0000 RPM / 050 ˚C / 007 Nm / 000˚");
            results.add("M4: 0000 RPM / 070 ˚C / 003 Nm / 045˚");

            // Device results
            results.add(deviceModel);
            results.add("X: 000˚ / Y: 000˚ / Z: 045˚");

            return this.results;
        }


    }

    public class SuiteExpListAdapter extends BaseExpandableListAdapter {

        Context mContext;
        ArrayList<MyTestSuiteResult> mTestSuiteResults;
        private boolean isSimpleMode;

        public SuiteExpListAdapter (Context context, ArrayList<MyTestSuiteResult> results) {
            mContext = context;
            mTestSuiteResults = results;
        }

        @Override
        public int getGroupCount() {
            return mTestSuiteResults.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mTestSuiteResults.get(groupPosition);
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {

            MyTestSuiteResult tsr = (MyTestSuiteResult) getGroup(groupPosition);

            String headerTitle = "Suite " + tsr.name;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            ImageView leftImageView = (ImageView) convertView
                    .findViewById(R.id.image_group_dot);

            ImageView rightImageView = (ImageView) convertView
                    .findViewById(R.id.image_group_check_right);

            if (isExpanded) {
                leftImageView.setImageResource(R.drawable.ic_check_black_24dp);
                rightImageView.setVisibility(View.GONE);
            } else {
                leftImageView.setImageResource(R.drawable.ic_test_suite_result_blue);
                rightImageView.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mTestSuiteResults.get(groupPosition).testCases.size();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mTestSuiteResults.get(groupPosition).testCases.get(childPosition);
        }


        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            MyTestCaseResult tcr = (MyTestCaseResult) getChild(groupPosition, childPosition);

            // Second Level ExpandableListView
            TestResultExpListView testResultExpListView = new TestResultExpListView(mContext);
            CaseExpListAdapter caseExpListAdapter = new CaseExpListAdapter(
                    mContext, tcr, isSimpleMode);
            testResultExpListView.setAdapter(caseExpListAdapter);
            testResultExpListView.setGroupIndicator(null);
            return testResultExpListView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public void setSimpleMode(boolean simpleMode) {
            this.isSimpleMode = simpleMode;
        }
    }

    public class CaseExpListAdapter extends BaseExpandableListAdapter {

        MyTestCaseResult myTestCaseResult;
        ArrayList<String> testResults;
        Context mContext;

        public CaseExpListAdapter (Context context, MyTestCaseResult testCaseResult,
                                   boolean simpleMode) {
            mContext = context;
            myTestCaseResult = testCaseResult;
            if (simpleMode) {
                testResults = myTestCaseResult.getSimpleResult();
            } else {
                testResults = myTestCaseResult.getFullResult();
            }
        }

        @Override
        public int getGroupCount() {
            return 1;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return myTestCaseResult;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {

            MyTestCaseResult tcr = myTestCaseResult;

            final String childText = tcr.name;

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return testResults.size();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return testResults.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.full_test_result_item, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.text_result_line);

            txtListChild.setText(testResults.get(childPosition));

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class TestResultExpListView extends ExpandableListView {

        public TestResultExpListView(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            //TODO: Understand the choice of height and width size
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(960,
                    MeasureSpec.AT_MOST);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(960,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
