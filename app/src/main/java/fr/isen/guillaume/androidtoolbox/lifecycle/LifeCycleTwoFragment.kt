package fr.isen.guillaume.androidtoolbox.lifecycle


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import fr.isen.guillaume.androidtoolbox.R
import fr.isen.guillaume.androidtoolbox.State
import kotlinx.android.synthetic.main.fragment_life_cycle_two.*

class LifeCycleTwoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_life_cycle_two, container, false)
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
            State.FOREGROUND -> txtLifeCycle.text = getString(R.string.fragment_two_foreground)
            State.BACKGROUND -> Log.d("TAG", getString(R.string.fragment_two_background))
            State.DESTROY -> Toast.makeText(context, getString(R.string.fragment_two_destroyed), Toast.LENGTH_LONG).show()
        }
    }
}
