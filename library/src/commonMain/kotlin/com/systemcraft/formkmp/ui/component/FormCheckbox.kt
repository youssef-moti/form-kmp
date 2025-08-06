package com.systemcraft.formkmp.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.systemcraft.formkmp.core.field.FormFieldConfig
import com.systemcraft.formkmp.core.form.FormState

/**
 * Checkbox field
 */
@Composable
fun FormCheckbox(
    key: String,
    formState: FormState,
    label: String,
    modifier: Modifier = Modifier
) {
    FormField<Boolean>(
        key = key,
        formState = formState,
        config = FormFieldConfig(modifier = modifier)
    ) { value, onValueChange, isError, enabled, fieldModifier ->
        Row(
            modifier = fieldModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = value ?: false,
                onCheckedChange = onValueChange,
                enabled = enabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }
    }
}