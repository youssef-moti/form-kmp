package com.systemcraft.formkmp.validation

fun interface ValidationRule<T> {
    suspend fun validate(value: T, formData: Map<String, Any?>): ValidationResult
}

