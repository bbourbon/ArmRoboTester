package br.org.cesar.armrobotester.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.content.TestManager;
import br.org.cesar.armrobotester.model.TestCase;

/**
 * Project ArmRoboTester
 * Created by Thiago on 15/11/2016.
 */

public class TestSuiteEditorAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private TestManager.TestSuite mTestSuite;

    public TestSuiteEditorAdapter(Context context, TestManager.TestSuite suite) {
        mContext = context;
        mTestSuite = suite;
    }

    @Override
    public int getGroupCount() {
        return mTestSuite != null ? mTestSuite.getTests().size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mTestSuite != null && groupPosition >= 0 &&
                mTestSuite.getTests().size() > groupPosition ?
                mTestSuite.getTest(groupPosition).getMotionTestList().size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mTestSuite != null && groupPosition >= 0 &&
                mTestSuite.getTests().size() > groupPosition ?
                mTestSuite.getTest(groupPosition) : null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mTestSuite != null && groupPosition >= 0 &&
                mTestSuite.getTests().size() > groupPosition &&
                childPosition >= 0 && getChildrenCount(groupPosition) > childPosition ?
                mTestSuite.getTests().get(groupPosition).getMotionTestList().get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mTestSuite != null && groupPosition >= 0 &&
                mTestSuite.getTests().size() > groupPosition ?
                mTestSuite.getTest(groupPosition).getId() : 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mTestSuite != null && groupPosition >= 0 &&
                mTestSuite.getTests().size() > groupPosition &&
                childPosition >= 0 && getChildrenCount(groupPosition) > childPosition ?
                mTestSuite.getTests().get(groupPosition).getMotionTestList().get(childPosition).hashCode() : 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = ((TestCase) getGroup(groupPosition)).getName();
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.suite_test_group_item, parent, false);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        TextView lblListId = (TextView) convertView
                .findViewById(R.id.test_id);
        lblListHeader.setText(headerTitle);
        lblListId.setText("(id: 00" + ((TestCase) getGroup(groupPosition)).getId() + ")");

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.suite_test_attribute_item, parent, false);
        }

        TextView action = (TextView) convertView.findViewById(R.id.add_action);

        switch (childPosition) {
            case 0: action.setText("Add movement"); break;
            case 1: action.setText("Add angle"); break;
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
