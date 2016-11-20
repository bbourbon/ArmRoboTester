package br.org.cesar.armrobotester.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
 * Created by bcb on 28/08/16.
 */

public class ExecutionFragment extends Fragment {

    private ExpandableListView lv;

    public ExecutionFragment () {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootview = inflater.inflate(R.layout.execution_fragment_layout, container, false );
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = (ExpandableListView) view.findViewById(R.id.expListView);
        ExecutionExpListAdapter adapter = new ExecutionExpListAdapter(getContext(),
                getMockUpExecution());
        lv.setAdapter(adapter);
        lv.setGroupIndicator(null);
    }

    //-----
    private ArrayList<ExecutionSuite> getMockUpExecution() {

        ArrayList<ExecutionSuite> testSuites = new ArrayList<>(8);

        // --- Test Suite ID_001 ---
        ExecutionSuite es001 = new ExecutionSuite("ID_001");
        es001.setDone();
        testSuites.add(es001);
        // --- Test Suite ID_002 ---
        ExecutionSuite es002 = new ExecutionSuite("ID_002");
        es002.setDone();
        testSuites.add(es002);
        // --- Test Suite ID_003 ---
        ExecutionSuite es003 = new ExecutionSuite("ID_003");
        es003.setProgress();
        testSuites.add(es003);
        // --- Test Suite ID_004 ---
        ExecutionSuite es004 = new ExecutionSuite("ID_004");
        es004.setWaiting();
        testSuites.add(es004);
        // --- Test Suite ID_005 ---
        ExecutionSuite es005 = new ExecutionSuite("ID_005");
        es005.setWaiting();
        testSuites.add(es005);
        // --- Test Suite ID_006 ---
        ExecutionSuite es006 = new ExecutionSuite("ID_006");
        es006.setWaiting();
        testSuites.add(es006);
        // --- Test Suite ID_007 ---
        ExecutionSuite es007 = new ExecutionSuite("ID_007");
        es007.setWaiting();
        testSuites.add(es007);
        // --- Test Suite ID_008 ---
        ExecutionSuite es008 = new ExecutionSuite("ID_008");
        es008.setWaiting();
        testSuites.add(es008);

        return testSuites;

    }
    //-----

    public class ExecutionSuite {

        String name;
        List<ExecutionTest> testCases;
        String status;
        boolean hasExecuted;
        boolean isInProgess;
        boolean isWaiting;

        public ExecutionSuite (String name) {
            this.name = name;
            this.testCases = new ArrayList<>(10);
            this.status = "(waiting)";
        }

        //TODO: Test Suite status depends on its Test Cases status

        void setWaiting() {
            this.status = "(waiting)";
            this.hasExecuted = false;
            this.isInProgess = false;
            this.isWaiting = true;
        }

        void setProgress() {
            this.status = "(in progress)";
            this.hasExecuted = false;
            this.isInProgess = true;
            this.isWaiting = false;
        }

        void setDone() {
            this.status = "";
            this.hasExecuted = true;
            this.isInProgess = false;
            this.isWaiting = false;
        }

    }

    public class ExecutionTest {

        String name;
        String status;
        boolean hasExecuted;

        public ExecutionTest (String name) {
            this.name = name;
            this.status = "(waiting)";
        }

        void setWaiting() {
            this.status = "(waiting)";
            this.hasExecuted = false;
        }

        void setProgress() {
            this.status = "(in progress)";
            this.hasExecuted = false;
        }

        void setDone() {
            this.status = "";
            this.hasExecuted = true;
        }


    }

    public class ExecutionExpListAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        private ArrayList<ExecutionSuite> mExecutionSuitesList;

        public ExecutionExpListAdapter(Context context,
                                       ArrayList<ExecutionSuite> executionSuitesList) {

            mContext = context;
            mExecutionSuitesList = executionSuitesList;
        }

        @Override
        public int getGroupCount() {
            return mExecutionSuitesList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mExecutionSuitesList.get(groupPosition).testCases.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mExecutionSuitesList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mExecutionSuitesList.get(groupPosition).testCases.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {

            ExecutionSuite suite = mExecutionSuitesList.get(groupPosition);

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_execution_item, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.text_item_id);

            txtListChild.setText("Suite " +suite.name);

            ImageView blueDot = (ImageView) convertView.findViewById(R.id.image_group_dot);
            ImageView check = (ImageView) convertView.findViewById(R.id.image_group_check_right);
            TextView textStatus = (TextView) convertView.findViewById(R.id.text_status);

            textStatus.setText(suite.status);

            if (suite.hasExecuted) {
                blueDot.setVisibility(View.VISIBLE);
                check.setVisibility(View.VISIBLE);
                textStatus.setVisibility(View.GONE);
            } else if (suite.isInProgess) {
                blueDot.setVisibility(View.VISIBLE);
                check.setVisibility(View.GONE);
                textStatus.setVisibility(View.VISIBLE);
            } else if (suite.isWaiting) {
                blueDot.setVisibility(View.GONE);
                check.setVisibility(View.GONE);
                textStatus.setVisibility(View.VISIBLE);
            }


            return convertView;

        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


}
