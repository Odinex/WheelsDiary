package com.kp.wheelsdiary;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.kp.wheelsdiary.dto.Wheel;
import com.kp.wheelsdiary.service.WheelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AddWheelActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_wheel);
        // Get reference of widgets from XML layout

        // Initializing a String Array
        String[] brands = new String[]{
                "Select a make...",
                "AUDI",
                "Nissan"
        };

        final Spinner brandSpinner = findViewById(R.id.brandSpinner);
        setSpinnerValues(brands, brandSpinner);
        final Button button = findViewById(R.id.addCarButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddCarClick(view);
            }
        });
        final Spinner modelSpinner = findViewById(R.id.modelSpinner);
        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String brand = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected make: " + brand, Toast.LENGTH_SHORT)
                            .show();
                    try {
                        String[] models = getModelsForBrand(brand);
                        setSpinnerValues(models, modelSpinner);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set up the user interaction to manually show or hide the system UI.
//        mContentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("name","dummy");
//                setResult(Activity.RESULT_OK,returnIntent);
//                finish();
//            }
//        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    private String[] getModelsForBrand(String brand) throws Exception {
        List<String> models = new ArrayList<>();
        models.add("Select a model...");
        if (brand.equals("AUDI")) {
            models.addAll(Collections.singletonList("A4"));
        } else if (brand.equals("Nissan")) {
            models.addAll(Collections.singletonList("Micra"));
        } else {
            throw new Exception("Invalid brand");
        }
        return models.toArray(new String[0]);
    }

    private void setSpinnerValues(String[] plants, Spinner spinner) {
        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }

    public void onAddCarClick(View view) {
        final Spinner brandSpinner = findViewById(R.id.brandSpinner);

        final Spinner modelSpinner = findViewById(R.id.modelSpinner);
        final EditText nameEditText = findViewById(R.id.nameInput);
        final EditText variantInput = findViewById(R.id.variantInput);
        if (brandSpinner.getSelectedItemId() > 0 && modelSpinner.getSelectedItemId() > 0 && nameEditText.getText() != null
                && !nameEditText.getText().toString().equals("")) {
            Intent returnIntent = new Intent();
            Wheel wheel = new Wheel((String) brandSpinner.getSelectedItem(), (String) modelSpinner.getSelectedItem(),
                    nameEditText.getText().toString(), variantInput.getText().toString());
            WheelService.saveWheel(wheel);
            returnIntent.putExtra("name", nameEditText.getText().toString());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

        } else {
            final Snackbar errorSnackBar = Snackbar
                    .make(view, "Make, model or name is not filled. Please fill all of those", Snackbar.LENGTH_LONG);

            errorSnackBar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    errorSnackBar.dismiss();
                }
            }).show(); // Don’t forget to show!
        }
    }
}
