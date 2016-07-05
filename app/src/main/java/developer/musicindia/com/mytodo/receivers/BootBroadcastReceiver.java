package developer.musicindia.com.mytodo.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import developer.musicindia.com.mytodo.dto.DTOProviderTODO;
import developer.musicindia.com.mytodo.model.TODO;
import developer.musicindia.com.mytodo.services.RemindMyTODO;

/**
 * Created by amitagarwal3 on 7/4/2016.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context pContext, Intent intent) {
        // Do your work related to alarm manager

        ArrayList<TODO> todoList = DTOProviderTODO.getTODOListinDatabase(pContext);

        for(int i = 0; i < todoList.size() ; i++){

            TODO todo = todoList.get(i);
            AlarmManager alarmMgr = (AlarmManager)pContext.getSystemService(Context.ALARM_SERVICE);
            Intent intentAlarm = new Intent(pContext, RemindMyTODO.class);
            intent.putExtra("TODOREMINDER", todo);
            PendingIntent alarmIntent = PendingIntent.getService(pContext, 0, intentAlarm, 0);
            Calendar currentCal = Calendar.getInstance();
            Date today = currentCal.getTime();
            if(todo.getReminderTime().after(today)) {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, todo.getReminderTime().getTime(), alarmIntent);
            }

        }


    }


}
