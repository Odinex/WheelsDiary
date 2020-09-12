package com.kp.wheelsdiary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.kp.wheelsdiary.dto.Wheel;
import com.kp.wheelsdiary.dto.WheelTask;
import com.kp.wheelsdiary.enums.TaskTypeEnum;
import com.kp.wheelsdiary.service.WheelService;
import com.kp.wheelsdiary.service.WheelTaskService;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WheelsActivity extends AppCompatActivity {

    private static final int EDIT_WHEEL_INTENT = 789;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_wheels);
        setupToolbar();
        setupCollapsingToolbarLayout();
        try {
            loadCards();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.wheels_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);


        }
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        // Show menu icon

    }

    private void setupCollapsingToolbarLayout() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.wheels_collapsing_toolbar);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle("Cars");
            //collapsingToolbarLayout.setCollapsedTitleTextColor(0xED1C24);
            //collapsingToolbarLayout.setExpandedTitleColor(0xED1C24);
        }
    }

    private void loadCards() throws ExecutionException, InterruptedException {
        LinearLayout cardLayout = findViewById(R.id.wheelsCardLinearLayout);
        cardLayout.removeAllViews();
        WheelService.clearWheels();
        Collection<Wheel> wheels = WheelService.getWheels();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;

        for (final Wheel wheel : wheels) {
            System.out.println(new Date() + " Wheel card is loaded:  " + wheel);
            CardView cardView = new CardView(cardLayout.getContext());
            CardView.LayoutParams cardViewParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardViewParams.setMargins(16, 16, 16, 16);
            cardView.setLayoutParams(cardViewParams);

            LinearLayout textAndButtonLayout = new LinearLayout(cardView.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(6, 6, 6, 6);
            textAndButtonLayout.setLayoutParams(layoutParams);
            textAndButtonLayout.setOrientation(LinearLayout.HORIZONTAL);

            Drawable drawable = ContextCompat.getDrawable(
                    textAndButtonLayout.getContext(), R.drawable.cardview_border);
            textAndButtonLayout.setBackground(drawable);

            ScrollView scrollView = new ScrollView(textAndButtonLayout.getContext());
            scrollView.setLayoutParams(new ViewGroup.LayoutParams((int) (screenWidth * 0.8), (int) (screenHeight * 0.15)));


            LinearLayout textLayout = new LinearLayout(textAndButtonLayout.getContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                    (int) (screenWidth * 0.8), (int) (screenHeight * 0.15));
            textLayout.setOrientation(LinearLayout.VERTICAL);
            layoutParams2.setMargins(16, 16, 16, 16);
            textLayout.setLayoutParams(layoutParams2);

            TextView titleTextView = new TextView(textLayout.getContext());

            LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParamsText.setMargins(16, 16, 16, 16);
            titleTextView.setLayoutParams(layoutParamsText);
            titleTextView.setText(wheel.getName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                titleTextView.setTextAppearance(R.style.TextAppearance_AppCompat_Title);
            }
            titleTextView.setTextColor(Color.WHITE);
            textLayout.addView(titleTextView);
            TextView descriptionTextView = new TextView(textLayout.getContext());
            descriptionTextView.setText(String.format("%s %s %s", wheel.getMake(), wheel.getModel(), wheel.getVariant() == null ? "" : wheel.getVariant()));
            descriptionTextView.setTextColor(Color.WHITE);
            descriptionTextView.setLayoutParams(layoutParamsText);
            textLayout.addView(descriptionTextView);
            ImageButton editButton = new ImageButton(textAndButtonLayout.getContext());
            Drawable editIcon = ContextCompat.getDrawable(
                    textLayout.getContext(), R.drawable.ic_editting_pen);
            editButton.setImageDrawable(editIcon);
            ViewGroup.LayoutParams editButtonParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            cardViewParams.setMargins(5, 16, 5, 16);
            editButton.setLayoutParams(editButtonParams);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editTaskIntent = new Intent(view.getContext(), WheelActivity.class);
                    editTaskIntent.putExtra("WHEEL_ID", wheel.getId());
                    editTaskIntent.putExtra("MODE", "EDIT");

                    startActivityForResult(editTaskIntent, EDIT_WHEEL_INTENT);
                }
            });
            scrollView.addView(textLayout);
            textAndButtonLayout.addView(scrollView);

            textAndButtonLayout.addView(editButton);
            cardView.addView(textAndButtonLayout);
            cardLayout.addView(cardView);

        }
    }
}
