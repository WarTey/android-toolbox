package fr.isen.guillaume.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.muddzdev.styleabletoast.StyleableToast
import fr.isen.guillaume.androidtoolbox.lifecycle.LifeCycleOneFragment
import fr.isen.guillaume.androidtoolbox.lifecycle.LifeCycleTwoFragment
import fr.isen.guillaume.androidtoolbox.tools.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_life_cycle.*

enum class State {
    FOREGROUND, BACKGROUND, DESTROY
}

class LifeCycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        val fragmentOne = LifeCycleOneFragment()
        val fragmentTwo = LifeCycleTwoFragment()

        notification(State.FOREGROUND)
        initViewPager(viewPagerAdapter, fragmentOne, fragmentTwo)
        btnFragment.setOnClickListener { switchFragment(viewPagerAdapter, fragmentOne, fragmentTwo) }
    }

    override fun onStart() {
        super.onStart()
        notification(State.FOREGROUND)
    }

    override fun onResume() {
        super.onResume()
        notification(State.FOREGROUND)
    }

    override fun onPause() {
        super.onPause()
        notification(State.BACKGROUND)
    }

    override fun onStop() {
        super.onStop()
        notification(State.BACKGROUND)
    }

    override fun onDestroy() {
        super.onDestroy()
        notification(State.DESTROY)
    }

    private fun notification(state : State) {
        when (state) {
            State.FOREGROUND -> txtLifeCycle.text = getString(R.string.activity_foreground)
            State.BACKGROUND -> Log.d("TAG", getString(R.string.activity_background))
            State.DESTROY -> StyleableToast.makeText(this, getString(R.string.activity_destroyed), Toast.LENGTH_LONG, R.style.StyleToast).show()
        }
    }

    private fun initViewPager(viewPagerAdapter: ViewPagerAdapter, fragmentOne: LifeCycleOneFragment, fragmentTwo: LifeCycleTwoFragment) {
        viewPagerAdapter.addFragment(fragmentOne)
        viewPagerAdapter.addFragment(fragmentTwo)
        viewPagerFragment.adapter = viewPagerAdapter
    }

    private fun checkCurrentPage(viewPagerAdapter: ViewPagerAdapter, fragmentTwo: LifeCycleTwoFragment): Boolean {
        return viewPagerFragment.currentItem == viewPagerAdapter.getItemPosition(fragmentTwo)
    }

    private fun switchFragment(viewPagerAdapter: ViewPagerAdapter, fragmentOne: LifeCycleOneFragment, fragmentTwo: LifeCycleTwoFragment) {
        if (checkCurrentPage(viewPagerAdapter, fragmentTwo))
            viewPagerFragment.setCurrentItem(viewPagerAdapter.getItemPosition(fragmentOne), true)
        else
            viewPagerFragment.setCurrentItem(viewPagerAdapter.getItemPosition(fragmentTwo), true)
    }
}
