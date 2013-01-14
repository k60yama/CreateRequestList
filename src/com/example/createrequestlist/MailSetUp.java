package com.example.createrequestlist;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MailSetUp extends Activity {
	//プリファレンス関連
	public static final String FILE_NAME = "AccountFile";
	private SharedPreferences pref;

	//EditText
	private EditText mailEd;
	private EditText passEd;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//カスタムタイトルバーを使用
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.mail_setup);	//レイアウト
		final Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/JohnHancockCP.otf");
	    this.setHeader(tf);		//ヘッダー
	    this.setFooter(tf);		//フッター
	    
		//プリファレンス取得
	    pref = this.getSharedPreferences(FILE_NAME, MODE_PRIVATE);

	    //EditText取得
	    mailEd = (EditText)this.findViewById(R.id.mailAddress);
	    passEd = (EditText)this.findViewById(R.id.password);

	    //アカウント情報設定
	    mailEd.setText(pref.getString("MAIL", ""));
	    passEd.setText(pref.getString("PASS", ""));
	}
	
	private void setHeader(Typeface tf){
		//タイトル用のレイアウト設定
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mail_setup_titlebar);
		
		//TextView オブジェクト取得
		TextView title = (TextView)this.findViewById(R.id.setup_title);
		title.setTypeface(tf);
		
		//Button オブジェクト取得
		Button button = (Button)this.findViewById(R.id.homeButton);
		button.setTypeface(tf);
	}
	
	private void setFooter(Typeface tf){
		//TextView オブジェクト取得
		TextView footer = (TextView)this.findViewById(R.id.footer);
		footer.setTypeface(tf);		
	}
	
	//トップページへ押下した場合
	public void returnTopPage(View view){
		//プリファレンス編集モード
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("MAIL", mailEd.getText().toString());
		editor.putString("PASS", passEd.getText().toString());
		editor.commit();
		this.finish();		//アクティビティ終了
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event){
		//デバイスボタンが押下された場合
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			//Backキーが押下された場合
			if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				//プリファレンス編集モード
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("MAIL", mailEd.getText().toString());
				editor.putString("PASS", passEd.getText().toString());
				editor.commit();
			}
		}
		return super.dispatchKeyEvent(event);
	}
}