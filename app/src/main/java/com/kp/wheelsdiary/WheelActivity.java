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
import com.kp.wheelsdiary.data.model.User;
import com.kp.wheelsdiary.dto.Wheel;
import com.kp.wheelsdiary.service.WheelService;
import com.kp.wheelsdiary.service.WheelTaskService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WheelActivity extends AppCompatActivity {
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
    private Wheel currentWheel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wheel);
        // Get reference of widgets from XML layout
        String mode = getIntent().getStringExtra("MODE");
        if (Objects.equals(mode, "EDIT")) {
            String wheelName = getIntent().getStringExtra("WHEEL_NAME");
            if (wheelName == null || wheelName.isEmpty()) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
            try {
                currentWheel = WheelService.getWheelByName(wheelName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Initializing a String Array
        String[] brands = new String[]{
                "Select a make...",
                "AUDI",
                "Nissan",
                "Lamborghini",
                "Porsche"
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
        if (currentWheel != null) {
            int makeIndex = getIndex(currentWheel.getMake(), brands);
            if (makeIndex != -1) {
                brandSpinner.setSelection(makeIndex);
            }
            if (currentWheel.getVariant() != null && !currentWheel.getVariant().isEmpty()) {
                EditText variantInput = findViewById(R.id.variantInput);
                variantInput.setText(currentWheel.getVariant());
            }
            if (currentWheel.getName() != null && !currentWheel.getName().isEmpty()) {
                EditText name = findViewById(R.id.nameInput);
                name.setText(currentWheel.getName());
            }
            TextView titleView = findViewById(R.id.wheel_title);
            String title = "Car " + currentWheel.getId();
            titleView.setText(title);
            String save_car = "Save car";
            button.setText(save_car);
        }
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
                        String[] models = getModelsForMake(brand);
                        setSpinnerValues(models, modelSpinner);
                        if (currentWheel != null && currentWheel.getMake().equals(brand)) {
                            try {
                                int modelIndex = getIndex(currentWheel.getModel(), getModelsForMake(brandSpinner.getSelectedItem().toString()));

                                if (modelIndex != -1) {
                                    modelSpinner.setSelection(modelIndex);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

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

    private int getIndex(String tabName, String[] wheelNameArray) {
        int wheelNameIndex = -1;
        for (int i = 0, wheelNameArrayLength = wheelNameArray.length; i < wheelNameArrayLength; i++) {
            String wheelName = wheelNameArray[i];
            if (tabName != null && !tabName.isEmpty() && wheelName.equals(tabName)) {
                wheelNameIndex = i;
                break;
            }
        }
        return wheelNameIndex;
    }

    private String[] getModelsForMake(String make) throws Exception {
        List<String> models = new ArrayList<>();
        models.add("Select a model...");
        switch (make) {
            case "AUDI":
                models.addAll(Collections.singletonList("A4"));
                break;
            case "Nissan":
                models.addAll(Collections.singletonList("Micra"));
                break;
            case "Lamborghini":
                models.addAll(Arrays.asList("Aventador S", "URUS"));
                break;
            case "Porsche":
                models.addAll(Arrays.asList("718 Boxster S", "911 Carrera  4"));
                break;
            default:
                throw new Exception("Invalid make");
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
            String make = (String) brandSpinner.getSelectedItem();
            String model = (String) modelSpinner.getSelectedItem();
            String name = nameEditText.getText().toString();
            String variant = variantInput.getText() != null ? variantInput.getText().toString() : null;
            User currentUser = WheelService.getCurrentUser();
            if (currentWheel == null) {

                Wheel wheel = new Wheel(null, make, model,
                        name, variant, currentUser);
                try {
                    WheelService.saveWheel(wheel);
                } catch (Exception e) {
                    e.printStackTrace();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
                returnIntent.putExtra("name", name);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                try {
                    Wheel wheel = new Wheel(currentWheel.getId(), make, model,
                            name, variant, currentUser);
                    WheelService.updateWheel(wheel);
                } catch (Exception e) {
                    e.printStackTrace();
                    returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
                returnIntent.putExtra("name", name);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

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
