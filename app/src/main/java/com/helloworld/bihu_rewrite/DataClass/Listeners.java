package com.helloworld.bihu_rewrite.DataClass;

import com.helloworld.bihu_rewrite.DataClass.Question;
import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class Listeners {
    public interface OnSuccessListener<T>{
        void OnSuccess(int operation, List<T> data);
    }
/*    public interface OnSuccessListener{
        void OnSuccess();
    }*/
    public interface OnFailListener{
        void OnFail(int operation);
    }

    public interface OnCreateEasyRecyclerViewLinstener{
        void initEasyRecyclerView(int operation, EasyRecyclerView easyRecyclerView);
    }
    public interface OnSendRequestSuccessListener {
        void OnSendRequestSuccess(int operation);
    }
    public interface OnAnswerFragmentCreateListener{
        void OnAnswerFragmentCreate(Question question, String questionId);
    }
}
