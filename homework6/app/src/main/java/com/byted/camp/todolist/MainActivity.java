package com.byted.camp.todolist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.UserDictionary;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.Priority;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.debug.DebugActivity;
import com.byted.camp.todolist.debug.FileDemoActivity;
import com.byted.camp.todolist.debug.SpDemoActivity;
import com.byted.camp.todolist.ui.NoteListAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;

    private TodoDbHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        dbHelper = new TodoDbHelper(this);
        database = dbHelper.getWritableDatabase();

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                // TODO: 2021/7/19 7. 此处删除数据库数据
                if(database== null){
                    return;
                }else{
                    String selection = TodoContract.TodoNote.COLUMN_id+"=?";
                    String[] selectionArgs = {String.valueOf(note.id)};
                    int row_num = database.delete(TodoContract.TodoNote.TABLE_NAME,selection,selectionArgs);
                    if(row_num>0){
                        notesAdapter.refresh(loadNotesFromDatabase());
                    }
                }
            }

            @Override
            public void updateNote(Note note) {
                // TODO: 2021/7/19 7. 此处更新数据库数据
                if(database==null){
                    return;
                }else{
                    ContentValues value = new ContentValues();
                    value.put(TodoContract.TodoNote.COLUMN_state,note.getState().intValue);
                    String selection = TodoContract.TodoNote.COLUMN_id+"=?";
                    String[] selectionArgs = {String.valueOf(note.id)};
                    int row_num = database.update(TodoContract.TodoNote.TABLE_NAME,value,selection,selectionArgs);
                    if(row_num>0){
                        notesAdapter.refresh(loadNotesFromDatabase());
                    }
                }
            }
        });
        recyclerView.setAdapter(notesAdapter);

        notesAdapter.refresh(loadNotesFromDatabase());
        /*Environment.getExternalStorageDirectory();
        getExternalFilesDir("");
        getExternalCacheDir();
        File file = new File("");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reader reader;
        try {
            InputStream inputStream = new FileInputStream("xxx");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor;
        BaseColumns columns;
        SQLiteDatabase database;
        SQLiteOpenHelper helper;

        String[] projection=null, selectionArgs=null;
        String selectionClause=null, sortOrder=null;

        getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,  // The content URI of the words table
                projection,                       // The columns to return for each row
                selectionClause,                  // Either null, or the word the user entered
                selectionArgs,                    // Either empty, or the string the user entered
                sortOrder);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
        database = null;
        dbHelper.close();
        dbHelper = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            case R.id.action_file:
                startActivity(new Intent(this, FileDemoActivity.class));
                return true;
            case R.id.action_sp:
                startActivity(new Intent(this, SpDemoActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    private List<Note> loadNotesFromDatabase() {
        if (database == null) {
            return Collections.emptyList();
        }
        List<Note> result = new LinkedList<>();
        // TODO: 2021/7/19 7. 此处query数据库数据
        Cursor cursor = null;
        try{
            cursor = database.query(TodoContract.TodoNote.TABLE_NAME,
                    new String[]{TodoContract.TodoNote.COLUMN_id, TodoContract.TodoNote.COLUMN_content,
                            TodoContract.TodoNote.COLUMN_state, TodoContract.TodoNote.COLUMN_date,
                            TodoContract.TodoNote.COLUMN_priority},null,null,null,
                    null, TodoContract.TodoNote.COLUMN_date + " DESC");
            while (cursor.moveToNext()){
                String content = cursor.getString(cursor.getColumnIndex(TodoContract.TodoNote.COLUMN_content));
                long dates = cursor.getLong(cursor.getColumnIndex(TodoContract.TodoNote.COLUMN_date));
                int intState = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoNote.COLUMN_state));
                int intpriority = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoNote.COLUMN_priority));
                long id = cursor.getLong(cursor.getColumnIndex(TodoContract.TodoNote.COLUMN_id));
                Note note = new Note(id);
                note.setContent(content);
                note.setDate(new Date(dates));
                note.setState(State.from(intState));
                note.setPriority(Priority.from(intpriority));
                result.add(note);
            }
        }finally {
            if(cursor!=null){
                cursor.close();
            }
        }
        return result;
    }
}
