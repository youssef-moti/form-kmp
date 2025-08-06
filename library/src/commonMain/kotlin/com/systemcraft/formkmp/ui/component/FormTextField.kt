package com.systemcraft.formkmp.ui.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import com.systemcraft.formkmp.core.field.FormFieldConfig
import com.systemcraft.formkmp.core.form.FormState
import kotlinx.coroutines.launch

/**
 * Text input field
 */
@Composable
fun FormTextField(
    key: String,
    formState: FormState,
    config: FormFieldConfig = FormFieldConfig()
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    FormField<String>(
        key = key,
        formState = formState,
        config = config
    ) { value, onValueChange, isError, enabled, modifier ->
        OutlinedTextField(
            value = value ?: "",
            onValueChange = onValueChange,
            label = config.label?.let { { Text(it) } },
            placeholder = config.placeholder?.let { { Text(it) } },
            isError = isError,
            enabled = enabled,
            singleLine = config.singleLine,
            maxLines = config.maxLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = config.keyboardType,
                imeAction = config.imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                onDone = { focusManager.clearFocus() }
            ),
            visualTransformation = config.visualTransformation,
            leadingIcon = config.leadingIcon,
            trailingIcon = config.trailingIcon,
            colors = config.colors ?: OutlinedTextFieldDefaults.colors(),
            modifier = modifier.onFocusChanged { focusState ->
                coroutineScope.launch {
                    formState.setFieldFocus(key, focusState.isFocused)
                }
            }
        )
    }
}