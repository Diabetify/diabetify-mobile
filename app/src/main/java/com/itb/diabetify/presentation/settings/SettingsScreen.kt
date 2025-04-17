package com.itb.diabetify.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.common.SecondaryButton
import com.itb.diabetify.presentation.settings.components.Card
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 30.dp),
                text = "Profil",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.primary)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 35.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // User profile row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(colorResource(id = R.color.black))
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            text = "Bernardus",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.primary)
                        )
                    }

                    SecondaryButton(
                        text = "Edit",
                        onClick = { /*TODO*/ },
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Height/Weight/Age boxes
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(
                                elevation = 40.dp,
                                shape = RoundedCornerShape(20.dp),
                                spotColor = Color.Gray.copy(alpha = 0.5f)
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorResource(id = R.color.white))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "180cm",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp,
                                color = colorResource(id = R.color.primary),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                modifier = Modifier,
                                text = "Tinggi",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = colorResource(id = R.color.gray),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(
                                elevation = 40.dp,
                                shape = RoundedCornerShape(20.dp),
                                spotColor = Color.Gray.copy(alpha = 0.5f)
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorResource(id = R.color.white))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "65kg",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp,
                                color = colorResource(id = R.color.primary),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                modifier = Modifier,
                                text = "Berat",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = colorResource(id = R.color.gray),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(
                                elevation = 40.dp,
                                shape = RoundedCornerShape(20.dp),
                                spotColor = Color.Gray.copy(alpha = 0.5f)
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorResource(id = R.color.white))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "22",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp,
                                color = colorResource(id = R.color.primary),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                modifier = Modifier,
                                text = "Umur",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = colorResource(id = R.color.gray),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Cards section
                cards.forEach { cardData ->
                    Card(cardData = cardData)
                    Spacer(modifier = Modifier.height(25.dp))
                }

                // Logout button
                PrimaryButton(
                    text = "Logout",
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                )

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}