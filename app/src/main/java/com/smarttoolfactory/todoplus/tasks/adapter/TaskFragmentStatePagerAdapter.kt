package com.smarttoolfactory.todoplus.tasks.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class TaskFragmentStatePagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }


    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }


    // TODO Uncomment to recreate fragments with notifyDataSetChanged()
    // @Override
    // public int getItemPosition(Object object) {
    // return POSITION_NONE;
    // }

    override fun instantiateItem(arg0: ViewGroup, arg1: Int): Any {
        return super.instantiateItem(arg0, arg1)
    }

}