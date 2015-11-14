package edu.cmu.juicymeeting.database.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.juicymeeting.database.model.*;

/**
 *
 * Created by qiuzhexin on 11/13/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // TAG
    private static final String TAG = "DatabaseHelper";

    // Database Info
    private static final String DATABASE_NAME = "JMDB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_EVENTS = "events";
    private static final String TABLE_CHATROOMS = "chatrooms";
    private static final String TABLE_CHATROOMMEMBERS = "members";
    private static final String TABLE_MESSAGES = "messages";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_ACCT = "account";
    private static final String KEY_USER_PSSWD = "password";
    private static final String KEY_USER_IMG = "image";

    // Event Table Columns
    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_EVENT_NAME = "name";
    private static final String KEY_EVENT_LOC = "location";
    private static final String KEY_EVENT_AGENDA = "agenda";
    private static final String KEY_EVENT_DATE = "date";
    private static final String KEY_EVENT_TIME = "time";
    private static final String KEY_EVENT_FOUNDER = "founder";
    private static final String KEY_EVENT_USER_ID_FK = "creatorId";
    private static final String KEY_EVENT_CHATROOM_ID_FK = "chatroomId";

    // Chatroom Table Columns
    private static final String KEY_CHATROOM_ID = "id";
    private static final String KEY_CHATROOM_PSSWD = "password";
    private static final String KEY_CHATROOM_EVENT_ID_FK = "eventId";
    private static final String KEY_CHATROOM_USER_ID_FK = "adminId";

    // Chatroom Member Table Columns
    private static final String KEY_CHATROOM_MEMBER_ID = "id";
    private static final String KEY_CHATROOM_MEMBER_CHATROOM_ID_FK = "chatroomId";
    private static final String KEY_CHATROOM_MEMBER_USER_ID_FK = "memberId";

    // Message Table Columns
    private static final String KEY_MSG_ID = "id";
    private static final String KEY_MSG_TXT = "text";
    private static final String KEY_MSG_TIMESTAMP = "timestamp";
    private static final String KEY_MSG_CHATROOM_ID_FK = "chatroomId"; // foreign key for joining with Chatroom table
    private static final String KEY_MSG_USER_ID_FK = "sayerId"; // foreign key for joining with User table

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create the user table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_USER_NAME + " TEXT," +
                KEY_USER_ACCT + " TEXT," + KEY_USER_PSSWD + " TEXT," + KEY_USER_IMG + " BLOB," +
                ")";

        // create the event table
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS +
                "(" +
                KEY_EVENT_ID + " INTEGER PRIMARY KEY," +
                KEY_EVENT_NAME + " TEXT," + KEY_EVENT_NAME + " TEXT,"
                + KEY_EVENT_LOC + " TEXT," + KEY_EVENT_AGENDA + " TEXT,"
                + KEY_EVENT_DATE + " TEXT," + KEY_EVENT_TIME + " TEXT,"
                + KEY_EVENT_FOUNDER + " TEXT," + KEY_EVENT_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS + ","
                + KEY_EVENT_CHATROOM_ID_FK + " INTEGER REFERENCES " + TABLE_CHATROOMS +
                ")";

        // create the chatroom table
        String CREATE_CHATROOMS_TABLE = "CREATE TABLE " + TABLE_CHATROOMS +
                "(" +
                KEY_CHATROOM_ID + " INTEGER PRIMARY KEY," +
                KEY_CHATROOM_PSSWD + " TEXT,"
                + KEY_CHATROOM_EVENT_ID_FK + " INTEGER REFERENCES " + TABLE_EVENTS + ","
                + KEY_CHATROOM_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS +
                ")";

        // create the chat room member table
        String CREATE_CHATROOM_MEMBERS_TABLE = "CREATE TABLE " + TABLE_CHATROOMMEMBERS +
                "(" +
                KEY_CHATROOM_MEMBER_ID + " INTEGER PRIMARY KEY," +
                KEY_CHATROOM_MEMBER_CHATROOM_ID_FK + " INTEGER REFERENCES " + TABLE_CHATROOMS + ","
                + KEY_CHATROOM_MEMBER_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS +
                ")";

        // create the message table
        String CREATE_MESSAGE_TABLE = "CREATE TABLE " + TABLE_MESSAGES +
                "(" +
                KEY_MSG_ID + " INTEGER PRIMARY KEY," + KEY_MSG_TXT + " TEXT,"
                + KEY_MSG_TIMESTAMP + " TEXT,"
                + KEY_MSG_CHATROOM_ID_FK + " INTEGER REFERENCES " + TABLE_CHATROOMS + ","
                + KEY_MSG_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_CHATROOMS_TABLE);
        db.execSQL(CREATE_CHATROOM_MEMBERS_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATROOMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATROOMMEMBERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
            onCreate(db);
        }
    }

    /* Create method */
    // Insert a user into the database
    public void addUser(User post) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            // TODO: insert user data here
            ContentValues values = new ContentValues();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert a event into the database
    public void addEvent(Event event) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            // TODO: insert event data here
            ContentValues values = new ContentValues();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert a chatroom into the database
    public void addChatRoom(ChatRoom chatRoom) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            // TODO: insert chatroom data here
            ContentValues values = new ContentValues();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert a chatroom member into the database
    public void addChatRoomMember(ChatRoomMember chatRoomMember) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            // TODO: insert chatroom member data here
            ContentValues values = new ContentValues();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert a message into the database
    public void addMessage(Message msg) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            // TODO: insert message data here
            ContentValues values = new ContentValues();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    /* Read method */
    // Get a User
    public User getUser(int id) {
        String GET_USER_QUERY = String.format("SELECT % FROM %s WHERE %s=%d", KEY_USER_ID, TABLE_USERS, KEY_USER_ID, id);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(GET_USER_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                User user = new User();
                user.usrName = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
                user.account = cursor.getString(cursor.getColumnIndex(KEY_USER_ACCT));
                user.password = cursor.getString(cursor.getColumnIndex(KEY_USER_PSSWD));
                // TODO: transfer to BitMap
                //user.selfie = (Bitmap) cursor.getBlob(cursor.getColumnIndex(KEY_USER_IMG));
                return user;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get user from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    // Get a list of events
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String GET_ALL_EVENTS_QUERY = String.format("SELECT * FROM %s", TABLE_EVENTS);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(GET_ALL_EVENTS_QUERY, null);
        // TODO: return a all events in the database
        return null;
    }

    // Get a list of messages in a chat room with a user
    public List<Message> getAllPosts() {
        List<Message> msgs = new ArrayList<>();

        // TODO: test join three tables
        String GET_MESSAGES_QUERY =
                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s LEFT OUTER JOIN %s ON %s.%s=%s.%s",
                        TABLE_MESSAGES, TABLE_USERS, TABLE_MESSAGES, KEY_MSG_USER_ID_FK, TABLE_USERS, KEY_USER_ID,
                        TABLE_CHATROOMS, TABLE_MESSAGES, KEY_MSG_CHATROOM_ID_FK, TABLE_CHATROOMS, KEY_CHATROOM_ID);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(GET_MESSAGES_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    // TODO: parse all message objects here
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get messages from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return msgs;
    }

    // get a list of members in a chat room
    public List<User> getMembersIn(int roomId) {
        List<User> users = new ArrayList<>();
        String CHATROOM_MEMBERS_QUERY = String.format("SELECT * FROM %s WHERE %s=%d",
                TABLE_CHATROOMMEMBERS, KEY_CHATROOM_MEMBER_CHATROOM_ID_FK, roomId);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(CHATROOM_MEMBERS_QUERY, null);
        // TODO: add all users to arraylist and return
        return null;
    }

    /* Update */

    // Update a user's name
    public int updateUserName(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.usrName);

        // Updating profile picture url for user with that userName
        return db.update(TABLE_USERS, values, KEY_USER_NAME + " = ?",
                new String[] { String.valueOf(user.usrName) });
    }

    // Delete a chat room (ChatRoom -> ChatRoomMember -> Messages)
    public void deleteChatroom(int roomId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_CHATROOMMEMBERS, KEY_CHATROOM_MEMBER_CHATROOM_ID_FK + " = ?", new String[] {String.valueOf(roomId)});
            db.delete(TABLE_MESSAGES, KEY_MSG_CHATROOM_ID_FK + " = ?", new String[] {String.valueOf(roomId)});
            db.delete(TABLE_CHATROOMS, KEY_CHATROOM_ID + " = ?", new String[] {String.valueOf(roomId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all a chat room");
        } finally {
            db.endTransaction();
        }
    }
}
