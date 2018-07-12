package com.purnima.alarmreminder.reminder;

import android.app.AlarmManager;
import android.content.Context;
import android.widget.Toast;


public class AlarmManagerProvider {
    private static final String TAG = AlarmManagerProvider.class.getSimpleName();
    private static AlarmManager a;
    public static synchronized void injectAlarmManager(AlarmManager alarmManager) {
        if (a != null) {
            throw new IllegalStateException("Alarm Manager Already Set");
        }
        a = alarmManager;
      //  Toast.makeText(getApplicationContext(), " Generate toast",
        //        Toast.LENGTH_LONG).show();
    }
        static synchronized AlarmManager getAlarmManager(Context context) {
        if (a == null) {
            a = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return a;
    }
}
