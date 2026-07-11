package com.wlmxenl.scaffold.util

import android.net.Uri
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.SingleMimeType
import androidx.core.app.ActivityOptionsCompat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PhotoPickerUtilsTest {

    @Test
    fun imageSingleUsesSingleContractAndMapsCancellationToEmptyList() {
        val caller = FakeCaller()
        var result: List<Uri>? = null
        val mediaTypeSelector: PhotoPickerUtils.MediaTypeSelector = PhotoPickerUtils.builder(caller)
        val selectionModeSelector: PhotoPickerUtils.SelectionModeSelector = mediaTypeSelector.image()
        val resultCallbackRegistrar: PhotoPickerUtils.ResultCallbackRegistrar = selectionModeSelector.single()
        val picker: PhotoPickerLauncher = resultCallbackRegistrar.onResult { result = it }

        assertTrue(caller.contract is PickVisualMedia)

        picker.launch()

        val request = caller.lastInput as PickVisualMediaRequest
        assertTrue(request.mediaType is PickVisualMedia.ImageOnly)

        caller.dispatch(null)

        assertEquals(emptyList<Uri>(), result)
    }

    @Test
    fun videoMultipleUsesMultipleContractAndCapsCallbackItems() {
        val caller = FakeCaller()
        var result: List<Uri>? = null
        val picker = PhotoPickerUtils.builder(caller)
            .video()
            .multiple(2)
            .onResult { result = it }

        assertTrue(caller.contract is PickMultipleVisualMedia)

        picker.launch()

        val request = caller.lastInput as PickVisualMediaRequest
        assertTrue(request.mediaType is PickVisualMedia.VideoOnly)

        @Suppress("UNCHECKED_CAST")
        caller.dispatch(listOf(null, null, null) as List<Uri>)

        assertEquals(2, result?.size)
    }

    @Test
    fun customMimeTypeIsIncludedInRequest() {
        val caller = FakeCaller()
        val picker = PhotoPickerUtils.builder(caller)
            .singleMimeType(" image/gif ")
            .single()
            .onResult {}

        picker.launch()

        val request = caller.lastInput as PickVisualMediaRequest
        val mediaType = request.mediaType as SingleMimeType
        assertEquals("image/gif", mediaType.mimeType)
    }

    @Test
    fun imageAndVideoIsIncludedInRequest() {
        val caller = FakeCaller()
        val picker = PhotoPickerUtils.builder(caller)
            .imageAndVideo()
            .single()
            .onResult {}

        picker.launch()

        val request = caller.lastInput as PickVisualMediaRequest
        assertTrue(request.mediaType is PickVisualMedia.ImageAndVideo)
    }

    @Test(expected = IllegalArgumentException::class)
    fun multipleRejectsSingleItemLimit() {
        PhotoPickerUtils.builder(FakeCaller())
            .image()
            .multiple(1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun singleMimeTypeRejectsBlankValue() {
        PhotoPickerUtils.builder(FakeCaller())
            .singleMimeType("   ")
    }

    private class FakeCaller : ActivityResultCaller {
        lateinit var contract: ActivityResultContract<*, *>
        var lastInput: Any? = null
        private var resultCallback: ((Any?) -> Unit)? = null

        override fun <I, O> registerForActivityResult(
            contract: ActivityResultContract<I, O>,
            callback: ActivityResultCallback<O>
        ): ActivityResultLauncher<I> {
            this.contract = contract
            resultCallback = { result ->
                @Suppress("UNCHECKED_CAST")
                callback.onActivityResult(result as O)
            }
            return FakeLauncher(contract) { input ->
                lastInput = input
            }
        }

        override fun <I, O> registerForActivityResult(
            contract: ActivityResultContract<I, O>,
            registry: ActivityResultRegistry,
            callback: ActivityResultCallback<O>
        ): ActivityResultLauncher<I> {
            return registerForActivityResult(contract, callback)
        }

        fun dispatch(result: Any?) {
            checkNotNull(resultCallback).invoke(result)
        }
    }

    private class FakeLauncher<I>(
        private val activityResultContract: ActivityResultContract<I, *>,
        private val onLaunch: (I) -> Unit
    ) : ActivityResultLauncher<I>() {
        override fun launch(input: I, options: ActivityOptionsCompat?) {
            onLaunch(input)
        }

        override fun getContract(): ActivityResultContract<I, *> = activityResultContract

        override fun unregister() = Unit
    }
}
