package com.example.pawcare.ui.profile.bottomsheet

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.pawcare.R
import com.example.pawcare.databinding.DialogAddPhotoBinding
import com.example.pawcare.ui.consultation.PaymentFragmentDirections
import com.example.pawcare.ui.profile.ProfileEditFragmentDirections
import com.example.pawcare.utils.*
import com.example.pawcare.viewmodel.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageSelectionBottomSheet : BottomSheetDialogFragment() {

    private val binding by viewBinding<DialogAddPhotoBinding>()
    private val viewModel by hiltNavGraphViewModels<ProfileViewModel>(R.id.profile)
    private val args by navArgs<ImageSelectionBottomSheetArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_add_photo, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkImageAndStoragePermission()
        initView()
        initComponentShape()
        initOnClick()
    }

    private fun initComponentShape() {
        val radius = context.dimen(R.dimen.semi_normal_shape_radius)
        val cameraShapeAppearance = binding.ivCameraAction.shapeAppearanceModel
        val galleryShapeAppearance = binding.ivGalleryAction.shapeAppearanceModel

        binding.ivCameraAction.shapeAppearanceModel =
            cameraShapeAppearance
                .toBuilder()
                .setAllCornerSizes(radius)
                .build()

        binding.ivGalleryAction.shapeAppearanceModel =
            galleryShapeAppearance
                .toBuilder()
                .setAllCornerSizes(radius)
                .build()
    }

    private fun initView() {
        binding.tvPhoto.textOrNull = args.title
    }

    private fun initOnClick() {
        binding.btnClose.setOnClickListener(onClickCallback)
        binding.ivCameraAction.setOnClickListener(onClickCallback)
        binding.ivGalleryAction.setOnClickListener(onClickCallback)
        binding.tvCameraAction.setOnClickListener(onClickCallback)
        binding.tvGalleryAction.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnClose -> dialog?.dismiss()
            binding.ivCameraAction -> createCameraResultFileAlsoLaunch()
            binding.ivGalleryAction -> onChooseGallerySelected.launch(ContentType.IMAGE.content)
            binding.tvCameraAction -> createCameraResultFileAlsoLaunch()
            binding.tvGalleryAction -> onChooseGallerySelected.launch(ContentType.IMAGE.content)
        }
    }

    private fun createCameraResultFileAlsoLaunch() = lifecycleScope.launchWhenCreated {
        val imageFile = createImageFile(requireContext())
        val provider = "${requireContext().packageName}.provider"

        cameraUriResult = FileProvider.getUriForFile(
            requireContext(),
            provider,
            imageFile
        )

        onTakeCameraSelected.launch(cameraUriResult)
    }

    private fun checkImageAndStoragePermission() {
        hasPermissionOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) { permissions ->
            if (!permissions.values.all { it }) {
                requestPermission.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { hasPermission ->
        if (!hasPermission.values.all { it }) {
            when (args.type) {
                Const.ImageType.IMAGE_PROFILE -> {
                    navController.navigateOrNull(
                        ProfileEditFragmentDirections.actionAddPhoto(
                            args.title,
                            args.type
                        )
                    )
                }
                Const.ImageType.IMAGE_BANNER -> {
                    navController.navigateOrNull(
                        ProfileEditFragmentDirections.actionAddPhoto(
                            args.title,
                            args.type
                        )
                    )
                }
                else -> {
                    navController.navigateOrNull(PaymentFragmentDirections.actionAddPhoto(args.title))
                }
            }
        }
    }

    private var cameraUriResult: Uri? = null

    private val onTakeCameraSelected = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            cameraUriResult?.let { uri ->
                when(args.type) {
                    Const.ImageType.IMAGE_PROFILE -> {
                        viewModel.isImage = true
                        viewModel.onResultUriChangedImage(uri)
                    }
                    Const.ImageType.IMAGE_BANNER -> {
                        viewModel.isBanner = true
                        viewModel.onResultUriChangedBanner(uri)
                    }
                    else -> viewModel.onResultUriChanged(uri)
                }
                dialog?.dismiss()
            }
        }
    }

    private val onChooseGallerySelected = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            when(args.type) {
                Const.ImageType.IMAGE_PROFILE -> {
                    viewModel.isImage = true
                    viewModel.onResultUriChangedImage(it)
                }
                Const.ImageType.IMAGE_BANNER -> {
                    viewModel.isBanner = true
                    viewModel.onResultUriChangedBanner(it)
                }
                else -> viewModel.onResultUriChanged(it)
            }
            dialog?.dismiss()
        }
    }

}