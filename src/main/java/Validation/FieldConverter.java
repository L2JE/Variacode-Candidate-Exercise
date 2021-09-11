package Validation;

/**
 * Transforms an object returning a new copy of it
 */
public interface FieldConverter {
    Object transform(Object field);
}
