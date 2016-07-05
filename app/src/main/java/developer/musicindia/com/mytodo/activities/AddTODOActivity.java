package developer.musicindia.com.mytodo.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import developer.musicindia.com.mytodo.R;
import developer.musicindia.com.mytodo.dto.DTOProviderTODO;
import developer.musicindia.com.mytodo.model.TODO;
import developer.musicindia.com.mytodo.services.RemindMyTODO;

public class AddTODOActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private Date mUserReminderDate;
    private EditText todoTitleInput;
    private EditText desMessageBox;
    private ImageView alarmImageView;
    private TextView dayOfReminder;
    private ImageView timeImageView;
    private TextView dateOfReminder;
    private Button reset;
    private Button cancel;
    private Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtodo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        todoTitleInput = (EditText) findViewById(R.id.todoTitleInput);
        desMessageBox = (EditText) findViewById(R.id.desMessageBox);
        alarmImageView = (ImageView) findViewById(R.id.alarmImageView);
        dayOfReminder = (TextView) findViewById(R.id.dayOfReminder);
        timeImageView = (ImageView) findViewById(R.id.timeImageView);
        dateOfReminder = (TextView) findViewById(R.id.dateOfReminder);
        reset = (Button) findViewById(R.id.reset);
        cancel = (Button) findViewById(R.id.cancel);
        save = (Button) findViewById(R.id.save);
        alarmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = new Date();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(AddTODOActivity.this, year, month, day);

                datePickerDialog.setThemeDark(true);

                datePickerDialog.show(getFragmentManager(), "DateFragment");

            }
        });


        timeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = new Date();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(AddTODOActivity.this, hour, minute, DateFormat.is24HourFormat(AddTODOActivity.this));
                timePickerDialog.show(getFragmentManager(), "TimeFragment");
            }
        });

        setDateAndTimeEditText();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = todoTitleInput.getText().toString().toUpperCase();
                String desc = desMessageBox.getText().toString().toUpperCase();
                Date date = mUserReminderDate;
                int completed = 0;

                if(title != null && desc != null && title.length() > 0 && desc.length() > 0) {

                    TODO todo = new TODO(0, title, desc, date, completed);
                    ContentValues values = DTOProviderTODO.putContentValues(todo);
                    DTOProviderTODO.insertTODOIteminDatabase(AddTODOActivity.this, values);
                    int toDoId = DTOProviderTODO.getMAXTODOIteminDatabase(AddTODOActivity.this);
                    todo.setTodoId(toDoId);
                    Toast.makeText(AddTODOActivity.this, "TODO Added Successfully", Toast.LENGTH_SHORT).show();


                    AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), RemindMyTODO.class);
                    intent.putExtra("TODOREMINDER", todo);
                    PendingIntent alarmIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
                    Calendar currentCal = Calendar.getInstance();
                    Date today = currentCal.getTime();
                    if(todo.getReminderTime().after(today)) {


                        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                                mUserReminderDate.getTime(), alarmIntent);


                    }
                    finish();
                }
                else{
                   // Toast.makeText(AddTODOActivity.this, "Please fill Title and Desc Properlys", Toast.LENGTH_SHORT).show();
                    Snackbar.make(v,"Please fill Title and Desc Properly",Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                todoTitleInput.setText("");
                desMessageBox.setText("");
                setDateAndTimeEditText();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });



    }

    private void setDateAndTimeEditText(){

            dayOfReminder.setText("TODAY");
            boolean time24 = DateFormat.is24HourFormat(this);
            Calendar cal = Calendar.getInstance();
            if(time24){
                cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            }
            else{
                cal.set(Calendar.HOUR, cal.get(Calendar.HOUR));
            }

            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
            mUserReminderDate = cal.getTime();
            String timeString;
            if(time24){
                timeString = formatDate("k:mm", mUserReminderDate);
            }
            else{
                timeString = formatDate("h:mm a", mUserReminderDate);
            }
            dateOfReminder.setText(timeString);


    }

    public static String formatDate(String formatString, Date dateToFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return simpleDateFormat.format(dateToFormat);
    }



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setDate(year,monthOfYear,dayOfMonth);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        setTime(hourOfDay, minute);
    }

    public void setDate(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        int hour, minute;

        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);

        if(reminderCalendar.before(calendar)){
            Toast.makeText(this, "My time-machine is a bit rusty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mUserReminderDate!=null){
            calendar.setTime(mUserReminderDate);
        }

        if(DateFormat.is24HourFormat(this)){
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        }
        else{

            hour = calendar.get(Calendar.HOUR);
        }
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hour, minute);
        mUserReminderDate = calendar.getTime();

//        setDateAndTimeEditText();
        setDateEditText();
    }

    public void setTime(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        if(mUserReminderDate!=null){
            calendar.setTime(mUserReminderDate);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, hour, minute, 0);
        mUserReminderDate = calendar.getTime();

        String time = String.valueOf(mUserReminderDate);


        setTimeEditText();
    }

    public void  setDateEditText(){
        String dateFormat = "d MMM, yyyy";
        dayOfReminder.setText(formatDate(dateFormat, mUserReminderDate));
    }

    public void  setTimeEditText(){
        String dateFormat;
        if(DateFormat.is24HourFormat(this)){
            dateFormat = "k:mm";
        }
        else{
            dateFormat = "h:mm a";

        }
        dateOfReminder.setText(formatDate(dateFormat, mUserReminderDate));
    }

    @Override
    public void onResume(){
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    public void onPause(){
        super.onPause();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

}
