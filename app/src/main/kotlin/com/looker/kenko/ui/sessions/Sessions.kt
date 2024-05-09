package com.looker.kenko.ui.sessions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.looker.kenko.R
import com.looker.kenko.data.model.Session
import com.looker.kenko.ui.components.texture.GradientStart
import com.looker.kenko.ui.components.texture.dottedGradient
import com.looker.kenko.ui.helper.plus
import com.looker.kenko.ui.planEdit.components.dayName
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme
import com.looker.kenko.utils.DateTimeFormat
import com.looker.kenko.utils.formatDate
import com.looker.kenko.utils.isToday
import kotlinx.datetime.LocalDate

@Composable
fun Sessions(
    viewModel: SessionsViewModel,
    onSessionClick: (LocalDate?) -> Unit,
) {
    Scaffold(
        modifier = Modifier.padding(bottom = 80.dp),
        floatingActionButton = {
            Button(
                onClick = { onSessionClick(null) },
                contentPadding = PaddingValues(vertical = 24.dp, horizontal = 40.dp)
            ) {
                val isActive by viewModel.isCurrentSessionActive.collectAsState()
                val text = remember(isActive) {
                    if (isActive) R.string.label_continue
                    else R.string.label_start
                }
                Text(text = stringResource(id = text))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = KenkoIcons.ArrowForward,
                    contentDescription = ""
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { padding ->
        val sessions by viewModel.sessionsStream.collectAsState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding + PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(
                items = sessions,
                key = { it.date.toEpochDays() },
            ) { session ->
                SessionCard(
                    session = session,
                    isTodayLabel = {
                        val isToday = remember(session.date) {
                            session.date.isToday
                        }
                        if (isToday) {
                            IsTodayLabel()
                        }
                    },
                    onClick = { onSessionClick(session.date) }
                )
            }
        }
    }
}

@Composable
private fun IsTodayLabel() {
    Surface(
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            text = stringResource(R.string.label_today),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun SessionCard(
    session: Session,
    modifier: Modifier = Modifier,
    isTodayLabel: @Composable () -> Unit,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .dottedGradient(
                    MaterialTheme.colorScheme.onSurfaceVariant,
                    start = GradientStart.TopRight
                )
                .padding(horizontal = 18.dp, vertical = 28.dp),
        ) {
            isTodayLabel()
            Text(
                text = dayName(session.date.dayOfWeek),
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = session.formattedDate(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun LocalDate.format(
    format: DateTimeFormat = DateTimeFormat.Short,
): String = remember(this) {
    formatDate(this, dateTimeFormat = format)
}

@Composable
fun Session.formattedDate(format: DateTimeFormat = DateTimeFormat.Short): String =
    remember(date) {
        formatDate(date, dateTimeFormat = format)
    }

@Preview
@Composable
private fun TodayLabelPreview() {
    KenkoTheme {
        IsTodayLabel()
    }
}

@PreviewLightDark
@Composable
private fun SessionCardPreview() {
    KenkoTheme {
        SessionCard(
            session = Session.SAMPLE,
            isTodayLabel = { IsTodayLabel() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}