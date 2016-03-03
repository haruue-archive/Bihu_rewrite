package com.helloworld.bihu_rewrite.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helloworld.bihu_rewrite.DataClass.Answer;
import com.helloworld.bihu_rewrite.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/2/24 0024.
 */
public class AnswerViewHolder extends BaseViewHolder<Answer> {

    private TextView qdetail = null;
    private TextView username = null;
    private TextView time = null;

    public AnswerViewHolder(ViewGroup parent) {
        super(parent, R.layout.detail_item);
        $(R.id.question_title).setVisibility(View.GONE);
        qdetail = $(R.id.question_detail);
        username = $(R.id.item_username);
        time = $(R.id.quest_time);
    }

    @Override
    public void setData(final Answer data) {
        qdetail.setText(data.getContent());
        username.setText(data.getAuthorName());
        time.setText(data.getDate());

    }
}
