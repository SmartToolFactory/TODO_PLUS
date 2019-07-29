package com.smarttoolfactory.todoplus.tasks.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

// TODO Tabs don't work wih ViewPager2 in this version
/**
 * [FragmentStateAdapter] for [androidx.viewpager2.widget.ViewPager2] which is contained inside [MainActivity]. Swipe to change
 * between list or map.
 */
//class ViewPager2FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
//
//    private val fragmentList = mutableListOf<Fragment>()
//    private val titleList = mutableListOf<String>()
//
//    public fun addFragment(fragment: Fragment) {
//        if (!fragmentList.contains(fragment)) {
//            fragmentList.add(fragment)
//        }
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        return fragmentList[position]
//    }
//
//    override fun getItemCount(): Int {
//        return fragmentList.size
//    }
//}