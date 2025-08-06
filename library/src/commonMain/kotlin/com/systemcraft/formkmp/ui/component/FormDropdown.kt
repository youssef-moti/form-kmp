package com.systemcraft.formkmp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.systemcraft.formkmp.core.field.FormFieldConfig
import com.systemcraft.formkmp.core.form.FormState

/**
 * Dropdown/Select field
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FormDropdown(
    key: String,
    formState: FormState,
    options: List<Pair<T, String>>,
    config: FormFieldConfig = FormFieldConfig(),
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    FormField<T>(
        key = key,
        formState = formState,
        config = config.copy(modifier = modifier)
    ) { value, onValueChange, isError, enabled, fieldModifier ->
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it && enabled },
            modifier = fieldModifier
        ) {
            OutlinedTextField(
                value = options.find { it.first == value }?.second ?: "",
                onValueChange = { },
                readOnly = true,
                label = config.label?.let { { Text(it) } },
                placeholder = config.placeholder?.let { { Text(it) } },
                isError = isError,
                enabled = enabled,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = config.colors ?: OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { (optionValue, optionLabel) ->
                    DropdownMenuItem(
                        text = { Text(optionLabel) },
                        onClick = {
                            onValueChange(optionValue)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}