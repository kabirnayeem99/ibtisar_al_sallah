package data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AuthUserDto(
    @SerialName("firstName") val firstName: String? = "",
    @SerialName("lastName") val lastName: String? = "",
    @SerialName("image") val image: String? = "",
    @SerialName("gender") val gender: String? = "",
    @SerialName("id") val id: Int? = 0,
    @SerialName("email") val email: String? = "",
    @SerialName("username") val username: String? = "",
    @SerialName("token") val token: String? = ""
)


