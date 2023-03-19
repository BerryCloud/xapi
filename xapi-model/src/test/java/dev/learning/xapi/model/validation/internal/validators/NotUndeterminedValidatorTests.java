/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model.validation.internal.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * NotUndeterminedValidator Tests.
 *
 * @author István Rátkai (Selindek)
 */
@DisplayName("NotUndeterminedValidator tests")
class NotUndeterminedValidatorTests {

  private static final NotUndeterminedValidator validator = new NotUndeterminedValidator();

  @Test
  void whenValueIsNullThenResultIsTrue() {

    // When Value Is Null
    var result = validator.isValid(null, null);

    // Then Result Is True
    assertTrue(result);
  }

  @Test
  void whenValueIsUndeterminedLocaleThenResultIsFalse()
      throws NoSuchFieldException, SecurityException {

    // When Value Is Undetermined Locale
    var result = validator.isValid(Locale.forLanguageTag("und"), null);

    // Then Result Is False
    assertFalse(result);
  }

  @Test
  void whenValueIsNotUndeterminedLocaleThenResultIsTrue()
      throws NoSuchFieldException, SecurityException {

    // When Value Is Not Undetermined Locale
    var result = validator.isValid(Locale.forLanguageTag("en-US"), null);

    // Then Result Is True
    assertTrue(result);
  }

}
