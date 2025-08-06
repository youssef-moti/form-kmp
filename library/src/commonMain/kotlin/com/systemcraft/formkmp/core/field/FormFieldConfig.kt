package com.systemcraft.formkmp.core.field

import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Form field configuration
 */
data class FormFieldConfig(
    val label: String? = null,
    val placeholder: String? = null,
    val helperText: String? = null,
    val isRequired: Boolean = false,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val imeAction: ImeAction = ImeAction.Next,
    val singleLine: Boolean = true,
    val maxLines: Int = 1,
    val visualTransformation: VisualTransformation = VisualTransformation.None,
    val leadingIcon: @Composable (() -> Unit)? = null,
    val trailingIcon: @Composable (() -> Unit)? = null,
    val colors: TextFieldColors? = null,
    val modifier: Modifier = Modifier
)