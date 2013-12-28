package com.uesocc.app;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by xtiyo on 12-21-13.
 */
public class HistorialActivity extends Activity {

    private ListView lista;
    private ArrayList<String> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial);
        lista = (ListView) findViewById(R.id.listView);
        array = new ArrayList<String>();

        SQLHelper helper = new SQLHelper(this,"HISTORIAL",null,1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String [] columnas = new String[]{"url","fecha","hora"};
        Cursor cursor = db.query("info",columnas,null,null,null,null,null,null);
        cursor.moveToPosition(-1);

        while (cursor.moveToNext()){
            array.add(cursor.getString(0)+" || "+cursor.getString(1)+" || "+cursor.getString(2));
        }

        cursor.close();
        db.close();
        helper.close();

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
        lista.setAdapter(adapter);
    }





}
