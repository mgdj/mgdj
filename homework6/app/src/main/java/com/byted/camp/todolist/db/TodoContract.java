package com.byted.camp.todolist.db;

import android.provider.BaseColumns;

public final class TodoContract {

    // TODO 1. 定义创建数据库以及升级的操作
    public static final String SQL_CREATE_NOTE = "CREATE TABLE "+TodoNote.TABLE_NAME+"("+
            TodoNote.COLUMN_id+" INTEGER PRIMARY KEY AUTOINCREMENT,"+TodoNote.COLUMN_date+ " INTEGER,"+
            TodoNote.COLUMN_state+" INTEGER,"+TodoNote.COLUMN_content+" TEXT,"+TodoNote.COLUMN_priority+" INTEGER)";
    private static final String SQL_DELETE_NOTE = "DELETE TABLE IF EXISTS "+TodoNote.TABLE_NAME;
    public static final String SQL_UPDATE_NOTE = "ALTER TABLE "+TodoNote.TABLE_NAME+" ADD "+
            TodoNote.COLUMN_priority + " INTEGER";
    private TodoContract() {
    }

    public static class TodoNote implements BaseColumns {
        // TODO 2.此处定义表名以及列明
        public static final String TABLE_NAME = "note";
        public static final String COLUMN_id = "id";
        public static final String COLUMN_date = "date";
        public static final String COLUMN_state = "state";
        public static final String COLUMN_content = "content";
        public static final String COLUMN_priority = "priority";
    }

}
