package com.itb.diabetify.presentation.guide.tips_detail

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun TipsDetailScreen(
    navController: NavController,
    tipsId: String
) {
    val tipsDetail = tipsDetails[tipsId]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colorResource(id = R.color.primary)
                    )
                }

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Tips",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.primary)
                )
            }

            if (tipsDetail != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Hero
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Image(
                            painter = painterResource(id = tipsDetail.image),
                            contentDescription = tipsDetail.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.5f),
                                            Color.Transparent,
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        Text(
                            text = tipsDetail.title,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        tipsDetail.content.forEach { section ->
                            TipsSection(section)
                        }

                        // Read More Section
                        if (tipsDetail.readMoreLinks.isNotEmpty()) {
                            ReadMoreSection(readMoreLinks = tipsDetail.readMoreLinks)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tips tidak ditemukan",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun TipsSection(
    section: TipsSection
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = section.title,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = colorResource(id = R.color.primary)
        )

        FormattedContent(content = section.content)
    }
}

@Composable
private fun FormattedContent(content: String) {
    val lines = content.split("\n")

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        lines.forEach { line ->
            when {
                line.trim().isEmpty() -> {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                line.trimStart().startsWith("•") -> {
                    val cleanText = line.trimStart().removePrefix("•").trim()
                    Point(
                        text = cleanText,
                        level = 0,
                        symbol = "•"
                    )
                }

                line.trimStart().startsWith("  -") || line.trimStart().startsWith("- ") -> {
                    val cleanText = line.trimStart().removePrefix("-").trim()
                    Point(
                        text = cleanText,
                        level = 1,
                        symbol = "-"
                    )
                }

                line.trimStart().matches(Regex("^\\d+\\..*")) -> {
                    val parts = line.trimStart().split(".", limit = 2)
                    if (parts.size == 2) {
                        NumberedPoint(
                            number = parts[0],
                            text = parts[1].trim(),
                            level = 0
                        )
                    }
                }

                line.trimStart().startsWith("  ") && line.trimStart().matches(Regex("^\\s*\\d+\\..*")) -> {
                    val trimmed = line.trimStart()
                    val parts = trimmed.split(".", limit = 2)
                    if (parts.size == 2) {
                        NumberedPoint(
                            number = parts[0],
                            text = parts[1].trim(),
                            level = 1
                        )
                    }
                }

                else -> {
                    if (line.trim().isNotEmpty()) {
                        Text(
                            text = line.trim(),
                            fontFamily = poppinsFontFamily,
                            fontWeight = if (line.trim().endsWith(":")) FontWeight.Medium else FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Black.copy(alpha = 0.8f),
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Point(
    text: String,
    level: Int,
    symbol: String = "•"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (level * 16).dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = symbol,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color.Black.copy(alpha = 0.6f),
            modifier = Modifier.width(16.dp)
        )
        Text(
            text = text,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color.Black.copy(alpha = 0.8f),
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ReadMoreSection(
    readMoreLinks: List<ReadMoreLink>
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Baca Selengkapnya",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = colorResource(id = R.color.primary)
        )

        readMoreLinks.forEach { link ->
            ReadMoreCard(
                readMoreLink = link,
                onLinkClick = { url ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )
        }
    }
}

@Composable
private fun ReadMoreCard(
    readMoreLink: ReadMoreLink,
    onLinkClick: (String) -> Unit = { }
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLinkClick(readMoreLink.url) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = readMoreLink.title,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.Black,
                    lineHeight = 22.sp
                )

                Text(
                    text = readMoreLink.url,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.primary).copy(alpha = 0.8f),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun NumberedPoint(
    number: String,
    text: String,
    level: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (level * 16).dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = colorResource(id = R.color.primary).copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.Black.copy(alpha = 0.8f),
            lineHeight = 24.sp,
            modifier = Modifier.weight(1f)
        )
    }
}