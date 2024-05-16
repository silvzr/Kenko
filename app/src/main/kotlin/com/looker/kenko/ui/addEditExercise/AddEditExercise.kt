package com.looker.kenko.ui.addEditExercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.kenko.R
import com.looker.kenko.data.model.MuscleGroups
import com.looker.kenko.ui.components.kenkoTextFieldColor
import com.looker.kenko.ui.theme.KenkoIcons
import com.looker.kenko.ui.theme.KenkoTheme

@Composable
fun AddEditExercise(onDone: () -> Unit) {
    val viewModel: AddEditExerciseViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AddEditExercise(
        exerciseName = viewModel.exerciseName,
        exerciseReference = viewModel.reference,
        state = state,
        onSelectTarget = viewModel::setTargetMuscle,
        onSelectIsometric = viewModel::setIsometric,
        onNameChange = viewModel::setName,
        onReferenceChange = viewModel::addReference,
        onDone = {
            viewModel.addNewExercise()
            onDone()
        }
    )
}

@Composable
private fun AddEditExercise(
    exerciseName: String,
    exerciseReference: String,
    state: AddEditExerciseUiState,
    onSelectTarget: (MuscleGroups) -> Unit,
    onSelectIsometric: (Boolean) -> Unit,
    onNameChange: (String) -> Unit,
    onReferenceChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .statusBarsPadding(),
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.label_new_exercise),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.height(32.dp))
        ExerciseTextField(
            exerciseName = exerciseName,
            onNameChange = onNameChange,
            isError = state.isError,
            isReadOnly = state.isReadOnly,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.label_target),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(8.dp))
        TargetMuscleSelection(target = state.targetMuscle, onSet = onSelectTarget)
        Spacer(modifier = Modifier.height(12.dp))
        IsIsometricButton(isIsometric = state.isIsometric, onChange = onSelectIsometric)
        Spacer(modifier = Modifier.height(18.dp))
        ReferenceTextField(
            reference = exerciseReference,
            onReferenceChange = onReferenceChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(18.dp))
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(12.dp)
                .navigationBarsPadding(),
            onClick = onDone,
            contentPadding = PaddingValues(vertical = 32.dp, horizontal = 48.dp)
        ) {
            Icon(
                imageVector = KenkoIcons.Save,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = stringResource(R.string.label_save))
        }
    }
}

@Composable
private fun ReferenceTextField(
    reference: String,
    onReferenceChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        modifier = modifier,
        value = reference,
        onValueChange = onReferenceChange,
        colors = kenkoTextFieldColor(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        shape = MaterialTheme.shapes.large,
        supportingText = {
            Text(text = stringResource(R.string.label_reference_optional))
        },
        label = {
            Text(text = stringResource(R.string.label_reference))
        },
        leadingIcon = {
            Icon(imageVector = KenkoIcons.Lightbulb, contentDescription = null)
        }
    )
}

@Composable
private fun ExerciseTextField(
    exerciseName: String,
    isError: Boolean,
    isReadOnly: Boolean,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        modifier = modifier,
        value = exerciseName,
        onValueChange = onNameChange,
        colors = kenkoTextFieldColor(),
        readOnly = isReadOnly,
        shape = MaterialTheme.shapes.large,
        label = {
            Text(text = stringResource(R.string.label_name))
        },
        isError = isError,
        leadingIcon = {
            Icon(imageVector = KenkoIcons.Rename, contentDescription = null)
        },
        supportingText = {
            if (isError) {
                Text(text = stringResource(R.string.label_exercise_exists))
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TargetMuscleSelection(
    target: MuscleGroups,
    onSet: (MuscleGroups) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MuscleGroups.entries.forEach { muscle ->
            FilterChip(
                selected = target == muscle,
                onClick = { onSet(muscle) },
                label = { Text(text = stringResource(muscle.stringRes)) }
            )
        }
    }
}

@Composable
private fun IsIsometricButton(isIsometric: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onChange(!isIsometric) }
    ) {
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = stringResource(R.string.label_is_isometric),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = stringResource(R.string.label_is_isometric_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
            )
        }
        Switch(checked = isIsometric, onCheckedChange = onChange)
    }
}

@PreviewLightDark
@Composable
private fun ReferenceTextFieldPreview() {
    KenkoTheme {
        ReferenceTextField(
            reference = "https://youtu.be",
            onReferenceChange = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IsIsometricButtonPreview() {
    KenkoTheme {
        var isIso by remember {
            mutableStateOf(false)
        }
        IsIsometricButton(isIsometric = isIso, onChange = { isIso = !isIso })
    }
}

@Preview(name = "Exercise Name Field")
@Composable
private fun NameTextFieldPreview() {
    KenkoTheme {
        ExerciseTextField(
            exerciseName = "Bench Press",
            onNameChange = {},
            isError = false,
            isReadOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "Exercise Name Field - Error")
@Composable
private fun ErrorNameTextFieldPreview() {
    KenkoTheme {
        ExerciseTextField(
            exerciseName = "Bench Press",
            onNameChange = {},
            isError = true,
            isReadOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEditPreview() {
    KenkoTheme {
        AddEditExercise(
            exerciseName = "BenchPress",
            exerciseReference = "yt.be",
            state = AddEditExerciseUiState(MuscleGroups.Chest, false, false, false),
            onSelectTarget = {},
            onSelectIsometric = {},
            onNameChange = {},
            onReferenceChange = {}
        ) {

        }
    }
}
