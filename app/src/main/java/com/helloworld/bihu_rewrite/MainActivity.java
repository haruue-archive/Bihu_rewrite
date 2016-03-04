package com.helloworld.bihu_rewrite;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.helloworld.bihu_rewrite.Adapter.AnswerAdapter;
import com.helloworld.bihu_rewrite.Adapter.QuestionAdapter;
import com.helloworld.bihu_rewrite.DataClass.Listeners;
import com.helloworld.bihu_rewrite.DataClass.Operations;
import com.helloworld.bihu_rewrite.DataClass.Question;
import com.helloworld.bihu_rewrite.DataSave.SaveData;
import com.helloworld.bihu_rewrite.Fragment.AnswerFragment;
import com.helloworld.bihu_rewrite.Fragment.QuestionFragment;
import com.helloworld.bihu_rewrite.Net.AandA;
import com.helloworld.bihu_rewrite.Net.Account;
import com.helloworld.bihu_rewrite.Net.QandA;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    private TextView exit = null;
    private TextView about = null;
    private TextView connecting = null;
    private RelativeLayout login_layout = null;
    private TextView username_txt = null;
    private ImageView user_icon = null;
    private ImageButton leftMore = null;
    private FloatingActionButton floatButton = null;
    private DrawerLayout drawerLayout = null;
    private EasyItemView itemView = null;
    private Dialog dialog = null;
    private TextView dialogConfirm = null;
    private TextView dialogCancel = null;
    private EditText dialogQuestion = null;
    private EditText dialogDetail = null;

    private FragmentManager fragmentManager = null;
    private QuestionFragment questionFragment = null;
    private AnswerFragment answerFragment = null;
    private FragmentTransaction fragmentTransaction = null;
    private QuestionAdapter questionAdapter = null;
    private AnswerAdapter answerAdapter = null;

    private Listener listener = null;
    Handler handler = new Handler();
    private Question question = null;
    private QandA qandA = null;
    private AandA aandA = null;
    private String token = null;
    private SaveData saveData = null;
    private String questionId = null;
    private boolean hasnetwork = false;
    private static final int initialpage = -1;
    private static final int questionpage = 0;
    private static final int answerpage = 1;
    private int currentpage = -1;
    private int q_page = 0;
    private int a_page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inits();
        initViews();
        new Thread(new Runnable() {
            @Override
            public void run() {
                qandA.getQuestions(q_page);
            }
        }).start();
    }

    private void inits(){
        listener = new Listener();
        saveData = new SaveData(this);
        questionAdapter = new QuestionAdapter(this);
        questionAdapter.setOnItemClickListener(listener);

        answerAdapter = new AnswerAdapter(this);
        fragmentManager = getSupportFragmentManager();
        qandA = new QandA();
        qandA.setOnFailListener(listener);
        qandA.setOnSuccessListener(listener);
        questionFragment = new QuestionFragment();
        questionFragment.setOnCreateEasyRecyclerViewLinstener(listener);
        answerFragment = new AnswerFragment();
        answerFragment.setOnCreateEasyRecyclerViewLinstener(listener);
    }

    private void initViews() {
        connecting = (TextView) findViewById(R.id.connecting_text);
        exit = (TextView) findViewById(R.id.exit);
        exit.setOnClickListener(listener);
        about = (TextView) findViewById(R.id.about);
        about.setOnClickListener(listener);
        login_layout = (RelativeLayout) findViewById(R.id.left_login);
        login_layout.setOnClickListener(listener);
        username_txt = (TextView) findViewById(R.id.main_username);//登录后单独设置
        user_icon = (ImageView) findViewById(R.id.mian_user_icon);
        Map<String,String> map = saveData.getAll();
        if (map.get("token") != null){
            token = map.get("token");
            username_txt.setText(map.get("name"));
//            user_icon.setImage;map.get("face")

        }
        leftMore = (ImageButton) findViewById(R.id.left_more);
        leftMore.setOnClickListener(listener);
        floatButton = (FloatingActionButton) findViewById(R.id.fab);
        floatButton.setOnClickListener(listener);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START);
        drawerLayout.setDrawerListener(listener);
        itemView = new EasyItemView();
    }

    private void changeFragment(int whichfragment){
        if (hasnetwork){
            if (connecting.getVisibility() != View.GONE) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        connecting.setVisibility(View.GONE);
                    }
                });
            }
        }else {
            if (connecting.getVisibility() != View.VISIBLE) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        connecting.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
        if (hasnetwork){
            fragmentTransaction = fragmentManager.beginTransaction();
            if (whichfragment == answerpage) {
                fragmentTransaction.replace(R.id.main_connecting, answerFragment);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        answerAdapter.notifyDataSetChanged();
                    }
                });
                currentpage = answerpage;
            } else {
                fragmentTransaction.replace(R.id.main_connecting, questionFragment);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        questionAdapter.notifyDataSetChanged();
                    }
                });
                currentpage = questionpage;
            }
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if(currentpage == answerpage){
            changeFragment(questionpage);
            a_page = 0;
            answerAdapter.clear();

        } else {
            super.onBackPressed();
        }
    }
    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            username_txt.setText(data.getStringExtra("username"));
            token = data.getStringExtra("token");
//            user_icon.setImage;
            saveData.saveAll(data.getStringExtra("username"), token, data.getStringExtra("face"));
        }
    }

    class Listener implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener,RecyclerArrayAdapter.OnLoadMoreListener,RecyclerArrayAdapter.OnItemClickListener,DrawerLayout.DrawerListener,Listeners.OnCreateEasyRecyclerViewLinstener,Listeners.OnSuccessListener,Listeners.OnFailListener,Account.OnLoginSuccessListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_more:
                    leftMore.setBackgroundResource(R.color.pressed);
                    drawerLayout.openDrawer(GravityCompat.START);
                    break;
                case R.id.exit:
                    finish();
                    break;
                case R.id.about:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
                case R.id.left_login:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivityForResult(new Intent(MainActivity.this, AccountActivity.class), 0);
                    break;
                case R.id.fab:
                    if (dialog == null) {
                        dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(getLayoutInflater().inflate(R.layout.input_dialog, null));
                        dialog.setCanceledOnTouchOutside(false);
                        dialogConfirm = (TextView) dialog.findViewById(R.id.input_dialog_confirm);
                        dialogConfirm.setOnClickListener(listener);
                        dialogCancel = (TextView) dialog.findViewById(R.id.input_dialog_cancrl);
                        dialogCancel.setOnClickListener(listener);
                        dialogQuestion = (EditText) dialog.findViewById(R.id.input_dialog_question);
                        dialogDetail = (EditText) dialog.findViewById(R.id.input_dialog_detail);
                    }
                    if (currentpage == answerpage) {
                        dialog.setTitle(getText(R.string.question_title_answer));
                        dialogQuestion.setText(question.getTitle());
                        dialogQuestion.setFocusableInTouchMode(false);
                        dialogDetail.setHint(R.string.question_title_answer);
                        dialogDetail.setText(null);
                    } else {
                        dialog.setTitle(getText(R.string.question_title_question));
                        dialogQuestion.setHint(R.string.question_title);
                        dialogQuestion.setText(null);
                        dialogQuestion.setFocusableInTouchMode(true);
                        dialogDetail.setHint(R.string.question_detail);
                        dialogDetail.setText(null);
                    }
                    dialog.show();
                    break;
                case R.id.input_dialog_cancrl:
                    dialog.dismiss();
                    break;
                case R.id.input_dialog_confirm:
                    if (token == null){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, R.string.not_login, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        break;
                    }
                    if (aandA == null) {
                        aandA = new AandA();
                        aandA.setOnFailListener(listener);
                        aandA.setOnSuccessListener(listener);
                    }
                    if (currentpage == answerpage) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                aandA.Rensponse(questionId,dialogDetail.getText().toString(),token);
                            }
                        }).start();
                    }else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                aandA.Ask(dialogQuestion.getText().toString(),dialogDetail.getText().toString(),token);
                            }
                        }).start();
                    }
            }
        }

        @Override
        public void onRefresh() {//SwipeRefreshLayout.OnRefreshListener
            if (currentpage == answerpage) {
                answerAdapter.clear();
                a_page = 0;
                answerAdapter.resumeMore();
            } else {
                questionAdapter.clear();
                q_page = 0;
                questionAdapter.resumeMore();
            }
        }

        @Override
        public void onLoadMore() {
            if (currentpage == answerpage) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        qandA.getAnswers(a_page, questionId);
                        a_page++;
                    }
                }).start();
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        qandA.getQuestions(q_page);
                        q_page++;
                    }
                }).start();
            }
        }

        @Override
        public void onItemClick(int position) {
            if (currentpage == questionpage){
                changeFragment(answerpage);
                question = (Question) questionAdapter.getItem(position);
                questionId = question.getId();
                itemView.setViews(question.getTitle(), question.getContent(), question.getDate(), question.getAuthorName());
                onLoadMore();
            }
        }
        @Override
        public void initEasyRecyclerView(int operation, EasyRecyclerView easyRecyclerView) {
            easyRecyclerView.setRefreshListener(listener);
            if (operation == Operations.OP_ANSWER) {//answer
                if (easyRecyclerView.getAdapter() == null) {
                    easyRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    easyRecyclerView.setAdapterWithProgress(answerAdapter);
                    answerAdapter.setError(R.layout.connect_fail).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    answerAdapter.resumeMore();
                                }
                            }).start();
                        }
                    });
                    answerAdapter.setMore(R.layout.loading, listener);
                    answerAdapter.setNoMore(R.layout.nomore);
                    answerAdapter.addHeader(itemView);
                    itemView.setViews(question.getTitle(), question.getContent(), question.getDate(), question.getAuthorName());
                }
            }else if (operation == Operations.OP_QUESTION) {
                if (easyRecyclerView.getAdapter() == null) {
                    easyRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    easyRecyclerView.setAdapterWithProgress(questionAdapter);
                    questionAdapter.setError(R.layout.connect_fail).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            questionAdapter.resumeMore();
                        }
                    });
                    questionAdapter.setMore(R.layout.loading, listener);
                    questionAdapter.setNoMore(R.layout.nomore);
                }
            }
        }

        @Override
        public void OnSuccess(int operation, final List data) {
            hasnetwork = true;
            if (operation == Operations.OP_QUESTION) {
                if (questionAdapter.getCount() == 0) {
                    changeFragment(questionpage);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        questionAdapter.addAll(data);
                        questionAdapter.notifyDataSetChanged();
                    }
                });
            }else if (operation == Operations.OP_ANSWER) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        answerAdapter.addAll(data);
                        answerAdapter.notifyDataSetChanged();
                    }
                });
            }else if (operation == Operations.OP_ASK) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, R.string.ask_success, Toast.LENGTH_SHORT).show();                                dialog.dismiss();
                        dialog.dismiss();
                        onRefresh();
                    }
                });
            }else if (operation == Operations.OP_ANSWER_QUESTION) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, R.string.answer_success, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        onRefresh();
                    }
                });
            }else if (operation == Operations.OP_REGIST) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, R.string.regist_success, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public void OnFail(int operation) {
            if (operation == Operations.OP_QUESTION||operation == Operations.OP_ANSWER||operation == Operations.OP_LOGIN) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, R.string.connecting_fail, Toast.LENGTH_SHORT).show();
                    }
                });
                hasnetwork = true;
            }else if (operation == Operations.OP_ASK||operation == Operations.OP_ANSWER_QUESTION) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, R.string.commite_fail, Toast.LENGTH_SHORT).show();
                        }
                    });
            }else if (operation == Operations.OP_REGIST) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, R.string.regist, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {}
        @Override
        public void onDrawerOpened(View drawerView) {
            leftMore.setBackgroundResource(R.color.pressed);
        }
        @Override
        public void onDrawerClosed(View drawerView) {
            leftMore.setBackgroundResource(R.color.none);
        }
        @Override
        public void onDrawerStateChanged(int newState) {}

        @Override
        public void OnLoginSuccess(Map<String, String> map) {
            Toast.makeText(MainActivity.this,R.string.login_success,Toast.LENGTH_SHORT).show();
            token = map.get("token");
            username_txt.setText(map.get("name"));
//            user_icon.setImage;map.get("face")
            saveData.saveAll(map.get("name"),map.get("token"),map.get("face"));
        }
    }
    class EasyItemView implements RecyclerArrayAdapter.ItemView {
        private View view = null;
        private TextView atitle = null;
        private TextView adetail = null;
        private TextView atime = null;
        private TextView auser = null;
        private String title  = null;
        private String detail = null;
        private String time = null;
        private String user = null;
        @Override
        public View onCreateView(ViewGroup parent) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.detail_item, parent, false);
                //TODO: 下面这个被我禁用的函数调用导致了 API 21 以下的崩溃问题。。。 --Haruue Icymoon
//                view.setBackgroundColor(0);
                atitle = (TextView) view.findViewById(R.id.question_title);
                adetail = (TextView) view.findViewById(R.id.question_detail);
                atime = (TextView) view.findViewById(R.id.quest_time);
                auser = (TextView) view.findViewById(R.id.item_username);
                atitle.setText(title);
                adetail.setText(detail);
                atime.setText(time);
                auser.setText(user);
            }
            return view;
        }

            @Override
            public void onBindView(View headerView) {}

        public void setViews(String title, String detail, String time, String user) {
            if (atitle == null) {
                this.title = title;
                this.detail = detail;
                this.user = user;
                this.time = time;
            } else {
                atitle.setText(title);
                adetail.setText(detail);
                atime.setText(time);
                auser.setText(user);
            }
        }
    }
}
