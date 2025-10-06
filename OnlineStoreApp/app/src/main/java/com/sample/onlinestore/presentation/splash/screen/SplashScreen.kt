package com.sample.onlinestore.presentation.splash.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.designsystem.foundation.OnlineStoreSize
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.R
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.utils.assistedHiltViewModel
import com.sample.onlinestore.domain.splash.model.UserStatus
import com.sample.onlinestore.presentation.splash.SplashUiModel
import com.sample.onlinestore.presentation.splash.SplashViewModel
import com.sample.onlinestore.presentation.splash.splashViewModelCreationCallback

@Composable
fun SplashScreen(
    navigateToScreens: (UserStatus?) -> Unit,
    modifier: Modifier = Modifier,
    splashViewModel: SplashViewModel = assistedHiltViewModel(creationCallback = splashViewModelCreationCallback)
) {
    val uiState: UiState<SplashUiModel> =
        splashViewModel.uiState.collectAsStateWithLifecycle().value

    if (uiState is UiState.Result && uiState.data?.initialDataLoaded == true) {
        navigateToScreens(uiState.data?.userStatus)
    }

    Box(modifier = modifier.fillMaxSize()) {
        SplashUI(Modifier)
    }
}

@Composable
fun SplashUI(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Content()
    }
}

@Composable
private fun Content(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = OnlineStoreSpacing.LARGE.dp()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.padding(horizontal = OnlineStoreSize.SMALL.dp()),
            painter = painterResource(R.drawable.ic_splash),
            contentDescription = "splash background",
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.size(OnlineStoreSpacing.LARGE.dp()))
        Text(
            stringResource(R.string.label_splash_online_store),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_XL)
private fun SplashScreenPreview() {
    SplashUI()
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, device = Devices.NEXUS_5)
private fun SplashScreenDarkPreview() {
    SplashUI()
}
