package data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProductsDto(
    @SerialName("total") val total: Int = 0,
    @SerialName("limit") val limit: Int = 0,
    @SerialName("skip") val skip: Int = 0,
    @SerialName("products") val products: List<ProductDto> = emptyList(),
)


@Serializable
data class ProductDto(
    @SerialName("discountPercentage") val discountPercentage: Double = 0.0,
    @SerialName("thumbnail") val thumbnail: String = "",
    @SerialName("images") val images: List<String>?,
    @SerialName("price") val price: Int = 0,
    @SerialName("rating") val rating: Double = 0.0,
    @SerialName("description") val description: String = "",
    @SerialName("id") val id: Int = 0,
    @SerialName("title") val title: String = "",
    @SerialName("stock") val stock: Int = 0,
    @SerialName("category") val category: String = "",
    @SerialName("brand") val brand: String = ""
)


