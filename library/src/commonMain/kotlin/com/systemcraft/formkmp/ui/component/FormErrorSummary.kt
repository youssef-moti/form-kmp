package com.systemcraft.formkmp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.systemcraft.formkmp.core.form.FormState


/**
 * Form error summary component
 */
@Composable
fun FormErrorSummary(
    formState: FormState,
    modifier: Modifier = Modifier
) {
    val fields by formState.fields.collectAsState()
    val errors = fields.values
        .filter { !it.isValid && it.isTouched }
        .flatMap { it.errors }

    if (errors.isNotEmpty()) {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Please fix the following errors:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )

                Spacer(modifier = Modifier.height(8.dp))

                errors.forEach { error ->
                    Text(
                        text = "• $error",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}