package com.marius.valeyou_admin.ui.activity.view_image;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.marius.valeyou_admin.R;

public class ViewImageActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        intent = getIntent();

        String url = intent.getStringExtra("url");

        ImageView imageView = findViewById(R.id.image);
        ImageView iv_cancel = findViewById(R.id.iv_cancel);

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
                onBackPressed();
            }
        });

        Glide.with(ViewImageActivity.this).load(url).into(imageView);

    }
}