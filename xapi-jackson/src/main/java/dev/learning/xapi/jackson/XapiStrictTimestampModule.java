/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.learning.xapi.jackson.deserializers.StrictTimestampDeserializer;
import java.time.Instant;

/**
 * xAPI JSON module for registering custom deserializer {@link Instant} objects.
 *
 * @author István Rátkai (Selindek)
 */
public class XapiStrictTimestampModule extends SimpleModule {

  private static final long serialVersionUID = 8667729487482112691L;

  /**
   * XapiStrictTimestampModule constructor. Adds custom {@link StrictTimestampDeserializer} to the
   * ObjectMapper.
   */
  public XapiStrictTimestampModule() {
    super("xApi Strict Timestamp Module");

    addDeserializer(Instant.class, new StrictTimestampDeserializer());

  }
}
