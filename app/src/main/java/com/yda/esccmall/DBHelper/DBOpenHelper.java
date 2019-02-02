package com.yda.esccmall.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yda.esccmall.Bean.StepEntity;


public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "StepCounter.db"; //数据库名称
    private static final int DB_VERSION = 1;//数据库版本,大于0
    private SQLiteDatabase db;

    //用于创建step表
    private static final String CREATE_BANNER = "create table step (_id INTEGER PRIMARY KEY AUTOINCREMENT, curDate TEXT, totalSteps TEXT,totalStepsKm TEXT,totalStepsKa TEXT);";
    private static final String CREATE_PLAN = "create table step_plan (_id INTEGER PRIMARY KEY AUTOINCREMENT, p_step TEXT, p_km TEXT,p_ka TEXT,u_id INTEGER);";
    private static final String CREATE_WEIGHT= "create table weight (_id INTEGER PRIMARY KEY AUTOINCREMENT, w_weight TEXT, w_date TEXT,w_text TEXT,w_aim TEXT);";
    //private static final String NEW_TABLE="create table if not exists eat_plan(id INTEGER PRIMARY KEY AUTOINCREMENT, totals_ka TEXT, aim_ka TEXT,date_ka TEXT);";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
        //db.execSQL(NEW_TABLE);
    }

    public DBOpenHelper(Context context, String name, int version, DatabaseErrorHandler errorHandler) {
        super(context, name,null, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BANNER);
        db.execSQL(CREATE_PLAN);
        db.execSQL(CREATE_WEIGHT);
        Log.e("onCreate","数据库创建了"+CREATE_BANNER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void addNewData(StepEntity stepEntity) {
       db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("curDate", stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getSteps());
        values.put("totalStepsKm",stepEntity.getTotalStepsKm());
        values.put("totalStepsKa",stepEntity.getTotalStepsKa());
        db.insert("step", null, values);

        Log.e("addNewData","加入了数据");

    }


    public StepEntity getCurDataByDate(String curDate) {
        db=getReadableDatabase();
        StepEntity stepEntity = null;
        Cursor cursor = db.query("step", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (curDate.equals(date)) {
                String steps = cursor.getString(cursor.getColumnIndexOrThrow("totalSteps"));
                String km = cursor.getString(cursor.getColumnIndexOrThrow("totalStepsKm"));
                String ka = cursor.getString(cursor.getColumnIndexOrThrow("totalStepsKa"));
                stepEntity = new StepEntity(date, steps,km,ka);
                //跳出循环
                break;
            }
        }
        //关闭
        cursor.close();
        return stepEntity;
    }


    //删除数据  根据id删除数据
    public void delete(int id){
        db=getWritableDatabase();
        db.delete(DB_NAME,"name=?",new String[]{String.valueOf(id)});
    }

    public void updateCurData(StepEntity stepEntity) {
        db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("curDate",stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getSteps());
        values.put("totalStepsKm",stepEntity.getTotalStepsKm());
        values.put("totalStepsKa",stepEntity.getTotalStepsKa());
        db.update("step", values, "curDate=?", new String[]{stepEntity.getCurDate()});
    }

    public void close(){

        //关闭数据库
        if (db!=null)
            db.close();
    }

    //遍历数据
    public Cursor mquery(){
        //获取到SQLiteDatabase对象
        db=getReadableDatabase();
        //获取Cursor
        Cursor cursor=db.query("step",null,null,null,null,null,null);
        return cursor;
    }
}
