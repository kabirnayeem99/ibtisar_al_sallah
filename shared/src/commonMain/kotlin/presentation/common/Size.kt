//package presentation.common
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.platform.LocalWindowInfo
//
//private var width: Int? = null
//
//class Size {
//    companion object {
//
//
//        @OptIn(ExperimentalComposeUiApi::class)
//        @Composable
//        fun width(): Int {
//            if (width != null) return width!!
//            val configuration = LocalConfiguration.current
//            val density = LocalDensity.current
//            val containerSize = LocalWindowInfo.current.containerSize
//            width = containerSize.width
//            return width!!
//        }
//    }
//}