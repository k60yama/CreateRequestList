package com.example.createrequestlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class Category extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		//Activity��OnCreate�����s
		super.onCreate(savedInstanceState);
		
		//���C�A�E�g�ݒ�t�@�C���̎w��
		this.setContentView(R.layout.category);
	}

	//�J�e�S���{�^���������̏���
	public void orderItemInfo(View view){
		
		//�C���e���g����
		Intent nextActivity = new Intent(this, SelectItems.class);
		
		//�J�ڐ�t�����ϐ�������
		String categoryName = "";
		
		//�����ʂ̕t�����ݒ�
		switch(view.getId()){
		case R.id.EggAndDairyAndDrink:
			categoryName = "���E�����i�E����";
			break;
		case R.id.ProcessItems:
			categoryName = "���H�i";
			break;
		case R.id.FishItems:
			categoryName = "����";
			break;
		case R.id.MeetItems:
			categoryName = "����";
			break;
		case R.id.VegetableItems:
			categoryName = "���";
			break;
		case R.id.FruitItems:
			categoryName = "�ʕ�";
			break;
		case R.id.ElseItems:
			categoryName = "���̑�";
			break;
		}
		
		//�C���e���g�t�����ݒ�
		nextActivity.putExtra("CategoryName", categoryName);
		
		//�A�N�e�B�r�e�B�N��
		this.startActivity(nextActivity);
	}
	
	//�g�b�v�y�[�W�փ{�^���������̏���
	public void returnTopPage(View view){
		//�A�N�e�B�r�e�B�I��
		this.finish();
	}
	
	//�����m�F�փ{�^���������̏���
	public void toConfirmPage(View view){
		
		//�C���e���g����
		Intent nextActivity = new Intent(this, OrderedConfirm.class);
		
		//���̃A�N�e�B�r�e�B�N��
		this.startActivity(nextActivity);
	}
	
	//Back�L�[����
	@Override
	public boolean dispatchKeyEvent(KeyEvent event){
		//�{�^�����������ꍇ
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			//Back�L�[���������ꂽ�ꍇ
			if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
}
