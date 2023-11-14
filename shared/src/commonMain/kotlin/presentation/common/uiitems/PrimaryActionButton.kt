package presentation.common.uiitems

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.common.SallahColor

@Composable
fun PrimaryActionButton(label: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = SallahColor.PinkLipstick),
        onClick = onClick
    ) {
        Text(label, style = MaterialTheme.typography.body1.copy(
            color = Color.White,
            fontSize = 16.sp,
        ))
    }
}

