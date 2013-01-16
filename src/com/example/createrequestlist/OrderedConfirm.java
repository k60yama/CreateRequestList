package com.example.createrequestlist;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
//import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class OrderedConfirm extends Activity {
	//文言設定
	private String mailTxt;
	private String inputTxt;
	
	//おねがいリスト用レイアウト
	private LinearLayout lLayout;
	private TableLayout tLayout;
	
	//ユーザー情報取得用
	private boolean[] checkStatus;
	private String[] usersName;
	private Map<String,String> userMap;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		//ActivityクラスのonCreateを実行
		super.onCreate(savedInstanceState);
		
		//カスタムタイトルバーを使用
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);		
		this.setContentView(R.layout.ordered_confirm);
		final Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/JohnHancockCP.otf");
	    this.setHeader(tf);		//ヘッダー
	    this.setFooter(tf);		//フッター
	    
		//LinearLayout取得
		lLayout = (LinearLayout)this.findViewById(R.id.orderedItem);
		lLayout.removeAllViews();
		mailTxt = "";
		inputTxt = "";
		dateSet();		//日付作成
		
		//品物情報取得
		Intent intent = this.getIntent();
		Bundle extras = intent.getExtras();
		@SuppressWarnings("unchecked")
		HashMap<String,String> itemMap = (HashMap<String,String>)extras.getSerializable("ITEM_MAP");
		
		createHeader();									//ヘッダー作成
		for(String itemName : itemMap.keySet()){
			String itemNum = itemMap.get(itemName);	 	//個数取得
			createOrderedList(itemName,itemNum);		//品物情報作成
			createItemText(itemName,itemNum);			//文言作成
		}
		lLayout.addView(tLayout);
	}
	
	
	private void setHeader(Typeface tf){
		//タイトル用のレイアウト設定
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.ordered_confirm_titlebar);
		
		//TextView オブジェクト取得
		TextView title = (TextView)this.findViewById(R.id.ordered_confirm_title);
		title.setTypeface(tf);
		
		//Button オブジェクト取得
		Button button = (Button)this.findViewById(R.id.BackButton);
		button.setTypeface(tf);
	}
	
	private void setFooter(Typeface tf){
		//TextView オブジェクト取得
		TextView footer = (TextView)this.findViewById(R.id.footer);
		footer.setTypeface(tf);		
	}
	
	
	private void dateSet(){
		//日付設定
		DateFormat format = new SimpleDateFormat("yyyy年MMMMd日");
		String dateTxt = format.format(new Date());
		
		//TextView生成
		TextView date = new TextView(this);
		date.setText(dateTxt);
		date.setTextSize(26);
		date.setPadding(5, 0, 0, 30);
		date.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_menu_more, 0, 0, 0);
		date.setGravity(Gravity.CENTER);
		lLayout.addView(date);	//LinearLayoutに追加
	}
	
	private void createHeader(){
		//ヘッダー作成
		tLayout = new TableLayout(this);
		tLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		TableRow head = new TableRow(this);
		head.setPadding(20, 0, 0, 40);
		TextView hName = new TextView(this);
		hName.setText("品物名");
		hName.setTextSize(22);
		head.addView(hName);
		
		TextView hNum = new TextView(this);
		hNum.setText("数量");
		hNum.setTextSize(22);
		hNum.setGravity(Gravity.CENTER_HORIZONTAL);
		head.addView(hNum);
		tLayout.addView(head);
	}
	
	private void createOrderedList(String itemName, String itemNum){
		//おねがい情報生成
		TableRow row = new TableRow(this);
		row.setPadding(20, 0, 0, 30);
		TextView name = new TextView(this);
		name.setText(itemName);
		name.setTextSize(18);
		name.setWidth(335);
		name.setPadding(0, 0, 40, 0);
		row.addView(name);
		
		TextView num = new TextView(this);
		num.setText(itemNum);
		num.setTextSize(18);
		num.setGravity(Gravity.CENTER_HORIZONTAL);
		row.addView(num);
		tLayout.addView(row);
	}
	
	public void finishActivity(View view){
		finish();	//アクティビティ終了
	}
	
	public void callAddressMain(View view){
		if(getAddress()){
			multiDialog();
		}else{
			showMsg("メールアドレスが設定されているユーザーが電話帳に登録されていません。");
		}
	}
	
	private boolean getAddress(){
		//電話帳アプリからメールアドレスを取得
		Cursor cur = this.managedQuery(Email.CONTENT_URI, null, null, null, null);
		if(cur.getCount() == 0){
			return false;	//電話帳アプリにメールアドレスを持っているユーザーが登録されていない。
		}else{
			//初期化
			usersName = new String[cur.getCount()];		//ユーザー情報設定(ダイアログ表示用)
			checkStatus = new boolean[cur.getCount()];	//チェックボックス初期化
			userMap = new HashMap<String,String>();		//<ユーザー情報,メールアドレス>のHashMap
			int userIndex = 0;							//配列：userName,checkStatus のインデックス
			
			//メールアドレス分繰り返す
			while(cur.moveToNext()){
				setUserInfo(cur,userIndex);
				userIndex = userIndex + 1;
			}
			return true;
		}
	}
	
	private void setUserInfo(Cursor cur, Integer userIndex){
		//ユーザー名取得
		String name = cur.getString(cur.getColumnIndex(Contacts.DISPLAY_NAME));		
		
		//メール種別取得
		int type = cur.getInt(cur.getColumnIndex(Email.DATA2));						
		int typeInt = Email.getTypeLabelResource(type);
		Resources res = this.getResources();
		String emailType = (res.getText(typeInt)).toString();
		
		//メールアドレス取得
		String email = cur.getString(cur.getColumnIndex(Email.DATA1));				
		
		//ユーザー情報設定(ダイアログ表示用)
		String userInfo = name + "\n" + "(" + emailType + ")";
		usersName[userIndex] = userInfo;	//ダイアログに表示するテキスト
		checkStatus[userIndex] = false;		//CheckBox は全て未選択
		
		//userMapに登録<ユーザー情報,メールアドレス>
		userMap.put(userInfo, email);
	}

	//ダイアログ表示
	private void showMsg(String msg){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setIcon(android.R.drawable.ic_menu_info_details);
		dialog.setTitle("メッセージ");
		dialog.setMessage(msg);
		dialog.setCancelable(false);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();		//ダイアログ表示
	}	
	
	//複数選択ダイアログ生成
	private void multiDialog(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setIcon(android.R.drawable.ic_menu_info_details);
		dialog.setTitle("宛先選択");
		dialog.setMultiChoiceItems(usersName, checkStatus, 
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					}
		});
		dialog.setPositiveButton("送信", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//メール送信処理確認へ
				isSendMailCheck(gmailMainHandling());
			}
		});
		dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();		//ダイアログ表示
	}
	
	//メール送信処理確認
	private void isSendMailCheck(int resultCode){
		//メール送信成功と失敗時のみ次のアクティビティに遷移する
		if((resultCode == 0) || (resultCode == 1)){
			Intent intent = new Intent(this,Compleat.class);
			intent.putExtra("RESULT", resultCode);
			startActivity(intent);
		//送信先が未選択の場合
		}else if(resultCode == 2){
			showMsg("送信先が選択されいてません。");
		//送信先作成失敗の場合
		}else if(resultCode == 3){
			showMsg("送信処理で異常が発生しました。");
		}
	}
	
	/*E-MAIL起動メイン処理
	戻り値：0 (送信OK)
	戻り値：1 (送信NG)
	戻り値：2 (宛先ダイアログ未選択)
	戻り値：3 (送信先作成失敗) */
	private int gmailMainHandling(){
		//初期化
		int resultCode = 0;			//戻り値
		int sendToCount = 0;		//送信件数
		ArrayList<InternetAddress> sendToAddressList = new ArrayList<InternetAddress>();
		
		//ユーザー数分繰り返す
		for(int i=0; i<usersName.length; i++){
			if(checkStatus[i]){
				try{
					//ユーザー名をキーにメールアドレスを取得する
					sendToAddressList.add(new InternetAddress(userMap.get(usersName[i])));
				}catch(AddressException e){
					Log.e("emailMainHandlingErr", "送信先作成に失敗しました。");
					resultCode = 3;
				}
				sendToCount = sendToCount + 1;		//送信件数カウント
			}
		}
		
		//送信件数チェック
		if(sendToCount == 0){
			//showMsg("宛先が選択されいてません。");
			resultCode = 2;
		}else if(!(sendGmail(sendToAddressList))){
			resultCode = 1;
		}else{
			//送信履歴作成
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			
			//ProductFilesインスタンス生成
			ProductFiles pf = new ProductFiles(OrderedConfirm.this,df.format(date),inputTxt);
			pf.fileMain();
		}
		return resultCode;
	}
	
	//JavaMailで送信処理
	private boolean sendGmail(ArrayList<InternetAddress> sendToAddressList){
		//仮引数 sendToAddressList を InternetAddress 型配列に変換
		InternetAddress[] sendToAddress = sendToAddressList.toArray(new InternetAddress[sendToAddressList.size()]);
		
		//プリファレンス取得
		SharedPreferences pref = this.getSharedPreferences(MailSetUp.FILE_NAME, MODE_PRIVATE);
		String user = pref.getString("MAIL", "");
		String pass = pref.getString("PASS", "");

		//Propertiesクラスのインスタンス生成
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");	//SMTPサーバー名
		prop.put("mail.smtp.port", "587");				//SMTPサーバーポート
		prop.put("mail.smtp.auth", "true");				//SMTP認証
		prop.put("mail.smtp.starttls.enable", "true");	//STTLS

		//セッション生成
		Session session = Session.getInstance(prop);

		//メッセージ生成
		MimeMessage mimeMsg = new MimeMessage(session);
		try {
			mimeMsg.setFrom(new InternetAddress(user));	//Fromアドレス設定
			mimeMsg.setRecipients(Message.RecipientType.TO, sendToAddress);	//送信先設定
			mimeMsg.setContent("body","text/plain; utf-8");
			mimeMsg.setHeader("Content-Transfer-Encoding", "7bit");
			mimeMsg.setSubject("【おねがい】必要な品物あり");	//件名
			mimeMsg.setText(createBodyMsg(),"utf-8");	//本文

			Transport transport = session.getTransport("smtp");
			transport.connect(user,pass);
			transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());	//メール送信
			transport.close();
		} catch (Exception e) {
			Log.e("sendGmailErr", "メール送信に失敗しました。");
			return false;
		}
		return true;
	}
	
	//本文作成処理
	private String createBodyMsg(){
		String body = "";
		body = "下記品物が必要です。\n" + 
		"お手数ですが、品物を購入もしくはいただけないでしょうか？\n" + 
		"(※ご注意　このメールは複数宛先に送信されている場合がございます。" +
		"詳細は宛先一覧を参照してください。)\n\n\n" +
		"【おねがいリスト】\n" + mailTxt;
		return body;
	}
	
	//メールandファイル用文言作成
	private void createItemText(String itemName, String itemNum){
		//メール本文用文言
		mailTxt = mailTxt + "品物名：" + itemName + "\n数量：" + itemNum + "\n\n";
		
		//ファイル用文言
		inputTxt = inputTxt + "'" + itemName + "','" + itemNum + "'\n";
	}
}
