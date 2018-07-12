package com.purnima.alarmreminder;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.delaroystudios.alarmreminder.R;
import com.purnima.alarmreminder.data.AlarmReminderContract;
import com.purnima.alarmreminder.reminder.AlarmScheduler;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


public class AddReminderActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_VEHICLE_LOADER = 0;

    private String mRepeat;
    private String mrn;

    private String time1;
    private TextView Dateview
            , timeshow
            , onrepeate1
            , it1
            , Settype;

    private String mDate;
    private FloatingActionButton btn1;


    private FloatingActionButton btn2;


    private String av;

    private Toolbar tb;
    private EditText txt1;
    private String rt12;
    private String tl1;


    private Calendar cal;
    private int yr, month, hr, min, day;
    private long mRepeatTime;
    private Switch mRepeatSwitch;

    private Uri mCurrentReminderUri;
    private boolean flag1 = false;

// time initialise
    private static final long min1 = 60000L;
    private static final long hr1 = 3600000L;
    private static final long day1 = 86400000L;
    private static final long week1 = 604800000L;
    private static final long mont1 = 2592000000L;

    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";




    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            flag1 = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();

        if (mCurrentReminderUri == null) {

            setTitle(getString(R.string.editor_activity_title_new_reminder));


            invalidateOptionsMenu();
        } else {

            setTitle(getString(R.string.editor_activity_title_edit_reminder));


            getLoaderManager().initLoader(EXISTING_VEHICLE_LOADER, null, this);
        }

        av = "true";
        mRepeat = "true";
        mrn = Integer.toString(1);
        rt12 = "Hour";

        cal = Calendar.getInstance();
        hr = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);
        yr = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DATE);

        mDate = day + "/" + month + "/" + yr;
        time1 = hr + ":" + min;

        btn1 = (FloatingActionButton) findViewById(R.id.starred1);
        btn2 = (FloatingActionButton) findViewById(R.id.starred2);


        tb = (Toolbar) findViewById(R.id.toolbar);


        Settype = (TextView) findViewById(R.id.set_repeat_type);
        mRepeatSwitch = (Switch) findViewById(R.id.repeat_switch);


        timeshow = (TextView) findViewById(R.id.set_time);
        onrepeate1 = (TextView) findViewById(R.id.set_repeat);
        it1 = (TextView) findViewById(R.id.set_repeat_no);
        txt1 = (EditText) findViewById(R.id.reminder_title);
        Dateview = (TextView) findViewById(R.id.set_date);
        txt1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tl1 = s.toString().trim();
                txt1.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Dateview.setText(mDate);
        timeshow.setText(time1);
        it1.setText(mrn);
        Settype.setText(rt12);
        onrepeate1.setText("Every " + mrn + " " + rt12 + "(s)");

        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            txt1.setText(savedTitle);
            tl1 = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            timeshow.setText(savedTime);
            time1 = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            Dateview.setText(savedDate);
            mDate = savedDate;

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            onrepeate1.setText(saveRepeat);
            mRepeat = saveRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            it1.setText(savedRepeatNo);
            mrn = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            Settype.setText(savedRepeatType);
            rt12 = savedRepeatType;

            av = savedInstanceState.getString(KEY_ACTIVE);
        }

        if (av.equals("false")) {
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.GONE);

        } else if (av.equals("true")) {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(tb);
        getSupportActionBar().setTitle(R.string.title_activity_add_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, txt1.getText());
        outState.putCharSequence(KEY_TIME, timeshow.getText());
        outState.putCharSequence(KEY_DATE, Dateview.getText());
        outState.putCharSequence(KEY_REPEAT, onrepeate1.getText());
        outState.putCharSequence(KEY_REPEAT_NO, it1.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, Settype.getText());
        outState.putCharSequence(KEY_ACTIVE, av);
    }

    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void setTime(View v){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }


    public void selectFab1(View v) {
        btn1 = (FloatingActionButton) findViewById(R.id.starred1);
        btn1.setVisibility(View.GONE);
        btn2 = (FloatingActionButton) findViewById(R.id.starred2);
        btn2.setVisibility(View.VISIBLE);
        av = "true";
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        hr = hourOfDay;
        min = minute;
        if (minute < 10) {
            time1 = hourOfDay + ":" + "0" + minute;
        } else {
            time1 = hourOfDay + ":" + minute;
        }
        timeshow.setText(time1);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        day = dayOfMonth;
        month = monthOfYear;
        yr = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        Dateview.setText(mDate);
    }

    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat = "true";
            onrepeate1.setText("Every " + mrn + " " + rt12 + "(s)");
        } else {
            mRepeat = "false";
            onrepeate1.setText(R.string.repeat_off);
        }
    }

    public void selectFab2(View v) {
        btn2 = (FloatingActionButton) findViewById(R.id.starred2);
        btn2.setVisibility(View.GONE);
        btn1 = (FloatingActionButton) findViewById(R.id.starred1);
        btn1.setVisibility(View.VISIBLE);
        av = "false";
    }



    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "Minute";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                rt12 = items[item];
                Settype.setText(rt12);
                onrepeate1.setText("Every " + mrn + " " + rt12 + "(s)");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setRepeatNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mrn = Integer.toString(1);
                            it1.setText(mrn);
                            onrepeate1.setText("Every " + mrn + " " + rt12 + "(s)");
                        }
                        else {
                            mrn = input.getText().toString().trim();
                            it1.setText(mrn);
                            onrepeate1.setText("Every " + mrn + " " + rt12 + "(s)");
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentReminderUri == null) {
            MenuItem menuItem = menu.findItem(R.id.discard_reminder);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_reminder:


                if (txt1.getText().toString().length() == 0){
                    txt1.setError("Reminder Title cannot be blank!");
                }

                else {
                    saveReminder();
                    finish();
                }
                return true;
            case R.id.discard_reminder:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!flag1) {
                    NavUtils.navigateUpFromSameTask(AddReminderActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(AddReminderActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteReminder();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteReminder() {
        if (mCurrentReminderUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentReminderUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    public void saveReminder(){


        ContentValues values = new ContentValues();

        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, tl1);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, time1);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mrn);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, rt12);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, av);


        cal.set(Calendar.MONTH, --month);
        cal.set(Calendar.YEAR, yr);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, 0);

        long selectedTimestamp =  cal.getTimeInMillis();

        if (rt12.equals("Minute")) {
            mRepeatTime = Integer.parseInt(mrn) * min1;
        } else if (rt12.equals("Hour")) {
            mRepeatTime = Integer.parseInt(mrn) * hr1;
        } else if (rt12.equals("Day")) {
            mRepeatTime = Integer.parseInt(mrn) * day1;
        } else if (rt12.equals("Week")) {
            mRepeatTime = Integer.parseInt(mrn) * week1;
        } else if (rt12.equals("Month")) {
            mRepeatTime = Integer.parseInt(mrn) * mont1;
        }

        if (mCurrentReminderUri == null) {

            Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentReminderUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        if (av.equals("true")) {
            if (mRepeat.equals("true")) {
                new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri, mRepeatTime);
            } else if (mRepeat.equals("false")) {
                new AlarmScheduler().setAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri);
            }

            Toast.makeText(this, "Alarm time is " + selectedTimestamp,
                    Toast.LENGTH_LONG).show();
        }

        Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }




    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE,
        };

        return new CursorLoader(this,
                mCurrentReminderUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }


        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_DATE);
            int timeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TIME);
            int repeatColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
            int repeatNoColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
            int repeatTypeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
            int activeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE);

            String title = cursor.getString(titleColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            String repeat = cursor.getString(repeatColumnIndex);
            String repeatNo = cursor.getString(repeatNoColumnIndex);
            String repeatType = cursor.getString(repeatTypeColumnIndex);
            String active = cursor.getString(activeColumnIndex);



            txt1.setText(title);
            Dateview.setText(date);
            timeshow.setText(time);
            it1.setText(repeatNo);
            Settype.setText(repeatType);
            onrepeate1.setText("Every " + repeatNo + " " + repeatType + "(s)");

            if (repeat.equals("false")) {
                mRepeatSwitch.setChecked(false);
                onrepeate1.setText(R.string.repeat_off);

            } else if (repeat.equals("true")) {
                mRepeatSwitch.setChecked(true);
            }

        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
