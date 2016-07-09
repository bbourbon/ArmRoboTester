package br.org.cesar.armrobotester.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.fragment.TestsFragment.OnListFragmentInteractionListener;
import br.org.cesar.armrobotester.fragment.content.TestContent.TestItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TestItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TestsRecyclerViewAdapter extends RecyclerView.Adapter<TestsRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<TestItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public TestsRecyclerViewAdapter(ArrayList<TestItem> items, OnListFragmentInteractionListener listener) {
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

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addItem(TestItem testItem) {
        if (testItem != null) {
            mValues.add(testItem);
            this.notifyDataSetChanged();
        }
    }

    public void removeItem(TestItem testItem) {
        if (testItem != null) {
            mValues.remove(testItem);
            this.notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTestDescription;
        public TestItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTestDescription = (TextView) view.findViewById(R.id.text_test_item);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTestDescription.getText() + "'";
        }
    }
}
