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

        // --- Test Suite ID_002 ---
        // --- Test Suite ID_003 ---
        // --- Test Suite ID_004 ---
        // --- Test Suite ID_005 ---
        // --- Test Suite ID_006 ---
        // --- Test Suite ID_007 ---
        // --- Test Suite ID_008 ---

        return testSuites;

    }
    //-----

    public class ExecutionSuite {

        String name;
        List<ExecutionTest> testCases;
        String status;
        boolean hasExecuted;

        public ExecutionSuite (String name) {
            this.name = name;
            this.testCases = new ArrayList<>(10);
            this.status = "(waiting)";
        }

        //TODO: Test Suite status depends on its Test Cases status

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
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            return null;
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
