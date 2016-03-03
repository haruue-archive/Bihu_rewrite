package com.helloworld.bihu_rewrite.Adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by Administrator on 2016/2/28.
 */
public class AnswerAdapter extends RecyclerArrayAdapter {

    public AnswerAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnswerViewHolder(parent);
    }
}
