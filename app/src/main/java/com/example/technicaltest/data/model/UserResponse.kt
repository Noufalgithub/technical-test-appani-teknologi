package com.example.technicaltest.data.model

import com.google.gson.annotations.SerializedName

/**
 * Soal 3: Model response REST API GET /users/{id}
 */
data class UserResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String
)

/**
 * Domain model terpisah untuk Clean Architecture decoupling
 */
data class UserProfile(
    val id: Int,
    val name: String,
    val email: String
)

fun UserResponse.toDomain(): UserProfile = UserProfile(
    id = this.id,
    name = this.name,
    email = this.email
)
