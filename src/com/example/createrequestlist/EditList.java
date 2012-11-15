package com.example.createrequestlist;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EditList extends Activity {

	//データベース
	private SQLiteDatabase productDB;
	
	//データベースの情報
	private static final String[] COLUMNS = {
		"id","product_name","product_category"
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		//ActivityクラスのonCreateを実行
		super.onCreate(savedInstanceState);
		
		//レイアウト設定ファイルの指定
		this.setContentView(R.layout.edit_list);
		
		//DB取得
		this.getDB();
		
	}
	
	private void getDB(){
		//データベースを開く
		ProductDBHelper pHelper = new ProductDBHelper(this);
		productDB = pHelper.openDataBase();
		
		//TableLayout生成
		this.createTable();
		
		//データベースを閉じる
		productDB.close();
	}
	
	private void createTable(){
		//データ取得
		Cursor cursor = productDB.query("product_info", COLUMNS, null, null, null, null, "product_category");
		
		//TableLayout取得
		TableLayout tLayout = (TableLayout)this.findViewById(R.id.itemInfo);
				
		//レコード数チェック
		//0件の場合:TextView生成
		//1件以上の場合:TableLayout生成
		if(cursor.moveToFirst()){
			
			//ヘッダー作成
			//TableRow生成
			TableRow head = new TableRow(this);
					
			//品物名
			TextView itemLabel = new TextView(this);
			itemLabel.setText("品物名");
			itemLabel.setGravity(Gravity.CENTER);
			head.addView(itemLabel);
			
			//カテゴリ
			TextView categoryLabel = new TextView(this);
			categoryLabel.setGravity(Gravity.CENTER);
			categoryLabel.setText("種別");
			head.addView(categoryLabel);
			
			//TableLayoutにTableRowを追加
			tLayout.addView(head);
			
			//登録内容分繰り返す
			do{
				//TableRow生成
				TableRow itemInfo = new TableRow(this);
						
				//品物名
				TextView item = new TextView(this);
				item.setText(cursor.getString(1));
				itemInfo.addView(item);
				
				//カテゴリ
				TextView category = new TextView(this);
				category.setText(cursor.getString(2));
				itemInfo.addView(category);
				
				//TableLayoutにTableRowを追加
				tLayout.addView(itemInfo);
				
			}while(cursor.moveToNext());			
		}else{
			//TableRow生成
			TableRow textInfo = new TableRow(this);
					
			//TextView生成
			TextView nothing = new TextView(this);
			nothing.setText("品物は登録されていません。");
			textInfo.addView(nothing);
			tLayout.addView(textInfo);
		}
				
		//検索結果クリア
		cursor.close();
	}
}
