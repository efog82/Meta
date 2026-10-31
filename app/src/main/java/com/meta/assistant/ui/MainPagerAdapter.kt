package com.meta.assistant.ui
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
class MainPagerAdapter(activity:FragmentActivity):FragmentStateAdapter(activity){
override fun getItemCount()=4
override fun createFragment(position:Int):Fragment=when(position){
0->ChatFragment()
1->AgendaFragment()
2->NotesFragment()
3->StudyFragment()
else->ChatFragment()
}
}
