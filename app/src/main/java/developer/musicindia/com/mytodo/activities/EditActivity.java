package developer.musicindia.com.mytodo.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import developer.musicindia.com.mytodo.R;
import developer.musicindia.com.mytodo.UI.EditToDoPresenter;
import developer.musicindia.com.mytodo.UI.EditToDoView;
import developer.musicindia.com.mytodo.app.MyToApplication;
import developer.musicindia.com.mytodo.dto.DTOProviderTODO;
import developer.musicindia.com.mytodo.model.TODO;
import developer.musicindia.com.mytodo.services.RemindMyTODO;

import javax.inject.Inject;

public class EditActivity extends AppCompatActivity implements EditToDoView,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    @Inject
    EditToDoPresenter editToDoPresenter;

    @BindView(R.id.todoDone)
    ImageView todoDone;
    @BindView(R.id.todoDelete)
    ImageView todoDelete;

    private Date mUserReminderDate;

    @BindView(R.id.todoTitleInput)
    EditText todoTitleInput;
    @BindView(R.id.desMessageBox)
    EditText desMessageBox;
    @BindView(R.id.alarmImageView)
    ImageView alarmImageView;
    @BindView(R.id.dayOfReminder)
    TextView dayOfReminder;
    @BindView(R.id.timeImageView)
    ImageView timeImageView;
    @BindView(R.id.dateOfReminder)
    TextView dateOfReminder;
    @BindView(R.id.reset)
    Button reset;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.save)
    Button save;



    private TODO mUserToDoItem;

    private static int todoId;
    private static String todoText;
    private static String todoTextDes;
    private static Date reminderTime;
    private static int completed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittodo);
        ButterKnife.bind(this);
        ((MyToApplication)getApplication()).getAppComponent().inject(this);

        editToDoPresenter.setView(this);

        mUserToDoItem = (TODO)getIntent().getSerializableExtra(MainActivity.TODO);

        todoId = mUserToDoItem.getTodoId();
        todoText = mUserToDoItem.getTodoText();
        todoTextDes = mUserToDoItem.getTodoTextDes();
        reminderTime = mUserToDoItem.getReminderTime();
        mUserReminderDate = mUserToDoItem.getReminderTime();
        completed = mUserToDoItem.getCompleted();

        todoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditActivity.this);
                alertDialogBuilder.setMessage("Are you sure,You wanted to delete this TODO");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        TODO todo = mUserToDoItem;

                        editToDoPresenter.deleteTODOfromDatabase(todoId);


                    }
                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

        if(completed == 0 ){

            Drawable d = getResources().getDrawable(R.drawable.btn_chkbox_disabled);
            todoDone.setImageDrawable(d);
            
        }
        else{
            Drawable d = getResources().getDrawable(R.drawable.ic_checkbox_marked_circle);
            todoDone.setImageDrawable(d);
        }

        todoDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFade;

                animFade = AnimationUtils.loadAnimation(EditActivity.this, R.anim.fadeout);


                animFade.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) {}
                    public void onAnimationRepeat(Animation animation) {}
                    public void onAnimationEnd(Animation animation) {
                        // when fadeout animation ends, fade in your second image

                        if(completed == 0){
                            Drawable d = getResources().getDrawable(R.drawable.ic_checkbox_marked_circle);
                            todoDone.setImageDrawable(d);

                            TODO item = mUserToDoItem;
                            item.setCompleted(1);
                            editToDoPresenter.updateTODOIteminDatabase(item);

                            completed = 1;
                            mUserToDoItem.setCompleted(1);

                        }
                        else{
                            Drawable d = getResources().getDrawable(R.drawable.btn_chkbox_disabled);
                            todoDone.setImageDrawable(d);

                            TODO item = mUserToDoItem;
                            item.setCompleted(0);
                            editToDoPresenter.updateTODOIteminDatabase(item);
                            completed = 0;
                            mUserToDoItem.setCompleted(0);
                        }

                    }
                });
                todoDone.startAnimation(animFade);

            }
        });

        todoTitleInput.setText(todoText.toString());
        desMessageBox.setText(todoTextDes.toString());

        alarmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date;

                if (mUserToDoItem.getReminderTime() != null) {

                    date = mUserReminderDate;
                } else {
                    date = new Date();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(EditActivity.this, year, month, day);

                datePickerDialog.show(getFragmentManager(), "DateFragment");

            }
        });


        timeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date;

                if (mUserToDoItem.getReminderTime() != null) {
//                    date = mUserToDoItem.getToDoDate();
                    date = mUserReminderDate;
                } else {
                    date = new Date();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(EditActivity.this, hour, minute, DateFormat.is24HourFormat(EditActivity.this));

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


                if(title != null && desc != null && title.length() > 0 && desc.length() > 0) {
                    TODO todo = new TODO(mUserToDoItem.getTodoId(), title, desc, date, completed);


                    editToDoPresenter.updateTODOIteminDatabaseAndClose(todo);

                }
                else{
                   // Toast.makeText(EditActivity.this, "Please fill Title and Desc Properly", Toast.LENGTH_SHORT).show();
                    Snackbar.make(v,"Please fill Title and Desc Properly",Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                todoTitleInput.setText(todoText);
                desMessageBox.setText(todoTextDes);
                setDateAndTimeEditTextReset();

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

        if(mUserReminderDate!=null){
            String userDate = formatDate("d MMM, yyyy", mUserReminderDate);
            String formatToUse;
            if(DateFormat.is24HourFormat(this)){
                formatToUse = "k:mm";
            }
            else{
                formatToUse = "h:mm a";

            }
            String userTime = formatDate(formatToUse, mUserReminderDate);
            dateOfReminder.setText(userTime);
            dayOfReminder.setText(userDate);

        }
        else {
            dayOfReminder.setText("TODAY");
            boolean time24 = DateFormat.is24HourFormat(this);
            Calendar cal = Calendar.getInstance();
            if (time24) {
                cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            } else {
                cal.set(Calendar.HOUR, cal.get(Calendar.HOUR));
            }

            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
            mUserReminderDate = cal.getTime();
            String timeString;
            if (time24) {
                timeString = formatDate("k:mm", mUserReminderDate);
            } else {
                timeString = formatDate("h:mm a", mUserReminderDate);
            }
            dateOfReminder.setText(timeString);
        }

    }

    private void setDateAndTimeEditTextReset(){

        if(reminderTime!=null){
            String userDate = formatDate("d MMM, yyyy", reminderTime);
            String formatToUse;
            if(DateFormat.is24HourFormat(this)){
                formatToUse = "k:mm";
            }
            else{
                formatToUse = "h:mm a";

            }
            String userTime = formatDate(formatToUse, reminderTime);
            mUserReminderDate = reminderTime;
            dateOfReminder.setText(userTime);
            dayOfReminder.setText(userDate);

        }
        else {
            dayOfReminder.setText("TODAY");
            boolean time24 = DateFormat.is24HourFormat(this);
            Calendar cal = Calendar.getInstance();
            if (time24) {
                cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            } else {
                cal.set(Calendar.HOUR, cal.get(Calendar.HOUR));
            }

            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
            mUserReminderDate = cal.getTime();
            String timeString;
            if (time24) {
                timeString = formatDate("k:mm", mUserReminderDate);
            } else {
                timeString = formatDate("h:mm a", mUserReminderDate);
            }
            dateOfReminder.setText(timeString);
        }

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

    @Override
    public void savedSuccessfully(TODO todo) {

        Toast.makeText(EditActivity.this, "TODO Updated Successfully", Toast.LENGTH_SHORT).show();

        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), RemindMyTODO.class);
        intent.putExtra("TODOREMINDER", todo);
        PendingIntent alarmIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        Calendar currentCal = Calendar.getInstance();
        Date today = currentCal.getTime();
        if(todo.getReminderTime().after(today)) {

            alarmMgr.set(AlarmManager.RTC_WAKEUP, mUserReminderDate.getTime(), alarmIntent);

        }

        EditActivity.this.finish();

    }

    @Override
    public void deletedSuccessfully(){
        Toast.makeText(EditActivity.this,"Deleted TODO Successfully",Toast.LENGTH_SHORT).show();
        EditActivity.this.finish();
    }

}
