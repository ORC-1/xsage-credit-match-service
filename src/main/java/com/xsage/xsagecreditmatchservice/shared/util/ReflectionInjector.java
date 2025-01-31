package com.xsage.xsagecreditmatchservice.shared.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;


public class ReflectionInjector {
    private final Object valueToSet;

    public ReflectionInjector(Object value) {
        Preconditions.notNull(value, "object to inject can not be null");
        this.valueToSet = value;
    }

    /**
     * Injects {@link #valueToSet} to target bean.
     *
     * @throws IllegalStateException when there is none or more than one fields of type of {@link #valueToSet} in target bean.
     */
    public void to(Object target) {
        Preconditions.notNull(target, "target to inject into cannot be null");
        Class<? extends Object> clazz = target.getClass();

        Set<Field> fields = getAllFieldsInClassAndSuperClasses(clazz);
        List<Field> allFieldsAssignableFrom = getAllFieldsAssignableFrom(fields);
        if (allFieldsAssignableFrom.isEmpty()) {
            throw new IllegalStateException("There must be EXACTLY one field of type: " + valueToSet.getClass().getName() + " in "
                                            + target.getClass().getName() + " but found NONE!");
        }
        if (allFieldsAssignableFrom.size() > 1) {
            throw new IllegalStateException("There must be EXACTLY one field of type: " + valueToSet.getClass().getName() + " in "
                                            + target.getClass().getName() + " but found " + allFieldsAssignableFrom.size());
        }
        setValueToObject(target, allFieldsAssignableFrom);
    }

    private Set<Field> getAllFieldsInClassAndSuperClasses(Class<? extends Object> clazz) {
        Set<Field> fields = newHashSet();
        while (clazz != null) {
            fields.addAll(newHashSet(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * Alias to {@link #to(Object)}.
     */
    public void in(Object target) {
        to(target);
    }

    /**
     * Alias to {@link #to(Object)}.
     */
    public void into(Object target) {
        to(target);
    }

    private void setValueToObject(Object target, List<Field> allFieldsAssignableFrom) {
        try {
            Field field = allFieldsAssignableFrom.get(0);
            field.setAccessible(true);
            field.set(target, valueToSet);
        } catch (final IllegalAccessException ex) {
            throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass()
                    .getName() + ": " + ex.getMessage());
        }
    }

    private List<Field> getAllFieldsAssignableFrom(Set<Field> fields) {
        return fields.stream().filter(field -> field.getType()
                        .isAssignableFrom(valueToSet.getClass()))
                .collect(Collectors.toList());
    }
}
