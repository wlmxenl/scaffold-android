package com.wlmxenl.scaffold.sample.features.mediapicker

import android.net.Uri
import android.os.Bundle
import com.wlmxenl.scafflod.sample.R
import com.wlmxenl.scafflod.sample.databinding.FragmentMediaPickerBinding
import com.wlmxenl.scaffold.sample.base.SampleBaseFragment
import com.wlmxenl.scaffold.util.PhotoPickerLauncher
import com.wlmxenl.scaffold.util.PhotoPickerUtils

class PhotoPickerFragment : SampleBaseFragment<FragmentMediaPickerBinding>() {

    private lateinit var imageSinglePicker: PhotoPickerLauncher
    private lateinit var imageMultiplePicker: PhotoPickerLauncher
    private lateinit var videoSinglePicker: PhotoPickerLauncher
    private lateinit var videoMultiplePicker: PhotoPickerLauncher
    private lateinit var mixedSinglePicker: PhotoPickerLauncher
    private lateinit var mixedMultiplePicker: PhotoPickerLauncher
    private lateinit var gifSinglePicker: PhotoPickerLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageSinglePicker = createPicker(R.string.media_picker_image_single) {
            image().single()
        }
        imageMultiplePicker = createPicker(R.string.media_picker_image_multiple) {
            image().multiple(MULTI_SELECT_LIMIT)
        }
        videoSinglePicker = createPicker(R.string.media_picker_video_single) {
            video().single()
        }
        videoMultiplePicker = createPicker(R.string.media_picker_video_multiple) {
            video().multiple(MULTI_SELECT_LIMIT)
        }
        mixedSinglePicker = createPicker(R.string.media_picker_mixed_single) {
            imageAndVideo().single()
        }
        mixedMultiplePicker = createPicker(R.string.media_picker_mixed_multiple) {
            imageAndVideo().multiple(MULTI_SELECT_LIMIT)
        }
        gifSinglePicker = createPicker(R.string.media_picker_gif_single) {
            singleMimeType("image/gif").single()
        }
    }

    override fun onPageViewCreated(savedInstanceState: Bundle?) {
        appBarView?.setup {
            setTitle(R.string.sample_media_picker)
        }
        binding.tvResult.setText(R.string.media_picker_result_idle)

        binding.btnImageSingle.setOnClickListener { imageSinglePicker.launch() }
        binding.btnImageMultiple.setOnClickListener { imageMultiplePicker.launch() }
        binding.btnVideoSingle.setOnClickListener { videoSinglePicker.launch() }
        binding.btnVideoMultiple.setOnClickListener { videoMultiplePicker.launch() }
        binding.btnMixedSingle.setOnClickListener { mixedSinglePicker.launch() }
        binding.btnMixedMultiple.setOnClickListener { mixedMultiplePicker.launch() }
        binding.btnGifSingle.setOnClickListener { gifSinglePicker.launch() }
    }

    private fun createPicker(
        labelRes: Int,
        configure: PhotoPickerUtils.MediaTypeSelector.() -> PhotoPickerUtils.ResultCallbackRegistrar
    ): PhotoPickerLauncher {
        return PhotoPickerUtils.builder(this).configure().onResult { uris ->
            if (isBindingAvailable()) {
                showResult(getString(labelRes), uris)
            }
        }
    }

    private fun showResult(label: String, uris: List<Uri>) {
        binding.tvResult.text = if (uris.isEmpty()) {
            getString(R.string.media_picker_result_cancelled, label)
        } else {
            getString(
                R.string.media_picker_result_selected,
                label,
                uris.size,
                uris.joinToString(separator = "\n")
            )
        }
    }

    companion object {
        private const val MULTI_SELECT_LIMIT = 3
    }
}
