package com.looker.kenko.ui.getStarted

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.ui.components.TypingText
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.ui.theme.header
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GetStarted(onNext: () -> Unit) {
    GetStarted(onNextClick = onNext)
}

@Composable
private fun GetStarted(
    modifier: Modifier = Modifier,
    onNextClick: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        val iconVisibility = remember { Animatable(-50F) }
        val buttonVisibility = remember { Animatable(0.75F) }
        LaunchedEffect(true) {
            buttonVisibility.animateTo(
                targetValue = 0.85F,
                animationSpec = spring()
            )
            launch {
                iconVisibility.animateTo(
                    targetValue = 0F,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessVeryLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                )
            }
            launch {
                buttonVisibility.animateTo(
                    targetValue = 1F,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessVeryLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
        ) {
            var startShowingFirstMeaning by remember { mutableStateOf(false) }
            var startShowingSecondMeaning by remember { mutableStateOf(false) }
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                text = stringResource(R.string.label_kenko),
                style = MaterialTheme.typography.header(),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 18.dp),
            )
            TypingText(
                text = stringResource(R.string.label_kenko_jp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                typingDelay = 10.milliseconds,
                onCompleteListener = { startShowingFirstMeaning = true },
                modifier = Modifier.padding(horizontal = 18.dp),
            )
            TypingText(
                text = stringResource(R.string.label_kenko_meaning),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline,
                startTyping = startShowingFirstMeaning,
                typingDelay = 10.milliseconds,
                initialDelay = 0.milliseconds,
                onCompleteListener = { startShowingSecondMeaning = true },
                modifier = Modifier.padding(horizontal = 18.dp),
            )
            TypingText(
                text = stringResource(R.string.label_kenko_meaning_ALT),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline,
                startTyping = startShowingSecondMeaning,
                typingDelay = 10.milliseconds,
                initialDelay = 0.milliseconds,
                modifier = Modifier.padding(horizontal = 18.dp),
            )
            Spacer(modifier = Modifier.weight(1F))
    	    Button(
        	onClick = onNextClick,
        	modifier = Modifier.align(Alignment.CenterHorizontally),
        	colors = ButtonDefaults.buttonColors(
            	    containerColor = MaterialTheme.colorScheme.tertiary,
            	    contentColor = MaterialTheme.colorScheme.onTertiary,
                ),
            	contentPadding = PaddingValues(
            	    vertical = 24.dp,
           	    horizontal = 40.dp
        	)
    	    ) {
        	Row(
            	    verticalAlignment = Alignment.CenterVertically
        	) {
            	    ButtonIcon(
            		modifier = Modifier
            	    	    .graphicsLayer {
                         	translationX = iconVisibility.value
                         	rotationZ = iconVisibility.value
                    	    }
            	    )
            	    Spacer(modifier = Modifier.width(8.dp))
            	    Text(text = stringResource(R.string.label_lets_go))
		}
            }
	    Spacer(modifier = Modifier.weight(0.05F))
        }
    }
}

@Composable
private fun ButtonIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.ArrowForward,
) {
    Icon(
        modifier = modifier
            .background(MaterialTheme.colorScheme.onTertiary, CircleShape)
            .padding(8.dp),
        imageVector = icon,
        tint = MaterialTheme.colorScheme.tertiary,
        contentDescription = ""
    )
}

@Preview
@PreviewScreenSizes
@Composable
private fun GetStartedPreview() {
    KenkoTheme {
        GetStarted(onNextClick = {})
    }
}
