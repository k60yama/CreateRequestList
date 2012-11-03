package com.example.createrequestlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class Category extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		//ActivityのOnCreateを実行
		super.onCreate(savedInstanceState);
		
		//レイアウト設定ファイルの指定
		this.setContentView(R.layout.category);
	}

	//カテゴリのボタン押下した場合
	public void orderItemInfo(View view){
		
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
		
		//���̃アクティビティ起動
		this.startActivity(nextActivity);
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
