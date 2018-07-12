package com.purnima.alarmreminder;

import android.app.Dialog;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.view.*;
import android.content.CursorLoader;
import android.content.Intent;
import android.widget.ListView;
import com.delaroystudios.alarmreminder.R;
import com.purnima.alarmreminder.data.AlarmReminderContract;
import com.purnima.alarmreminder.data.AlarmReminderDbHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ProgressDialog prgDialog;
    /* int item;
    int count;
     */
    View emptyView;
    Dialog onclick;

    private FloatingActionButton mButton;
    private Toolbar tb;
    AlarmCursorAdapter Cadp;
    ArrayList<String> arrayList;
    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(this);
    ListView remind;



    private static final int VEHICLE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setTitle(R.string.app_name);

/*
* reminder list
* */
        remind = (ListView) findViewById(R.id.list);
        emptyView = findViewById(R.id.empty_view);
        remind.setEmptyView(emptyView);

        Cadp = new AlarmCursorAdapter(this, null);
        remind.setAdapter(Cadp);

        remind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);
/*
* intent.setData();
*
*
* */
                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);


                intent.setData(currentVehicleUri);

                startActivity(intent);

            }
        });


        mButton = (FloatingActionButton) findViewById(R.id.fab);

        /*
        * moving to reminder activity
        *
        * */

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddReminderActivity.class);
                startActivity(intent);
            }
        });



        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);


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
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

    }
    public  void stopActivity(){
    //    AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO

    }
    public void NullFunction(){

       // AlarmReminderContract.BASE_CONTENT_URI;
        Toast.makeText(MainActivity.this, "Null !!!",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Cadp.swapCursor(cursor);

    }

    public void onLoadStart(){
        Cadp.swapCursor(null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Cadp.swapCursor(null);

    }

}
