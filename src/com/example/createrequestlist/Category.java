package com.example.createrequestlist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Category extends Activity{
	
	//データベース
	private SQLiteDatabase productDB;
	
	//データベースの情報
	private static final String[] COLUMNS = {
		"id","product_name","product_category"
	};
	
	//カテゴリの種類
	private static final String[] CATEGORIES ={
		"卵・乳製品・飲料","加工品","魚介","肉類","野菜","果物","その他"
	};
	
	//カウント値
	private Integer count;
	
	//ScrollView
	//private ScrollView sv;
	
	//EditText
	private static ArrayList<EditText> edit = new ArrayList<EditText>();

	@Override
	public void onCreate(Bundle savedInstanceState){
		//ActivityのOnCreateを実行
		super.onCreate(savedInstanceState);
		
		//レイアウト設定ファイルの指定
		this.setContentView(R.layout.category);
		
		//DB取得
		this.getDB();
	}

	//カテゴリのボタン押下した場合
	public void orderItemInfo(View view){
		
		//Button型にキャスト
		//Button btn = (Button)view;
		
		//表示するカテゴリ名称取得
		//String categoryView = btn.getText().toString();
		
		//表示・非表示の制御は後で考える
		
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
	
	//データベース取得
	private void getDB(){
		//データベースを開く
		ProductDBHelper pHelper = new ProductDBHelper(this);
		productDB = pHelper.openDataBase();
		
		//カウント初期化
		count = 0;
		
		//カテゴリ数分繰り返す
		for(String categoryName:CATEGORIES){
			this.createTable(categoryName);					//TableLayout生成
			//count = count + 1;							//カウントアップ
		}
		
		//データベース閉じる
		productDB.close();
	}
	
	//TableLayout生成
	private void createTable(String categoryName){
		//データ取得
		Cursor cursor = this.getProductData(categoryName);
		
		//TableLayout取得
		TableLayout tLayout = (TableLayout)this.findViewById(R.id.itemInfo);
		
		//レコード数チェック
		//0件の場合:TextView生成
		//1件以上の場合:TableLayout生成
		if(cursor.moveToFirst()){
			
			//レコード数分作成する
			do{
				//カウントアップ
				count = count + 1;
				
				//TableRow生成
				TableRow row = new TableRow(this);
				
				//CheckBox
				CheckBox chk = new CheckBox(this);
				chk.setWidth(100);
				chk.setHighlightColor(60);
				row.addView(chk);
				
				//TextView
				TextView tv = new TextView(this);
				tv.setText(cursor.getString(1));
				row.addView(tv);
				
				//EditText
				EditText ed = new EditText(this);
				ed.setInputType(InputType.TYPE_CLASS_NUMBER);
				ed.setText("0");
				ed.setWidth(80);
				row.addView(ed);
				
				//ArrayListに確保
				edit.add(ed);
				
				//Button
				Button btn = new Button(this);
				btn.setWidth(100);
				btn.setText("テスト");
				btn.setTag(count);
				btn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO 自動生成されたメソッド・スタブ
						Button b = (Button)v;
						Integer i = (Integer)b.getTag();
						EditText num = edit.get(i-1);
						//Log.d("おしたボタン：", String.valueOf(i-1));
						Integer count2 = Integer.valueOf(num.getText().toString());
						count2 = count2 + 1;
						num.setText(String.valueOf(count2));
						//Toast.makeText(Category.this, String.valueOf(count2), Toast.LENGTH_SHORT).show();
					}
				});
				row.addView(btn);
				
				tLayout.addView(row);
			}while(cursor.moveToNext());			
		}else{
			//TableRow生成
			TableRow row = new TableRow(this);
			
			//TextView生成
			TextView nothing = new TextView(this);
			nothing.setText("選択されたカテゴリに品物は登録されていません。");
			row.addView(nothing);
			tLayout.addView(row);
		}
		
		//検索結果クリア
		cursor.close();
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
