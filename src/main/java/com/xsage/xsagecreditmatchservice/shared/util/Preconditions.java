package com.xsage.xsagecreditmatchservice.shared.util;

/**
 * Wrapper class for Spring's Assert class. <br />
 * Use this class to make it easier to replace Spring with another framework at a later point
 */
public class Preconditions extends org.springframework.util.Assert {
    /**
     * Convenience {@link Preconditions#noNullElements(Object[], String)} wrapper using varargs array.
     * <p/>
     * Assert that an array has no null elements. <br/>
     * Note: Does not complain if the array is empty!<br>
     * <p/>
     * <pre class="code">
     * Assert.noNullElements("The array must have non-null elements", object1, object2, objectN ...);
     * </pre>
     *
     * @param message the exception message to use if the assertion fails
     * @param objects - objects to check
     * @throws IllegalArgumentException if the object array contains a <code>null</code> element
     */
    public static void noNullElements(final String message, final Object... objects) {
        org.springframework.util.Assert.noNullElements(objects, message);
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isFalse(boolean expression) {
        isFalse(expression, "[Assertion failed] - this expression must be false");
    }
}
