package br.org.cesar.armrobotester.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.content.TestContent;
import br.org.cesar.armrobotester.fragments.TestsFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TestContent.MotionTestItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TestsRecyclerViewAdapter extends RecyclerView.Adapter<TestsRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<TestContent.MotionTestItem> mValues;
    private final TestsRecyclerViewAdapter.OnTestAdapterListener mListener;

    public TestsRecyclerViewAdapter(ArrayList<TestContent.MotionTestItem> items,
                                    TestsRecyclerViewAdapter.OnTestAdapterListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_test_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTestDescription.setText(mValues.get(position).toString());
        holder.mTestStatus.setText(mValues.get(position).getStatus());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onClick(holder.mItem);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    mListener.onLongClick(holder.mItem);
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

    public void addItem(TestContent.MotionTestItem testItem) {
        if (testItem != null) {
            mValues.add(testItem);
            if (null != mListener) mListener.onListFragmentItemCountChanged(mValues.size());
            this.notifyDataSetChanged();
        }
    }

    public void removeItem(TestContent.MotionTestItem testItem) {
        if (testItem != null) {
            mValues.remove(testItem);
            if (null != mListener) mListener.onListFragmentItemCountChanged(mValues.size());
            this.notifyDataSetChanged();
        }
    }

    public void updateItem(int position, TestContent.MotionTestItem testItem) {
        if (null == testItem) return;
        if (position >= 0 && position < mValues.size()) {
            mValues.set(position, testItem);
        } else {
            int idx = mValues.indexOf(testItem);
            if (idx != -1) mValues.set(idx, testItem);
        }
        this.notifyDataSetChanged();
    }

    public interface OnTestAdapterListener {
        void onListFragmentInteraction(TestContent.MotionTestItem item);

        void onLongClick(TestContent.MotionTestItem item);

        void onClick(TestContent.MotionTestItem item);

        void onListFragmentItemCountChanged(int count);
    }

    /**
     * View Holder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTestDescription;
        public final TextView mTestStatus;
        public TestContent.MotionTestItem mItem;

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

}
