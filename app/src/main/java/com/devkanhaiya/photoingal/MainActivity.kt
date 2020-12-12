package com.devkanhaiya.photoingal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devkanhaiya.photoingal.Fragment.CameraFragment
import com.devkanhaiya.photoingal.Fragment.GoogleFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpTabLayout()
    }











    private fun setUpTabLayout() {

        val adapter = TabAdapter(supportFragmentManager)
        adapter.addFragment(CameraFragment(),"Local Photo")
        adapter.addFragment(GoogleFragment(),"Photos")
        view_pager.adapter = adapter

        tab_layout.setupWithViewPager(view_pager)
    }
}