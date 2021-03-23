package com.rania.useralbum.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rania.useralbum.UserAlbumApplication
import com.rania.useralbum.adapter.PhotoAdapter
import com.rania.useralbum.databinding.ActivityPhotoBinding
import com.rania.useralbum.model.Photo
import com.rania.useralbum.repository.Resource
import com.rania.useralbum.utils.Constants
import com.rania.useralbum.viewmodel.PhotoViewModel
import com.rania.useralbum.viewmodel.PhotoViewModelFactory

class PhotoActivity : AppCompatActivity() {

    lateinit var mPhotoActivityBinding: ActivityPhotoBinding
    lateinit var mPhotoAdapter: PhotoAdapter
    var mPhotoList: List<Photo> = emptyList()
    var mAlbumId: Int? = 0

    private val mPhotoViewModel: PhotoViewModel by viewModels {
        PhotoViewModelFactory((application as UserAlbumApplication).mPhotoRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPhotoActivityBinding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(mPhotoActivityBinding.root)

        setData()
        setView()
        setListener()
    }

    private fun setData() {
        mAlbumId = getIntent().getIntExtra(AlbumActivity.ALBUM_EXTRA, Constants.DEFAULT_ID)

        mPhotoViewModel.photoList(mAlbumId!!).observe(this, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                Log.d(TAG, "Status ${it.status}")
                val hasData = it.data?.isNotEmpty()

                if (hasData!!) {
                    Log.d(TAG, "response not empty")
                    mPhotoList = it.data
                    mPhotoAdapter.updateList(mPhotoList)
                }
                setViewVisibility(hasData)
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

    private fun setView() {
        mPhotoAdapter = PhotoAdapter(this@PhotoActivity, mPhotoList)
        mPhotoActivityBinding.photoRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager =
                GridLayoutManager(this@PhotoActivity, Constants.COL_SPAN)
            adapter = mPhotoAdapter
        }
    }

    private fun setListener() {
        mPhotoAdapter.onItemClick = { item ->
            Log.i(TAG, "photo item clicked")
            val phd = PhotoDialog.newInstance(item.url)
            phd.show(supportFragmentManager, PhotoDialog.TAG)
        }
    }

    companion object {
        val TAG = PhotoActivity::class.java.simpleName
    }
}