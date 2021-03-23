package com.rania.useralbum.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rania.useralbum.UserAlbumApplication
import com.rania.useralbum.adapter.AlbumAdapter
import com.rania.useralbum.databinding.ActivityAlbumBinding
import com.rania.useralbum.model.Album
import com.rania.useralbum.repository.Resource
import com.rania.useralbum.utils.Constants
import com.rania.useralbum.viewmodel.AlbumViewModel
import com.rania.useralbum.viewmodel.AlbumViewModelFactory

class AlbumActivity : AppCompatActivity() {

    lateinit var mAlbumActivityBinding: ActivityAlbumBinding
    lateinit var mAlbumAdapter: AlbumAdapter
    var mAlbumList: List<Album> = emptyList()
    var mUserId: Int? = 0

    private val mAlbumViewModel: AlbumViewModel by viewModels {
        AlbumViewModelFactory((application as UserAlbumApplication).mAlbumRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAlbumActivityBinding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(mAlbumActivityBinding.root)

        setData()
        setView()
        setListener()
    }

    private fun setData() {
        mUserId = getIntent().getIntExtra(MainActivity.USER_EXTRA, Constants.DEFAULT_ID)

        mAlbumViewModel.albumList(mUserId!!).observe(this, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                Log.d(TAG, "Status ${it.status}")
                val hasData = it.data?.isNotEmpty()

                if (hasData!!) {
                    Log.d(TAG, "response not empty")
                    mAlbumList = it.data
                    mAlbumAdapter.updateList(mAlbumList)
                }
                setViewVisibility(hasData)
            }
        })
    }

    private fun setViewVisibility(hasData: Boolean) {
        if (hasData) {
            mAlbumActivityBinding.albumNoDataTextView.visibility = View.GONE
            mAlbumActivityBinding.albumRecyclerView.visibility = View.VISIBLE
        } else {
            mAlbumActivityBinding.albumNoDataTextView.visibility = View.VISIBLE
            mAlbumActivityBinding.albumRecyclerView.visibility = View.GONE
        }
    }


    private fun setView() {
        mAlbumAdapter = AlbumAdapter(this@AlbumActivity, mAlbumList)

        mAlbumActivityBinding.albumRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@AlbumActivity)
            adapter = mAlbumAdapter
        }
    }

    private fun setListener() {
        mAlbumAdapter.onItemClick = { item ->
            Log.i(TAG, "album item clicked")
            val intent = Intent(this@AlbumActivity, PhotoActivity::class.java)
            intent.putExtra(AlbumActivity.ALBUM_EXTRA, item.id)
            startActivity(intent)
        }
    }

    companion object {
        val TAG = AlbumActivity::class.java.simpleName
        val ALBUM_EXTRA = "album_extra"
    }
}