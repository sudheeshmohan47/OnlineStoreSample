package com.sample.onlinestore.presentation.appintro.appintro.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.R
import com.sample.onlinestore.commonmodule.utils.getStringFromId
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroPage


@Composable
fun AppIntroPageContent(
    appIntroPage: AppIntroPage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        BackgroundImage(imageId = appIntroPage.imageId, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(OnlineStoreSpacing.LARGE.dp()))
        TextContents(
            title = getStringFromId(context, appIntroPage.titleResId),
            subtitle = getStringFromId(context, appIntroPage.subTitleResId)
        )
    }
}

@Composable
private fun BackgroundImage(imageId: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OnlineStoreSpacing.EXTRA_LARGE.dp())
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.Fit,

        )
    }
}

@Composable
private fun TextContents(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(OnlineStoreSpacing.MEDIUM.dp()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        LargeTitle(text = title)
        Spacer(modifier = Modifier.height(OnlineStoreSpacing.SMALL.dp()))
        Subtitle(text = subtitle)
        Spacer(modifier = Modifier.height(OnlineStoreSpacing.MEDIUM.dp()))
        Spacer(modifier = Modifier.padding(bottom = OnlineStoreSpacing.LARGE.dp()))
    }
}

@Composable
private fun ColumnScope.LargeTitle(text: String) {
    Text(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ColumnScope.Subtitle(text: String) {
    Text(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
@Preview
private fun OnBoardContentUIPreview() {
    AppIntroPageContent(
        AppIntroPage(
            imageId = R.drawable.img_app_intro_1,
            titleResId = R.string.app_intro_title_1,
            subTitleResId = R.string.app_intro_subtitle_1
        )
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun OnBoardContentUIDarkPreview() {
    OnBoardContentUIPreview()
}
