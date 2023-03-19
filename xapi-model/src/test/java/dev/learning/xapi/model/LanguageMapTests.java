/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.HashMap;
import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * LanguageMap Tests.
 */
@DisplayName("LanguageMap tests")
class LanguageMapTests {

  @ParameterizedTest
  @CsvSource({
    "en-GB, Colour",
    "en-US, Color",
    "de, Colour"})
  void givenUkAndUSKeyWhenGettingLocaleValueThenValueIsExpected(String locale, String expected) throws Exception {

    final LanguageMap languageMap = new LanguageMap();

    // Given UK and US Key
    languageMap.put(Locale.UK, "Colour");
    languageMap.put(Locale.US, "Color");

    // When Getting Locale Value
    final String value = languageMap.get(LanguageRange.parse(locale));

    // Then Value Is Expected
    assertThat(value, is(expected));

  }

  @Test
  void givenFranceGermanyandUSKeyWhenGettingUKValueThenValueIsEnglish() throws Exception {

    final LanguageMap languageMap = new LanguageMap();

    // Given France Germany and US Key
    languageMap.put(Locale.FRANCE, "Couleur");
    languageMap.put(Locale.GERMANY, "Farbe");
    languageMap.put(Locale.ENGLISH, "Color");

    // When Getting UK Value
    final String value = languageMap.get(LanguageRange.parse("en-GB"));

    // Then Value Is English
    assertThat(value, is("Color"));

  }

  @Test
  void givenFrenchAndEnglishKeyWhenGettingUSValueThenValueIsEnglish() throws Exception {

    final LanguageMap languageMap = new LanguageMap();

    // Given French And English Key
    languageMap.put(Locale.FRENCH, "Couleur");
    languageMap.put(Locale.ENGLISH, "Color");

    // When Getting US Value
    final String value = languageMap.get(LanguageRange.parse("en-US"));

    // Then Value Is English
    assertThat(value, is("Color"));

  }

  @Test
  void givenFrenchAndUsKeyWhenGettingCanadaFrenchValueThenValueIsFrench() throws Exception {

    final LanguageMap languageMap = new LanguageMap();

    // Given US Key And French
    languageMap.put(Locale.US, "Color");
    languageMap.put(Locale.FRENCH, "Couleur");

    // When Getting Canada French Value
    final String value = languageMap.get(LanguageRange.parse("fr-ca"));

    // Then Value Is French
    assertThat(value, is("Couleur"));

  }

  @Test
  void givenMapIsEmptyWhenGettingGermanValueThenValueIsNull() throws Exception {

    final LanguageMap languageMap = new LanguageMap();

    // Given Map Is Empty

    // When Getting German Value
    final String value = languageMap.get(LanguageRange.parse("de"));

    // Then Value Is Null
    assertNull(value);

  }

  @Test
  void givenUsAndUndKeyWhenGettingGermanValueThenValueIsUnd() throws Exception {

    final LanguageMap languageMap = new LanguageMap();

    // Given US And UND Key
    languageMap.put(Locale.US, "Color");
    languageMap.put(new Locale("und"), "Colour");

    // When Getting German Value
    final String value = languageMap.get(LanguageRange.parse("de"));

    // Then Value Is UND
    assertThat(value, is("Colour"));

  }

  @Test
  void givenUkAndUsKeyInMapUsedToConstructLanguageMapWhenGettingUKValueThenValueIsUK()
      throws Exception {

    final Map<Locale, String> map = new HashMap<>();
    map.put(Locale.UK, "Colour");
    map.put(Locale.US, "Color");

    // Given UK and US Key In Map Used To Construct LanguageMap
    final LanguageMap languageMap = new LanguageMap(map);

    // When Getting UK Value
    final String value = languageMap.get(LanguageRange.parse("en-GB"));

    // Then Value Is UK
    assertThat(value, is("Colour"));

  }

}
