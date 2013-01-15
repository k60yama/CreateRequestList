package com.example.createrequestlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Compleat extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//カスタムタイトルバーを使用
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.compleat);	//レイアウト
		final Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/JohnHancockCP.otf");
	    this.setHeader(tf);		//ヘッダー
	    this.setFooter(tf);		//フッター

	    //TextView取得
	    TextView tv = (TextView)this.findViewById(R.id.compleat_message);
	    
	    //インテント取得
	    Intent intent = this.getIntent();
	    Bundle date = intent.getExtras();
	    
	    if(date.getBoolean("RESULT")){
	    	tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_menu_send, 0, 0, 0);
	    	tv.setText("メールを送信しました。");
	    }else{
	    	tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_menu_close_clear_cancel, 0, 0, 0);
	    	tv.setText("メールが送信できませんでした。");
	    }
	}
	
	private void setHeader(Typeface tf){
		//タイトル用のレイアウト設定
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.compleat_titlebar);
		
		//TextView オブジェクト取得
		TextView title = (TextView)this.findViewById(R.id.compleat_title);
		title.setTypeface(tf);
	}
	
	private void setFooter(Typeface tf){
		//TextView オブジェクト取得
		TextView footer = (TextView)this.findViewById(R.id.footer);
		footer.setTypeface(tf);		
	}
	
	//トップページへ押下した場合
	public void returnTopPage(View view){
		//トップページへ
		Intent intent = new Intent(this,MainMenu.class);
		startActivity(intent);
	}
	
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
