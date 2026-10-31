package com.meta.assistant.ui
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.meta.assistant.databinding.ActivityMainBinding
class MainActivity:AppCompatActivity(){
private lateinit var binding:ActivityMainBinding
override fun onCreate(savedInstanceState:Bundle?){
super.onCreate(savedInstanceState)
binding=ActivityMainBinding.inflate(layoutInflater)
setContentView(binding.root)
binding.viewPager.adapter=MainPagerAdapter(this)
TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,pos->tab.text=listOf("Chat","Agenda","Notas","Estudo").getOrElse(pos){"Aba"}}.attach()
}
}
