package org.assignment.recipe.exception;

/**
 * Exception thrown when an attempt is made to access or manipulate a resource
 * (either recipe or ingredient) that cannot be found in the system.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
