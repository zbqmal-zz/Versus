package com.example.versus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ItemImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_image);

        // Set up imageView
        Intent intent = getIntent();
        ImageView itemImageView = findViewById(R.id.itemImageView);
        String imageURI = intent.getStringExtra("imageURI");

        if (imageURI != null) {
            itemImageView.setImageURI(Uri.parse(imageURI));
        } else {
            itemImageView.setImageResource(R.drawable.ic_empty_image);
        }
    }
}
