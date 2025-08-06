package com.systemcraft.formkmp.validation

class Validator<T> : ValidationRule<T> {
    private val rules = mutableListOf<ValidationRule<T>>()

    fun rule(rule: ValidationRule<T>) = apply { rules.add(rule) }

    fun required(message: String = "This field is required") = rule { value, _ ->
        when {
            value == null -> ValidationResult.Invalid(listOf(message))
            value is String && value.isBlank() -> ValidationResult.Invalid(listOf(message))
            else -> ValidationResult.Valid
        }
    }

    fun minLength(
        length: Int,
        message: String = "This field must have at least $length characters"
    ) = rule { value, _ ->
        if (value is String && value.length < length) {
            ValidationResult.Invalid(listOf(message))
        } else ValidationResult.Valid
    }

    fun maxLength(length: Int, message: String = "This field must have at most $length characters") = rule { value, _ ->
        if (value is String && value.length > length) {
            ValidationResult.Invalid(listOf(message))
        } else ValidationResult.Valid
    }

    fun email(message: String = "This field must be a valid email address", pattern: Regex = Regex("^[\\w-.]+@([\\w-]+.)+[\\w-]{2,4}\$")) = rule { value, _ ->
        if (value is String && !value.matches(pattern)) {
            ValidationResult.Invalid(listOf(message))
        } else ValidationResult.Valid
    }

    fun min(min: Int, message: String = "This field must be at least $min") = rule { value, _ ->
        if (value is Int && value < min) {
            ValidationResult.Invalid(listOf(message))
        } else ValidationResult.Valid
    }

    fun max(max: Int, message: String = "This field must be at most $max") = rule { value, _ ->
        if (value is Int && value > max) {
            ValidationResult.Invalid(listOf(message))
        } else ValidationResult.Valid
    }

    fun between(min: Int, max: Int, message: String = "This field must be between $min and $max") = rule { value, _ ->
        if (value is Int && (value < min || value > max)) {
            ValidationResult.Invalid(listOf(message))
        } else ValidationResult.Valid
    }

   fun pattern(pattern: Regex, message: String = "This field must match the specified pattern") = rule { value, _ ->
        if (value is String && !pattern.matches(value)) {
            ValidationResult.Invalid(listOf(message))
        } else ValidationResult.Valid
    }

    fun range(min: Int, max: Int, message: String = "This field must be between $min and $max") = rule { value, _ ->
        if (value is Int && (value < min || value > max)) {
            ValidationResult.Invalid(listOf(message))
        } else ValidationResult.Valid
    }

    fun custom(validator: suspend (value: T, formData: Map<String, Any?>) -> ValidationResult) = rule(validator)

    override suspend fun validate(
        value: T,
        formData: Map<String, Any?>
    ): ValidationResult {
        val errors = mutableListOf<String>()

        for (rule in rules) {
            when (val result = rule.validate(value, formData)) {
                is ValidationResult.Pending -> return ValidationResult.Pending
                is ValidationResult.Invalid -> errors.addAll(result.errors)
                is ValidationResult.Valid -> continue
            }
        }

        return if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
    }

}