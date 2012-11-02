package com.example.createrequestlist;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class MainMenu extends Activity implements OnClickListener{

	//�J�ڐ�̃A�N�e�B�r�e�B�w��
	private Intent nextActivity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Activity�N���X��OnCreate
    	super.onCreate(savedInstanceState);
        
    	//���C�A�E�g�ݒ�t�@�C���̎w��
        this.setContentView(R.layout.main);
        
        //Button �I�u�W�F�N�g
        Button[] buttons = {
        	(Button)this.findViewById(R.id.order),
        	(Button)this.findViewById(R.id.editList),
        	(Button)this.findViewById(R.id.orderHistory)
        };
        
        //Button �I�u�W�F�N�g�ɃN���b�N���X�i�[�ݒ�
        for(Button button:buttons){
        	button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view){
    	
    	//�J�ڐ�w��
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
    	
    	//�w�肵����ʂɑJ��
    	startActivity(nextActivity);
    }
}
