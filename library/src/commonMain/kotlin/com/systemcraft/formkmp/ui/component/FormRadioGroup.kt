package com.systemcraft.formkmp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.systemcraft.formkmp.core.field.FormFieldConfig
import com.systemcraft.formkmp.core.form.FormState

/**
 * Radio button group field
 */
@Composable
fun <T> FormRadioGroup(
    key: String,
    formState: FormState,
    options: List<Pair<T, String>>,
    label: String? = null,
    modifier: Modifier = Modifier
) {
    FormField<T>(
        key = key,
        formState = formState,
        config = FormFieldConfig(label = label, modifier = modifier)
    ) { value, onValueChange, isError, enabled, fieldModifier ->
        Column(modifier = fieldModifier) {
            label?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            options.forEach { (optionValue, optionLabel) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = value == optionValue,
                        onClick = { onValueChange(optionValue) },
                        enabled = enabled,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = if (isError) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = optionLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (enabled) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                }
            }
        }
    }
}