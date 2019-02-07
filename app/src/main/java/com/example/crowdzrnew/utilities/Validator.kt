package com.example.crowdzrnew.utilities

import android.util.Patterns
import java.util.regex.Pattern

/**
 * Created by Arman on 11/15/2017.
 */


open class Validator {

    class ReasonValidator {
        companion object {
            fun validate(reason: CharSequence): Boolean = reason.isNotEmpty()
            fun validateNotZero(reason: Int): Boolean = reason != 0
            fun validateCCExpiryDate(reason: CharSequence): Boolean = reason.isNotEmpty() && reason.length == 5
            fun validateAmount(reason: String): Boolean = reason.isNotEmpty() && reason.toInt() > 9
            fun validateCreditCard(reason: String): Boolean = reason.isNotEmpty() && reason.length >= 15
            fun validateEmail(reason: CharSequence): Boolean = reason.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(reason).matches()
            fun validateMalaysianId(reason: CharSequence): Boolean = reason.isNotEmpty() && reason.length in 4..13
            fun validateForeignerId(reason: CharSequence): Boolean = reason.isNotEmpty() && reason.length in 4..20
            fun validateMalaysianPostcode(reason: CharSequence): Boolean = reason.isNotEmpty() && reason.length == 5
            fun validateForeignerPostcode(reason: CharSequence): Boolean = reason.isNotEmpty() && reason.length in 3..20
            fun validateCity(reason: CharSequence): Boolean = reason.isNotEmpty() && reason.length in 2..30
            fun validateState(reason: CharSequence): Boolean = reason.isNotEmpty() && reason.length in 2..50
            fun validateAddress(reason: CharSequence): Boolean = reason.isNotEmpty() && reason.length in 5..50
            fun validateName(reason: CharSequence): Boolean = reason.length in 2..100
            fun validatePassword(reason: CharSequence): Boolean = reason.length in 8..40
            fun validatePasswordLength(reason: CharSequence): Boolean = reason.length in 8..40
            fun validatePasswordCase(reason: CharSequence): Boolean = reason != reason.toString().toLowerCase()
            fun validatePasswordDigit(reason: CharSequence): Boolean = Pattern.compile(".*\\d+.*").matcher(reason).find()
            fun validatePasswordLetter(reason: CharSequence): Boolean = Pattern.compile("[A-Za-z]").matcher(reason).find()
            fun validatePasswordLetterLowerCase(reason: CharSequence): Boolean = Pattern.compile("[a-z]").matcher(reason).find()
            fun validatePasswordLetterUpperCase(reason: CharSequence): Boolean = Pattern.compile("[A-Z]").matcher(reason).find()
            fun validatePasswordSpecialChar(reason: CharSequence): Boolean = Pattern.compile("[^A-Za-z0-9]").matcher(reason).find()
            fun validatePasswordFormat(reason: CharSequence): Boolean =
                    validatePasswordLength(reason) &&
                            validatePasswordCase(reason) &&
                            validatePasswordDigit(reason) &&
                            validatePasswordSpecialChar(reason)

            fun validatePhoneIsEmpty(reason: CharSequence): Boolean = reason.isNotEmpty() && reason.length > 2
            fun validatePhoneLength(reason: CharSequence): Boolean = reason.isNotEmpty() && reason.length in 11..12
        }
    }

    class PinValidator {
        companion object {
            fun validateSameNumber(pin: CharSequence): Boolean {
                val pattern = Pattern.compile("\\b(\\d)\\1+\\b")
                return pattern.matcher(pin).find()
            }

            fun validateIncrementalNumber(pin: CharSequence): Boolean {
                var isIncremental = false
                for (i in pin.indices) {
                    if (i < pin.length - 1) {
                        if (pin[i].toInt() == pin[i + 1].toInt() - 1) isIncremental = true
                        else return false
                    }
                }
                return isIncremental
            }

            fun validateDecrementalNumber(pin: CharSequence): Boolean {
                var isDecremental = false
                for (i in pin.indices) {
                    if (i < pin.length - 1) {
                        if (pin[i].toInt() == pin[i + 1].toInt() + 1) isDecremental = true
                        else return false
                    }
                }
                return isDecremental
            }
        }
    }
}