package br.org.cesar.armrobotester.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.adapters.TestSuiteEditorAdapter;
import br.org.cesar.armrobotester.content.TestManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TestSuiteEditorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TestSuiteEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestSuiteEditorFragment extends Fragment implements ExpandableListView.OnItemClickListener {

    // TODO: Rename and change types of parameters
    private String mSuiteName;

    private OnFragmentInteractionListener mListener;

    public TestSuiteEditorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TestSuiteEditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestSuiteEditorFragment newInstance(String suiteName) {
        TestSuiteEditorFragment fragment = new TestSuiteEditorFragment();
        Bundle args = new Bundle();
        args.putString(TestSuiteFragment.ARG_SUITE, suiteName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSuiteName = getArguments().getString(TestSuiteFragment.ARG_SUITE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_test_suite_editor, container, false);

        ExpandableListView expListView = (ExpandableListView) layout.findViewById(R.id.test_list);
        TextView title = (TextView) layout.findViewById(R.id.suiteTitle);

        expListView.setOnItemClickListener(this);

        TestManager.TestSuite suite = TestManager.getInstance().getSuiteByName(mSuiteName);
        title.setText(suite.getName());

        TestSuiteEditorAdapter adapter = new TestSuiteEditorAdapter(getContext(), suite);
        expListView.setAdapter(adapter);

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        Log.i("TAG", "CLICKING");

        // Create and show the dialog.
        DialogFragment newFragment = AttributeDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
