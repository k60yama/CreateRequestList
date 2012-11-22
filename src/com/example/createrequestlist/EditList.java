package com.example.createrequestlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EditList extends Activity {

	//ヘルパークラス
	private ProductDBHelper pHelper;
	
	//データベース
	private SQLiteDatabase productDB;
	
	//データベースの情報
	private static final String[] COLUMNS = {
		"id","product_name","product_category"
	};
	
	//品物情報
	private String item_name;
	private String category_name;
	
	//選択ダイアログ位置情報
	private int which = 0;

	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊初期処理メソッド　ここから＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	@Override
	public void onCreate(Bundle savedInstanceState){
		//ActivityクラスのonCreateを実行
		super.onCreate(savedInstanceState);
		
		//レイアウト設定ファイルの指定
		this.setContentView(R.layout.edit_list);
		
		//データ取得
		this.getData();
	}
	
	private void getData(){
		//データベースを開く
		pHelper = new ProductDBHelper(this);
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
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊初期処理メソッド　ここまで＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	
	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊追加処理メソッド ここから＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	//追加する押下した場合
	public void itemAdd(View view){
		
		//初期化
		item_name = "";
		category_name = "";
		
		//EditText生成
		final EditText itemName = new EditText(this);
		itemName.setHint("(例) トマト");
		itemName.setInputType(InputType.TYPE_CLASS_TEXT);
				
		//ダイアログ生成
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("品物入力ダイアログ");
		dialog.setMessage("追加したい品物名を入力してください。");
		dialog.setView(itemName);
		dialog.setPositiveButton("次へ", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//カテゴリ選択ダイアログ画面へ
				item_name = itemName.getText().toString();
				selectCategory();
			}
		});
		dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//追加処理キャンセル表示
				showCancel("追加");
			}
		});
				
		//ダイアログ表示
		dialog.show();
	}
		
	//カテゴリ選択画面表示
	private void selectCategory(){
		
		//品物未入力チェック
		if("".equals(item_name.trim())){
			Toast cancel = Toast.makeText(this, "品物名が未入力です。" + "\n追加処理を終了します。" , Toast.LENGTH_LONG);
			cancel.setGravity(Gravity.CENTER, 0, 0);
			cancel.show();
		}else{
			//ダイアログ生成
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("種別選択ダイアログ");
			dialog.setSingleChoiceItems(Category.CATEGORIES, 0, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					//選択位置保持
					which = whichButton;
				}
			});
			dialog.setPositiveButton("次へ", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					//種別情報取得
					category_name = Category.CATEGORIES[which];
					
					//確認ダイアログ表示
					confirmDialog();
				}
			});
			dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					//追加処理キャンセル表示
					showCancel("追加");
				}
			});
			
			//ダイアログ表示
			dialog.show();
		}
	}
	
	//確認ダイアログ
	private void confirmDialog(){
		//ダイアログ用メッセージ設定
		String confirmTxt = "追加する品物は以下でよろしいでしょうか？" + 
							"\n品物名：　" + item_name +
							"\n種別：　　" + category_name;
		
		//ダイアログ生成
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("確認ダイアログ");
		dialog.setMessage(confirmTxt);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//データ追加処理へ
				addData();
			}
		});
		dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//追加処理キャンセル
				showCancel("追加");
			}
		});
		
		//ダイアログ表示
		dialog.show();
	}
	
	//データベース追加処理
	private void addData(){
		//データベースを開く
		productDB = pHelper.openDataBase();
		
		try{
			//トランザクション制御開始
			productDB.beginTransaction();
			
			//登録データ設定
			ContentValues val = new ContentValues();
			val.put("product_name", item_name);
			val.put("product_category", category_name);
			
			//データ登録
			productDB.insert("product_info", null, val);
			
			//コミット
			productDB.setTransactionSuccessful();
			
			//トランザクション制御終了
			productDB.endTransaction();
			
		}catch(Exception e){
			Log.e("InsertError",e.toString());
		}finally{
			//データベースを閉じる
			productDB.close();
		}
		
		Toast.makeText(this, "データを追加しました。", Toast.LENGTH_LONG).show();
		
		//ラジオボタン更新
		this.getData();
	}
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊追加処理メソッド　ここまで＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	
	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊共通処理メソッド　ここから＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	//処理キャンセル表示
	private void showCancel(String modeName){
		Toast cancel = Toast.makeText(EditList.this, "キャンセルが押されました\n" + modeName + "処理を終了します。" ,  Toast.LENGTH_LONG);
		cancel.setGravity(Gravity.CENTER, 0, 0);
		cancel.show();
	}
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊共通処理メソッド　ここまで＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//要改善
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
	
	//要改善
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
	
	//トップページへ押下した場合
	public void returnTopPage(View view){
		//アクティビティ終了
		this.finish();
	}
}
