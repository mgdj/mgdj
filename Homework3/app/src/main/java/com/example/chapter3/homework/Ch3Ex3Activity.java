package com.example.chapter3.homework;

import android.os.Bundle;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

/**
 * 使用 ViewPager 和 Fragment 做一个简单版的好友列表界面
 * 1. 使用 ViewPager 和 Fragment 做个可滑动界面
 * 2. 使用 TabLayout 添加 Tab 支持
 * 3. 对于好友列表 Fragment，使用 Lottie 实现 Loading 效果，在 5s 后展示实际的列表，要求这里的动效是淡入淡出
 */
public class Ch3Ex3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch3ex3);
        ViewPager view=findViewById(R.id.view_pager);
        TabLayout tab=findViewById(R.id.tab_layout);
        view.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new PlaceholderFragment();
            }
            @Override
            public int getCount() {
                return 3;
            }
            @Override
            public CharSequence getPageTitle(int pos){
                if(pos==1){
                    return "好友列表";
                }else if(pos==0){
                    return "消息列表";
                }else{
                    return "好友空间";
                }
            }
        });
        tab.setupWithViewPager(view);

        // TODO: ex3-1. 添加 ViewPager 和 Fragment 做可滑动界面



        // TODO: ex3-2, 添加 TabLayout 支持 Tab
    }
}
