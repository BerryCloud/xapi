package dev.learning.xapi.client;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriBuilder;

/**
 * Base class for xAPI request.
 *
 * @author István Rátkai (Selindek)
 * @param <T> The type of the response body. Can be {@link Void} for responses without body.
 */
@SuperBuilder
@Getter
@RequiredArgsConstructor
abstract class Request {

  protected abstract UriBuilder url(UriBuilder uriBuilder, Map<String, Object> queryParams);

  /**
   * The request method.
   *
   * @return the request method as a {@link HttpMethod} object.
   */
  protected abstract HttpMethod getMethod();


}