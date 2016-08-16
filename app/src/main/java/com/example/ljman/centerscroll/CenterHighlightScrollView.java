package com.example.ljman.centerscroll;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ljman on 2016/8/15.
 */
public class CenterHighlightScrollView extends FrameLayout {

    private RecyclerView recyclerView;
    private RecyAdapter adapter;
    private float width;
    private float num;
    private ArrayList<String> datalist;
    private float trax = 0;
    private float item_width;
    private ScrollListener listener;
    private float content_right;

    public CenterHighlightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private void init(Context context) {

        recyclerView = new RecyclerView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(recyclerView, layoutParams);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
    }

    public void setParameter(ArrayList<String> datalist, float width, float num, ScrollListener listener) {
        this.width = width;
        this.num = num;
        this.item_width = width / num;
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < (num - 1); i++) {
            strings.add("");
        }
        strings.addAll((int) ((num - 1) / 2), datalist);
        this.datalist = strings;
        this.listener = listener;
        this.content_right=(num+1)*width/2/num;
        build();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    int a=-msg.arg1;
                    if (a>0){
                        for (int i = 0; i < a; i++) {
                            sendEmptyMessageDelayed(1,5*(i+1));
                        }
                    }else if (a<0){
                        for (int i = 0; i < -a; i++) {
                            sendEmptyMessageDelayed(-1,5*(i+1));
                        }
                    }
                    break;
                case 1:
                    recyclerView.scrollBy(1,0);
                    break;

                case -1:
                    recyclerView.scrollBy(-1,0);
                    break;
            }
        }
    };

    int ss = 0;
    private void build() {
        adapter = new RecyAdapter(datalist, width, num, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerVie, int dx, int dy) {
                super.onScrolled(recyclerVie, dx, dy);
                trax += dx;
                int a = (int) (Math.round(trax / -item_width) + (num + 1) / 2 - 1);
                if (ss != a) {
                    adapter.setView(a);
                }
                ss = a;
                adapter.scrollView();
            }
        });
    }

    public class RecyAdapter extends RecyclerView.Adapter {

        private ArrayList<String> arrayList;
        private ArrayList<RecyViewHolder> recyViewHolders;
        private int old_item = 0;
        private float width;
        private RecyViewHolder old;
        private float num;
        private ScrollListener listener;

        public RecyAdapter(ArrayList<String> arrayList, float width, float num, ScrollListener listener) {
            this.arrayList = arrayList;
            this.width = width;
            this.num = num;
            this.listener = listener;
            recyViewHolders=new ArrayList<>();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // TODO: 2016/8/16 可以在这里加载自定义的item布局
            TextView textView = new TextView(parent.getContext());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(Math.round(width / num), RecyclerView.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            //设置textview的style
            textView.setTextColor(0xFEBEC1C0);
            textView.setTextSize(12);
            RecyViewHolder recyViewHolder = new RecyViewHolder(textView);
            recyViewHolders.add(recyViewHolder);
            return recyViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((RecyViewHolder) holder).textView.setText(arrayList.get(position));
            ((RecyViewHolder) holder).position=position;
        }

        // TODO: 2016/8/16 在这里设置在条目居中是的style，记得一定要把之前居中的viewholder的style变回原来的
        public void setView(int i) {
            if (old!=null){
                old.textView.setTextColor(0xFEBEC1C0);
                old.textView.setTextSize(12);
            }
            for (RecyViewHolder t : recyViewHolders) {
                if (t.position == i){
                    t.textView.setTextColor(0xffffffff);
                    t.textView.setTextSize(20);
                    t.textView.setTag(new Object());
                    old=t;
                    if (listener!=null)
                        listener.scrolllistener(old.textView.getText().toString());
                }
            }
        }

        public void scrollView(){
            if (old!=null&&old.textView.getTag()!=null){
                handler.removeMessages(0);
                int tx= (int) (content_right-old.textView.getRight());
                Message message=new Message();
                message.what=0;
                message.arg1=tx;
                handler.sendMessageDelayed(message,100);
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class RecyViewHolder extends RecyclerView.ViewHolder {

            TextView textView;
            int position;

            public RecyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }

    public static interface ScrollListener {
        void scrolllistener(String data);
    }
}


