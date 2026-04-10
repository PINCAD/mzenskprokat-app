package com.mzenskprokat.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.mzenskprokat.app.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.text.style.TextAlign

private data class RemoteImage(
    val previewUrl: String,
    val fullUrl: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onNavigateToCatalog: () -> Unit,
    onNavigateToOrder: () -> Unit
) {
    val certificates = listOf(
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6533-6664-4134-a563-373961376435/-/cover/432x432/center/center/-/format/webp/_.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6533-6664-4134-a563-373961376435/_.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3066-3164-4764-a132-343962656234/-/cover/432x432/center/center/-/format/webp/___1.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3066-3164-4764-a132-343962656234/___1.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6630-3634-4830-b261-313164363664/-/cover/432x432/center/center/-/format/webp/__.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6630-3634-4830-b261-313164363664/__.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3565-6137-4038-b431-326338643530/-/cover/432x432/center/center/-/format/webp/____.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3565-6137-4038-b431-326338643530/____.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6164-3661-4162-a431-383565313264/-/cover/432x432/center/center/-/format/webp/1__1.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6164-3661-4162-a431-383565313264/1__1.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6666-3131-4636-b664-393162336162/-/cover/432x432/center/center/-/format/webp/1__2.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6666-3131-4636-b664-393162336162/1__2.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6262-6538-4438-b537-623163623236/-/cover/432x432/center/center/-/format/webp/20240307_093706.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6262-6538-4438-b537-623163623236/20240307_093706.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3138-6231-4161-b863-373765336266/-/cover/432x432/center/center/-/format/webp/20240307_094017.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3138-6231-4161-b863-373765336266/20240307_094017.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3230-3434-4864-b131-626336303835/-/cover/432x432/center/center/-/format/webp/DOC-20240215-WA0008_.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3230-3434-4864-b131-626336303835/DOC-20240215-WA0008_.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6434-6336-4432-b562-373936626666/-/cover/432x432/center/center/-/format/webp/DOC-20240215-WA0008_.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6434-6336-4432-b562-373936626666/DOC-20240215-WA0008_.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3161-3261-4065-a132-396133393130/-/cover/432x432/center/center/-/format/webp/DOC-20240215-WA0009_.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3161-3261-4065-a132-396133393130/DOC-20240215-WA0009_.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6131-3062-4066-a637-313136613064/-/cover/432x432/center/center/-/format/webp/DOC-20240215-WA0009_.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6131-3062-4066-a637-313136613064/DOC-20240215-WA0009_.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3862-3462-4834-a631-666339376634/-/cover/432x432/center/center/-/format/webp/mp100_page-0001.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3862-3462-4834-a631-666339376634/mp100_page-0001.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6338-3536-4037-a137-336165376139/-/cover/432x432/center/center/-/format/webp/p-05_page-0001.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6338-3536-4037-a137-336165376139/p-05_page-0001.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3763-3532-4334-b434-613861373038/-/cover/432x432/center/center/-/format/webp/photo.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3763-3532-4334-b434-613861373038/photo.jpg.webp"
        )
    )

    val galleryPhotos = listOf(
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3337-6533-4538-b938-353864643435/-/resize/500x400/-/format/webp/DSC0535.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3337-6533-4538-b938-353864643435/DSC0535.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6233-6266-4434-a530-313537623237/-/resize/500x400/-/format/webp/IMG_20200423_144419-.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6233-6266-4434-a530-313537623237/IMG_20200423_144419-.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3934-3637-4564-b466-646439643833/-/resize/500x400/-/format/webp/IMG_20200512_163339-.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3934-3637-4564-b466-646439643833/IMG_20200512_163339-.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3633-6134-4037-b262-376264363733/-/resize/500x400/-/format/webp/WhatsApp-Image-2020-.jpeg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3633-6134-4037-b262-376264363733/WhatsApp-Image-2020-.jpeg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6633-3036-4331-b063-386239633565/-/resize/500x400/-/format/webp/WhatsApp-Image-2020-.jpeg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6633-3036-4331-b063-386239633565/WhatsApp-Image-2020-.jpeg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6634-3931-4438-b533-343831333537/-/resize/500x400/-/format/webp/20240524_105546.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6634-3931-4438-b533-343831333537/20240524_105546.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6465-6164-4264-a363-373234313932/-/resize/500x400/-/format/webp/WhatsApp-Image-2020-.jpeg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6465-6164-4264-a363-373234313932/WhatsApp-Image-2020-.jpeg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3533-3738-4532-b737-373430343865/-/resize/500x400/-/format/webp/ZIL_8447.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3533-3738-4532-b737-373430343865/ZIL_8447.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3765-3766-4065-b531-373865353132/-/resize/500x400/-/format/webp/-1.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3765-3766-4065-b531-373865353132/-1.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3761-6434-4435-b732-636135303830/-/resize/500x400/-/format/webp/20231220_075106.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3761-6434-4435-b732-636135303830/20231220_075106.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6331-3039-4265-a262-633039373962/-/resize/500x400/-/format/webp/IMG_20231228_172230_.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6331-3039-4265-a262-633039373962/IMG_20231228_172230_.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3639-6365-4161-b366-343836613364/-/resize/500x400/-/format/webp/DSC0380.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3639-6365-4161-b366-343836613364/DSC0380.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3565-3733-4633-b163-353335373963/-/resize/500x400/-/format/webp/DSC0384.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3565-3733-4633-b163-353335373963/DSC0384.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3730-3933-4565-b664-316631323361/-/resize/500x400/-/format/webp/DSC0388.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3730-3933-4565-b664-316631323361/DSC0388.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6530-6436-4433-b038-386532336438/-/resize/500x400/-/format/webp/DSC0533.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6530-6436-4433-b038-386532336438/DSC0533.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6139-6161-4634-a130-323563653562/-/resize/500x400/-/format/webp/IMG_20200331_151106-.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6139-6161-4634-a130-323563653562/IMG_20200331_151106-.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3362-6134-4164-a161-346537316433/-/resize/500x400/-/format/webp/ZIL_8414.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3362-6134-4164-a161-346537316433/ZIL_8414.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3931-6664-4565-b835-313637303835/-/resize/500x400/-/format/webp/-.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3931-6664-4565-b835-313637303835/-.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3635-3363-4064-a538-633333643336/-/resize/500x400/-/format/webp/DSC0567.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3635-3363-4064-a538-633333643336/DSC0567.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3435-3936-4163-b835-353935386230/-/resize/500x400/-/format/webp/DSC0577.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3435-3936-4163-b835-353935386230/DSC0577.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3362-3435-4866-b961-323438613664/-/resize/500x400/-/format/webp/DSC0579-1.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3362-3435-4866-b961-323438613664/DSC0579-1.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3333-6534-4338-a264-636332616163/-/resize/500x400/-/format/webp/IMG_20200331_123322-.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3333-6534-4338-a264-636332616163/IMG_20200331_123322-.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6435-3930-4337-b161-373062346334/-/resize/500x400/-/format/webp/-scaled.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6435-3930-4337-b161-373062346334/-scaled.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3635-3532-4730-a235-316464316233/-/resize/500x400/-/format/webp/-scaled.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3635-3532-4730-a235-316464316233/-scaled.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3930-3531-4738-b734-326165653865/-/resize/500x400/-/format/webp/-scaled.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3930-3531-4738-b734-326165653865/-scaled.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3332-3530-4862-a266-663030353334/-/resize/500x400/-/format/webp/-scaled.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3332-3530-4862-a266-663030353334/-scaled.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3062-3634-4338-b864-643530636163/-/resize/500x400/-/format/webp/-scaled.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3062-3634-4338-b864-643530636163/-scaled.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6531-3961-4237-b962-363966646135/-/resize/500x400/-/format/webp/-scaled.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6531-3961-4237-b962-363966646135/-scaled.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6632-3335-4436-a431-666236303831/-/resize/500x400/-/format/webp/-scaled.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6632-3335-4436-a431-666236303831/-scaled.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6663-6431-4537-b733-643036663761/-/resize/500x400/-/format/webp/-scaled.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6663-6431-4537-b733-643036663761/-scaled.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3035-3938-4839-b666-363635306632/-/resize/500x400/-/format/webp/-scaled.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3035-3938-4839-b666-363635306632/-scaled.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6663-3332-4130-a139-663065323866/-/resize/500x400/-/format/webp/20240612_130439.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6663-3332-4130-a139-663065323866/20240612_130439.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3534-3039-4535-b433-663332636262/-/resize/500x400/-/format/webp/DSC0217.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3534-3039-4535-b433-663332636262/DSC0217.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3036-3435-4431-a234-303939346331/-/resize/500x400/-/format/webp/DSC0231.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3036-3435-4431-a234-303939346331/DSC0231.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3265-3763-4434-b566-636137313263/-/resize/500x400/-/format/webp/DSC0251.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3265-3763-4434-b566-636137313263/DSC0251.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3664-3531-4231-b161-383465343765/-/resize/500x400/-/format/webp/DSC0254.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3664-3531-4231-b161-383465343765/DSC0254.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6237-3766-4230-a134-396331303933/-/resize/500x400/-/format/webp/DSC0262.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6237-3766-4230-a134-396331303933/DSC0262.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6334-3732-4164-b566-336332666233/-/resize/500x400/-/format/webp/DSC0267.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6334-3732-4164-b566-336332666233/DSC0267.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3233-3364-4635-a137-396661653465/-/resize/500x400/-/format/webp/DSC0503.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3233-3364-4635-a137-396661653465/DSC0503.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3038-3237-4931-a665-336463386530/-/resize/500x400/-/format/webp/IMG_20191210_141404.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3038-3237-4931-a665-336463386530/IMG_20191210_141404.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3038-3435-4766-b431-666330326531/-/resize/500x400/-/format/webp/IMG_20200331_150426-.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3038-3435-4766-b431-666330326531/IMG_20200331_150426-.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6232-6366-4834-b139-636364643235/-/resize/500x400/-/format/webp/IMG_20200401_080430-.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6232-6366-4834-b139-636364643235/IMG_20200401_080430-.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3264-6138-4366-b634-356537356431/-/resize/500x400/-/format/webp/IMG_20200401_080440-.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3264-6138-4366-b634-356537356431/IMG_20200401_080440-.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6435-6465-4237-b739-333637386162/-/resize/500x400/-/format/webp/IMG_20200401_080459-.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6435-6465-4237-b739-333637386162/IMG_20200401_080459-.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild6639-6161-4234-a666-616337613963/-/resize/500x400/-/format/webp/IMG_20200401_080512-.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild6639-6161-4234-a666-616337613963/IMG_20200401_080512-.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3836-6636-4633-b636-313664326637/-/resize/500x400/-/format/webp/ZIL_8419-1.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3836-6636-4633-b636-313664326637/ZIL_8419-1.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3734-3565-4130-a663-336439386334/-/resize/500x400/-/format/webp/ZIL_8632-1.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3734-3565-4130-a663-336439386334/ZIL_8632-1.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3333-3033-4032-b632-613830313337/-/resize/500x400/-/format/webp/ZIL_9011.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3333-3033-4032-b632-613830313337/ZIL_9011.jpg.webp"
        ),
        RemoteImage(
            previewUrl = "https://optim.tildacdn.com/tild3964-6331-4930-b437-636564666230/-/resize/500x400/-/format/webp/ZIL_9019.jpg.webp",
            fullUrl = "https://optim.tildacdn.com/tild3964-6331-4930-b437-636564666230/ZIL_9019.jpg.webp"
        )
    )

    val galleryPhotoHeights = listOf(260.dp, 170.dp, 300.dp, 210.dp, 250.dp, 180.dp)

    var fullscreenImageUrl by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            HeroBlock()
        }

        item {
            SectionTitle(
                title = "О КОМПАНИИ",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            ManifestBlock(
                modifier = Modifier.padding(horizontal = 16.dp),
                lines = listOf(
                    "Мценскпрокат — современный завод прецизионных сплавов и металлопроката.",
                    "Производство ориентировано на сложные промышленные задачи, стабильное качество и технологичность.",
                    "Предприятие работает как с типовыми позициями, так и с индивидуальными требованиями заказчика."
                )
            )
        }

        item {
            SectionTitle(
                title = "ПРЕИМУЩЕСТВА",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdvantageCard(
                    icon = Icons.Outlined.Inventory2,
                    title = "ШИРОКАЯ НОМЕНКЛАТУРА",
                    description = "Прокат и сплавы по ГОСТ, ТУ и индивидуальным проектам."
                )
                AdvantageCard(
                    icon = Icons.Outlined.Verified,
                    title = "КОНТРОЛЬ КАЧЕСТВА",
                    description = "Лабораторный контроль, сертификация и внимание к стабильности продукции."
                )
                AdvantageCard(
                    icon = Icons.Outlined.Build,
                    title = "СЛОЖНЫЕ ЗАКАЗЫ",
                    description = "Изготовление по чертежам заказчика и нестандартным требованиям."
                )
            }
        }

        item {
            SectionTitle(
                title = "СЕРТИФИКАТЫ",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(certificates) { certificate ->
                    CertificateCard(
                        imageUrl = certificate.previewUrl,
                        onClick = { fullscreenImageUrl = certificate.fullUrl }
                    )
                }
            }
        }

        item {
            SectionTitle(
                title = "ГАЛЕРЕЯ ПРОИЗВОДСТВА",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            GallerySection(
                photos = galleryPhotos,
                heights = galleryPhotoHeights,
                onImageClick = { fullscreenImageUrl = it }
            )
        }
    }

    fullscreenImageUrl?.let { imageUrl ->
        FullscreenImageDialog(
            imageUrl = imageUrl,
            onDismiss = { fullscreenImageUrl = null }
        )
    }
}

@Composable
private fun HeroBlock() {

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .aspectRatio(9f / 6f),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.hero_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // затемнение фона для читаемости текста
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(1.dp))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mtsenk),
                        contentDescription = "Логотип",
                        modifier = Modifier
                            .padding(14.dp)
                            .size(90.dp)
                    )
                }

                Text(
                    text = "Завод прецизионных сплавов\nМценскпрокат",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun ManifestBlock(
    modifier: Modifier = Modifier,
    lines: List<String>
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            lines.forEachIndexed { index, line ->
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (index != lines.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

@Composable
private fun AdvantageCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CertificateCard(
    imageUrl: String,
    onClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(model = imageUrl)
    val state = painter.state

    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
        ) {
            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Empty) {
                SkeletonBlock(modifier = Modifier.fillMaxSize())
            }

            AsyncImage(
                model = imageUrl,
                contentDescription = "Сертификат",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun GalleryCard(
    imageUrl: String,
    imageHeight: Dp,
    onClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(model = imageUrl)
    val state = painter.state

    Card(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Empty) {
                SkeletonBlock(modifier = Modifier.fillMaxSize())
            }

            AsyncImage(
                model = imageUrl,
                contentDescription = "Фото производства",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(22.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Composable
private fun SkeletonBlock(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFFE5E5E5),
                shape = RoundedCornerShape(22.dp)
            )
    )
}

@Composable
private fun FullscreenImageDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "Изображение",
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            val newScale = (scale * zoom).coerceIn(1f, 5f)
                            scale = newScale

                            if (newScale > 1f) {
                                offsetX += pan.x
                                offsetY += pan.y
                            } else {
                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    }
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offsetX
                        translationY = offsetY
                    },
                contentScale = ContentScale.Fit
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(12.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.45f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Закрыть",
                    tint = Color.White
                )
            }

            if (scale > 1f) {
                TextButton(
                    onClick = {
                        scale = 1f
                        offsetX = 0f
                        offsetY = 0f
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.45f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = "Сбросить",
                        color = Color.White
                    )
                }
            }
        }
    }
}
private data class GalleryColumnItem(
    val photo: RemoteImage,
    val height: Dp
)

@Composable
private fun GallerySection(
    photos: List<RemoteImage>,
    heights: List<Dp>,
    onImageClick: (String) -> Unit
) {
    val columns = remember(photos, heights) {
        balanceGalleryColumns(photos, heights)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            columns.first.forEach { item ->
                GalleryCard(
                    imageUrl = item.photo.previewUrl,
                    imageHeight = item.height,
                    onClick = { onImageClick(item.photo.fullUrl) }
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            columns.second.forEach { item ->
                GalleryCard(
                    imageUrl = item.photo.previewUrl,
                    imageHeight = item.height,
                    onClick = { onImageClick(item.photo.fullUrl) }
                )
            }
        }
    }
}

private fun balanceGalleryColumns(
    photos: List<RemoteImage>,
    heights: List<Dp>
): Pair<List<GalleryColumnItem>, List<GalleryColumnItem>> {
    val left = mutableListOf<GalleryColumnItem>()
    val right = mutableListOf<GalleryColumnItem>()

    var leftHeight = 0f
    var rightHeight = 0f

    photos.forEachIndexed { index, photo ->
        val itemHeight = heights[index % heights.size]
        val item = GalleryColumnItem(photo = photo, height = itemHeight)

        if (leftHeight <= rightHeight) {
            left += item
            leftHeight += itemHeight.value
        } else {
            right += item
            rightHeight += itemHeight.value
        }
    }

    return left to right
}