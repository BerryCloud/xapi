package dev.learning.xapi.model.validation.internal.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.net.URI;
import org.junit.jupiter.api.Test;

class HasSchemeValidatorForUriTests {

  private HasSchemeValidatorForUri constraintValidator = new HasSchemeValidatorForUri();

  @Test
  void whenCallingIsValidWithURIWithoutSchemeThenResultIsFalse() {

    // When Calling IsValid With URI Without Scheme
    boolean valid = constraintValidator.isValid(URI.create("example.com"), null);

    // Then Result Is False
    assertThat(valid, is(false));
  }

  @Test
  void whenCallingIsValidWithURIWithSchemeThenResultIsTrue() {

    // When Calling IsValid With URI With Scheme
    boolean result = constraintValidator.isValid(URI.create("https://example.com"), null);

    // Then Result Is True
    assertThat(result, is(true));
  }

}
