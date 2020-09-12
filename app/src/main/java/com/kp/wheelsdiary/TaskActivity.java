package com.kp.wheelsdiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.kp.wheelsdiary.dto.WheelTask;
import com.kp.wheelsdiary.dto.Wheel;
import com.kp.wheelsdiary.enums.TaskTypeEnum;
import com.kp.wheelsdiary.service.WheelTaskService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TaskActivity extends AppCompatActivity {

    private WheelTask currentWheelTask = null;
    public static final int OTHER_TASK_TYPE_INDEX = TaskTypeEnum.OTHER.ordinal() + 1;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String tabName = null;
        String mode = getIntent().getStringExtra("MODE");
        if (Objects.equals(mode, "ADD")) {
            tabName = getIntent().getStringExtra("TAB_NAME");
        } else if (Objects.equals(mode, "EDIT")) {
            Long taskId = getIntent().getLongExtra("TASK_ID", -1L);
            try {
                currentWheelTask = WheelTaskService.getTaskById(taskId);
                tabName = currentWheelTask.getWheel().getName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String[] wheelNameArray = WheelService.getWheelNameArray();
        int wheelNameIndex = getWheelNameIndex(tabName, wheelNameArray);

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

        if (currentWheelTask != null) {
            taskTypeSpinner.setSelection(currentWheelTask.getTaskType().ordinal() + 1);
            final EditText otherTypeEditText = findViewById(R.id.otherTypeEditText);
            final EditText detailsInput = findViewById(R.id.detailsInput);
            final EditText dateScheduled = findViewById(R.id.dateScheduled);
            if (currentWheelTask.getTaskType() == TaskTypeEnum.OTHER) {
                otherTypeEditText.setText(currentWheelTask.getOtherTaskType());
            }
            detailsInput.setText(currentWheelTask.getDetails());
            dateScheduled.setText(format.format(currentWheelTask.getDateScheduled()));
            TextView textView = findViewById(R.id.task_title);
            String text = "Task " + currentWheelTask.getId();
            textView.setText(text);
            String buttonSaveText = "Save Task " + currentWheelTask.getId();
            addTaskButton.setText(buttonSaveText);
        }
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Spinner carSpinner = findViewById(R.id.carSpinner);

                final Spinner taskTypeSpinner = findViewById(R.id.taskTypeSpinner);
                final EditText otherTypeEditText = findViewById(R.id.otherTypeEditText);
                final EditText detailsInput = findViewById(R.id.detailsInput);
                final EditText dateScheduledView = findViewById(R.id.dateScheduled);
                if (areFieldsValid(carSpinner, taskTypeSpinner, otherTypeEditText, detailsInput, dateScheduledView)) {

                    try {
                        Date dateScheduled = format.parse(dateScheduledView.getText().toString());

                        TaskTypeEnum taskType = TaskTypeEnum.valueOf((String) taskTypeSpinner.getSelectedItem());
                        WheelTask wheelTask;
                        String details = detailsInput.getText().toString();
                        String wheelName = (String) carSpinner.getSelectedItem();

                        Wheel wheelByName = WheelService.getWheelByName(wheelName);
                        if(currentWheelTask == null) {
                            if (taskType != TaskTypeEnum.OTHER) {
                                wheelTask = new WheelTask(dateScheduled, taskType,
                                        details, wheelByName, WheelTaskService.getNextId());
                            } else {
                                String otherTaskType = otherTypeEditText.getText().toString();
                                wheelTask = new WheelTask(dateScheduled, taskType, otherTaskType,
                                        details, wheelByName, WheelTaskService.getNextId());
                            }
                            try {
                                WheelTaskService.saveTask(wheelTask);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            currentWheelTask.setDateScheduled(dateScheduled);
                            currentWheelTask.setTaskType(taskType);
                            if (taskType == TaskTypeEnum.OTHER) {
                                currentWheelTask.setOtherTaskType(otherTypeEditText.getText().toString());
                            }
                            currentWheelTask.setDetails(details);
                            currentWheelTask.setWheel(wheelByName);
                            WheelTaskService.updateTask(currentWheelTask);
                        }
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("RESULT", "OK");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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


    private int getWheelNameIndex(String tabName, String[] wheelNameArray) {
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

    public WheelTask getCurrentWheelTask() {
        return currentWheelTask;
    }

    public void setCurrentWheelTask(WheelTask currentWheelTask) {
        this.currentWheelTask = currentWheelTask;
    }
}
