package com.purnima.alarmreminder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.purnima.alarmreminder.R;
import com.purnima.alarmreminder.data.AlarmReminderContract;



public class AlarmCursorAdapter extends CursorAdapter {
    private ColorGenerator cgn = ColorGenerator.DEFAULT;
    private TextDrawable dbr1;
    private TextView txt1
            , dttx1
            , reptx1;
    private ImageView Aimg
            , timg;
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.alarm_items, parent, false);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        dttx1 = (TextView) view.findViewById(R.id.recycle_date_time);


        timg = (ImageView) view.findViewById(R.id.thumbnail_image);

        reptx1 = (TextView) view.findViewById(R.id.recycle_repeat_info);
        txt1 = (TextView) view.findViewById(R.id.recycle_title);

        Aimg = (ImageView) view.findViewById(R.id.active_image);

        int timeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TIME);



        int activeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE);

        int repeatColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
        int repeatNoColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);

        int titleColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
        int dateColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_DATE);

        int repeatTypeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);

        String date = cursor.getString(dateColumnIndex);

        String repeatType = cursor.getString(repeatTypeColumnIndex);
        String active = cursor.getString(activeColumnIndex);

        String time = cursor.getString(timeColumnIndex);


        String title = cursor.getString(titleColumnIndex);

        String repeat = cursor.getString(repeatColumnIndex);
        String repeatNo = cursor.getString(repeatNoColumnIndex);


        String dateTime = date + " " + time;


        setReminderTitle(title);
        setReminderDateTime(dateTime);
        setReminderRepeatInfo(repeat, repeatNo, repeatType);
        setActiveImage(active);




    }
    public void setReminderDateTime(String datetime) {
        dttx1.setText(datetime);
    }


    public AlarmCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    public void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType) {
        if(repeat.equals("true")){
            reptx1.setText("Every " + repeatNo + " " + repeatType + "(s)");
        }else if (repeat.equals("false")) {
            reptx1.setText("Repeat Off");
        }
    }





    public void setReminderTitle(String title) {
        txt1.setText(title);
        String letter = "A";

        if(title != null && !title.isEmpty()) {
            letter = title.substring(0, 1);
        }

        int color = cgn.getRandomColor();

        dbr1 = TextDrawable.builder()
                .buildRound(letter, color);
        timg.setImageDrawable(dbr1);
    }


    public void setActiveImage(String active){
        if(active.equals("true")){
            Aimg.setImageResource(R.drawable.ic_notifications_on_white_24dp);
        }else if (active.equals("false")) {
            Aimg.setImageResource(R.drawable.ic_notifications_off_grey600_24dp);
        }
    }
}
