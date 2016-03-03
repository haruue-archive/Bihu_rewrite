package com.helloworld.bihu_rewrite.Net;

import android.accounts.NetworkErrorException;

import com.helloworld.bihu_rewrite.DataClass.ApiPath;
import com.helloworld.bihu_rewrite.DataClass.Operations;
import com.helloworld.bihu_rewrite.DataClass.Listeners;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by Administrator on 2016/2/28.
 */
public class AandA {
    private static final String askquestion_path = "question.php";
    private static final String answerquestion_path = "answer.php";
    private Listeners.OnSuccessListener onSuccessListener = null;

    public void setOnSuccessListener(Listeners.OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    private Listeners.OnFailListener onFailListener = null;

    public void setOnFailListener(Listeners.OnFailListener onFailListener) {
        this.onFailListener = onFailListener;
    }

    public void Ask(String title,String content,String token){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder().add("title",title).add("content", content).add("token",token).build();
        Request request = new Request.Builder().url(ApiPath.PATH + askquestion_path).post(requestBody).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response == null) {
                throw new NetworkErrorException("Unknow Error");
            } else if (response.isSuccessful()) {
                onSuccessListener.OnSuccess(Operations.OP_ASK, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            onFailListener.OnFail(Operations.OP_ASK);
        }

    }
    public void Rensponse(String questionId,String content,String token){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder().add("questionId",questionId).add("content", content).add("token",token).build();
        Request request = new Request.Builder().url(ApiPath.PATH + answerquestion_path).post(requestBody).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response == null) {
                throw new NetworkErrorException("Unknow Error");
            } else if (response.isSuccessful()) {
                onSuccessListener.OnSuccess(Operations.OP_ANSWER_QUESTION,null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            onFailListener.OnFail(Operations.OP_ANSWER_QUESTION);
        }

    }
}
