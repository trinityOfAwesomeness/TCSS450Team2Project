package edu.tacoma.uw.tslinard.tcss450team2project.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import edu.tacoma.uw.tslinard.tcss450team2project.authenticate.SignInActivity;

public class MainMenuActivity extends AppCompatActivity {

    ImageButton NextButton, PreviousButton;
    TextView CurrentDate;
    GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar mainCalendar = Calendar.getInstance(Locale.ENGLISH);       // contains today's date
    GridAdapter myGridAdapter;
    List<Date> pageDates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        NextButton = findViewById(R.id.nextBtn);
        PreviousButton = findViewById(R.id.previousBtn);
        CurrentDate = findViewById(R.id.current_Date);
        gridView = findViewById(R.id.gridview);

        SetUpCalendar();

        PreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainCalendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainCalendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "You clicked " + position , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.action_logout){
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false).commit();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SetUpCalendar() {
        String currentDate = dateFormat.format(mainCalendar.getTime());
        CurrentDate.setText(currentDate);
        pageDates.clear();

        //setting up the start day of the calendar
        Calendar pageCalendar = (Calendar) mainCalendar.clone();
        pageCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = pageCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        pageCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);

        while (pageDates.size() < MAX_CALENDAR_DAYS) {
            pageDates.add(pageCalendar.getTime());
            pageCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        myGridAdapter = new GridAdapter(MainMenuActivity.this,pageDates,mainCalendar,eventsList);
        gridView.setAdapter(myGridAdapter);
    }
}