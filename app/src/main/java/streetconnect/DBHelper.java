package streetconnect;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
    // database name
    public static final String DATABASE_TABLE = "Notifications";

    // column names
    public static final String ID_COLUMN = "_id";
    public static final String TITLE_COLUMN = "_title";
    public static final String MESSAGE_COLUMN = "_message";
    public static final String SURVEY_COLUMN = "_survey";
    public static final String SUBTITLE_COLUMN = "_subtitle";
    public static final String FILE_PATH_COLUMN = "_filepath";
    public static final String ORG_COLUMN = "_org";
    public static final String ADDRESS_COLUMN = "_address";


    // miscellaneous
    public static final int DATABASE_VERSION = 3; // used to be 2. change to 3 for address form.

    //
    private static final String DATABASE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "  %s integer primary key autoincrement, " +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text," +
                    "  %s text)",
            DATABASE_TABLE, ID_COLUMN, TITLE_COLUMN, MESSAGE_COLUMN, SURVEY_COLUMN, SUBTITLE_COLUMN, FILE_PATH_COLUMN, ORG_COLUMN, ADDRESS_COLUMN);

    public DBHelper(Context context)
    {
        super(context, DATABASE_TABLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }
}
