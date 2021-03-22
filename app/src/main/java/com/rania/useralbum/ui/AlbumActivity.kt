package com.rania.useralbum.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rania.useralbum.adapter.AlbumAdapter
import com.rania.useralbum.databinding.ActivityAlbumBinding
import com.rania.useralbum.model.Album
import com.rania.useralbum.network.UserService
import com.rania.useralbum.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumActivity : AppCompatActivity() {

    lateinit var mAlbumActivityBinding: ActivityAlbumBinding
    lateinit var mAlbumAdapter: AlbumAdapter
    lateinit var mAlbumList: List<Album>
    var mUserId: Int? = 0

    val mAlbumServe by lazy {
        UserService.createNetworkService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAlbumActivityBinding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(mAlbumActivityBinding.root)

        setData()
    }

    private fun setData() {
        mUserId = getIntent().getIntExtra(MainActivity.USER_EXTRA, Constants.DEFAULT_ID)

        mAlbumServe.getAlbumList(mUserId!!).enqueue(object : Callback<List<Album>> {
            override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
                if (response.code() == 200 && response.body() != null) {
                    mAlbumList = response.body()!!
                    val hasData = mAlbumList.isNotEmpty()

                    if (hasData) {
                        mAlbumAdapter = AlbumAdapter(this@AlbumActivity, mAlbumList)
                        mAlbumActivityBinding.albumRecyclerView.apply {
                            layoutManager = LinearLayoutManager(this@AlbumActivity)
                            adapter = mAlbumAdapter
                        }
                        mAlbumAdapter.onItemClick = { item ->
                            Log.i(TAG, "album item clicked")
                            val intent = Intent(this@AlbumActivity, PhotoActivity::class.java)
                            intent.putExtra(AlbumActivity.ALBUM_EXTRA, item.id)
                            startActivity(intent)
                        }
                    }
                    setViewVisibility(hasData)
                }
            }

            override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                TODO("Not yet implemented")
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

    companion object {
        val TAG = AlbumActivity::class.java.simpleName
        val ALBUM_EXTRA = "album_extra"
    }
}