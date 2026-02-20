package com.example.android.wearable.composestarter.presentation

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonGroup
import androidx.wear.compose.material3.FilledIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.SwitchButton
import androidx.wear.compose.material3.SwitchButtonDefaults
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.example.android.wearable.composestarter.R
import com.example.android.wearable.composestarter.presentation.theme.AppCardDefaults

@Composable
fun TlcEnhancementScreen() {
    var reverseLayout by remember { mutableStateOf(false) }
    var useSnapping by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val state = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()

    ScreenScaffold(scrollState = state) { contentPadding ->
        TransformingLazyColumn(
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            flingBehavior = if (useSnapping) {
                TransformingLazyColumnDefaults.snapFlingBehavior(state)
            } else {
                ScrollableDefaults.flingBehavior()
            },
            rotaryScrollableBehavior = if (useSnapping) {
                RotaryScrollableDefaults.snapBehavior(state)
            } else {
                RotaryScrollableDefaults.behavior(state)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec)
                ) {
                    Text(stringResource(R.string.tlc_settings))
                }
            }
            item {
                SwitchButton(
                    checked = reverseLayout,
                    onCheckedChange = { reverseLayout = it },
                    label = { Text(stringResource(R.string.reverse_layout)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec)
                )
            }
            item {
                SwitchButton(
                    checked = useSnapping,
                    onCheckedChange = { useSnapping = it },
                    label = { Text(stringResource(R.string.snapping)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec)
                )
            }
            item {
                ListHeader(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec)
                ) { Text(text = stringResource(R.string.item_list)) }
            }
            item {
                TitleCard(
                    title = {
                        Text(
                            stringResource(R.string.example_card_title)
                        )
                    },
                    onClick = { },
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                    colors = AppCardDefaults.cardColors()
                ) {
                    Text(stringResource(R.string.example_card_content))
                }
            }
            item {
                ButtonGroup(
                    modifier =
                    Modifier
                        .graphicsLayer {
                            with(transformationSpec) {
                                applyContainerTransformation(scrollProgress)
                            }
                        }.transformedHeight(this, transformationSpec)
                ) {
                    FilledIconButton(
                        onClick = { showDialog = true },
                        colors =
                        IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.settings),
                            contentDescription =
                            stringResource(
                                R.string.settings_button_content_description
                            )
                        )
                    }
                    FilledIconButton(
                        onClick = { },
                        shapes = IconButtonDefaults.animatedShapes()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.thumb_up),
                            contentDescription =
                            stringResource(
                                R.string.thumbs_up_button_content_description
                            )
                        )
                    }
                }
            }
            items(10) { index ->
                Button(
                    label = {
                        Text(
                            text = stringResource(R.string.item_label, index + 1),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec)
                )
            }
        }
    }

    SampleDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onCancel = {},
        onOk = {}
    )
}
