/*
 * Copyright 2012-2013 Andrea De Cesare
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package developer.musicindia.com.mytodo.dto;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import developer.musicindia.com.mytodo.app.MyToApplication;
import developer.musicindia.com.mytodo.data.UserProvider;
import developer.musicindia.com.mytodo.model.TODO;

import javax.inject.Inject;


public class DTOProviderTODO {

    @Inject ContentResolver contentResolver;

    private final static String[] projection = {
            UserProvider._TODOID,
            UserProvider._TODO_TEXT,
            UserProvider._TODO_TEXT_DES,
            UserProvider._TODO_REMINDER,
            UserProvider._TODO_COMPLETED
    };


    private final static String[] projectionMaxId = {
            "MAX("+UserProvider._TODOID+")"
    };

    public DTOProviderTODO(Context context){
        ((MyToApplication)context).getAppComponent().inject(this);
    }

    public ArrayList<TODO> getTODOListinDatabase() {
        ArrayList<TODO> todoList = new ArrayList<>();


        //String where = UserProvider._SONG_DELETE + "=?";

        //String[] selectionArgs = new String[]{String.valueOf(0)};


        String sortOrder = UserProvider._TODOID + " DESC , " + UserProvider._TODO_COMPLETED+ " DESC ";
        Uri uri = UserProvider.CONTENT_URI_TODO;

        Cursor cursor = contentResolver.query(uri, projection, null, null, sortOrder);

        int todoidindex = cursor.getColumnIndex(UserProvider._TODOID);
        int todotextindex = cursor.getColumnIndex(UserProvider._TODO_TEXT);
        int tododesindex = cursor.getColumnIndex(UserProvider._TODO_TEXT_DES);
        int todoremindex = cursor.getColumnIndex(UserProvider._TODO_REMINDER);
        int todocomindex = cursor.getColumnIndex(UserProvider._TODO_COMPLETED);



        if (cursor != null && cursor.moveToFirst()) {
            do {
                int todoid = cursor.getInt(todoidindex);

                String title = cursor.getString(todotextindex);
                String des = cursor.getString(tododesindex);
                String remTime = cursor.getString(todoremindex);
                int completed = cursor.getInt(todocomindex);

                String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                Date date = new Date();
                try {
                    date = inputFormat.parse(remTime);

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                todoList.add(new TODO(todoid, title, des, date, completed));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return todoList;
    }

    public ArrayList<TODO> getTODOIteminDatabase(int id) {
        ArrayList<TODO> todoList = new ArrayList<>();


        String where = UserProvider._TODOID + " =? ";

        String[] selectionArgs = new String[]{String.valueOf(id)};

        String sortOrder = UserProvider._TODOID + " DESC ";
        Uri uri = UserProvider.CONTENT_URI_TODO;

        Cursor cursor = contentResolver.query(uri, projection, where, selectionArgs, sortOrder);

        int todoidindex = cursor.getColumnIndex(UserProvider._TODOID);
        int todotextindex = cursor.getColumnIndex(UserProvider._TODO_TEXT);
        int tododesindex = cursor.getColumnIndex(UserProvider._TODO_TEXT_DES);
        int todoremindex = cursor.getColumnIndex(UserProvider._TODO_REMINDER);
        int todocomindex = cursor.getColumnIndex(UserProvider._TODO_COMPLETED);



        if (cursor != null && cursor.moveToFirst()) {
            do {
                int todoid = cursor.getInt(todoidindex);

                String title = cursor.getString(todotextindex);
                String des = cursor.getString(tododesindex);
                String remTime = cursor.getString(todoremindex);
                int completed = cursor.getInt(todocomindex);

                String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                Date date = new Date();
                try {
                    date = inputFormat.parse(remTime);

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                todoList.add(new TODO(todoid, title, des, date, completed));

            } while (cursor.moveToNext());
        }
        cursor.close();

        return todoList;
    }

    public int getMAXTODOIteminDatabase() {
        ArrayList<TODO> todoList = new ArrayList<>();
        int maxValue = 0;


        Uri uri = UserProvider.CONTENT_URI_TODO;



        Cursor cursor = contentResolver.query(uri, projectionMaxId, null,null, null);

        /*int todoidindex = cursor.getColumnIndex(UserProvider._TODOID);*/

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int todoid = cursor.getInt(0);
                maxValue =todoid;

            } while (cursor.moveToNext());
        }
        cursor.close();

        return maxValue;
    }

    public void insertTODOIteminDatabase(ContentValues values) {

        Uri uri = UserProvider.CONTENT_URI_TODO;

        Uri uris;
        try{

            uris = contentResolver.insert(uri, values);
        }
        catch (Exception e){
            Log.e("Exception Updating",e.toString());

        }


    }

    public int updateTODOIteminDatabase(int id,ContentValues values) {



        String where = UserProvider._TODOID + " =? ";

        String[] selectionArgs = new String[]{String.valueOf(id)};
        String sortOrder = UserProvider._TODOID + " DESC ";
        Uri uri = UserProvider.CONTENT_URI_TODO;

        int recUpdated=0;
        try{
            recUpdated = contentResolver.update(uri, values, where, selectionArgs);
        }
        catch (Exception e){
            Log.e("Exception Updating",e.toString());

        }

        return recUpdated;
    }

    public ContentValues getContentValues(TODO todo){

        ContentValues values = new ContentValues();

        values.put(UserProvider._TODOID,todo.getTodoId());
        values.put(UserProvider._TODO_TEXT,todo.getTodoText());
        values.put(UserProvider._TODO_TEXT_DES,todo.getTodoTextDes());
        values.put(UserProvider._TODO_REMINDER,String.valueOf(todo.getReminderTime()));
        values.put(UserProvider._TODO_COMPLETED,todo.getCompleted());

        return values;

    }

    public ContentValues putContentValues(TODO todo){

        ContentValues values = new ContentValues();


        values.put(UserProvider._TODO_TEXT,todo.getTodoText());
        values.put(UserProvider._TODO_TEXT_DES,todo.getTodoTextDes());
        values.put(UserProvider._TODO_REMINDER,String.valueOf(todo.getReminderTime()));
        values.put(UserProvider._TODO_COMPLETED,todo.getCompleted());

        return values;

    }

    public int deleteTODOfromDatabase(int id) {
        ArrayList<TODO> todoList = new ArrayList<>();


        String where = UserProvider._TODOID + "=?";

        String[] selectionArgs = new String[]{String.valueOf(id)};


        Uri uri = UserProvider.CONTENT_URI_TODO;

        int deleted = 0;
        try{
            deleted = contentResolver.delete(uri, where, selectionArgs);
        }
        catch (Exception e){
            Log.e("Error Deleting",e.toString());
        }
        return deleted;

    }


}

