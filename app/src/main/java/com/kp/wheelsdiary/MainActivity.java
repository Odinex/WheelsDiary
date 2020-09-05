package com.kp.wheelsdiary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.kp.wheelsdiary.dto.Task;
import com.kp.wheelsdiary.dto.Wheel;
import com.kp.wheelsdiary.service.TaskService;
import com.kp.wheelsdiary.service.WheelService;
import com.kp.wheelsdiary.ui.login.LoginActivity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int LOGIN_RESULT = 123;
    public static final int ADD_WHEEL_RESULT = 234;
    public static final String ALL = "All";
    DrawerLayout drawerLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    TabLayout tabLayout;
    FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton addWheelFab;
    private boolean isFABOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_main);

        setupNavigationView();
        setupToolbar();
        setupTablayout();
        setupCollapsingToolbarLayout();
        setupFab();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, LOGIN_RESULT);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        addWheelFab = (FloatingActionButton) findViewById(R.id.addWheelFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        addWheelFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addWheelIntent = new Intent(view.getContext(), AddWheelActivity.class);
                startActivityForResult(addWheelIntent, ADD_WHEEL_RESULT);
            }
        });
        Objects.requireNonNull(tabLayout.getTabAt(0)).select();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
//                View viewById = findViewById(R.id);
//                TextView greeting = (TextView) viewById;
//                greeting.setText(String.format("Hello %s", result));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Not logged in
            }
        } else if (requestCode == ADD_WHEEL_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String tabName = data.getStringExtra("name");
                tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                final TabLayout.Tab tab = tabLayout.newTab().setText(tabName);

                tabLayout.addTab(tab);
                Snackbar openNewTab = Snackbar
                        .make(findViewById(R.id.coordinatorLayout), "Open the new tab '" + tabName + "'", Snackbar.LENGTH_LONG);

                openNewTab.setAction("Open", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tab.select();
                        // TODO refresh data for the car
                    }
                } ).show(); // Don’t forget to show!
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Not logged in
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        int asd = R.menu.menu;
        inflater.inflate(asd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout != null)
                    drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void setupCollapsingToolbarLayout() {

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(toolbar.getTitle());
            //collapsingToolbarLayout.setCollapsedTitleTextColor(0xED1C24);
            //collapsingToolbarLayout.setExpandedTitleColor(0xED1C24);
        }
    }

    private void setupTablayout() {

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        if (tabLayout == null)
            return;

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                String tabName = tab.getText().toString();
                System.out.println(tabName + "tabName");
                List<Task> tasks = getTasks(tabName);
                for(Task task : tasks) {
                    System.out.println(new Date() + "is now. Task:  " + task);
                    // TODO make cards with the tasks
                }
                String title = ("Wheel Diary of " + tabName);
                System.out.println("Title: " + title);
                toolbar.setTitle(title);
                setupCollapsingToolbarLayout();
            }

            private List<Task> getTasks(String tabName) {
                List<Task> tasks;
                if(tabName.equals(ALL)) {
                    tasks = TaskService.getTasks();
                } else {
                    tasks = TaskService.getTasksForWheel(tabName);
                }
                return tasks;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TabLayout.Tab all = tabLayout.newTab().setText(ALL);
        tabLayout.addTab(all);
        addTabs(tabLayout);
        all.select();



    }

    private void addTabs(TabLayout tabLayout) {
        for(Wheel wheel : WheelService.getWheels()) {
            TabLayout.Tab tab = tabLayout.newTab().setText(wheel.getName());
            tabLayout.addTab(tab);
        }
    }

    private void setupFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null)
            fab.setOnClickListener(this);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);

        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.fab) {


//            Snackbar
//                    .make(findViewById(R.id.coordinatorLayout), "This is Snackbar", Snackbar.LENGTH_LONG)
//                    .setAction("Action", this)
//                    .show(); // Don’t forget to show!
        }
    }

    private void showFABMenu() {
        isFABOpen = true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        addWheelFab.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        fab.animate().rotation(45);
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        addWheelFab.animate().translationY(0);
        fab.animate().rotation(0);
    }
}
