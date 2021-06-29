package com.yjr.meeting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {


    lateinit var viewpager:ViewPager
    lateinit var tabLayout:TabLayout

    lateinit var pageAdapter:PageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager=findViewById(R.id.viewpager)
        tabLayout=findViewById(R.id.tablayout)


        pageAdapter=PageAdapter(supportFragmentManager) // 엑티비티에 기본으로 들어있는 멤버변수
        //페이지어뎀터가 이를 달아서 일어나는 ui적 변화를 프레그먼트에 전달가능하게해준다.
        //페이지어뎁터를 뷰 페이저에 연결 하기.
        viewpager.adapter=pageAdapter //뷰페이저의 어뎁터를 페이지 어뎁터로 해주었다. 각각의 프레그먼트..



        tabLayout.setupWithViewPager(viewpager)
        //탭레이어가 변경될때 뷰페이저로 전달해준다.

        //아이콘 버그 해결
        tabLayout.getTabAt(0)?.setIcon(R.drawable.baseline_home_black_48)?.setText("홈")
        tabLayout.getTabAt(1)?.setIcon(R.drawable.baseline_add_black_48)?.setText("모집 공고")
        tabLayout.getTabAt(2)?.setIcon(R.drawable.baseline_star_outline_black_48)?.setText("채팅")
        tabLayout.getTabAt(3)?.setIcon(R.drawable.baseline_perm_identity_black_48)?.setText("마이페이지")
    }

    //업로드이후 홈으로 이동하는 함수
    fun moveTab(position:Int){
        tabLayout.selectTab(tabLayout.getTabAt(position))
    }
}