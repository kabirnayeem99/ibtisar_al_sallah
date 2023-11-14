package domain.entity

data class Product(
    val thumbnail: String = "",
    val price: Int = 0,
    val rating: Double = 0.0,
    val id: String = "0",
    val title: String = "",
    val category: String = "",
    val brand: String = ""
)
