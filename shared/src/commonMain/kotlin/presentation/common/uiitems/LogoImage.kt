package presentation.common.uiitems

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import presentation.common.SallahColor

@Composable
fun LogoImage(modifier: Modifier = Modifier.size(100.dp)) {
    KamelImage(
        resource = asyncPainterResource("https://freshmindideas.com/wp-content/uploads/2021/08/ecommerce-branding.png"),
        contentDescription = "logo",
        contentScale = ContentScale.Fit,
        modifier = modifier,
        colorFilter = ColorFilter.tint(SallahColor.PinkLipstick)
    )
}