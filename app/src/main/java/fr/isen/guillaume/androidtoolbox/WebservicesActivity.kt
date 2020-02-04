package fr.isen.guillaume.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.isen.guillaume.androidtoolbox.model.user.User
import fr.isen.guillaume.androidtoolbox.recycler.user.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_webservices.*
import org.json.JSONObject

class WebservicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webservices)

        val users: ArrayList<User> = ArrayList()
        val gson = GsonBuilder().create()

        makeRequest(false, users, gson)
        setScrollListener(users, gson)
    }

    private fun makeRequest(isUpdate: Boolean, users: ArrayList<User>, gson: Gson) {
        val request = JsonObjectRequest(Request.Method.GET, URL, null, Response.Listener<JSONObject> {
            val jsonArray = it.getJSONArray(("results"))
            for (index in 0 until jsonArray.length()) { users.add(gson.fromJson(jsonArray.getJSONObject(index).toString(), User::class.java)) }
            if (!isUpdate) {
                recyclerWebservices.layoutManager = LinearLayoutManager(this)
                recyclerWebservices.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                recyclerWebservices.adapter = RecyclerAdapter(users)
            } else
                recyclerWebservices.adapter?.notifyDataSetChanged()
        }, Response.ErrorListener {
            Toast.makeText(this, getString(R.string.error_users_webservices), Toast.LENGTH_LONG).show()
        })
        Volley.newRequestQueue(this).add(request)
    }

    private fun setScrollListener(users: ArrayList<User>, gson: Gson) {
        recyclerWebservices.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1))
                    makeRequest(true, users, gson)
            }
        })
    }

    companion object {
        private const val URL = "https://randomuser.me/api/?inc=name,location,email,picture&results=10"
    }
}
