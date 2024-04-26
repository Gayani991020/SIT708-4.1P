import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Helper class for managing the SQLite database
public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db"; // Database name
    private static final int DATABASE_VERSION = 1; // Database version

    // Constructor for MySQLiteHelper
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the "tasks" table
        String CREATE_TABLE_QUERY =
                "CREATE TABLE tasks (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT, " +
                        "description TEXT, " +
                        "dueDate TEXT)";
        // Execute the SQL query to create the table
        db.execSQL(CREATE_TABLE_QUERY);
    }

    // Method called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing "tasks" table if it exists
        db.execSQL("DROP TABLE IF EXISTS tasks");
        // Call onCreate to recreate the table
        onCreate(db);
    }
}
