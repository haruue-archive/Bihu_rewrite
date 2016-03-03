package com.helloworld.bihu_rewrite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.helloworld.bihu_rewrite.DataClass.Operations;
import com.helloworld.bihu_rewrite.DataClass.Listeners;
import com.helloworld.bihu_rewrite.Net.Account;

import java.util.Map;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener,Account.OnLoginSuccessListener,Listeners.OnFailListener{

    private ImageButton back = null;
    private Spinner spinner = null;
    private EditText username_edit = null;
    private EditText password_edit = null;
    private EditText repassword_edit = null;
    private Button login_sign = null;

    private String username = null;
    private String password = null;
    private Account account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initViews();
    }

    private void initViews(){
        back = (ImageButton) findViewById(R.id.account_back);
        back.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.account_operation_spiner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    repassword_edit.setVisibility(View.GONE);
                    login_sign.setText(R.string.login);
                }else if (position == 1){
                    login_sign.setText(R.string.regist);
                    repassword_edit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        username_edit = (EditText) findViewById(R.id.account_username);
        password_edit = (EditText) findViewById(R.id.account_password);
        repassword_edit = (EditText) findViewById(R.id.account_repassword);
        login_sign = (Button) findViewById(R.id.login_sign);
        login_sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_sign:
                if ((username = username_edit.getText().toString()).isEmpty() || (password = password_edit.getText().toString()).isEmpty()) {
                    Toast.makeText(AccountActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }else if ((spinner.getSelectedItemPosition() != 0)&& repassword_edit.getText().toString().isEmpty()) {
                    Toast.makeText(AccountActivity.this, "请重复密码", Toast.LENGTH_SHORT).show();
                }else if ((spinner.getSelectedItemPosition() != 0) && !(password = password_edit.getText().toString()).equals(repassword_edit.getText().toString())) {
                    Toast.makeText(AccountActivity.this, "两次密码不同", Toast.LENGTH_SHORT).show();
                } else {
                    account = new Account(username, password, spinner.getSelectedItemPosition());
                    account.setOnLoginSuccessListener(this);
                    account.setOnFailListener(this);
                }
                break;
            case R.id.account_back:
                finish();
                break;
        }
    }

    @Override
    public void OnFail(int operation) {
        if (operation == Operations.OP_REGIST){
            Toast.makeText(this,getText(R.string.connecting_fail)+"或此账户已被注册",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,R.string.connecting_fail,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnLoginSuccess(Map<String,String> data) {
        if (data != null){
            Intent intent = new Intent();
            intent.putExtra("username", username);
            intent.putExtra("token", data.get("token"));
//        intent.putExtra("usericon",data.get("face"));
            setResult(1, intent);
            Toast.makeText(this,R.string.login_success,Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
