package com.xsage.xsagecreditmatchservice.shared.validation.domain.annotation;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * A component that is a {@link com.xsage.xsagedebitservice.xsage.shared.validation.domain.DomainValidator} implementation.
 * <p>
 * A domain validator is a component that can be used to start a chain of domain checks,
 * validating domain constraints.
 * <p>
 * This is a utility annotation that marks the class as a component and enables read-only transactions for all public methods.
 *
 * @see Component
 * @see Transactional
 */
@Component
@Transactional(readOnly = true)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DomainValidator {
}
