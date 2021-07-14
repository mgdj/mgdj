package com.example.chapter3.homework;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

public class PlaceholderFragment extends Fragment {
    private Context context;
    private RecyclerView rview;
    private LottieAnimationView lview;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        //lview=getView().findViewById(R.id.animation_view);
        //rview=getView().findViewById(R.id.rv);
        //lview.setVisibility(View.VISIBLE);
        //rview.setVisibility(View.GONE);
        View view=inflater.inflate(R.layout.fragment_placeholder,container,false);
        rview=view.findViewById(R.id.rv);
        lview=view.findViewById(R.id.animation_view);
        context= view.getContext();
        rview.setAlpha(0.0f);
        rview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        gridLayoutManager = new GridLayoutManager(context, 2);
        rview.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(TestDataJoin.getData());
        rview.setAdapter(mAdapter);
        LinearItemDecoration itemDecoration = new LinearItemDecoration(Color.BLUE);
        rview.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(3000);
        rview.setItemAnimator(animator);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rview=getView().findViewById(R.id.rv);
        lview=getView().findViewById(R.id.animation_view);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(lview,
                        "alpha",1.0f,0.0f);
                animator1.setDuration(500);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(rview,
                        "alpha",0.0f,1.0f);
                animator2.setDuration(500);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animator1,animator2);
                animatorSet.start();
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
            }
        }, 5000);
    }
}
