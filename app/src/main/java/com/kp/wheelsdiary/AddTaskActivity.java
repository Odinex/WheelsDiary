package com.kp.wheelsdiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.kp.wheelsdiary.dto.Task;
import com.kp.wheelsdiary.dto.Wheel;
import com.kp.wheelsdiary.enums.TaskTypeEnum;
import com.kp.wheelsdiary.service.TaskService;
import com.kp.wheelsdiary.service.WheelService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity {

    public static final int OTHER_TASK_TYPE_INDEX = TaskTypeEnum.OTHER.ordinal() + 1;

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
        setSpinnerValues(wheelNameArray, wheelSpinner);
        if (wheelNameIndex != -1) {
            wheelSpinner.setSelection(wheelNameIndex);
        }
        String[] taskTypeArray = TaskTypeEnum.getTaskTypeArray();
        final Spinner taskTypeSpinner = findViewById(R.id.taskTypeSpinner);
        taskTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                EditText other = findViewById(R.id.otherTypeEditText);
                if (i == OTHER_TASK_TYPE_INDEX) {
                    other.setEnabled(true);
                } else {
                    other.setEnabled(false);
                    other.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setSpinnerValues(taskTypeArray, taskTypeSpinner);
        final Button button = findViewById(R.id.pickDateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        final Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Spinner carSpinner = findViewById(R.id.carSpinner);

                final Spinner taskTypeSpinner = findViewById(R.id.taskTypeSpinner);
                final EditText otherTypeEditText = findViewById(R.id.otherTypeEditText);
                final EditText detailsInput = findViewById(R.id.detailsInput);
                final EditText dateScheduled = findViewById(R.id.dateScheduled);
                if (areFieldsValid(carSpinner, taskTypeSpinner, otherTypeEditText, detailsInput, dateScheduled)) {
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                    Intent returnIntent = new Intent();
                    Task task = null;
                    try {
                        TaskTypeEnum taskType = TaskTypeEnum.valueOf((String) taskTypeSpinner.getSelectedItem());
                        if (taskType != TaskTypeEnum.OTHER) {
                            task = new Task(format.parse(dateScheduled.getText().toString()), taskType,
                                    detailsInput.getText().toString(), (String) carSpinner.getSelectedItem());
                        } else {
                            task = new Task(format.parse(dateScheduled.getText().toString()), taskType, otherTypeEditText.getText().toString(),
                                    detailsInput.getText().toString(), (String) carSpinner.getSelectedItem());
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (task != null) {
                        TaskService.saveTask(task);
                    } else {
                        final Snackbar errorSnackBar = Snackbar
                                .make(view, "Car, task type, other task type, details or date scheduled is not filled. Please fill all of those", Snackbar.LENGTH_LONG);

                        errorSnackBar.setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                errorSnackBar.dismiss();
                            }
                        }).show();
                    }
                    returnIntent.putExtra("RESULT", "OK");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                } else {
                    final Snackbar errorSnackBar = Snackbar
                            .make(view, "Car, task type, other task type, details or date scheduled is not filled. Please fill all of those", Snackbar.LENGTH_LONG);

                    errorSnackBar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            errorSnackBar.dismiss();
                        }
                    }).show(); // Don’t forget to show!
                }
            }

            private boolean areFieldsValid(Spinner carSpinner, Spinner taskTypeSpinner, EditText otherTypeEditText, EditText detailsInput, EditText dateScheduled) {
                return carSpinner.getSelectedItemId() > 0 && taskTypeSpinner.getSelectedItemId() > 0 && detailsInput.getText() != null
                        && !detailsInput.getText().toString().equals("") && dateScheduled.getText() != null
                        && !dateScheduled.getText().toString().equals("") && (taskTypeSpinner.getSelectedItemId() != OTHER_TASK_TYPE_INDEX || otherTypeEditText.getText() != null
                        && !otherTypeEditText.getText().toString().equals(""));
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
