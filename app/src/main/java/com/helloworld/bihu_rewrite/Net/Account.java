package com.helloworld.bihu_rewrite.Net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.helloworld.bihu_rewrite.DataClass.ApiPath;
import com.helloworld.bihu_rewrite.DataClass.Operations;
import com.helloworld.bihu_rewrite.DataClass.Listeners;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/25.
 */
public class Account {
    private String username = null;
    private String password = null;
    private OnLoginSuccessListener onLoginSuccessListener = null;
    private Listeners.OnFailListener onFailListener = null;
    private Context context;

    Handler handler = null;

    private static final String regist_path = "register.php";
    private static final String login_path = "login.php";
    private static final int op_login = 0;
    private static final int op_regist = 1;

    public void setOnFailListener(Listeners.OnFailListener onFailListener) {
        this.onFailListener = onFailListener;
    }

    public void setOnLoginSuccessListener(OnLoginSuccessListener onLoginSuccessListener) {
        this.onLoginSuccessListener = onLoginSuccessListener;
    }

    public Account(Context context, String username, String password, int operation) throws RuntimeException {
        this.username = username;
        this.password = password;
        this.context = context;
        if (operation == op_login){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    login();
                }
            }).start();
        }else if (operation == op_regist){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    regist();
                }
            }).start();
        }else throw new RuntimeException("Unknonw Exception");//不知道后面会加找回密码不,先这样写着
    }

    private void login(){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder().add("name",username).add("password",password).build();
        final Request request = new Request.Builder().url(ApiPath.PATH + login_path).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        try {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Looper.prepare();
                    handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onFailListener.OnFail(Operations.OP_LOGIN);
                        }
                    });
                    Looper.loop();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Looper.prepare();
                    handler = new Handler();
                    final Response finalResponse = response;
                    if (response.code() == 200) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onLoginSuccessListener.OnLoginSuccess(responseToMap(finalResponse));
                            }
                        });
                    } else {
                        Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void regist(){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder().add("name", username).add("password", password).build();
        final Request request = new Request.Builder().url(ApiPath.PATH+regist_path).post(requestBody).build();
        try {
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Looper.prepare();
                    handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onFailListener.OnFail(Operations.OP_REGIST);
                        }
                    });
                    Looper.loop();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Looper.prepare();
                    if (response.code() == 200) {
                        Toast.makeText(context, "注册成功，正在使用您新注册的账户进行登录", Toast.LENGTH_SHORT).show();
                        login();
                    } else {
                        handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onFailListener.OnFail(Operations.OP_REGIST);
                            }
                        });
                        Looper.loop();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String,String> responseToMap(Response response){
        try {
            JSONObject jsonObject = new JSONObject(response.body().string());
            Map<String,String> map = new HashMap<>();
            map.put("name",jsonObject.getString("name"));
            map.put("token",jsonObject.getString("token"));
            map.put("face",jsonObject.getString("face"));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface OnLoginSuccessListener{
        void OnLoginSuccess(Map<String, String> map);
    }

}
