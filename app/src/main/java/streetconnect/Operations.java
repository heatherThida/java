package streetconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

// Class with CRUD methods on the database. Currently isn't being used, but should stay in app because
// it gives us flexibility with how we want to access the database.

public class Operations
{
    private DBHelper dbHelper;

    public Operations(Context context) {
        dbHelper = new DBHelper(context);
    }

    private void update(String caption, String filepath)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DBHelper.ID_COLUMN + "= ?";
        String[] whereArgs = {"1"};
        ContentValues newValues = new ContentValues();
        newValues.put(DBHelper.MESSAGE_COLUMN, caption);
        newValues.put(DBHelper.FILE_PATH_COLUMN, filepath);
        db.update(DBHelper.DATABASE_TABLE, newValues, whereClause, whereArgs);
    }

    private void delete()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DBHelper.ID_COLUMN + "=?";
        String[] whereArgs = {"2"};
        db.delete(DBHelper.DATABASE_TABLE, whereClause, whereArgs);
    }

    private void query()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {DBHelper.ID_COLUMN, DBHelper.MESSAGE_COLUMN, DBHelper.FILE_PATH_COLUMN};
        Cursor cursor = db.query(DBHelper.DATABASE_TABLE, resultColumns, where, whereArgs, groupBy, having, order);
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            String filepath = cursor.getString(3);
            Log.d("Photo", String.format("%s,%s,%s,%s", id, name, description, filepath));
        }
    }
    private void insert(String caption, String filepath)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(DBHelper.MESSAGE_COLUMN, caption);
        newValues.put(DBHelper.FILE_PATH_COLUMN, filepath);
        db.insert(DBHelper.DATABASE_TABLE, null, newValues);
    }

    private void query1()
    {
        List<String> test = new ArrayList<String>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        test.clear();

        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = { DBHelper.ID_COLUMN, DBHelper.MESSAGE_COLUMN, DBHelper.FILE_PATH_COLUMN };
        Cursor cursor = db.query(DBHelper.DATABASE_TABLE, resultColumns, where, whereArgs, groupBy, having, order);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);

            String note = cursor.getString(1);
            test.add(note);

        }

        cursor.close();
        db.close();

    }

}
