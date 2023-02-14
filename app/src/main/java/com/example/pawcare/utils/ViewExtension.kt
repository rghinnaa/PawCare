package com.example.pawcare.utils

import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnPreDraw
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.pawcare.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

val File.size get() = if (!exists()) 0.0 else length().toDouble()
val File.sizeInKb get() = size / 1024
val File.sizeInMb get() = sizeInKb / 1024

fun ImageView.loadImage(
    source: Any?,
    corner: ImageCornerOptions? = ImageCornerOptions.NORMAL,
    radius: Int? = null,
    overrideSize: Int? = null,
    @ColorRes background: Int? = null,
    scaleType: ImageView.ScaleType? = null,
    placeHolder: Drawable? = ColorDrawable(Color.LTGRAY),
) {
    val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

    when (corner) {
        ImageCornerOptions.NORMAL -> {
            requestOptions.transform(
                CenterCrop()
            )
        }

        ImageCornerOptions.CIRCLE -> {
            requestOptions.transform(
                CenterCrop(),
                RoundedCorners(
                    resources.getDimensionPixelSize(
                        R.dimen.image_options_circle_radius
                    )
                )
            )
        }

        ImageCornerOptions.ROUNDED -> {
            val cornerRadius = if (radius != null) {
                round(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        radius.toFloat(),
                        resources.displayMetrics
                    )
                ).toInt()
            } else {
                resources.getDimensionPixelSize(R.dimen.image_options_round_radius)
            }

            requestOptions.transform(
                CenterCrop(),
                RoundedCorners(cornerRadius)
            )
        }
        else -> {}
    }

    source?.let {
        if (scaleType == ImageView.ScaleType.FIT_CENTER) requestOptions.fitCenter()
        else if (scaleType == ImageView.ScaleType.CENTER_INSIDE) requestOptions.centerInside()

        if (overrideSize != null) {
            requestOptions.override(overrideSize)
        }

        requestOptions.placeholder(placeHolder).error(placeHolder)

        Glide.with(context)
            .load(it)
            .apply(requestOptions)
            .into(this)

        if (background != null) {
            doOnPreDraw {
                setBackground(
                    context.createCircleDrawable(
                        Pair(measuredWidth, measuredHeight),
                        background
                    )
                )
            }
        }
    }
}

fun Context.createCircleDrawable(
    whSize: Pair<Int, Int>,
    @ColorRes backgroundColor: Int = R.color.black
): GradientDrawable {
    return GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        color = colorStateList(backgroundColor)
        setSize(whSize.first, whSize.second)
    }
}

fun Context?.colorStateList(@ColorRes colorRes: Int) =
    this?.let { ContextCompat.getColorStateList(it, colorRes) }
        ?: ColorStateList.valueOf(Color.TRANSPARENT)

fun ViewPager2.autoScroll(lifecycleScope: LifecycleCoroutineScope, interval: Long) {
    lifecycleScope.launchWhenResumed {
        scrollIndefinitely(interval)
    }
}

private suspend fun ViewPager2.scrollIndefinitely(interval: Long) {
    delay(interval)
    val numberOfItems = adapter?.itemCount ?: 0
    val lastIndex = if (numberOfItems > 0) numberOfItems - 1 else 0
    val nextItem = if (currentItem == lastIndex) 0 else currentItem + 1

    setCurrentItem(nextItem, true)

    scrollIndefinitely(interval)
}

var TextView.textOrNull: CharSequence?
    get() = text.orEmpty
    set(value) = textOrNull(value)

fun TextView.textOrNull(
    text: CharSequence?,
    default: String = ""
) {
    this.text = text.orEmpty(default)
}

fun Activity?.hideKeyboard(view: View) {
    val imm = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.dialog(
    title: String? = null,
    message: String? = null,
    icon: Drawable? = null,
    centered: Boolean = false,
    isCancelable: Boolean = true,
    style: DialogStyle = DialogStyle.DEFAULT,
    items: Array<String> = emptyArray(),
    view: ((MaterialAlertDialogBuilder) -> View)? = null,
    positiveMessage: String? = null,
    onClickedAction: ((dialog: DialogInterface, position: Int) -> Unit)? = null,
    onMultiChoiceAction: ((dialog: DialogInterface, position: Int, checked: Boolean) -> Unit)? = null
): MaterialAlertDialogBuilder {
    val styleRes =
        if (centered) R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered
        else R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog

    return MaterialAlertDialogBuilder(this, styleRes).apply {
        if (title != null) {
            setTitle(title)
        }
        if (message != null) {
            setMessage(message)
        }
        if (icon != null) {
            setIcon(icon)
        }
        if (view != null) {
            setView(view(this))
        }

        setCancelable(isCancelable)

        if (onClickedAction != null) {
            setPositiveButton(positiveMessage) { dialogInterface, position ->
                onClickedAction(dialogInterface, position)
            }
        }

        when {
            style == DialogStyle.DEFAULT && items.isEmpty() -> return@apply
            style == DialogStyle.SIMPLE && items.isNotEmpty() -> setItems(items) { dialog, which ->
                if (onClickedAction != null) onClickedAction(dialog, which)
            }

            style == DialogStyle.SINGLE && items.isNotEmpty() -> setSingleChoiceItems(
                items,
                0
            ) { dialog, which ->
                if (onClickedAction != null) onClickedAction(dialog, which)
            }

            style == DialogStyle.MULTI && items.isNotEmpty() -> setMultiChoiceItems(
                items,
                booleanArrayOf(false)
            ) { dialog, which, isChecked ->
                if (onMultiChoiceAction != null) onMultiChoiceAction(dialog, which, isChecked)
            }
        }
    }
}

fun Context.alertDialog(
    view: View,
    isCancelable: Boolean = true,
    fullScreen: Boolean = true
): AlertDialog {
    return dialog(view = { view }, isCancelable = isCancelable).create().apply {
        if (fullScreen) {
            val params = WindowManager.LayoutParams().apply {
                copyFrom(window?.attributes)
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }

            window?.attributes = params
        }
    }
}

fun TextView.strike() {
    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
}

fun LifecycleOwner.coroutinesCountDown(
    delayMillis: Long = 0,
    repeatMillis: Long = 0,
    action: suspend () -> Unit
) = lifecycleScope.launch {
    delay(delayMillis)
    if (repeatMillis > 0) {
        while (true) {
            action.invoke()
            delay(repeatMillis)
        }
    } else action()
}

fun Context.clip(
    source: CharSequence,
    label: CharSequence = javaClass.simpleName
) {
    val manager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    manager.setPrimaryClip(ClipData.newPlainText(label, source))
}

fun Context?.toast(
    message: CharSequence?,
    length: Int = Toast.LENGTH_SHORT
) {
    this?.let { context ->
        Toast.makeText(context, message, length)?.apply {
            view?.apply {
                findViewById<TextView>(android.R.id.message)?.apply {
                    typeface = font(R.font.roboto_regular)
                }
            }
        }?.show()
    }
}

fun Context?.font(@FontRes fontRes: Int) =
    this?.let { ResourcesCompat.getFont(it, fontRes) }

fun getTimeZoneById(id: String = "GMT+07:00"): TimeZone {
    return TimeZone.getTimeZone(id)
}

val applicationTimeZone get() = getTimeZoneById()

fun dateFormatter(format: String = "yyyy-MM-dd"): SimpleDateFormat {
    val locale = Locale("id", "ID")

    return SimpleDateFormat(format, locale).apply {
        timeZone = applicationTimeZone
    }
}

fun Context?.dimen(@DimenRes dimenRes: Int) =
    this?.resources?.getDimension(dimenRes).orEmpty()

fun Context?.color(@ColorRes colorRes: Int) =
    this?.let { ContextCompat.getColor(it, colorRes) } ?: Color.TRANSPARENT

fun createImageFile(
    context: Context
): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    val timeStamp = SimpleDateFormat.getDateTimeInstance()
        .format(Date())
        .replace(",", "")
        .replace(" ", "_")

    val appName = context.applicationInfo
        .loadLabel(context.packageManager)
        .toString()
        .replace(" ", "_")
        .lowercase(Locale.ROOT)

    return File.createTempFile(
        "${appName}_JPEG_${timeStamp}_",
        ".jpg",
        storageDir
    )
}

fun Fragment.hasPermissionOf(
    vararg permissions: String,
    onGrantedAction: (Map<String,Boolean>) -> Unit
): Boolean {
    val context = context ?: return false
    var isAllGranted = true
    val grantSequence = mutableMapOf<String, Boolean>()

    for(permission in permissions) {
        val isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        grantSequence[permission] = isGranted

        if (!isGranted) isAllGranted = false
        if (permissions.indexOf(permission) == permissions.lastIndex) onGrantedAction.invoke(grantSequence)
    }

    return isAllGranted
}

fun Uri?.copyAndGetPath(
    context: Context
): String {
    val contentResolver = context.contentResolver

    val file = File(
        (context.applicationInfo.dataDir + File.separator + context.filePath)
    )

    try {
        val inputStream = contentResolver.openInputStream(this ?: return emptyString)
            ?: return emptyString
        val outputStream = FileOutputStream(file)

        val byteSize = ByteArray(1024)
        var length: Int

        while (inputStream.read(byteSize).also { length = it } > 0) {
            outputStream.write(byteSize, 0, length)
        }

        outputStream.close()
        inputStream.close()
    } catch (ignore: IOException) {
        return emptyString
    }

    return file.absolutePath
}

val Context.filePath: String get() {
    val timeStamp = SimpleDateFormat.getDateTimeInstance()
        .format(Date())
        .replace(" ", "_")

    val appName = applicationInfo
        .loadLabel(packageManager)
        .toString()
        .replace(" ", "_")
        .toLowerCase(Locale.ROOT)

    return "${appName}_JPEG_${timeStamp}.jpg"
}

fun String.fileOf() = if (containWhiteSpace()) null else File(this.orEmpty)

private fun String.isLocal() = !this.startsWith("http://") && !this.startsWith("https://")

fun Uri.getFile(context: Context) = this.let {
    val providerPath = getPath(context).orEmpty()
    if(providerPath.isLocal()) File(providerPath) else File(path.orEmpty)
}

private fun Uri.getPath(context: Context): String? {
    val isLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    if (isLollipop && DocumentsContract.isDocumentUri(context, this)) {
        if (isExternalStorageDocument()) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument()) {
            val id = DocumentsContract.getDocumentId(this)
            val contentUri: Uri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
            )
            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(this)) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null

            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(
                split[1]
            )
            return getDataColumn(context, contentUri, selection, selectionArgs)
        } else {
            return DocumentsContract.getDocumentId(this)
        }
    } else if ("content".equals(
            this.scheme,
            ignoreCase = true
        )
    ) {
        return if (isGooglePhotosUri()) this.lastPathSegment else getDataColumn(
            context,
            this,
            null,
            null
        )
    } else if ("file".equals(this.scheme, ignoreCase = true)) {
        return this.path
    }
    return null
}

@Suppress("Deprecation")
fun Bitmap?.save(
    context: Context,
    fileName: String? = Const.File.Image.defaultFileName,
    quality: Int = 100,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
): Uri? {
    return this?.let {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            compress(format, quality, ByteArrayOutputStream())
            Uri.parse(
                MediaStore.Images.Media.insertImage(
                    context.contentResolver,
                    it,
                    fileName + System.currentTimeMillis(),
                    null
                )
            )
        } else {
            val contentValues = ContentValues().apply {
                put(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    fileName + System.currentTimeMillis()
                )
                put(
                    MediaStore.MediaColumns.MIME_TYPE,
                    Const.File.MimeType.image
                )
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/${Const.File.Location.basePath}${Const.File.Location.storePath}"
                )
                put(
                    MediaStore.MediaColumns.IS_PENDING,
                    Const.File.Pending.isPending
                )
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                contentValues
            )

            uri?.let { currentUri ->
                resolver.openOutputStream(currentUri).use { output ->
                    compress(format, quality, output)
                }
            }

            contentValues.apply {
                clear()
                put(
                    MediaStore.MediaColumns.IS_PENDING,
                    Const.File.Pending.notPending
                )
            }

            uri?.apply {
                resolver.update(
                    this,
                    contentValues,
                    null,
                    null
                )
            }
        }
    }
}

private fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )
    try {
        cursor = uri?.let {
            context.contentResolver.query(
                it, projection, selection, selectionArgs,
                null
            )
        }
        if (cursor != null && cursor.moveToFirst()) {
            val indexColumn: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(indexColumn)
        }
    } finally {
        cursor?.close()
    }
    return null
}

fun Context?.drawable(@DrawableRes drawableRes: Int) =
    this?.let { ContextCompat.getDrawable(it, drawableRes) } ?: ColorDrawable(Color.TRANSPARENT)

fun EditText.addDelayOnTypeWithScope(
    delayMillis: Long = 0,
    scope: CoroutineScope,
    action: (String) -> Unit
) {
    var job: Job? = null

    addTextChangedListener {
        job?.cancel()

        job = scope.launch {
            delay(delayMillis)
            action(it.toString())
        }
    }
}

fun Context.convertDpToPixel(dp: Float): Float {
    return dp * (resources
        .displayMetrics
        .densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

private fun Uri.isExternalStorageDocument() =
    "com.android.externalstorage.documents" == authority

private fun Uri.isDownloadsDocument() =
    "com.android.providers.downloads.documents" == authority

private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

private fun Uri.isGooglePhotosUri() =
    "com.google.android.apps.photos.content" == authority

enum class ImageCornerOptions {
    NORMAL, CIRCLE, ROUNDED
}

enum class DialogStyle {
    DEFAULT, SIMPLE, SINGLE, MULTI
}

enum class ContentType(val content: String) {
    IMAGE("image/*"),
    DOCUMENT("document/*"),
    PDF("pdf/*")
}