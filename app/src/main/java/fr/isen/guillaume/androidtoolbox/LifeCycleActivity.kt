package fr.isen.guillaume.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_life_cycle.*

enum class State {
    FOREGROUND, BACKGROUND, DESTROY
}

class LifeCycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)

        notification(State.FOREGROUND)
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
            State.DESTROY -> Toast.makeText(this, getString(R.string.activity_destroyed), Toast.LENGTH_LONG).show()
        }
    }
}
