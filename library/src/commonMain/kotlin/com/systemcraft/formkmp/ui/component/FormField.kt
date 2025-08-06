package com.systemcraft.formkmp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.systemcraft.formkmp.core.field.FormFieldConfig
import com.systemcraft.formkmp.core.form.FormState
import kotlinx.coroutines.launch

/**
 * Base form field component
 */
@Composable
fun <T> FormField(
    key: String,
    formState: FormState,
    config: FormFieldConfig = FormFieldConfig(),
    content: @Composable (
        value: T?,
        onValueChange: (T) -> Unit,
        isError: Boolean,
        enabled: Boolean,
        modifier: Modifier
    ) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val field by formState.fields.collectAsState()
    val fieldState = field[key]

    if (fieldState?.isVisible != true) return

    val currentValue = fieldState.value
    val isError = !fieldState.isValid && fieldState.isTouched
    val enabled = fieldState.isEnabled

    Column(
        modifier = config.modifier.fillMaxWidth()
    ) {
        content(
            currentValue as T?,
            { newValue ->
                coroutineScope.launch {
                    formState.updateField(key, newValue)
                }
            },
            isError,
            enabled,
            Modifier.fillMaxWidth()
        )

        // Error messages
        if (isError && fieldState.errors.isNotEmpty()) {
            fieldState.errors.forEach { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        // Helper text
        if (!isError && config.helperText != null) {
            Text(
                text = config.helperText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

/*
fun <T> FormField(
    definition: FieldDefinition<T>,
    modifier: Modifier = Modifier.fillMaxWidth(),
    textStyle: TextStyle = LocalTextStyle.current,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        if (!definition.visible) return

        definition.externalLabel?.let { Text(it) }
        OutlinedTextField(
            value = definition.initialValue?.let { it.toString() } ?: "",
            onValueChange = {},
            modifier = modifier,
            enabled = definition.enabled,
            readOnly = definition.readOnly,
            textStyle = textStyle,
            label = { definition.label?.let { Text(it) } },
            placeholder = { definition.placeholder?.let { Text(it) } },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = {
                if (definition.isError) {
                    Text("Error:")
                }

                if (!definition.isError && definition.supportingText != null) {
                    Text(definition.supportingText)
                }
            },
            isError = definition.isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = definition.singleLine,
            maxLines = definition.maxLines,
            minLines = definition.minLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors,
        )
    }
}


@Preview
@Composable
fun FormFieldPreview() {
    FormField(definition = FieldDefinition(key = "name", initialValue = "John"))
}
 */
