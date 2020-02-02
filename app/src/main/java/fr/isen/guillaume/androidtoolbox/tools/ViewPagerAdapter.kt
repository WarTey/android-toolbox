package fr.isen.guillaume.androidtoolbox.tools

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(manager: FragmentManager, private val fragmentList: MutableList<Fragment> = ArrayList()) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItemPosition(`object`: Any): Int {
        for ((index, fragment) in fragmentList.withIndex())
            if (`object` == fragment)
                return index
        return super.getItemPosition(`object`)
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }
}