package com.systemcraft.formkmp.validation

sealed class ValidationResult {
    data object Pending: ValidationResult()
    data object Valid: ValidationResult()
    data class Invalid(val errors: List<String>): ValidationResult()
}