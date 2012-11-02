package com.example.createrequestlist;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class MainMenu extends Activity implements OnClickListener{

	//遷移先のアクティビティ指定
	private Intent nextActivity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //ActivityクラスのOnCreate
    	super.onCreate(savedInstanceState);
        
    	//レイアウト設定ファイルの指定
        this.setContentView(R.layout.main);
        
        //Button オブジェクト
        Button[] buttons = {
        	(Button)this.findViewById(R.id.order),
        	(Button)this.findViewById(R.id.editList),
        	(Button)this.findViewById(R.id.orderHistory)
        };
        
        //Button オブジェクトにクリックリスナー設定
        for(Button button:buttons){
        	button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view){
    	
    	//遷移先指定
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
    	
    	//指定した画面に遷移
    	startActivity(nextActivity);
    }
}
