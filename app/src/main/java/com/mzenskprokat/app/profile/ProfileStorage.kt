package com.mzenskprokat.app.data

import android.content.Context

data class ProfileData(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val isCompany: Boolean = false,
    val companyName: String = "",
    val inn: String = "",
    val kpp: String = "",
    val city: String = "",
    val position: String = ""
)

class ProfileStorage(context: Context) {

    private val prefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

    fun saveProfile(profile: ProfileData) {
        prefs.edit()
            .putString(KEY_NAME, profile.name)
            .putString(KEY_PHONE, profile.phone)
            .putString(KEY_EMAIL, profile.email)
            .putBoolean(KEY_IS_COMPANY, profile.isCompany)
            .putString(KEY_COMPANY_NAME, profile.companyName)
            .putString(KEY_INN, profile.inn)
            .putString(KEY_KPP, profile.kpp)
            .putString(KEY_CITY, profile.city)
            .putString(KEY_POSITION, profile.position)
            .apply()
    }

    fun getProfile(): ProfileData {
        return ProfileData(
            name = prefs.getString(KEY_NAME, "") ?: "",
            phone = prefs.getString(KEY_PHONE, "") ?: "",
            email = prefs.getString(KEY_EMAIL, "") ?: "",
            isCompany = prefs.getBoolean(KEY_IS_COMPANY, false),
            companyName = prefs.getString(KEY_COMPANY_NAME, "") ?: "",
            inn = prefs.getString(KEY_INN, "") ?: "",
            kpp = prefs.getString(KEY_KPP, "") ?: "",
            city = prefs.getString(KEY_CITY, "") ?: "",
            position = prefs.getString(KEY_POSITION, "") ?: ""
        )
    }

    fun isProfileComplete(): Boolean {
        val profile = getProfile()
        val baseFilled = profile.name.isNotBlank() &&
                profile.phone.isNotBlank() &&
                profile.email.isNotBlank()

        val companyFilled = !profile.isCompany || (
                profile.companyName.isNotBlank() &&
                        profile.inn.isNotBlank()
                )

        return baseFilled && companyFilled
    }

    companion object {
        private const val KEY_NAME = "profile_name"
        private const val KEY_PHONE = "profile_phone"
        private const val KEY_EMAIL = "profile_email"
        private const val KEY_IS_COMPANY = "profile_is_company"
        private const val KEY_COMPANY_NAME = "profile_company_name"
        private const val KEY_INN = "profile_inn"
        private const val KEY_KPP = "profile_kpp"
        private const val KEY_CITY = "profile_city"
        private const val KEY_POSITION = "profile_position"
    }
}