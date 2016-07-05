package developer.musicindia.com.mytodo.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import developer.musicindia.com.mytodo.R;
import developer.musicindia.com.mytodo.activities.EditActivity;
import developer.musicindia.com.mytodo.activities.MainActivity;
import developer.musicindia.com.mytodo.dto.DTOProviderTODO;
import developer.musicindia.com.mytodo.model.TODO;

public class RemindMyTODO extends IntentService {

    public RemindMyTODO() {
        super("RemindMyTODO");
    }

    private TODO mUserToDoItem;

    @Override
    protected void onHandleIntent(Intent intent) {

        mUserToDoItem = (TODO)intent.getSerializableExtra("TODOREMINDER");
        Date remDate = mUserToDoItem.getReminderTime();
        ArrayList<TODO> foundList =  DTOProviderTODO.getTODOIteminDatabase(this, mUserToDoItem.getTodoId());
        if (intent != null && mUserToDoItem != null && foundList != null && foundList.size() > 0) {

            Notification notification;
            Log.e("IN OnHandle Intent","here");
            Log.e("IN OnHandle Intent","here");
            Log.e("IN OnHandle Intent","here");
            Intent i = new Intent(this,EditActivity.class);
            i.putExtra(MainActivity.TODO,mUserToDoItem);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setOngoing(true);
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setWhen(0);
            mBuilder.setSmallIcon(R.drawable.action_setdate);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
            mBuilder.setContentTitle(mUserToDoItem.getTodoText());
            mBuilder.setContentText(mUserToDoItem.getTodoTextDes());
            mBuilder.setAutoCancel(true);

            notification = mBuilder.build();
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
            mNotificationManager.notify(1, notification);


        }
    }

}
