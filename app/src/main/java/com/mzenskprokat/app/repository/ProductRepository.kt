package com.mzenskprokat.app.repository

import com.mzenskprokat.app.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private val telegramService = TelegramNotificationService()

class ProductRepository {

    // Получить все продукты
    fun getAllProducts(): Flow<Result<List<Product>>> = flow {
        emit(Result.Loading)
        try {
            val products = getProductsList()
            emit(Result.Success(products))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Ошибка загрузки данных"))
        }
    }

    // Получить продукты по категории
    fun getProductsByCategory(category: ProductCategory): Flow<Result<List<Product>>> = flow {
        emit(Result.Loading)
        try {
            val products = getProductsList().filter { it.category == category }
            emit(Result.Success(products))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Ошибка загрузки данных"))
        }
    }

    // Получить продукт по ID
    fun getProductById(id: String): Flow<Result<Product>> = flow {
        emit(Result.Loading)
        try {
            val product = getProductsList().find { it.id == id }
            if (product != null) {
                emit(Result.Success(product))
            } else {
                emit(Result.Error("Продукт не найден"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Ошибка загрузки данных"))
        }
    }

    // Отправить заявку
    fun submitOrder(order: OrderRequest): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        try {
            val text = buildString {
                appendLine("📦 НОВАЯ ЗАЯВКА")
                appendLine("👤 Имя: ${order.customerName}")
                appendLine("📞 Телефон: ${order.phone}")
                appendLine("📧 Email: ${order.email}")
                appendLine("🧾 Продукция: ${order.productName}")
                appendLine("⚖️ Количество: ${order.quantity}")
                if (order.comment.isNotBlank()) appendLine("💬 Комментарий: ${order.comment}")
                val formattedTime = java.text.SimpleDateFormat(
                    "dd.MM.yyyy HH:mm",
                    java.util.Locale.getDefault()
                ).format(java.util.Date(order.timestamp))

                appendLine("🕒 Дата: $formattedTime")

            }

            val sendResult = telegramService.sendText(text)
            if (sendResult.isSuccess) {
                emit(Result.Success(true))
            } else {
                emit(Result.Error(sendResult.exceptionOrNull()?.message ?: "Не удалось отправить в Telegram"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Ошибка отправки заявки"))
        }
    }


    // Получить данные для главной страницы
    fun getHomeData(): Flow<Result<HomeData>> = flow {
        emit(Result.Loading)
        try {
            emit(Result.Success(HomeData()))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Ошибка загрузки данных"))
        }
    }

    // Статические данные продукции
    private fun getProductsList(): List<Product> {
        return listOf(
            Product(
                id = "1",
                name = "Прецизионные сплавы с высоким электрическим сопротивлением",
                category = ProductCategory.PRECISION_HIGH_RESISTANCE,
                description = "Необходимое сочетание электрических свойств",
                specifications = listOf(
                    "Высокое электрическое сопротивление",
                    "Стабильность характеристик",
                    "Широкий диапазон рабочих температур"
                ),
                alloys = listOf("Х15Ю5", "Х23Ю5", "Х23Ю5Т", "Х27Ю5Т", "Х15Н60",
                    "Х15Н60-Н", "Х20Н80-Н", "ХН70Ю-Н", "ХН20ЮС")
            ),
            Product(
                id = "2",
                name = "Магнитно-мягкие сплавы",
                category = ProductCategory.MAGNETIC_SOFT,
                description = "Высокая магнитная проницаемость и малая коэрцитивная сила в слабых полях",
                specifications = listOf(
                    "Высокая магнитная проницаемость",
                    "Малая коэрцитивная сила",
                    "Низкие потери на перемагничивание"
                ),
                alloys = listOf("16Х", "34НКМ", "35НКХСП", "36КНМ", "40Н", "40НКМ",
                    "45Н", "47НК", "47НКХ", "49К2Ф", "49К2ФА", "50Н",
                    "50НХС", "50ХНС", "64Н", "68НМ", "76НХД", "77НМД",
                    "79Н3М", "79НМ", "80Н2М", "80НХС", "81НМА", "83НФ", "83НФ-Ш")
            ),
            Product(
                id = "3",
                name = "Магнитно-мягкие с высокой индукцией",
                category = ProductCategory.MAGNETIC_HIGH_INDUCTION,
                description = "Высокая магнитная индукция технического насыщения",
                specifications = listOf(
                    "Высокая магнитная индукция насыщения",
                    "Отличные магнитные характеристики"
                ),
                alloys = listOf("27КХ", "49КФ")
            ),
            Product(
                id = "4",
                name = "Сплавы с заданным ТКЛР",
                category = ProductCategory.TEMPERATURE_COEFFICIENT,
                description = "С заданным температурным коэффициентом линейного расширения",
                specifications = listOf(
                    "Заданный ТКЛР",
                    "Термостабильность",
                    "Точные размеры при изменении температуры"
                ),
                alloys = listOf("36Н", "32НКД", "30НКД", "30НКД-ВИ", "29НК", "29НК-ВИ",
                    "29НК-1", "29НК-ВИ-1", "38НКД", "38НКД-ВИ", "33НК",
                    "33НК-ВИ", "47НХР", "47НЗХ", "47НХ", "48НХ", "47НД",
                    "47НД-ВИ", "52Н", "52Н-ВИ", "42Н")
            ),
            Product(
                id = "5",
                name = "Сплавы на железо-никелевой основе",
                category = ProductCategory.IRON_NICKEL,
                description = "Жаропрочные сплавы на железо-никелевой основе",
                specifications = listOf(
                    "Высокая жаропрочность",
                    "Коррозионная стойкость",
                    "Стабильность при высоких температурах"
                ),
                alloys = listOf("06ХН28МДТ (ЭИ943)", "ХН30МДБ (ЭК77)",
                    "ХН40МДТЮ (ЭП543У)", "03ХН28МДТ (ЭП516)",
                    "ХН40МДБ-ВИ (ЭП937-ВИ)")
            ),
            Product(
                id = "6",
                name = "Сплавы на никелевой основе",
                category = ProductCategory.NICKEL_BASE,
                description = "Жаропрочные сплавы на никелевой основе",
                specifications = listOf(
                    "Высокая жаропрочность",
                    "Отличная коррозионная стойкость",
                    "Работа при экстремальных температурах"
                ),
                alloys = listOf("ХН65МВУ (ЭП760)", "ХН65МВ (ЭП567)", "НП2",
                    "Н70МФВ-ВИ (ЭП814А-ВИ)", "ХН55МБЮ (ЭП666)",
                    "ХН63МБ (ЭП758У)", "Н65М-ВИ (ЭП982-ВИ)",
                    "ХН58В (ЭП795)", "НП1А-ИД", "НП1А")
            ),
            Product(
                id = "7",
                name = "Сплавы для упругих элементов",
                category = ProductCategory.ELASTIC_ELEMENTS,
                description = "Специализированные сплавы для упругих элементов",
                specifications = listOf(
                    "Высокие упругие свойства",
                    "Усталостная прочность",
                    "Стабильность характеристик"
                ),
                alloys = listOf("40КХНМ", "40КНХМВТЮ", "36НХТЮ5М", "36НХТЮ",
                    "36НХТЮ8М", "42НХТЮ", "44НХТЮ")
            ),
            Product(
                id = "8",
                name = "Стали коррозионностойкие",
                category = ProductCategory.CORROSION_RESISTANT,
                description = "Специальные коррозионностойкие стали",
                specifications = listOf(
                    "Высокая коррозионная стойкость",
                    "Механическая прочность",
                    "Долговечность"
                ),
                alloys = listOf("07Х17Н16ТЛ", "08Х17Н34В5Т3Ю2РЛ", "10Х17Н10Г4МБЛ",
                    "110Г13Х2БРЛ", "12Х25Н5ТМФЛ", "15Х18Н22В6М2РЛ",
                    "20Х12ВНМФЛ", "20Х25Н19С2Л", "35Х18Н24С2Л",
                    "55Х18Г14С2ТЛ", "07Х18Н9Л", "09Х16Н4БЛ",
                    "10Х18Н11БЛ", "120Г10ФЛ", "130Г14ХМФАЛ",
                    "15Х23Н18Л", "20Х13Л", "20Х5МЛ", "35Х23Н7СЛ", "85Х4М5Ф2В6Л")
            ),
            Product(
                id = "9",
                name = "Стали жаростойкие",
                category = ProductCategory.HEAT_RESISTANT,
                description = "Специальные жаростойкие стали",
                specifications = listOf(
                    "Жаростойкость",
                    "Окалиностойкость",
                    "Работа при высоких температурах"
                ),
                alloys = listOf("20Х25Н18", "20Х25Н19С2", "20Х20Н14С2")
            ),
            Product(
                id = "10",
                name = "Проволока нихром",
                category = ProductCategory.NICHROME_WIRE,
                description = "Проволока нихром диаметром от 0,1 мм до 10,0 мм",
                specifications = listOf(
                    "Диаметры: от 0,1 мм до 10,0 мм",
                    "Высокое электрическое сопротивление",
                    "Жаростойкость",
                    "ГОСТ соответствие"
                ),
                alloys = listOf("Х20Н80", "Х15Н60")
            )
        )
    }
}