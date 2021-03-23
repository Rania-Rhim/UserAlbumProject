package com.rania.useralbum.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rania.useralbum.R
import com.rania.useralbum.UserAlbumApplication
import com.rania.useralbum.adapter.UserAdapter
import com.rania.useralbum.databinding.ActivityMainBinding
import com.rania.useralbum.model.User
import com.rania.useralbum.repository.Resource
import com.rania.useralbum.viewmodel.UserViewModel
import com.rania.useralbum.viewmodel.UserViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var mMainActivityBinding: ActivityMainBinding
    lateinit var mUserAdapter: UserAdapter
    var mUserList: List<User> = emptyList()

    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UserAlbumApplication).mUserRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mMainActivityBinding.root)

        setData()
        setView()
        setListener()
    }

    private fun setData() {
        mUserViewModel.userList.observe(this, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                Log.d(TAG, "Status ${it.status}")
                val hasData = it.data?.isNotEmpty()

                if (hasData!!) {
                    Log.d(TAG, "response not empty")
                    mUserList = it.data
                    mUserAdapter.updateList(mUserList)
                }
                setViewVisibility(hasData)
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

    private fun setView() {
        mUserAdapter = UserAdapter(this@MainActivity, mUserList)

        mMainActivityBinding.userRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mUserAdapter
        }
    }

    private fun setListener() {
        mUserAdapter.onItemClick = { item ->
            Log.i(TAG, "user item clicked")
            val intent = Intent(this@MainActivity, AlbumActivity::class.java)
            intent.putExtra(USER_EXTRA, item.id)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val search = menu.findItem(R.id.userSearchBar)
        val searchView = search.actionView as android.widget.SearchView
        searchView.queryHint = getString(R.string.user_search_menu)
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mUserAdapter.updateList(
                    mUserList.filter { it.name.contains(newText.toString(), true) }
                )
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        val TAG = MainActivity::class.java.simpleName
        val USER_EXTRA = "user_extra"
    }
}