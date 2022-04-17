package com.fitgroup.fitpower.main.calendar;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;
import static com.fitgroup.fitpower.utils.StaticVar.selectedCalendarDate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.NavActivity;
import com.fitgroup.fitpower.main.calendar.view.CalendarAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalendarActivity extends NavActivity implements CalendarAdapter.OnItemListener {

    private TextView dateLabel;
    private RecyclerView calendar;



    public CalendarActivity() {
        super(R.id.calendar_drawer, R.id.calendar_nav,R.layout.activity_calendar);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setMonthView(){
        dateLabel.setText(monthYearFromDate());
        ArrayList<String> daysInMonth = getDaysInMonth();
        CalendarAdapter adapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),7);
        calendar.setLayoutManager(manager);
        calendar.setAdapter(adapter);
    }

    private ArrayList<String> getDaysInMonth(){
        ArrayList<String> days = new ArrayList<>();

        Calendar calendar = selectedCalendarDate;

        calendar.set(Calendar.DAY_OF_MONTH,1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.MONTH,1);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        int daysInMonth = calendar.get(Calendar.DAY_OF_MONTH);
        for(int i = 2; i <= 43; i++){
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                days.add("");
            }else{
                days.add(String.valueOf(i - dayOfWeek));
            }
        }

        return days;
    }

    private String monthYearFromDate(){
        return new SimpleDateFormat("MMMM yyyy",Locale.CANADA).format(selectedCalendarDate.getTime());
    }

    @Override
    protected void initiateFields() {
        calendar = findViewById(R.id.cal_calendar_view);
        dateLabel = findViewById(R.id.dcal_date_lbl);

        ImageButton prev = findViewById(R.id.cal_prev_btn);
        ImageButton next = findViewById(R.id.cal_next_btn);

        prev.setOnClickListener(view -> previousMonthAction());
        next.setOnClickListener(view -> nextMonthAction());

        setMonthView();
    }

    public void nextMonthAction()
    {
        selectedCalendarDate.add(Calendar.MONTH,1);
        setMonthView();
    }

    public void previousMonthAction()
    {
        selectedCalendarDate.add(Calendar.MONTH,-1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(!dayText.equals("")){
            selectedCalendarDate.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dayText));;
            openDailyCalendar();
        }
    }

    protected void openDailyCalendar(){
        activeActivity = R.layout.activity_dailycalendar;
        startActivity(new Intent(this, DailyCalendar.class));
    }
}
