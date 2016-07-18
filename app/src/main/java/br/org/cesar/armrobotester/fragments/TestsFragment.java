package br.org.cesar.armrobotester.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

import br.org.cesar.armrobotester.MainNaviActivity;
import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.fragments.content.TestContent;
import br.org.cesar.armrobotester.fragments.content.TestContent.MotionTestItem;
import br.org.cesar.armrobotester.model.MotionTest;



/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TestsFragment extends Fragment implements View.OnClickListener,
        TestsRecyclerViewAdapter.OnTestAdapterListener {

    RecyclerView.AdapterDataObserver mObserver;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<TestContent.MotionTestItem> mListTestItems;
    private Handler mHandler;
    private AlertDialog mTestListAlertDialog;
    private TestsRecyclerViewAdapter mTestRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TestsFragment() {
        mListTestItems = new ArrayList<>();
        mHandler = new Handler();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tests_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mTestRecyclerViewAdapter =
                    new TestsRecyclerViewAdapter(mListTestItems, this);
            recyclerView.setAdapter(mTestRecyclerViewAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof FloatingActionButton) {
            onAddTest();
        }
    }

    public void removeMotionTestItem(MotionTestItem item) {
        if (null != item) {
            mTestRecyclerViewAdapter.removeItem(item);
        }
    }

    private void onAddTest() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this.getActivity());

        alertBuilder.setSingleChoiceItems(getTestsItemType(), 0,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(MainNaviActivity.TAG, "Which: " + which);
                        if (dialog != null && dialog.equals(mTestListAlertDialog)) {
                            ListView lw = ((AlertDialog) dialog).getListView();
                            Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                            if (checkedItem != null
                                    && mTestRecyclerViewAdapter != null
                                    && checkedItem instanceof TestContent.MotionTestItem) {
                                TestContent.MotionTestItem selectItem = (TestContent.MotionTestItem) checkedItem;
                                Log.d(MainNaviActivity.TAG, "selected: " + selectItem.toString());
                                mTestRecyclerViewAdapter.addItem(selectItem);
                            }
                        }
                        dialog.dismiss();
                    }
                });

        mTestListAlertDialog = alertBuilder.create();
        mTestListAlertDialog.show();

    }

    private ArrayAdapter<MotionTestItem> getTestsItemType() {
        ArrayAdapter<TestContent.MotionTestItem> arrayAdapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_list_item_1);
        arrayAdapter.setNotifyOnChange(true);

        Set<Integer> typeIds = MotionTest.validKeys();

        if (typeIds != null) {
            for (Integer type : typeIds) {
                arrayAdapter.add(new TestContent.MotionTestItem(type));
            }
        }

        arrayAdapter.sort(MotionTest.getComparator());
        return arrayAdapter;
    }

    @Override
    public void onListFragmentInteraction(MotionTestItem item) {
        if (null != mListener)
            mListener.onListFragmentInteraction(item);
    }

    @Override
    public void onLongClick(MotionTestItem item) {
        if (null != mListener) mListener.onLongClick(item);
        if (null != mTestRecyclerViewAdapter)
            mTestRecyclerViewAdapter.removeItem(item);
    }

    @Override
    public void onClick(MotionTestItem item) {
        if (null != mListener) mListener.onClick(item);

        if (null != mTestRecyclerViewAdapter) {
            switch (item.status) {
                case MotionTestItem.Status.NONE:
                    item.status = MotionTestItem.Status.RUNNING;
                    break;
                case MotionTestItem.Status.RUNNING:
                    item.status = MotionTestItem.Status.PAUSE;
                    break;
                case MotionTestItem.Status.PAUSE:
                    item.status = MotionTestItem.Status.COMPLETED;
                    break;
                case MotionTestItem.Status.COMPLETED:
                    item.status = MotionTestItem.Status.CANCELLED;
                    break;
                case MotionTestItem.Status.CANCELLED:
                    item.status = MotionTestItem.Status.NONE;
                    break;
                default:
                    Log.e(MainNaviActivity.TAG, "Unknown Test Status");
                    break;
            }
        }

        if (null != mTestRecyclerViewAdapter)
            mTestRecyclerViewAdapter.updateItem(-1, item);
    }

    @Override
    public void onListFragmentItemCountChanged(int count) {
        if (null != mListener)
            mListener.onListFragmentItemCountChanged(count);
    }


    // TODO: Change return type to boolean, to check if handled on above layer
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(MotionTestItem item);

        void onLongClick(MotionTestItem item);

        void onClick(MotionTestItem item);

        void onListFragmentItemCountChanged(int count);
    }

}
