/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * Score Tests.
 * 
 * @author Lukáš Sahula
 * @author Martin Myslik
 */
@DisplayName("Score tests")
class ScoreTests {

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Test
  void whenSerializingScoreThenResultIsInstanceOfScore() throws IOException {

    final File file = ResourceUtils.getFile("classpath:Score/Score.json");

    // When Serializing Score
    final Score result = objectMapper.readValue(file, Score.class);

    // Then Result Is Instance Of Score
    assertThat(result, instanceOf(Score.class));

  }

  @Test
  void whenSerializingScoreThenScaledIsExpected() throws IOException {

    final File file = ResourceUtils.getFile("classpath:Score/Score.json");

    // When Serializing Score
    final Score result = objectMapper.readValue(file, Score.class);

    // Then Scaled Is Expected
    assertThat(result.getScaled(), is(1.0F));

  }

  @Test
  void whenSerializingScoreThenRawIsExpected() throws IOException {

    final File file = ResourceUtils.getFile("classpath:Score/Score.json");

    // When Serializing Score
    final Score result = objectMapper.readValue(file, Score.class);

    // Then Raw Is Expected
    assertThat(result.getRaw(), is(1.0F));

  }

  @Test
  void whenSerializingScoreThenMinIsExpected() throws IOException {

    final File file = ResourceUtils.getFile("classpath:Score/Score.json");

    // When Serializing Score
    final Score result = objectMapper.readValue(file, Score.class);

    // Then Min Is Expected
    assertThat(result.getMin(), is(0.0F));

  }

  @Test
  void whenSerializingScoreThenMaxIsExpected() throws IOException {

    final File file = ResourceUtils.getFile("classpath:Score/Score.json");

    // When Serializing Score
    final Score result = objectMapper.readValue(file, Score.class);

    // Then Max Is Expected
    assertThat(result.getMax(), is(5.0F));

  }

  @Test
  void whenSerializingScoreThenResultIsEqualToExpectedJson() throws IOException {

    final Score score = Score.builder()

        .max(5.0f)

        .min(0.0f)

        .raw(1.0f)

        .scaled(1.0f)

        .build();

    // When Serializing Score
    final JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(score));

    // Then Result Is Equal To Expected Json
    assertThat(result,
        is(objectMapper.readTree(ResourceUtils.getFile("classpath:score/score.json"))));

  }

  @Test
  void whenCallingToStringThenResultIsExpected() throws IOException {

    final Score score =
        objectMapper.readValue(ResourceUtils.getFile("classpath:Score/Score.json"), Score.class);

    // When Calling ToString
    final String result = score.toString();

    // Then Result Is Expected
    assertThat(result, is("Score(scaled=1.0, raw=1.0, min=0.0, max=5.0)"));

  }

}
