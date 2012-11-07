package com.example.createrequestlist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Category extends Activity{
	
	//データベース
	private SQLiteDatabase productDB;
	
	//データベースの情報
	private static final String[] COLUMNS = {
		"id","product_name","product_category"
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		//ActivityのOnCreateを実行
		super.onCreate(savedInstanceState);
		
		//レイアウト設定ファイルの指定
		this.setContentView(R.layout.category);
	}

	//カテゴリのボタン押下した場合
	public void orderItemInfo(View view){
		
		//Button型にキャスト
		Button btn = (Button)view;
		
		//検索条件取得
		String categoryWhere = btn.getText().toString();
		
		//データベースを開く
		ProductDBHelper pHelper = new ProductDBHelper(this);
		productDB = pHelper.openDataBase();
		
		//データ取得
		Cursor cursor = this.getProductData(categoryWhere);
		
		//ビュー成形
		while(cursor.moveToNext()){
			Toast.makeText(this, cursor.getString(1), Toast.LENGTH_SHORT).show();
		}
		
		
		/*
		//インテント生成
		Intent nextActivity = new Intent(this, SelectItems.class);
		
		//カテゴリ名初期化
		String categoryName = "";
		
		//押下別にカテゴリ名設定
		switch(view.getId()){
		case R.id.EggAndDairyAndDrink:
			categoryName = "卵・乳製品・飲料";
			break;
		case R.id.ProcessItems:
			categoryName = "加工品";
			break;
		case R.id.FishItems:
			categoryName = "魚介";
			break;
		case R.id.MeetItems:
			categoryName = "肉類";
			break;
		case R.id.VegetableItems:
			categoryName = "野菜";
			break;
		case R.id.FruitItems:
			categoryName = "果物";
			break;
		case R.id.ElseItems:
			categoryName = "その他";
			break;
		}
		
		//インテント付加情報設定
		nextActivity.putExtra("CategoryName", categoryName);
		
		//アクティビティ起動
		this.startActivity(nextActivity);
		*/
	}
	
	//トップページへ押下した場合
	public void returnTopPage(View view){
		//アクティビティ終了
		this.finish();
	}
	
	//注文確認を押下した場合
	public void toConfirmPage(View view){
		
		//インテント生成
		Intent nextActivity = new Intent(this, OrderedConfirm.class);
		
		//アクティビティ起動
		this.startActivity(nextActivity);
	}
	
	//データ取得
	private Cursor getProductData(String categoryName){
		return productDB.query("product_info", COLUMNS, "product_category='" + categoryName + "'", null, null, null, "id");
	}
	
	//Backキー無効
	@Override
	public boolean dispatchKeyEvent(KeyEvent event){
		//デバイスボタンが押下された場合
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			//Backキーが押下された場合
			if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
}
