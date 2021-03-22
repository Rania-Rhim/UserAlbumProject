package com.rania.useralbum.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rania.useralbum.adapter.UserAdapter
import com.rania.useralbum.databinding.ActivityMainBinding
import com.rania.useralbum.model.User
import com.rania.useralbum.network.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var mMainActivityBinding: ActivityMainBinding
    lateinit var mUserAdapter: UserAdapter
    lateinit var mUserList: List<User>

    val mUserServe by lazy {
        UserService.createNetworkService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mMainActivityBinding.root)

        setData()
    }


    private fun setData() {
        mUserServe.getUserList().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.code() == 200 && response.body() != null) {
                    mUserList = response.body()!!
                    val hasData = mUserList.isNotEmpty()

                    if (hasData) {
                        mUserAdapter = UserAdapter(this@MainActivity, mUserList)
                        mMainActivityBinding.userRecyclerView.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            adapter = mUserAdapter
                        }
                    }
                    setViewVisibility(hasData)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setViewVisibility(hasData: Boolean) {
        if (hasData) {
            mMainActivityBinding.userNoDataTextView.visibility = View.GONE
            mMainActivityBinding.userRecyclerView.visibility = View.VISIBLE
        } else {
            mMainActivityBinding.userNoDataTextView.visibility = View.VISIBLE
            mMainActivityBinding.userRecyclerView.visibility = View.GONE
        }
    }
}