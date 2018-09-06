package com.alexandrealencar.videoclean.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alexandrealencar.videoclean.R;
import java.util.ArrayList;
import java.util.List;

public class LinkVideoAdapter extends RecyclerView.Adapter<LinkVideoAdapter.ViewHolder> {
    private List<String> mDataset;
    private OnListInteraction onListInteraction;

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView;
        ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(android.R.id.text1);
        }
    }

    public LinkVideoAdapter(OnListInteraction onListInteraction) {
        this.onListInteraction = onListInteraction;
        mDataset = new ArrayList<>();
    }

    public void setmDataset(List<String> myDataset){
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String p = mDataset.get(position);
        holder.mTextView.setText(p);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListInteraction.onClickIem(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnListInteraction{
        public void onClickIem(String s);
    }
}