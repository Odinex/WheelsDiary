package com.kp.wheelsdiary;

import android.graphics.Color;
import android.os.Bundle;

import com.kp.wheelsdiary.enums.TaskTypeEnum;
import com.kp.wheelsdiary.service.WheelService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String tabName = getIntent().getStringExtra("TAB_NAME");
        int wheelNameIndex = -1;
        String[] wheelNameArray = WheelService.getWheelNameArray();
        for (int i = 0, wheelNameArrayLength = wheelNameArray.length; i < wheelNameArrayLength; i++) {
            String wheelName = wheelNameArray[i];
            if (wheelName.equals(tabName)) {
                wheelNameIndex = i;
                break;
            }
        }

        final Spinner wheelSpinner = findViewById(R.id.carSpinner);
        setSpinnerValues(wheelNameArray,wheelSpinner);
        if(wheelNameIndex != -1) {
            wheelSpinner.setSelection(wheelNameIndex);
        }
        String[] taskTypeArray = TaskTypeEnum.getTaskTypeArray();
        final Spinner taskTypeSpinner = findViewById(R.id.taskTypeSpinner);
        setSpinnerValues(taskTypeArray,taskTypeSpinner);
        final Button button = findViewById(R.id.pickDateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    private void setSpinnerValues(String[] values, Spinner spinner) {
        final List<String> plantsList = new ArrayList<>(Arrays.asList(values));

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

}
