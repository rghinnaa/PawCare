package com.example.pawcare.utils

object Const {

    const val AUTH_PREFIX = "Bearer"

    object Network {
        const val LOGIN = "login"
        const val REGISTER = "register"
        const val FORGOT_PASSWORD = "forgotpassword"

        const val BANNER_LIST = "banner/list"
        const val DOCTOR_LIST = "doctor/list"
        const val DOCTOR_DETAIL = "doctor/detail/{id}"
        const val CONSULTATION = "consultation"
        const val PAYMENT = "payment"
        const val HISTORY_LIST = "consultation/list"
        const val HISTORY_DETAIL = "consultation/detail/{consultation_id}"
        const val USER_DETAIL = "user/detail"
        const val USER_EDIT = "user/edit"
        const val USER_EDIT_PROFILE = "user/edit/profile"
        const val USER_EDIT_BANNER = "user/edit/banner"
        const val USER_EDIT_PASSWORD = "user/change_password"
        const val WRITE_REVIEW = "review"
        const val REVIEWS = "review/list/{doctor_id}"
    }

    object Login {
        const val EMAIL = "email"
        const val PASSWORD = "password"
    }

    const val NOT_AVAILABLE = "N/A"
    const val BANK_ACCOUNT_NUMBER = "1390967957"

    object BodyRequest {
        const val USER_DOCTOR_DETAIL_ID = "user_doctor_detail_id"
        const val CONSULTATION_ID = "consultation_id"
        const val BANK_NAME = "bank_name"
        const val SENDER_NAME = "sender_name"
        const val IMAGE = "image"
        const val STAR = "star"
        const val REVIEW = "review"
        const val NAME = "name"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val GENDER = "gender"
        const val ADDRESS = "address"
        const val PHONE = "phone"
        const val OLD_PASSWORD = "old_password"
        const val NEW_PASSWORD = "new_password"
    }

    object File {
        object Location {
            const val basePath = "Paninti/"
            const val storePath = "Store/"
        }

        object MimeType {
            const val image = "image/jpeg"
            const val pdf = "application/pdf"
        }

        object Image {
            const val defaultFileName = "Paninti-Image"
        }

        object Pending {
            const val isPending = 1
            const val notPending = 0
        }
    }

    object Extras {
        const val HISTORY_STATUS = "historyStatus"
    }

    object ImageType {
        const val IMAGE_PROFILE = "imageProfile"
        const val IMAGE_BANNER = "imageBanner"
    }

}