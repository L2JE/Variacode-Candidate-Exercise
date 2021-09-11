package Validation;

/**
 * Validates that a field is correct based on its implementation "valid" criteria
 */
public interface FieldValidator {
    boolean validate(Object field);
}
