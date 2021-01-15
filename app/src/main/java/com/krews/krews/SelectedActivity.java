package com.krews.krews;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectedActivity extends AppCompatActivity {

    LinearLayout laySelected;
    ImageView ivBack;
    RecyclerView recyclerView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);

        laySelected=findViewById(R.id.laySelected);
        ivBack=findViewById(R.id.ivBack);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager lmBrands=new GridLayoutManager(SelectedActivity.this,2);
        recyclerView.setLayoutManager(lmBrands);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        laySelected.setOnTouchListener(new OnSwipeTouchListener(SelectedActivity.this) {

            public void onSwipeRight() {

                onBackPressed();

            }

        });

        ArrayList<ModelCharity> arraySelectedCharity = (ArrayList<ModelCharity>) getIntent().getSerializableExtra("selectedCharity");
        AdapterCharityGrid adapterCharityGrid=new AdapterCharityGrid(arraySelectedCharity,SelectedActivity.this);
        recyclerView.setAdapter(adapterCharityGrid);
        adapterCharityGrid.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in2,R.anim.slide_out2);
    }
}