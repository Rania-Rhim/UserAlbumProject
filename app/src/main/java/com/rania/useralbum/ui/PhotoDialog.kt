package com.rania.useralbum.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.rania.useralbum.databinding.FragmentDialogPhotoBinding
import com.rania.useralbum.utils.Constants
import com.squareup.picasso.Picasso


class PhotoDialog : DialogFragment() {

    lateinit var mPhotoDialogBinding: FragmentDialogPhotoBinding
    var mPhotoUrl: String? = Constants.EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPhotoUrl = it.getString(KEY_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mPhotoDialogBinding = FragmentDialogPhotoBinding.inflate(LayoutInflater.from(context))
        return mPhotoDialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setView() {
        Picasso.get()
            .load(Uri.parse(mPhotoUrl))
            .tag(requireActivity())
            .into(mPhotoDialogBinding.photoFullDialog)
    }

    companion object {

        val TAG = PhotoDialog::class.java.simpleName

        private const val KEY_URL = "KEY_URL"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param languageList: list of languages to display in fragment.
         * @param fragmentPos: position of the fragment.
         * @return A new instance of fragment LanguageFragment.
         */
        @JvmStatic
        fun newInstance(url: String) =
            PhotoDialog().apply {
                arguments = Bundle().apply {
                    putString(KEY_URL, url)
                }
            }

    }

}