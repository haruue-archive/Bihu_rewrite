package com.helloworld.bihu_rewrite.Adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by Administrator on 2016/2/24 0024.
 */
public class QuestionAdapter extends RecyclerArrayAdapter {


    public QuestionAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new QuestionViewHolder(parent);
    }

}
