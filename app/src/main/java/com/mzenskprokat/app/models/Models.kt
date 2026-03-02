package com.mzenskprokat.app.models

// Основные категории продукции
enum class ProductCategory(val shortName: String) {
    PRECISION_HIGH_RESISTANCE("Прецизионные сплавы с высоким сопротивлением"),
    MAGNETIC_SOFT("Магнитно-мягкие сплавы"),
    MAGNETIC_HIGH_INDUCTION("Магнитно-мягкие с высокой индукцией"),
    TEMPERATURE_COEFFICIENT("Сплавы с заданным ТКЛР"),
    HIGH_PERMEABILITY("Сплавы с высокой магнитной проницаемостью"),
    IRON_NICKEL("Сплавы на железо-никелевой основе"),
    NICKEL_BASE("Сплавы на никелевой основе"),
    ELASTIC_ELEMENTS("Сплавы для упругих элементов"),
    CORROSION_RESISTANT("Стали коррозионностойкие"),
    HEAT_RESISTANT("Стали жаростойкие"),
    NICHROME_WIRE("Проволока нихром")
}

// Модель продукта
data class Product(
    val id: String,
    val name: String,
    val category: ProductCategory,
    val description: String,
    val specifications: List<String>,
    val alloys: List<String>,
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,
    val stockQty: Int? = null
)

// Контактная информация
data class ContactInfo(
    val phone: String = "+7 (495) 291-09-14",
    val email: String = "sale@mzenskprokat.ru",
    val address: String = "Металлургический завод Мценскпрокат",
    val website: String = "https://mzenskprokat.ru"
)

// Модель заявки
data class OrderRequest(
    val customerName: String,
    val phone: String,
    val email: String,
    val productName: String,
    val quantity: String,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)

// Результат операции (для UI/данных)
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val message: String) : Result<Nothing>
    data object Loading : Result<Nothing>
    data object Idle : Result<Nothing>
}

// Данные для главной страницы
data class HomeData(
    val companyName: String = "Завод прецизионных сплавов Мценскпрокат",
    val description: String = "Производитель прецизионных, медных и никелевых сплавов",
    val features: List<String> = listOf(
        "Прямые поставки от производителя",
        "Широкий ассортимент продукции",
        "Оптовые цены",
        "Высокое качество продукции"
    ),
    val contactInfo: ContactInfo = ContactInfo()
)