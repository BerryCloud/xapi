/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.client;

import dev.learning.xapi.model.StatementFormat;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Request for getting a Statement.
 *
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Communication.md#213-get-statements">GET
 *      Statements</a>
 *
 * @author Thomas Turrell-Croft
 */
@Builder
@Getter
public class GetStatementRequest implements Request {

  @NonNull
  private final UUID id;

  private final StatementFormat format;

  private final Boolean attachments;

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.GET;
  }

  @Override
  public UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams) {

    return uriBuilder.path("statements")

        .queryParam("statementId", id)

        .queryParamIfPresent("format", Optional.ofNullable(format))

        .queryParamIfPresent("attachments", Optional.ofNullable(attachments));

  }

  /**
   * Builder for GetStatementRequest.
   */
  public static class Builder {

    /**
     * Sets the id.
     *
     * @param id The id of the GetStatementRequest.
     *
     * @return This builder
     *
     * @see GetStatementRequest#id
     */
    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    /**
     * Sets the id.
     *
     * @param id The id of the GetStatementRequest.
     *
     * @return This builder
     *
     * @see GetStatementRequest#id
     */
    public Builder id(String id) {
      this.id = UUID.fromString(id);
      return this;
    }

    // This static class extends the lombok builder.

  }

}
