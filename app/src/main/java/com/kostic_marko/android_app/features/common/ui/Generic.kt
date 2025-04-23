package com.kostic_marko.android_app.features.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kostic_marko.android_app.R

@Composable
fun GenericError(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(R.drawable.error),
            contentDescription = "Error icon",
            modifier = Modifier.fillMaxWidth(0.4f),
            contentScale = ContentScale.Crop
        )
        Text("Something went wrong.", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        ElevatedButton(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
fun GenericLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
fun FavouriteStatus(
    onClicked: () -> Unit,
    favouriteStatus: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = onClicked,
    ) {
        Icon(
            if (favouriteStatus) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
            "Favourite"
        )
    }
}