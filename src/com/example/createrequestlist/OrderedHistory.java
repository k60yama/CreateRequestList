package com.example.createrequestlist;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class OrderedHistory extends Activity implements OnItemClickListener{

	private ArrayList<String> dateList = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		//ActivityクラスのonCreateを実行
		super.onCreate(savedInstanceState);
		
		//カスタムタイトルバーを使用
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.ordered_history);
		final Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/JohnHancockCP.otf");
	    this.setHeader(tf);		//ヘッダー
	    this.setFooter(tf);		//フッター
		
		//ListViewの設定
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		ListView listview = (ListView)this.findViewById(R.id.listview);
		getFiles();
		
		//降順にソート
		Collections.sort(dateList, new Comparator<String>(){
			@Override
			public int compare(String lhs, String rhs) {
				return rhs.compareTo(lhs);
			}
		});
		
		for(int i=0; i<dateList.size(); i++){
			adapter.add(dateList.get(i));		//日付追加	
		}
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);	//クリックリスナーの設定
	}
	
	
	private void setHeader(Typeface tf){
		//タイトル用のレイアウト設定
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.ordered_history_titlebar);
		
		//TextView オブジェクト取得
		TextView title = (TextView)this.findViewById(R.id.historyTitle);
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
	
	//ファイル取得
	private void getFiles(){
		String[] pFiles = this.fileList();
		for(String pFile:pFiles){
			setDate(pFile);
		}
	}
	
	//日時設定
	private void setDate(String pFile){
		try {
			//一行目(日時)のみ取得
			FileInputStream stream = this.openFileInput(pFile);
			BufferedReader in = new BufferedReader(new InputStreamReader(stream));
			String date = in.readLine();
			
			//検索位置設定
			Integer start = date.indexOf("'");
			Integer end = date.lastIndexOf("'");
			
			//日時設定
			dateList.add(date.substring(start+1, end));
			in.close();
		} catch (IOException e) {
			Log.e("setDateErr","ファイルの読み込みに失敗しました。");
		}
	}
	
	//トップページへ押下した場合
	public void returnTopPage(View view){
		this.finish();		//アクティビティ終了
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		//ListView取得
		ListView listview = (ListView)parent;
		
		//選択された日時取得
		String selectedDate = (String)listview.getItemAtPosition(position);
		
		//インテント設定
		Intent intent = new Intent(this,ProductHistory.class);
		intent.putExtra("OrderedDate", selectedDate);
		startActivity(intent);		//アクティビティ起動
	}	
}
