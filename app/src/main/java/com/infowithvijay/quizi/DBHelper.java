package com.infowithvijay.quizi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String db_name = "quizi_db.db";
    private String db_path;
    private static int db_version = 1;

    Context con;

    // Constructor of Class
    public DBHelper(Context con) {
        super(con, db_name, null, db_version);
              this.con = con;
        db_path = con.getDatabasePath(db_name).toString().replace(db_name, "");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }



    public void createDB() throws IOException {

        if (checkDB()) {
        } else if (!checkDB()) {
            this.getReadableDatabase();
            copyDB();
        }

    }

    // checkDB() method

    private boolean checkDB() {

        SQLiteDatabase cDB = null;
        try {
            cDB = SQLiteDatabase.openDatabase(db_path + db_name, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (cDB != null) {
            cDB.close();
        }
        return cDB != null ? true : false;
    }

    // copyDB() method

    private void copyDB() throws IOException {
        InputStream inputFile = con.getAssets().open(db_name);
        String outFileName = db_path + db_name;
        OutputStream outFile = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputFile.read(buffer)) > 0) {
            outFile.write(buffer, 0, length);
        }
        outFile.flush();
        outFile.close();
        inputFile.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub


    }

    public List<Quizplay> getQuestion(int noOfQuestion, int level) {
        List<Quizplay> quizplay = new ArrayList<Quizplay>();
        int total = noOfQuestion;
        String sql = "select *  FROM questions_list  where level=" + level + " ORDER BY RANDOM() LIMIT " + total;
        SQLiteDatabase db = this.getReadableDatabase();
        //SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/" + packageName + "/databases/" + DATABASE_NAME, null, 0);
        Cursor cursor = db.rawQuery(sql, null);
        int i = 1;
        if (cursor.moveToFirst()) {
            do {
                Quizplay question = new Quizplay(cursor.getString(cursor.getColumnIndex("question")));
                question.addOption(cursor.getString(cursor.getColumnIndex("option_a")));
                question.addOption(cursor.getString(cursor.getColumnIndex("option_b")));
                question.addOption(cursor.getString(cursor.getColumnIndex("option_c")));
                question.addOption(cursor.getString(cursor.getColumnIndex("option_d")));
                String rightAns = cursor.getString(cursor.getColumnIndex("answer"));
                System.out.println("right ans "+rightAns);
                if (rightAns.equalsIgnoreCase("A")) {
                    question.setTrueAns(cursor.getString(cursor.getColumnIndex("option_a")));
                } else if (rightAns.equalsIgnoreCase("B")) {
                    question.setTrueAns(cursor.getString(cursor.getColumnIndex("option_b")));
                } else if (rightAns.equalsIgnoreCase("C")) {
                    question.setTrueAns(cursor.getString(cursor.getColumnIndex("option_c")));
                } else {
                    question.setTrueAns(cursor.getString(cursor.getColumnIndex("option_d")));
                }
                if (question.getOptions().size() == 4) {
                    quizplay.add(question);
                }
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Collections.shuffle(quizplay);
        quizplay = quizplay.subList(0, noOfQuestion);
        return quizplay;
    }

}
