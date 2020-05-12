package com.example.versus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.itemViewHolder> {

    private ArrayList<VersusItem> mItemList;

    public static class itemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTextView;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.itemTextView);
        }
    }

    public itemAdapter(ArrayList<VersusItem> itemList) {
        mItemList = itemList;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.versus_item, parent, false);
        itemViewHolder ivh = new itemViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        VersusItem currentItem = mItemList.get(position);

        holder.itemTextView.setText(currentItem.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
