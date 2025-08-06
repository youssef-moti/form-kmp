package com.systemcraft.formkmp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.systemcraft.formkmp.core.form.FormState
import kotlinx.coroutines.launch

/**
 * Form submission and actions
 */
@Composable
fun FormActions(
    formState: FormState,
    onSubmit: suspend () -> Unit,
    submitButtonText: String = "Submit",
    resetButtonText: String = "Reset",
    showResetButton: Boolean = true,
    submitButtonEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val isSubmitting by formState.isSubmitting.collectAsState()
    val fields by formState.fields.collectAsState()

    val isFormValid = fields.values.all { it.isValid }
    val canSubmit = submitButtonEnabled && isFormValid && !isSubmitting

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showResetButton) {
            OutlinedButton(
                onClick = { formState.reset() },
                enabled = !isSubmitting
            ) {
                Text(resetButtonText)
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    onSubmit()
                }
            },
            enabled = canSubmit
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(submitButtonText)
        }
    }
}