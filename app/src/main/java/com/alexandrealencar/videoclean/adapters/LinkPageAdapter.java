package com.alexandrealencar.videoclean.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alexandrealencar.videoclean.R;
import java.util.ArrayList;
import java.util.List;

public class LinkPageAdapter extends RecyclerView.Adapter<LinkPageAdapter.ViewHolder> {
    private List<String> mDataset;
    private OnListInteraction onListInteraction;

    static class ViewHolder extends RecyclerView.ViewHolder {
        final CheckBox mCheckBox;
        ViewHolder(View v) {
            super(v);
            mCheckBox = v.findViewById(android.R.id.checkbox);
        }
    }

    public LinkPageAdapter(OnListInteraction onListInteraction) {
        this.onListInteraction = onListInteraction;
        mDataset = new ArrayList<>();
    }

    public void setmDataset(List<String> myDataset){
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link_page, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String[] p = mDataset.get(position).split(",");
        if(p.length > 0)
        try {
            holder.mCheckBox.setText(p[1]);
        }catch (IndexOutOfBoundsException e){
            holder.mCheckBox.setText("-");
        }



        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( holder.mCheckBox.isChecked() )
                    onListInteraction.onClickIem(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnListInteraction{
        public void onClickIem(String[] s);
    }
}