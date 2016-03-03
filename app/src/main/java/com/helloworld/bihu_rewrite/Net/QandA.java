package com.helloworld.bihu_rewrite.Net;

import android.accounts.NetworkErrorException;

import com.helloworld.bihu_rewrite.DataClass.Answer;
import com.helloworld.bihu_rewrite.DataClass.ApiPath;
import com.helloworld.bihu_rewrite.DataClass.Operations;
import com.helloworld.bihu_rewrite.DataClass.Question;
import com.helloworld.bihu_rewrite.DataClass.Listeners;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/24 0024.
 */
public class QandA<T> {


    private Listeners.OnSuccessListener<T> onSuccessListener = null;
    private Listeners.OnFailListener onFailListener = null;
    public void setOnFailListener(Listeners.OnFailListener onFailListener) {
        this.onFailListener = onFailListener;
    }
    public void setOnSuccessListener(Listeners.OnSuccessListener<T> onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }


    List<T> questions = null;
    List<T> answers = null;
    private static final String question_path = "getQuestionList.php";
    private static final String answer_path = "getAnswerList.php";


    public void getQuestions(int page) {
        //接收数据,解析,储存
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder().add("page", String.valueOf(page)).add("count", String.valueOf(10)).build();
        Request request = new Request.Builder().url(ApiPath.PATH + question_path).post(requestBody).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response == null) {
                throw new NetworkErrorException("Unknow Error");
            } else if (response.isSuccessful()) {
                json2List_q(response.body().string());
            }
            onSuccessListener.OnSuccess(Operations.OP_QUESTION, questions);

        } catch (Exception e) {
            e.printStackTrace();
            questions = null;
            onFailListener.OnFail(Operations.OP_QUESTION);
        }
    }

    private void json2List_q(String jsonstr){
        questions = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonstr);

        JSONArray jsonArray = jsonObject.getJSONArray("questions");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            Question question = new Question(item.getString("id"), item.getString("title"), item.getString("content"), item.getString("date"), item.getString("recent"), item.getString("authorName"), item.getString("authorFace"));
            questions.add((T) question);
        }
        } catch (JSONException e) {
        e.printStackTrace();
            questions = null;
    }
    }

    public void getAnswers(int  page,String questionId) {
        //接收数据,解析,储存
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder().add("page", String.valueOf(page)).add("questionId",questionId).add("count", String.valueOf(10)).build();
        Request request = new Request.Builder().url(ApiPath.PATH + answer_path).post(requestBody).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response == null) {
                throw new NetworkErrorException("Unknow Error");
            } else if (response.isSuccessful()) {
                json2List_a(response.body().string());
            }
            if (answers == null){
                onSuccessListener.OnSuccess(Operations.OP_ANSWER, null);
                return;
            }
            onSuccessListener.OnSuccess(Operations.OP_ANSWER, answers);

        } catch (Exception e) {
            e.printStackTrace();
            answers = null;
            onFailListener.OnFail(Operations.OP_ANSWER);
        }
    }
    private void json2List_a(String jsonstr){
        try {
            answers = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(jsonstr);
            JSONArray jsonArray = jsonObject.getJSONArray("answers");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                Answer answer = new Answer(item.getString("id"), item.getString("content"), item.getString("date"), item.getString("authorId"), item.getString("authorName"), item.getString("authorFace"));
                answers.add((T) answer);
            }
        }catch (JSONException e){
            e.printStackTrace();
            answers = null;
        }

    }



}
