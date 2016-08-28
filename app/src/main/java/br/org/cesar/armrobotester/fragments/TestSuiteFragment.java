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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.content.TestManager;
import br.org.cesar.armrobotester.model.TestCase;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TestSuiteFragment extends Fragment implements View.OnClickListener,
        TestSuiteRecyclerViewAdapter.OnTestAdapterListener {

    private OnListFragmentInteractionListener mListener;
    private ArrayList<TestCase> mListTestCases;
    private Handler mHandler;
    private AlertDialog mTestListAlertDialog;
    private TestSuiteRecyclerViewAdapter mTestRecyclerViewAdapter;
    private TestManager mTestManager;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TestSuiteFragment() {
        mListTestCases = new ArrayList<>();
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

        mTestManager = TestManager.getInstance(getContext().getApplicationContext());
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mTestRecyclerViewAdapter =
                    new TestSuiteRecyclerViewAdapter(mTestManager, getContext(), this);
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
        mTestRecyclerViewAdapter.refresh();
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
        // TODO: Start Add Test Activity (or Fragment)
    }

    @Override
    public void onLongClick(TestCase item) {
        DialogInterface.OnClickListener onClickListener = new RemoveTestDialogListener(item);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert!")
                .setMessage("Do you want to remove the test case?")
                .setCancelable(true)
                .setNegativeButton("No", onClickListener)
                .setPositiveButton("Yes", onClickListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(TestCase item) {
        // TODO: Open Test Case Fragment for edit
    }

    ;

    @Override
    public void onListFragmentItemCountChanged(int count) {
        if (null != mListener)
            mListener.onListFragmentItemCountChanged(count);
    }

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
        void onListFragmentItemCountChanged(int count);
    }


    // TODO: Change return type to boolean, to check if handled on above layer

    private class RemoveTestDialogListener implements DialogInterface.OnClickListener {

        private final TestCase mTestCase;

        public RemoveTestDialogListener(TestCase item) {
            mTestCase = item;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (null != mTestManager)
                        mTestManager.removeTest(mTestCase);
                    dialog.dismiss();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    dialog.dismiss();
                default:
                    dialog.dismiss();
            }
        }
    }

}