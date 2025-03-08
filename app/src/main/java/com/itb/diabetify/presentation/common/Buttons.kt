package com.itb.diabetify.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean? = true,
    leftImageResId: Int? = null,
    rightImageResId: Int? = null,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(28.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.primary),
            disabledContainerColor = colorResource(id = R.color.primary).copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(28.dp),
        enabled = (enabled ?: true) && !isLoading
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                if (leftImageResId != null) {
                    Image(
                        painter = painterResource(id = leftImageResId),
                        contentDescription = "Left Image",
                        modifier = Modifier.size(15.dp)
                    )
                }

                Spacer(modifier = Modifier.size(5.dp))

                Text(
                    text = text,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.size(5.dp))

                if (rightImageResId != null) {
                    Image(
                        painter = painterResource(id = rightImageResId),
                        contentDescription = "Right Image",
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}