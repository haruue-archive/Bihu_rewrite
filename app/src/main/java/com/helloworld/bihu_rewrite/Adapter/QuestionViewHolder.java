package com.helloworld.bihu_rewrite.Adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.helloworld.bihu_rewrite.DataClass.Question;
import com.helloworld.bihu_rewrite.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/2/24 0024.
 */
public class QuestionViewHolder extends BaseViewHolder<Question> {

    private TextView qtitle = null;
    private TextView qdetail = null;
    private TextView username = null;
    private TextView time = null;

    public QuestionViewHolder(ViewGroup parent) {
        super(parent, R.layout.detail_item);
        qtitle = $(R.id.question_title);
        qdetail = $(R.id.question_detail);
        username = $(R.id.item_username);
        time = $(R.id.quest_time);
    }

    @Override
    public void setData(final Question data) {
        qtitle.setText(data.getTitle());
        qdetail.setText(data.getContent());
        username.setText(data.getAuthorName());
        time.setText(data.getDate());

    }
}
