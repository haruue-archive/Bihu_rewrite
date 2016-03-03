package com.helloworld.bihu_rewrite.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helloworld.bihu_rewrite.DataClass.Listeners;
import com.helloworld.bihu_rewrite.DataClass.Operations;
import com.helloworld.bihu_rewrite.R;
import com.jude.easyrecyclerview.EasyRecyclerView;

/**
 * Created by Administrator on 2016/2/25.
 */
public class QuestionFragment extends Fragment {

    private EasyRecyclerView easyRecyclerView = null;
    private Listeners.OnCreateEasyRecyclerViewLinstener onCreateEasyRecyclerViewLinstener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (easyRecyclerView == null) {
            easyRecyclerView = (EasyRecyclerView) inflater.inflate(R.layout.easyrecyclerview, container, false);
            onCreateEasyRecyclerViewLinstener.initEasyRecyclerView(Operations.OP_QUESTION, easyRecyclerView);
        }
        return easyRecyclerView;
    }

    public void setOnCreateEasyRecyclerViewLinstener(Listeners.OnCreateEasyRecyclerViewLinstener onCreateEasyRecyclerViewLinstener) {
        this.onCreateEasyRecyclerViewLinstener = onCreateEasyRecyclerViewLinstener;
    }
}
