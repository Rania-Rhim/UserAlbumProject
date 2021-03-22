package com.rania.useralbum.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.rania.useralbum.adapter.PhotoAdapter
import com.rania.useralbum.databinding.ActivityPhotoBinding
import com.rania.useralbum.model.Photo
import com.rania.useralbum.network.UserService
import com.rania.useralbum.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoActivity : AppCompatActivity() {

    lateinit var mPhotoActivityBinding: ActivityPhotoBinding
    lateinit var mPhotoAdapter: PhotoAdapter
    lateinit var mPhotoList: List<Photo>
    var mAlbumId: Int? = 0

    val mPhotoServe by lazy {
        UserService.createNetworkService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPhotoActivityBinding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(mPhotoActivityBinding.root)

        setData()
    }


    private fun setData() {
        mAlbumId = getIntent().getIntExtra(AlbumActivity.ALBUM_EXTRA, Constants.DEFAULT_ID)

        mPhotoServe.getPhotoList(mAlbumId!!).enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (response.code() == 200 && response.body() != null) {
                    mPhotoList = response.body()!!
                    val hasData = mPhotoList.isNotEmpty()

                    if (hasData) {
                        mPhotoAdapter = PhotoAdapter(this@PhotoActivity, mPhotoList)
                        mPhotoActivityBinding.photoRecyclerView.apply {
                            layoutManager =
                                GridLayoutManager(this@PhotoActivity, Constants.COL_SPAN)
                            adapter = mPhotoAdapter
                        }
                        mPhotoAdapter.onItemClick = { item ->
                            Log.i(TAG, "photo item clicked")
                            val phd = PhotoDialog.newInstance(item.url)
                            phd.show(supportFragmentManager, PhotoDialog.TAG)
                        }
                    }
                    setViewVisibility(hasData)
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setViewVisibility(hasData: Boolean) {
        if (hasData) {
            mPhotoActivityBinding.photoNoDataTextView.visibility = View.GONE
            mPhotoActivityBinding.photoRecyclerView.visibility = View.VISIBLE
        } else {
            mPhotoActivityBinding.photoNoDataTextView.visibility = View.VISIBLE
            mPhotoActivityBinding.photoRecyclerView.visibility = View.GONE
        }
    }

    companion object {
        val TAG = PhotoActivity::class.java.simpleName
    }
}