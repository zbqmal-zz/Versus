package com.example.versus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.itemViewHolder> {

    private ArrayList<VersusCategory> categoryList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onCategoryClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class itemViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryTextView;
        public ImageView categoryDeleteImage;

        public itemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            categoryDeleteImage = itemView.findViewById(R.id.image_Category_Delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCategoryClick(position);
                        }
                    }
                }
            });

            categoryDeleteImage.setOnClickListener(new View.OnClickListener() {
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

    public categoryAdapter(ArrayList<VersusCategory> itemList) {
        categoryList = itemList;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.versus_category, parent, false);
        itemViewHolder ivh = new itemViewHolder(v, mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        VersusCategory currentItem = categoryList.get(position);

        holder.categoryTextView.setText(currentItem.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
