package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.webapps.googleassigment_test.MapsActivity;
import com.example.webapps.googleassigment_test.ShowLocationData;

import java.util.ArrayList;

public class DBhelper {
	public static final String EMP_ID = "id";
	public static final String LAT = "lat";
	public static final String LNG = "lng";
	public static final String AREA = "area";
	public static final String TIMEE = "tdate";
	public static final String UNIQID = "uniqid";
	public static final String PREFS_NAME = "LoginPrefs";
	private static final String DATABASE_NAME = "Location_Data.db";
	private static final int DATABASE_VERSION =10;
	private static final String CAPTURE_TABLE = "location_details";
	private static String sql = null;
	private final Context mCtx;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	public DBhelper(Context ctx) {
		mCtx = ctx;
		mDbHelper = new DatabaseHelper(mCtx);
	}

	public void Reset() {
		mDbHelper.onUpgrade(this.mDb, 10, 1);

	}

	public DBhelper open() throws SQLException {
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	//cv.put(EMP_PHOTO, Utilitydata.getBytes(employee.getBitmap()));
	public void insertLocationDetails(Location_Data locationData) {
		ContentValues cv = new ContentValues();


		cv.put(LAT, locationData.getLag());
		cv.put(LNG, locationData.getLng());
		cv.put(AREA, locationData.getArea());
		cv.put(TIMEE, locationData.getTdate());
		cv.put(UNIQID, locationData.getUniqId());
		mDb.insert(CAPTURE_TABLE, null, cv);


		Log.d("Tag", "Create table " + sql);
		Log.d("Tag", "insert valuse1 :"+ " Lag :" + locationData.getLag() + " Lng : " + locationData.getLng() + " Area : " + locationData.getArea()+ " Uniqid :"+ locationData.getUniqId()+"DateTime"+ locationData.getTdate());

	}

	public ArrayList<Location_Data> getAllRecords() {
		//  mDb = this.getReadableDatabase();

		Cursor cursor = mDb.query(CAPTURE_TABLE, null, null, null, null, null, null);
		ArrayList<Location_Data> contacts = new ArrayList<Location_Data>();
		Location_Data contactModel;
		Log.d("Tag","cursor length"+cursor.getCount());
		if (cursor.getCount() > 0) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				contactModel = new Location_Data();


				contactModel.setArea(cursor.getString(cursor.getColumnIndex(AREA)));
				contactModel.setLag(cursor.getString(cursor.getColumnIndex(LAT)));
				contactModel.setLng(cursor.getString(cursor.getColumnIndex(LNG)));
				contactModel.setTdate(cursor.getString(cursor.getColumnIndex(TIMEE)));
				contactModel.setUniqId(cursor.getString(cursor.getColumnIndex(UNIQID)));



				Log.i("TAG","uniqne"+cursor.getString(cursor.getColumnIndex(UNIQID)));
				Log.i("TAG","Area"+cursor.getString(cursor.getColumnIndex(AREA)));
				Log.i("TAG","Lat"+cursor.getString(cursor.getColumnIndex(LAT)));
				Log.i("TAG","Lng"+cursor.getString(cursor.getColumnIndex(LNG)));
				Log.i("TAG","Datetime"+cursor.getString(cursor.getColumnIndex(TIMEE)));


				contacts.add(contactModel);

				//  Log.d("Tag","Local Data",contactModel.setName(cursor.getString(cursor.getColumnIndex(FISH_NAME))));
			}
		}
		cursor.close();
		mDb.close();
		return contacts;
	}

	public boolean deleteLocation(String code) {
		//return mDb.delete(CAPTURE_TABLE, null, null) > 0;
		Log.d("Tag","del location from sql"+code);
		return mDb.delete(CAPTURE_TABLE, UNIQID + "=" + code, null) > 0;


	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
			//db.execSQL(CREATE_EMPLOYEES_TABLE);
			Log.d("Tag", "Data base cretated");
			sql = "CREATE TABLE "+CAPTURE_TABLE+" (_id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,lat text, lng text, area VARCHAR, tdate VARCHAR, uniqid text );";
			db.execSQL(sql);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + CAPTURE_TABLE);
			onCreate(db);
		}
	}

}
