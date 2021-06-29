package com.yjr.meeting

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapter(fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager) {
    //이 클래스를 상속 받으면 오버라이딩을 해야하는 함수들을 해줘야한다
    override fun getItem(position: Int): Fragment { //각각의 텝이 눌러질 떄마다 그 포지션이 호출이 된다.
        if(position==0){
            return HomeFragment()
        }
        else if(position==1){
            return AddFragment()
        }
        else if(position==2){
            return StarFragment()
        }
        else{
            return ProfileFragment()
        }






    }

    override fun getCount(): Int { //우리가 가지고있는 페이지는 5개 즉 int는 5
       return 4
    }

}