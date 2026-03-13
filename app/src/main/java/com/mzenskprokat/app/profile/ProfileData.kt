package com.mzenskprokat.app.models

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