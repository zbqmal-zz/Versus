package com.example.versus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.itemViewHolder> {

    private ArrayList<VersusItem> mItemList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class itemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTextView;
        public ImageView itemDeleteImage;

        public itemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.itemTextView);
            itemDeleteImage = itemView.findViewById(R.id.image_Delete);

            itemDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public itemAdapter(ArrayList<VersusItem> itemList) {
        mItemList = itemList;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.versus_item, parent, false);
        itemViewHolder ivh = new itemViewHolder(v, mListener);
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
