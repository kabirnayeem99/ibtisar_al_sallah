package presentation.common.uiitems

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import presentation.common.SallahColor

@Composable
fun ProgressIndicator(showProgress: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(visible = showProgress, modifier = modifier) {
        CircularProgressIndicator(
            strokeWidth = 4.dp, modifier = Modifier.size(32.dp),
            color = SallahColor.PinkLipstick,
            strokeCap = StrokeCap.Round,
        )
    }
}