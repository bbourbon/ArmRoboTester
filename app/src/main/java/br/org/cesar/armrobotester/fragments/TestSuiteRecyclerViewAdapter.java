package br.org.cesar.armrobotester.fragments;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.content.TestManager;
import br.org.cesar.armrobotester.fragments.TestSuiteFragment.OnListFragmentInteractionListener;
import br.org.cesar.armrobotester.model.TestCase;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TestCase} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TestSuiteRecyclerViewAdapter extends
        RecyclerView.Adapter<TestSuiteRecyclerViewAdapter.ViewHolder> {

    private final DataSetObserver mTestSuiteObserver;
    private final TestManager mTestManager;
    private final TestSuiteRecyclerViewAdapter.OnTestAdapterListener mListener;
    private final Context mContext;
    private List<TestManager.TestSuite> mValues;

    public TestSuiteRecyclerViewAdapter(@NonNull TestManager testManager, Context context,
                                        TestSuiteRecyclerViewAdapter.OnTestAdapterListener listener) {

        mTestSuiteObserver = new TestSuiteDataObserver(this);
        mListener = listener;
        mContext = context;

        mTestManager = testManager;
        mTestManager.registerObserver(mTestSuiteObserver);
        // TODO - Change the method to return the preferable values
        mValues = mTestManager.getTestSuite(); // THIS WILL RETURN NULL

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_test_suite_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTestSuite = mValues.get(position);

        holder.mTestDescription.setText(holder.mTestSuite.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onClick(holder.mTestSuite);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    mListener.onLongClick(holder.mTestSuite);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void refresh() {
        if (null != mValues) {
            mValues.clear();
            mValues = mTestManager.getTestSuite();

            // TODO: Force List Update
            notifyDataSetChanged();
        }
    }

    /**
     * Listener for this recycle view adapter
     */
    public interface OnTestAdapterListener {
        void onClick(TestManager.TestSuite testSuite);

        void onLongClick(TestManager.TestSuite testSuite);
        void onListFragmentItemCountChanged(int count);
    }

    /**
     * View Holder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTestDescription;
        public final TextView mTestStatus;
        public TestManager.TestSuite mTestSuite;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTestDescription = (TextView) view.findViewById(R.id.text_test_item);
            mTestStatus = (TextView) view.findViewById(R.id.text_test_item_status);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTestDescription.getText() + "'";
        }
    }

    /**
     * DataSetObserver for TestSuite
     */
    private class TestSuiteDataObserver extends DataSetObserver {

        private final TestSuiteRecyclerViewAdapter mAdapter;

        public TestSuiteDataObserver(TestSuiteRecyclerViewAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onChanged() {
            super.onChanged();
            if (null != mAdapter) mAdapter.refresh();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            if (null != mAdapter) mAdapter.refresh();
        }
    }
}
