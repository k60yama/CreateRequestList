package com.example.createrequestlist;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
		
		//ラジオボタン生成
		this.createRadio();
		
		//データベースを閉じる
		productDB.close();
	}
	
	private void createRadio(){
		//データ取得
		Cursor cursor = productDB.query("product_info", COLUMNS, null, null, null, null, "product_category");
		
		//LinearLayout取得
		LinearLayout lLayout = (LinearLayout)this.findViewById(R.id.itemInfo);
		
		//RadioGroup生成
		RadioGroup rGroup = new RadioGroup(this);
		rGroup.setId(0);
		
		//レコード数チェック
		//0件の場合:TextView生成
		//1件以上の場合:RadioGroup生成
		if(cursor.moveToFirst()){

			//登録内容分繰り返す
			do{
				//キーワード設定(　品物名　（種別名）)
				String itemName = "　品物名：　" + cursor.getString(1) + "\n　種別：　　" + cursor.getString(2);
				
				//RadioButton生成
				RadioButton rButton = new RadioButton(this);
				rButton.setId(cursor.getInt(0));
				rButton.setText(itemName);
				
				//RadioGroupにRadioButtonを追加
				rGroup.addView(rButton);
			}while(cursor.moveToNext());			
		
			//LinearLayoutにRadioGroupを追加
			lLayout.addView(rGroup);
			
		}else{
			//TextView生成
			TextView nothing = new TextView(this);
			nothing.setText("品物は現在登録されていません。");
			
			//LinearLayoutにTextViewを追加
			lLayout.addView(nothing);
		}
				
		//検索結果クリア
		cursor.close();
	}
	
	//トップページへ押下した場合
	public void returnTopPage(View view){
		//アクティビティ終了
		this.finish();
	}
	
	//追加する押下した場合
	public void itemAdd(View view){
		
		
	}
	
	//変更する押下した場合
	public void itemUpdate(View view){
		//生成したRadioGroupを取得
		RadioGroup rGroup = (RadioGroup)this.findViewById(0);
				
		//チェックされているRadioButtonを取得する
		RadioButton rButton = (RadioButton)this.findViewById(rGroup.getCheckedRadioButtonId());
				
		//チェック有無確認
		if(rButton == null){
			Toast.makeText(this, "【品物リスト】から変更する品物をひとつ選択してください。", Toast.LENGTH_SHORT).show();
		}else{
			//String confirm = rButton.getText().toString() + "\n　の内容を変更しますか？";
			//Toast.makeText(this, confirm, Toast.LENGTH_SHORT).show();
			Toast.makeText(this, Integer.toString(rButton.getId()), Toast.LENGTH_SHORT).show();
		}
	}
	
	//削除する押下した場合
	public void itemDelete(View view){
		//生成したRadioGroupを取得
		RadioGroup rGroup = (RadioGroup)this.findViewById(0);
						
		//チェックされているRadioButtonを取得する
		RadioButton rButton = (RadioButton)this.findViewById(rGroup.getCheckedRadioButtonId());
						
		//チェック有無確認
		if(rButton == null){
			Toast.makeText(this, "【品物リスト】から削除する品物をひとつ選択してください。", Toast.LENGTH_SHORT).show();
		}else{
			//String confirm = rButton.getText().toString() + "\n　を削除しますか？";
			//Toast.makeText(this, confirm, Toast.LENGTH_SHORT).show();
			Toast.makeText(this, Integer.toString(rButton.getId()), Toast.LENGTH_SHORT).show();
		}
	}
}
