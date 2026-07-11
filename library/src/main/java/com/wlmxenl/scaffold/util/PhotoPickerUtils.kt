package com.wlmxenl.scaffold.util

import android.net.Uri
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.IntRange
import androidx.annotation.MainThread

object PhotoPickerUtils {

    interface MediaTypeSelector {
        fun image(): SelectionModeSelector

        fun video(): SelectionModeSelector

        fun imageAndVideo(): SelectionModeSelector

        /**
         * 仅选择指定 MIME 类型的媒体，每次只能传入一个 MIME 类型字符串。
         *
         * 示例：
         * - `singleMimeType("image/gif")`：仅选择 GIF 图片
         * - `singleMimeType("image/jpeg")`：仅选择 JPEG 图片
         * - `singleMimeType("video/mp4")`：仅选择 MP4 视频
         * - `singleMimeType("image/gif").multiple(3)`：最多选择 3 个 GIF 图片
         *
         * 不支持使用 `"image/jpeg,image/png"` 等方式拼接多个 MIME 类型。
         * 如需选择所有图片或视频，请使用 [image]、[video] 或 [imageAndVideo]。
         *
         * @param mimeType 单个有效的 MIME 类型字符串。
         */
        fun singleMimeType(mimeType: String): SelectionModeSelector
    }

    interface SelectionModeSelector {
        fun single(): ResultCallbackRegistrar

        /**
         * 配置多选模式，并限制结果回调中的最大 URI 数量。
         *
         * @param maxItems 结果回调允许返回的最大数量，必须大于 1。
         */
        fun multiple(@IntRange(from = 2) maxItems: Int): ResultCallbackRegistrar

    }

    interface ResultCallbackRegistrar {
        /**
         * @param onResult 用于接收所选媒体 URI 的回调；取消时返回空列表。
         */
        @MainThread
        fun onResult(onResult: (List<Uri>) -> Unit): PhotoPickerLauncher
    }

    /**
     * @param caller 用于注册 Activity Result 的 Activity 或 Fragment；在 `onCreate` 中调用。
     */
    @MainThread
    fun builder(caller: ActivityResultCaller): MediaTypeSelector = Builder(caller)

    private class Builder(
        private val caller: ActivityResultCaller
    ) : MediaTypeSelector, SelectionModeSelector, ResultCallbackRegistrar {
        private var mediaType: PickVisualMedia.VisualMediaType? = null
        private var selectionLimit: Int? = null

        override fun image(): SelectionModeSelector = setMediaType(PickVisualMedia.ImageOnly)

        override fun video(): SelectionModeSelector = setMediaType(PickVisualMedia.VideoOnly)

        override fun imageAndVideo(): SelectionModeSelector = setMediaType(PickVisualMedia.ImageAndVideo)

        override fun singleMimeType(mimeType: String): SelectionModeSelector {
            val normalizedMimeType = mimeType.trim()
            require(normalizedMimeType.isNotEmpty()) { "mimeType must not be blank" }
            return setMediaType(PickVisualMedia.SingleMimeType(normalizedMimeType))
        }

        override fun single(): ResultCallbackRegistrar = setSelectionLimit(SINGLE_SELECTION)

        override fun multiple(maxItems: Int): ResultCallbackRegistrar {
            require(maxItems > SINGLE_SELECTION) { "maxItems must be greater than 1" }
            return setSelectionLimit(maxItems)
        }

        override fun onResult(onResult: (List<Uri>) -> Unit): PhotoPickerLauncher {
            val selectedMediaType = checkNotNull(mediaType) {
                "A media type must be selected before registering a result callback"
            }
            val selectedSelectionLimit = checkNotNull(selectionLimit) {
                "A selection count must be selected before registering a result callback"
            }
            val request = PickVisualMediaRequest.Builder()
                .setMediaType(selectedMediaType)
                .build()
            val launcher = if (selectedSelectionLimit == SINGLE_SELECTION) {
                caller.registerForActivityResult(PickVisualMedia()) { uri ->
                    onResult(uri?.let(::listOf) ?: emptyList())
                }
            } else {
                caller.registerForActivityResult(PickMultipleVisualMedia(selectedSelectionLimit)) { uris ->
                    onResult(uris.take(selectedSelectionLimit))
                }
            }
            return RegisteredLauncher(launcher, request)
        }

        private fun setMediaType(value: PickVisualMedia.VisualMediaType): SelectionModeSelector {
            check(mediaType == null) { "A media type has already been selected" }
            mediaType = value
            return this
        }

        private fun setSelectionLimit(value: Int): ResultCallbackRegistrar {
            check(selectionLimit == null) { "A selection count has already been selected" }
            selectionLimit = value
            return this
        }

        private companion object {
            const val SINGLE_SELECTION = 1
        }
    }

    private class RegisteredLauncher(
        private val launcher: ActivityResultLauncher<PickVisualMediaRequest>,
        private val request: PickVisualMediaRequest
    ) : PhotoPickerLauncher {
        override fun launch() {
            launcher.launch(request)
        }
    }
}

interface PhotoPickerLauncher {
    @MainThread
    fun launch()
}
