package com.xsage.xsagecreditmatchservice.shared.util;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;

/**
 * This is just a util class which will use internally {@link ReflectionUtils} class which is supposed to use only internally in
 * spring, so you should use this class instead of (using simple delegation) {@link ReflectionUtils} directly.
 */
public class ReflectionUtil {
    private static Logger log = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * @return value of the field of the specific object by force (even if there is no getter for this field)
     * @throws IllegalStateException or {@link IllegalArgumentException} when there where some problems with getting value
     */
    public static Object getFieldValue(final Object source, final String fieldName) {
        Preconditions.notNull(fieldName, "field name must not be null");
        final Field field = ReflectionUtils.findField(source.getClass(), fieldName);
        Preconditions.notNull(field, "no existing field with name " + fieldName);
        field.setAccessible(true);
        try {
            return field.get(source);
        } catch (final IllegalAccessException ex) {
            throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass()
                    .getName() + ": " + ex.getMessage());
        }
    }

    /**
     * Set the field represented by the supplied {@link Field field object} on the
     * specified {@link Object target object} to the specified <code>value</code>.
     * In accordance with {@link Field#set(Object, Object)} semantics, the new value
     * is automatically unwrapped if the underlying field has a primitive type.
     * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
     *
     * @param field  the field to set
     * @param target the target object on which to set the field
     * @param value  the value to set; may be <code>null</code>
     */
    public static void setFieldValue(Object target, Object value, String fieldName) {
        Preconditions.notNull(target, "target must not be null");
        final Field field = ReflectionUtils.findField(target.getClass(), fieldName);
        Preconditions.notNull(field, "no existing field with name " + fieldName);
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": "
                    + ex.getMessage());
        }
    }

    /**
     * Use to inject beans (or mocks) into already created bean. To use only in tests.
     * Intended use:
     * <pre>ReflectionUtil.inject(dependencyBean).in(targetBean);</pre>
     * <p/>
     * Limitation: There must be exactly one field of a dependencyBean class in targetBean class,
     * if there will be none or more than one then {@link IllegalStateException} will be thrown.
     */
    public static ReflectionInjector inject(Object value) {
        return new ReflectionInjector(value);
    }

    /**
     * Returns all the fields which are declared in specific class and
     * all of the base classes for this class
     */
    public static Field[] getAllFields(Class<?> clazz) {
        List<Class<?>> classes = getAllSuperclasses(clazz);
        classes.add(clazz);
        return getAllFields(classes);
    }

    /**
     * Returns all the constants of the given class
     */
    public static List getAllConstants(Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredFields())
                .stream()
                .filter(f -> isFinal(f.getModifiers()) && isStatic(f.getModifiers()))
                .map(f -> {
                    try {
                        return f.get(null);
                    } catch (IllegalAccessException e) {
                        log.error(e.getLocalizedMessage(), e);
                        throw new IllegalArgumentException(e.getLocalizedMessage());
                    }
                })
                .collect(Collectors.toList());
    }

    private static Field[] getAllFields(List<Class<?>> classes) {
        Set<Field> fields = new HashSet<>();
        for (Class<?> clazz : classes) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return fields.toArray(new Field[fields.size()]);
    }

    private static List<Class<?>> getAllSuperclasses(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        Class<?> superclass = clazz.getSuperclass();
        while (superclass != null) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return classes;
    }

    public static ParameterizedType findByRawType(Type[] genericInterfaces, Class<?> expectedRawType) {
        for (Type type : genericInterfaces) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parametrized = (ParameterizedType) type;
                if (expectedRawType.equals(parametrized.getRawType())) {
                    return parametrized;
                }
            }
        }
        throw new RuntimeException();
    }

    public static Class<?> findActualTypeArgumentByRawTypeDeclaredOn(Class<?> baseClass, Class<?> expectedRawType, Class<?> declaredOnClass) {
        Class<?> resolvedType = findActualTypeArgumentDeclaredOnClass(baseClass, expectedRawType);

        if (resolvedType != null) {
            return resolvedType;
        }

        List<Class<?>> resolvedTypes = Stream.of(ArrayUtils.add(baseClass.getInterfaces(), baseClass.getSuperclass()))
                .filter(Objects::nonNull)
                .map(Class.class::cast)
                .filter(declaredOnClass::isAssignableFrom)
                .map(superClass -> findActualTypeArgumentByRawType(superClass, expectedRawType))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (resolvedTypes.size() > 1) {
            throw new IllegalArgumentException(format(
                    "More than one type argument of type %s found in class hierarchy of %s",
                    expectedRawType.getName(), baseClass.getName()
            ));
        }

        if (CollectionUtils.isEmpty(resolvedTypes)) {
            return null;
        }

        return resolvedTypes.get(0);
    }

    public static Class<?> findActualTypeArgumentByRawType(Class<?> baseClass, Class<?> expectedRawType) {
        Class<?> resolvedType = findActualTypeArgumentDeclaredOnClass(baseClass, expectedRawType);

        if (resolvedType != null) {
            return resolvedType;
        }

        List<Class<?>> resolvedTypes = Stream.of(ArrayUtils.add(baseClass.getInterfaces(), baseClass.getSuperclass()))
                .filter(Objects::nonNull)
                .map(Class.class::cast)
                .map(superClass -> findActualTypeArgumentByRawType(superClass, expectedRawType))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (resolvedTypes.size() > 1) {
            throw new IllegalArgumentException(format(
                    "More than one type argument of type %s found in class hierarchy of %s",
                    expectedRawType.getName(), baseClass.getName()
            ));
        }

        if (CollectionUtils.isEmpty(resolvedTypes)) {
            return null;
        }

        return resolvedTypes.get(0);
    }

    private static Class<?> findActualTypeArgumentDeclaredOnClass(Class<?> clazz, Class<?> expectedRawType) {
        return Stream.of(ArrayUtils.add(clazz.getGenericInterfaces(), clazz.getGenericSuperclass()))
                .filter(Objects::nonNull)
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .flatMap(type -> Stream.of(type.getActualTypeArguments()))
                .filter(Class.class::isInstance)
                .map(Class.class::cast)
                .filter(expectedRawType::isAssignableFrom)
                .findFirst()
                .orElse(null);
    }

    public static Class<?> getGenericSelfTypeOrThrow(Class<?> selfClass, Class<?> typeClass) {
        Class<?> actualChecksType = ReflectionUtil.findActualTypeArgumentByRawTypeDeclaredOn(
                selfClass, typeClass, typeClass
        );
        if (actualChecksType != null && selfClass.isAssignableFrom(actualChecksType)) {
            return actualChecksType;
        } else {
            throw new IllegalStateException(String.format(
                    "The generic %s type declared on %s should be itself instead of %s", typeClass.getSimpleName(),
                    selfClass.getName(), Optional.ofNullable(actualChecksType).map(Class::getName).orElse(null)
            ));
        }
    }

}
