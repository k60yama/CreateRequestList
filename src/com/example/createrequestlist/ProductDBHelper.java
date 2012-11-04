package com.example.createrequestlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDBHelper extends SQLiteOpenHelper {
	
	private static final String DB_PATH = "/data/data/com.example.createrequestlist/databases/";		//データベースが保存されているパス
	private static final String DB_NAME = "product";													//データベース名
	private static final String DB_NAME_ASSET = "product.db";											//事前に作成したデータベースファイル
	private Context context;																			//呼び出し元のアクティビティ保持用
	
	public ProductDBHelper(Context con){
		//データベースを開く
		super(con,"product",null,1);
		
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
