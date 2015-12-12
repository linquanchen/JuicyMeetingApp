package edu.cmu.juicymeeting.database.chatDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Juicy local DB for storing chat info
 * Created by qiuzhexin on 12/5/15.
 */
public class DatabaseConnector extends SQLiteOpenHelper {
    private static final String TAG = DatabaseConnector.class.getSimpleName();
    // Singleton pattern
    private static DatabaseConnector sInstance;
    // Database Info
    private static final String DATABASE_NAME = "JuicyLocalDB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_GROUP = "chatGroup";
    private static final String TABLE_MSG = "message";

    // Chat group table columns
    private static final String KEY_GROUP_CODE = "groupCode";
    private static final String KEY_GROUP_TIME = "createTime";

    // Message table columns
    private static final String KEY_MSG_GROUP_CODE_FK = "groupCode";
    private static final String KEY_MSG_MSG = "message";
    private static final String KEY_MSG_OWNER = "owner";
    private static final String KEY_MSG_TIME = "sendTime";
    private static final String KEY_MSG_IS_SELF = "isSelf";

    public static synchronized DatabaseConnector getInstance(Context context) {
        // avoid leak activity context
        if (sInstance == null) {
            sInstance = new DatabaseConnector(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseConnector(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // configure database connection, like foreign key support, write-ahead logging, etc
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // when DB is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROUP_TABLE = "CREATE TABLE " + TABLE_GROUP +
                "(" +
                KEY_GROUP_CODE + " INTEGER PRIMARY KEY," +
                KEY_GROUP_TIME + " INTEGER" +
                ")";

        String CREATE_MSG_TABLE = "CREATE TABLE " + TABLE_MSG +
                "(" +
                KEY_MSG_GROUP_CODE_FK + " INTEGER REFERENCES " + TABLE_GROUP + "," +
                KEY_MSG_MSG + " TEXT," +
                KEY_MSG_OWNER + " TEXT," +
                KEY_MSG_TIME + " INTEGER," +
                KEY_MSG_IS_SELF + " INTEGER" +
                ")";

        Log.d(TAG, "create group table: " + CREATE_GROUP_TABLE);
        Log.d(TAG, "create message tab;e: " + CREATE_MSG_TABLE);

        db.execSQL(CREATE_GROUP_TABLE);
        db.execSQL(CREATE_MSG_TABLE);
    }

    // call when a newer version of DB comes out
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MSG);
            onCreate(db);
        }
    }

    // Insert chat group into DB
    public void addGroup(Group group) {
        // Create or open the db for writing
        SQLiteDatabase db = getWritableDatabase();

        // wrap insert in a transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_GROUP_CODE, group.groupCode);
            values.put(KEY_GROUP_TIME, group.createTime);

            db.insertOrThrow(TABLE_GROUP, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add group to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert message into DB
    public void addMessage(Message message) {
        // Create or open the db for writing
        SQLiteDatabase db = getWritableDatabase();

        // wrap insert in a transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_MSG_GROUP_CODE_FK, message.group.groupCode);
            values.put(KEY_MSG_MSG, message.message);
            values.put(KEY_MSG_OWNER, message.owner);
            values.put(KEY_MSG_TIME, message.createTime);
            values.put(KEY_MSG_IS_SELF, message.isSelf);

            db.insertOrThrow(TABLE_MSG, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add group to database");
        } finally {
            db.endTransaction();
        }
    }

    // query groups info
    public List<Group> getAllGroupsOrderByCreateTime() {
        List<Group> groups = new ArrayList<>();

        // SELECT * FROM group ORDER BY date(createTime) DESC
        String GROUP_SELECT_QUERY = String.format("SELECT * FROM %s ORDER BY %s DESC",
                TABLE_GROUP, KEY_GROUP_TIME);
        // get the database
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(GROUP_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Group newGroup = new Group();
                    newGroup.groupCode = cursor.getInt(cursor.getColumnIndex(KEY_GROUP_CODE));
                    newGroup.createTime = cursor.getLong(cursor.getColumnIndex(KEY_GROUP_TIME));

                    groups.add(newGroup);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get groups from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return groups;
    }

    // get group by group code, if not exist return null
    public Group getGroupByGroupCode(int groupCode) {
        Group result = null;
        // SELECT * FROM message WHERE groupCode=groupCode
        String GROUP_SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s=%d",
                TABLE_GROUP, KEY_GROUP_CODE, groupCode);
        // get the database
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(GROUP_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Group newGroup = new Group();
                    newGroup.groupCode = cursor.getInt(cursor.getColumnIndex(KEY_GROUP_CODE));
                    newGroup.createTime = cursor.getLong(cursor.getColumnIndex(KEY_GROUP_TIME));
                    result = newGroup;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error querying messages from db");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    // query message info by group code
    public List<Message> getAllMessagesByGroupOrderByTime(int groupCode) {
        List<Message> messages = new ArrayList<>();

        // SELECT * FROM message NATURAL JOIN group
        String MESSAGE_SELECT_QUERY = String.format("SELECT * FROM %s NATURAL JOIN %s WHERE %s.%s=%d ORDER BY %s ASC",
                TABLE_MSG, TABLE_GROUP, TABLE_GROUP, KEY_MSG_GROUP_CODE_FK, groupCode, KEY_MSG_TIME);
        // get the database
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(MESSAGE_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Group newGroup = new Group();
                    newGroup.groupCode = cursor.getInt(cursor.getColumnIndex(KEY_GROUP_CODE));
                    newGroup.createTime = cursor.getLong(cursor.getColumnIndex(KEY_GROUP_TIME));
                    Message newMsg = new Message();

                    newMsg.group = newGroup;
                    newMsg.message = cursor.getString(cursor.getColumnIndex(KEY_MSG_MSG));
                    newMsg.owner = cursor.getString(cursor.getColumnIndex(KEY_MSG_OWNER));
                    newMsg.createTime = cursor.getLong(cursor.getColumnIndex(KEY_MSG_TIME));
                    newMsg.isSelf = cursor.getInt(cursor.getColumnIndex(KEY_MSG_IS_SELF));

                    messages.add(newMsg);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error querying messages from db");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return messages;
    }

    // delete group from database by groupCode
    public void deleteChatGroup(int groupCode) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String DELETE_GROUP_QUERY = String.format("DELETE FROM %s WHERE %s=%d",
                    TABLE_GROUP, KEY_GROUP_CODE, groupCode);
            String DELETE_MSG_QUERY = String.format("DELETE FROM %s WHERE %s=%d",
                    TABLE_MSG, KEY_GROUP_CODE, groupCode);
            db.execSQL(DELETE_MSG_QUERY);
            db.execSQL(DELETE_GROUP_QUERY);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while tring to delete group and its messages");
        } finally {
            db.endTransaction();
        }
    }

    // drop all tables
    public void dropAllTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String DROP_TABLE_QUERY = "DROP TABLE %s";
            db.execSQL(String.format(DROP_TABLE_QUERY, TABLE_MSG));
            db.execSQL(String.format(DROP_TABLE_QUERY, TABLE_GROUP));
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all tables");
        } finally {
            db.endTransaction();
        }

    }

    // delete all rows from in all tables
    public void deleteAllRecords() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String DELETE_RECORDS_QUERY = "DELETE FROM %s";
            db.execSQL(String.format(DELETE_RECORDS_QUERY, TABLE_MSG));
            db.execSQL(String.format(DELETE_RECORDS_QUERY, TABLE_GROUP));
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all records");
        } finally {
            db.endTransaction();
        }
    }
}
