package com.systemcraft.formkmp.core.field

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.systemcraft.formkmp.validation.ValidationResult

data class FieldState<T>(
    val value: T,
    val validationResult: ValidationResult = ValidationResult.Valid,
    val isTouched: Boolean = false,
    val isDirty: Boolean = true,
    val isFocused: Boolean = true,
    val isVisible: Boolean = true,
    val isEnabled: Boolean = true,
) {
    val isValid: Boolean get() = validationResult is ValidationResult.Valid
    val errors: List<String> get() = (validationResult as ValidationResult.Invalid).errors ?: emptyList()
}