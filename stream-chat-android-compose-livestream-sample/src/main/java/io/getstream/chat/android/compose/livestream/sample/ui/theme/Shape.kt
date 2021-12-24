package io.getstream.chat.android.compose.livestream.sample.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.compose.ui.theme.StreamShapes

val Shapes = StreamShapes.defaultShapes().copy(
    inputField = RoundedCornerShape(4.dp)
)
