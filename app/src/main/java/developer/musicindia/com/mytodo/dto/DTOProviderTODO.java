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

import developer.musicindia.com.mytodo.data.UserProvider;
import developer.musicindia.com.mytodo.model.TODO;


public class DTOProviderTODO {

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

    private static ContentResolver contentResolver;

    public static ArrayList<TODO> getTODOListinDatabase(Context mContext) {
        ArrayList<TODO> todoList = new ArrayList<>();
        Context contexts = mContext;

        //String where = UserProvider._SONG_DELETE + "=?";

        //String[] selectionArgs = new String[]{String.valueOf(0)};

        ContentResolver resolver = contexts.getContentResolver();

        String sortOrder = UserProvider._TODOID + " DESC , " + UserProvider._TODO_COMPLETED+ " DESC ";
        Uri uri = UserProvider.CONTENT_URI_TODO;

        Cursor cursor = resolver.query(uri, projection, null, null, sortOrder);

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

    public static ArrayList<TODO> getTODOIteminDatabase(Context mContext,int id) {
        ArrayList<TODO> todoList = new ArrayList<>();
        Context contexts = mContext;

        String where = UserProvider._TODOID + " =? ";

        String[] selectionArgs = new String[]{String.valueOf(id)};

        ContentResolver resolver = contexts.getContentResolver();

        String sortOrder = UserProvider._TODOID + " DESC ";
        Uri uri = UserProvider.CONTENT_URI_TODO;

        Cursor cursor = resolver.query(uri, projection, where, selectionArgs, sortOrder);

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

    public static int getMAXTODOIteminDatabase(Context mContext) {
        ArrayList<TODO> todoList = new ArrayList<>();
        Context contexts = mContext;
        int maxValue = 0;
        ContentResolver resolver = contexts.getContentResolver();

        Uri uri = UserProvider.CONTENT_URI_TODO;



        Cursor cursor = resolver.query(uri, projectionMaxId, null,null, null);

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

    public static void insertTODOIteminDatabase(Context mContext,ContentValues values) {

        Context contexts = mContext;
        ContentResolver resolver = contexts.getContentResolver();
        Uri uri = UserProvider.CONTENT_URI_TODO;

        Uri uris;
        try{

            uris = resolver.insert(uri, values);
        }
        catch (Exception e){
            Log.e("Exception Updating",e.toString());

        }


    }

    public static int updateTODOIteminDatabase(Context mContext,int id,ContentValues values) {

        Context contexts = mContext;

        String where = UserProvider._TODOID + " =? ";

        String[] selectionArgs = new String[]{String.valueOf(id)};

        ContentResolver resolver = contexts.getContentResolver();

        String sortOrder = UserProvider._TODOID + " DESC ";
        Uri uri = UserProvider.CONTENT_URI_TODO;

        int recUpdated=0;
        try{
            recUpdated = resolver.update(uri, values, where, selectionArgs);
        }
        catch (Exception e){
            Log.e("Exception Updating",e.toString());

        }

        return recUpdated;
    }

    public static ContentValues getContentValues(TODO todo){

        ContentValues values = new ContentValues();

        values.put(UserProvider._TODOID,todo.getTodoId());
        values.put(UserProvider._TODO_TEXT,todo.getTodoText());
        values.put(UserProvider._TODO_TEXT_DES,todo.getTodoTextDes());
        values.put(UserProvider._TODO_REMINDER,String.valueOf(todo.getReminderTime()));
        values.put(UserProvider._TODO_COMPLETED,todo.getCompleted());

        return values;

    }

    public static ContentValues putContentValues(TODO todo){

        ContentValues values = new ContentValues();


        values.put(UserProvider._TODO_TEXT,todo.getTodoText());
        values.put(UserProvider._TODO_TEXT_DES,todo.getTodoTextDes());
        values.put(UserProvider._TODO_REMINDER,String.valueOf(todo.getReminderTime()));
        values.put(UserProvider._TODO_COMPLETED,todo.getCompleted());

        return values;

    }

    public static int deleteTODOfromDatabase(Context mContext,int id) {
        ArrayList<TODO> todoList = new ArrayList<>();
        Context contexts = mContext;

        String where = UserProvider._TODOID + "=?";

        String[] selectionArgs = new String[]{String.valueOf(id)};

        ContentResolver resolver = contexts.getContentResolver();


        Uri uri = UserProvider.CONTENT_URI_TODO;

        int deleted = 0;
        try{
            deleted = resolver.delete(uri, where, selectionArgs);
        }
        catch (Exception e){
            Log.e("Error Deleting",e.toString());
        }
        return deleted;

    }


}

