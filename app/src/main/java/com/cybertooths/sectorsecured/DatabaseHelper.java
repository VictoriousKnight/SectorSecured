package com.cybertooths.sectorsecured;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper {

    private static final String DB_NAME = "userDatabase.db";
    private static final String DB_PATH = "/data/data/com.cybertooths.sectorsecured/databases/";
    private SQLiteDatabase database;
    private final Context context;

    public DatabaseHelper(Context context) {
        this.context = context;
        copyDatabase();
    }

    private void copyDatabase() {
        String dbPath = DB_PATH + DB_NAME;
        File dbFile = new File(dbPath);
        if (dbFile.exists()) {
            return;
        }

        File dbDirectory = new File(DB_PATH);
        if (!dbDirectory.exists()) {
            dbDirectory.mkdirs();
        }

        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(DB_NAME);
            OutputStream outputStream = new FileOutputStream(dbPath);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Error copying database", e);
        }
    }

    public SQLiteDatabase openDatabase() {
        String dbPath = DB_PATH + DB_NAME;

        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            return null;
        }

        return SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public String authenticateUser(String username, String password) {
        SQLiteDatabase db = openDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = null;

        try {
            String query = "SELECT username FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                int usernameIndex = cursor.getColumnIndex("username");
                if (usernameIndex != -1) {
                    return cursor.getString(usernameIndex);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL Error", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return null;
    }
}
