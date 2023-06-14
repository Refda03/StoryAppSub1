package com.rehza.storyapp.ui.story.create

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.rehza.storyapp.R
import com.rehza.storyapp.util.Resource
import com.rehza.storyapp.databinding.FragmentCreateStoryBinding
import com.rehza.storyapp.util.ViewModelFactory
import com.rehza.storyapp.util.reduceFileImage
import com.rehza.storyapp.util.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoryFragment : Fragment() {
    private var _binding: FragmentCreateStoryBinding? = null
    private val binding get() = _binding!!

    private val createStoryViewModel: CreateStoryViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateStoryBinding.inflate(inflater, container, false)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(CreateStoryFragmentDirections.actionCreateStoryFragmentToListStoryFragment())
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btOpenCamera.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_createStoryFragment_to_cameraFragment))
        binding.btOpenGallery.setOnClickListener{
            startGallery()
        }
        binding.buttonAdd.setOnClickListener{
            uploadImage()
        }
        val fileUri = arguments?.getString("selected_image")
        if (fileUri != null) {
            val uri: Uri = Uri.parse(fileUri)
            getFile = uri.toFile()
            binding.ivImagePreview.setImageBitmap(BitmapFactory.decodeFile(uri.path))
        }
    }

    private var getFile: File? = null

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile
            binding.ivImagePreview.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            showLoading(true)
            val file = reduceFileImage(getFile as File)
            val descriptionText = binding.edAddDescription.text
            if (!descriptionText.isNullOrEmpty()) {
                val description = descriptionText.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                createStoryViewModel.addNewStory(imageMultipart, description).observe(viewLifecycleOwner) {
                    if (it != null) {
                        when (it) {
                            is Resource.Success -> {
                                showLoading(false)
                                Toast.makeText(context, it.data.message, Toast.LENGTH_LONG).show()
                                findNavController().navigate(CreateStoryFragmentDirections.actionCreateStoryFragmentToListStoryFragment())
                            }
                            is Resource.Loading -> {
                                showLoading(true)
                            }
                            is Resource.Error -> {
                                showLoading(false)
                                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), R.string.mandatory_desc, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), R.string.mandatory_image, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.pbCreateStory.isVisible = state
        binding.edAddDescription.isInvisible = state
        binding.ivImagePreview.isInvisible = state
        binding.btOpenCamera.isInvisible = state
        binding.btOpenGallery.isInvisible = state
        binding.buttonAdd.isInvisible = state
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}