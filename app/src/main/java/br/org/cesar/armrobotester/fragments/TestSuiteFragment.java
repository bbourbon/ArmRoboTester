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
import br.org.cesar.armrobotester.content.TestContent.MotionItem;
import br.org.cesar.armrobotester.model.Motion;



/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TestSuiteFragment extends Fragment implements View.OnClickListener,
        TestSuiteRecyclerViewAdapter.OnTestAdapterListener {

    private OnListFragmentInteractionListener mListener;
    private ArrayList<MotionItem> mListTestItems;
    private Handler mHandler;
    private AlertDialog mTestListAlertDialog;
    private TestSuiteRecyclerViewAdapter mTestRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TestSuiteFragment() {
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
                    new TestSuiteRecyclerViewAdapter(mListTestItems, this);
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
                                    && checkedItem instanceof MotionItem) {
                                MotionItem selectItem = (MotionItem) checkedItem;
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

    private ArrayAdapter<MotionItem> getTestsItemType() {
        ArrayAdapter<MotionItem> arrayAdapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_list_item_1);
        arrayAdapter.setNotifyOnChange(true);

        Set<Integer> typeIds = Motion.validKeys();

        if (typeIds != null) {
            for (Integer type : typeIds) {
                arrayAdapter.add(new MotionItem(type));
            }
        }

        arrayAdapter.sort(Motion.getComparator());
        return arrayAdapter;
    }

    @Override
    public void onListFragmentInteraction(MotionItem item) {
        if (null != mListener)
            mListener.onListFragmentInteraction(item);
    }

    @Override
    public void onLongClick(MotionItem item) {
        if (null != mListener) mListener.onLongClick(item);
        if (null != mTestRecyclerViewAdapter)
            mTestRecyclerViewAdapter.removeItem(item);
    }

    @Override
    public void onClick(MotionItem item) {
        if (null != mListener) mListener.onClick(item);

        if (null != mTestRecyclerViewAdapter) {
            switch (item.status) {
                case MotionItem.Status.NONE:
                    item.status = MotionItem.Status.RUNNING;
                    break;
                case MotionItem.Status.RUNNING:
                    item.status = MotionItem.Status.PAUSE;
                    break;
                case MotionItem.Status.PAUSE:
                    item.status = MotionItem.Status.COMPLETED;
                    break;
                case MotionItem.Status.COMPLETED:
                    item.status = MotionItem.Status.CANCELLED;
                    break;
                case MotionItem.Status.CANCELLED:
                    item.status = MotionItem.Status.NONE;
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
        void onListFragmentInteraction(MotionItem item);

        void onLongClick(MotionItem item);

        void onClick(MotionItem item);

        void onListFragmentItemCountChanged(int count);
    }

}
