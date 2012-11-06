package com.example.createrequestlist;



import java.io.UnsupportedEncodingException;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MainMenu extends Activity implements OnClickListener{
	
	private SQLiteDatabase db;
	
	//データベースの列名
	private static final String[] COLUMNS = {
		"product_id","product_name","product_category"
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        //ActivityのOnCreate実行
    	super.onCreate(savedInstanceState);
        
    	//レイアウト設定ファイルの指定
        this.setContentView(R.layout.main);
        
        //データベース事前設定
        this.setDataBase();
     
        //Button オブジェクト取得
        Button[] buttons = {
        	(Button)this.findViewById(R.id.order),
        	(Button)this.findViewById(R.id.editList),
        	(Button)this.findViewById(R.id.orderHistory)
        };
        
        //Button オブジェクトにクリックリスナーを設定
        for(Button button:buttons){
        	button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view){
    	
    	//Intent変数初期化
    	Intent nextActivity = null;
    	
    	//押下別にインテント生成
    	switch(view.getId()){
    	case R.id.order:
    		nextActivity = new Intent(this,Category.class);
    		break;
    	case R.id.editList:
    		nextActivity = new Intent(this,EditList.class);
    		break;
    	case R.id.orderHistory:
    		nextActivity = new Intent(this,OrderedHistory.class);
    		break;
    	}
    	
    	//アクティビティ起動
    	startActivity(nextActivity);
    }
    
    @Override
    public void onDestroy(){
    	//アプリを終了するタイミングでデータベースを閉じる
    	db.close();
    	super.onDestroy();
    }
    
    public void setDataBase(){
    	//DatabaseHelperクラスのインスタンス生成
    	ProductDBHelper mDbHelper = new ProductDBHelper(this);
    	
    	//空のデータベースを作成する
    	mDbHelper.createEmptyDataBase();
    	
    	//ここからテスト
    	//既存データベースを開く
		db = mDbHelper.openDataBase();
		
		Cursor cursor = db.query("product_info", COLUMNS, null, null, null, null, null);
    	
		int count = 1;
		while(cursor.moveToNext()){
        	
        	//Toast.makeText(this, cursor.getInt(0),Toast.LENGTH_SHORT).show();
			
			if(isWindows31j(cursor.getString(1))){
				Toast.makeText(this, count + "文字コードはWindows31j",Toast.LENGTH_SHORT).show();
			}else if(isSJIS(cursor.getString(1))){
				Toast.makeText(this, count + "文字コードはSJIS",Toast.LENGTH_SHORT).show();
			}else if(isEUC(cursor.getString(1))){
				Toast.makeText(this, count + "文字コードはEUC",Toast.LENGTH_SHORT).show();
			}else if(isUTF8(cursor.getString(1))){
				Toast.makeText(this, count + "文字コードはUTF8",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, count + "該当する文字コードはありません",Toast.LENGTH_SHORT).show();
			}
			
			count = count + 1;
			
        	//Toast.makeText(this, cursor.getString(2),Toast.LENGTH_SHORT).show();
        }
		
		//ここまでテスト
		
    }
    
    
    private static boolean checkCharacterCode(String str, String encoding) {
		if (str == null) {
			return true;
		}

		try {
			byte[] bytes = str.getBytes(encoding);
			return str.equals(new String(bytes, encoding));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException("エンコード名称が正しくありません。", ex);
		}
	}

	public static boolean isWindows31j(String str) {
		return checkCharacterCode(str, "Windows-31j");
	}

	public static boolean isSJIS(String str) {
		return checkCharacterCode(str, "SJIS");
	}

	public static boolean isEUC(String str) {
		return checkCharacterCode(str, "euc-jp");
	}

	public static boolean isUTF8(String str) {
		return checkCharacterCode(str, "UTF-8");
	}
    
}
