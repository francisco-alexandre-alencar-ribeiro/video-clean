package com.alexandrealencar.videoclean.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexandrealencar.videoclean.R;

import java.util.ArrayList;
import java.util.List;

public class LinkPageAdapter extends RecyclerView.Adapter<LinkPageAdapter.ViewHolder> {
    private List<String[]> mDataset;
    private OnListInteraction onListInteraction;

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView1;

        ViewHolder(View v) {
            super(v);
            mTextView1 = v.findViewById(android.R.id.text1);
        }
    }

    public LinkPageAdapter(OnListInteraction onListInteraction) {
        this.onListInteraction = onListInteraction;
        mDataset = new ArrayList<>();
    }

    public void setmDataset(List<String[]> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String[] p = mDataset.get(position);
        try {
            holder.mTextView1.setText(p[1]);
        } catch (IndexOutOfBoundsException e) {
            holder.mTextView1.setText(p[0]);
        }
        holder.mTextView1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
            onListInteraction.onClickIem(p);
            holder.mTextView1.setText(holder.mTextView1.getText() + " * ");
            }
        });

        holder.mTextView1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
            onListInteraction.onClickLongIem(p);
            return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnListInteraction {
        public void onClickIem(String[] s);
        public void onClickLongIem(String[] s);
    }
}