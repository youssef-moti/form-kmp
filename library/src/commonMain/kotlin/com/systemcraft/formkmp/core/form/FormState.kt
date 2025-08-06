package com.systemcraft.formkmp.core.form

import com.systemcraft.formkmp.core.field.FieldState
import com.systemcraft.formkmp.validation.ValidationResult
import com.systemcraft.formkmp.validation.ValidationRule
import com.systemcraft.formkmp.validation.ValidationTrigger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FormState {
    private val _fields = MutableStateFlow<Map<String, FieldState<Any?>>>(emptyMap())
    val fields: StateFlow<Map<String, FieldState<Any?>>> = _fields.asStateFlow()

    private val validationRules = mutableMapOf<String, ValidationRule<Any?>>()
    private val fieldDependencies = mutableMapOf<String, Set<String>>()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _validationTriggers = MutableStateFlow(setOf(ValidationTrigger.ON_SUBMIT))
    val validationTriggers: StateFlow<Set<ValidationTrigger>> = _validationTriggers.asStateFlow()

    val isValid: Boolean
        get() = _fields.value.values.all { it.isValid }

    val isDirty: Boolean
        get() = _fields.value.values.any { it.isDirty }

    val isTouched: Boolean
        get() = _fields.value.values.any { it.isTouched }

    fun <T> field(key: String): FieldState<T>? {
        @Suppress("UNCHECKED_CAST")
        return _fields.value[key] as? FieldState<T>
    }

    fun <T> getValue(key: String): T? {
        @Suppress("UNCHECKED_CAST")
        return _fields.value[key]?.value as? T
    }

    fun <T> registerField(
        key: String,
        initialValue: T,
        validation: ValidationRule<T>? = null,
        dependencies: Set<String> = emptySet(),
        isVisible: Boolean = true,
        isEnabled: Boolean = true
    ) {
        if (validation != null) {
            @Suppress("UNCHECKED_CAST")
            validationRules[key] = validation as ValidationRule<Any?>
        }

        if (dependencies.isNotEmpty()) {
            fieldDependencies[key] = dependencies
        }

        val currentFields = _fields.value.toMutableMap()
        currentFields[key] = FieldState(
            value = initialValue,
            isVisible = isVisible,
            isEnabled = isEnabled
        )
        _fields.value = currentFields
    }

    suspend fun <T> updateField(
        key: String,
        value: T,
        trigger: ValidationTrigger = ValidationTrigger.ON_BLUR
    ) {
        val currentField = _fields.value[key] ?: return
        val updatedField = currentField.copy(
            value = value,
            isDirty = true,
            isTouched = currentField.isTouched
        )

        val currentFields = _fields.value.toMutableMap()
        currentFields[key] = updatedField
        _fields.value = currentFields

        // Validate if trigger matches
        if (_validationTriggers.value.contains(trigger)) {
            validateField(key)
        }

        // Update dependent fields
        updateDependentFields(key)
    }

    suspend fun setFieldFocus(key: String, focused: Boolean) {
        val currentField = _fields.value[key] ?: return
        val updatedField = currentField.copy(
            isFocused = focused,
            isTouched = currentField.isTouched || !focused
        )

        val currentFields = _fields.value.toMutableMap()
        currentFields[key] = updatedField
        _fields.value = currentFields
    }

    suspend fun validateField(key: String) {
        val rule = validationRules[key] ?: return
        val currentField = _fields.value[key] ?: return

        val formData = _fields.value.mapValues { it.value.value }
        val validationState = rule.validate(currentField.value, formData)

        val updatedField = currentField.copy(validationResult = validationState)
        val currentFields = _fields.value.toMutableMap()
        currentFields[key] = updatedField
        _fields.value = currentFields
    }

    suspend fun validateAll(): Boolean {
        val keys = _fields.value.keys.toList()
        keys.forEach { validateField(it) }
        return isValid
    }

    private suspend fun updateDependentFields(changedKey: String) {
        fieldDependencies.entries
            .filter { it.value.contains(changedKey) }
            .forEach { (dependentKey, _) ->
                validateField(dependentKey)
            }
    }

    fun setValidationTriggers(triggers: Set<ValidationTrigger>) {
        _validationTriggers.value = triggers
    }

    suspend fun submit(onSubmit: suspend (Map<String, Any?>) -> Unit): Boolean {
        _isSubmitting.value = true

        try {
            if (!validateAll()) {
                return false
            }

            val formData = _fields.value.mapValues { it.value.value }
            onSubmit(formData)
            return true
        } finally {
            _isSubmitting.value = false
        }
    }

    fun reset() {
        val resetFields = _fields.value.mapValues { (_, field) ->
            field.copy(
                validationResult = ValidationResult.Valid,
                isTouched = false,
                isDirty = false,
                isFocused = false
            )
        }
        _fields.value = resetFields
    }

    fun getFormData(): Map<String, Any?> {
        return _fields.value.mapValues { it.value.value }
    }

    fun getAllErrors(): Map<String, List<String>> {
        return _fields.value
            .filter { !it.value.isValid }
            .mapValues { it.value.errors }
    }
}