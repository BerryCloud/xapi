/*
 * Copyright 2016rue-2023 Berry Cloud Ltd. All rights reserved.
 */
package dev.learning.xapi.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import dev.learning.xapi.model.Activity;
import dev.learning.xapi.model.Agent;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.SubStatement;
import dev.learning.xapi.model.Verb;
import java.net.URI;
import java.time.Instant;
import java.util.Locale;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * XapiClient Tests.
 *
 * @author Thomas Turrell-Croft
 */
@DisplayName("XapiClient Tests")
@SpringBootTest
class XapiClientMultipartTests {

  @Autowired
  private WebClient.Builder webClientBuilder;

  private MockWebServer mockWebServer;
  private XapiClient client;

  @BeforeEach
  void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    webClientBuilder.baseUrl(mockWebServer.url("").toString());

    client = new XapiClient(webClientBuilder);

  }

  @AfterEach
  void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void whenPostingStatementWithAttachmentThenContentTypeHeaderIsMultipartMixed()
      throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statement With Attachment
    client.postStatement(
        r -> r.statement(s -> s.actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .addAttachment(a -> a.content("Simple attachment").length(17).contentType("text/plain")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))
                .addDisplay(Locale.ENGLISH, "text attachment"))

            .verb(Verb.ATTEMPTED)

            .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Content Type Header Is Multipart Mixed
    assertThat(recordedRequest.getHeader("content-type"), startsWith("multipart/mixed"));
  }

  @Test
  void whenPostingStatementWithTextAttachmentThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statement With Text Attachment
    client.postStatement(
        r -> r.statement(s -> s.actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .addAttachment(a -> a.content("Simple attachment").length(17).contentType("text/plain")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))
                .addDisplay(Locale.ENGLISH, "text attachment"))

            .verb(Verb.ATTEMPTED)

            .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    final var boundary = "--" + recordedRequest.getHeader("content-type").substring(25);

    assertThat(recordedRequest.getBody().readUtf8(), is(boundary
        + "\r\nContent-Type: application/json\r\nContent-Length: 531\r\n\r\n{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/text\",\"display\":{\"en\":\"text attachment\"},\"contentType\":\"text/plain\",\"length\":17,\"sha2\":\"b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\"}]}\r\n"
        + boundary
        + "\r\nContent-Type: text/plain\r\nContent-Transfer-Encoding: binary\r\nX-Experience-API-Hash: b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\r\n\r\nSimple attachment\r\n"
        + boundary + "--\r\n"));
  }

  @Test
  void whenPostingStatementWithBinaryAttachmentThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statement With Binary Attachment
    client.postStatement(
        r -> r.statement(s -> s.actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .addAttachment(a -> a.content(new byte[] {64, 65, 66, 67, 68, (byte) 255}).length(6)
                .contentType("application/octet-stream")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/code"))
                .addDisplay(Locale.ENGLISH, "binary attachment"))

            .verb(Verb.ATTEMPTED)

            .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    final var boundary = "--" + recordedRequest.getHeader("content-type").substring(25);

    assertThat(recordedRequest.getBody().readUtf8(), is(boundary
        + "\r\nContent-Type: application/json\r\nContent-Length: 546\r\n\r\n{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"sha2\":\"0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\"}]}\r\n"
        + boundary
        + "\r\nContent-Type: application/octet-stream\r\nContent-Transfer-Encoding: binary\r\nX-Experience-API-Hash: 0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\r\n\r\n@ABCD�\r\n"
        + boundary + "--\r\n"));
  }

  @Test
  void whenPostingStatementWithoutAttachmentDataThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statement Without Attachment Data
    client.postStatement(
        r -> r.statement(s -> s.actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .addAttachment(a -> a.length(6).contentType("application/octet-stream")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/code"))
                .fileUrl(URI.create("example.com/attachment"))
                .addDisplay(Locale.ENGLISH, "binary attachment"))

            .verb(Verb.ATTEMPTED)

            .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
        .block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    assertThat(recordedRequest.getBody().readUtf8(), is(
        "{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"fileUrl\":\"example.com/attachment\"}]}"));
  }

  @Test
  void whenPostingSubStatementWithTextAttachmentThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting SubStatement With Text Attachment
    client.postStatement(r -> r.statement(s -> s
        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.ABANDONED)

        .object(SubStatement.builder()

            .actor(Agent.builder().name("A N Other").mbox("mailto:another@example.com").build())

            .verb(Verb.ATTENDED)

            .object(Activity.builder().id("https://example.com/activity/simplestatement")
                .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")).build())

            .addAttachment(a -> a.content("Simple attachment").length(17).contentType("text/plain")
                .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))
                .addDisplay(Locale.ENGLISH, "text attachment"))

            .build())

    )).block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    final var boundary = "--" + recordedRequest.getHeader("content-type").substring(25);

    assertThat(recordedRequest.getBody().readUtf8(), is(boundary
        + "\r\nContent-Type: application/json\r\nContent-Length: 742\r\n\r\n{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"https://w3id.org/xapi/adl/verbs/abandoned\",\"display\":{\"und\":\"abandoned\"}},\"object\":{\"objectType\":\"SubStatement\",\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attended\",\"display\":{\"und\":\"attended\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/text\",\"display\":{\"en\":\"text attachment\"},\"contentType\":\"text/plain\",\"length\":17,\"sha2\":\"b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\"}]}}\r\n"
        + boundary
        + "\r\nContent-Type: text/plain\r\nContent-Transfer-Encoding: binary\r\nX-Experience-API-Hash: b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\r\n\r\nSimple attachment\r\n"
        + boundary + "--\r\n"));
  }

  @Test
  void whenPostingStatementsWithAttachmentsThenBodyIsExpected() throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    // When Posting Statements With Attachments
    final var statement1 = Statement.builder()

        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .addAttachment(a -> a.content(new byte[] {64, 65, 66, 67, 68, (byte) 255}).length(6)
            .contentType("application/octet-stream")
            .usageType(URI.create("http://adlnet.gov/expapi/attachments/code"))
            .addDisplay(Locale.ENGLISH, "binary attachment"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))

        .build();

    final var statement2 = Statement.builder()

        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .addAttachment(a -> a.content(new byte[] {64, 65, 66, 67, 68, (byte) 255}).length(6)
            .contentType("application/octet-stream")
            .usageType(URI.create("http://adlnet.gov/expapi/attachments/code"))
            .addDisplay(Locale.ENGLISH, "binary attachment"))

        .addAttachment(a -> a.content("Simple attachment").length(17).contentType("text/plain")
            .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))
            .addDisplay(Locale.ENGLISH, "text attachment"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))

        .build();

    // When posting Statements
    client.postStatements(r -> r.statements(statement1, statement2)).block();

    final var recordedRequest = mockWebServer.takeRequest();

    // Then Body Is Expected
    final var boundary = "--" + recordedRequest.getHeader("content-type").substring(25);

    assertThat(recordedRequest.getBody().readUtf8(), is(boundary
        + "\r\nContent-Type: application/json\r\nContent-Length: 1301\r\n\r\n[{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"sha2\":\"0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\"}]},{\"actor\":{\"objectType\":\"Agent\",\"name\":\"A N Other\",\"mbox\":\"mailto:another@example.com\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/attempted\",\"display\":{\"und\":\"attempted\"}},\"object\":{\"objectType\":\"Activity\",\"id\":\"https://example.com/activity/simplestatement\",\"definition\":{\"name\":{\"en\":\"Simple Statement\"}}},\"attachments\":[{\"usageType\":\"http://adlnet.gov/expapi/attachments/code\",\"display\":{\"en\":\"binary attachment\"},\"contentType\":\"application/octet-stream\",\"length\":6,\"sha2\":\"0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\"},{\"usageType\":\"http://adlnet.gov/expapi/attachments/text\",\"display\":{\"en\":\"text attachment\"},\"contentType\":\"text/plain\",\"length\":17,\"sha2\":\"b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\"}]}]\r\n"
        + boundary
        + "\r\nContent-Type: application/octet-stream\r\nContent-Transfer-Encoding: binary\r\nX-Experience-API-Hash: 0f4b9b79ad9e0572dbc7ce7d4dd38b96dc66d28ca87d7fd738ec8f9a30935bf6\r\n\r\n@ABCD�\r\n"
        + boundary
        + "\r\nContent-Type: text/plain\r\nContent-Transfer-Encoding: binary\r\nX-Experience-API-Hash: b154d3fd46a5068da42ba05a8b9c971688ab5a57eb5c3a0e50a23c42a86786e5\r\n\r\nSimple attachment\r\n"
        + boundary + "--\r\n"));


  }

  @Test
  void whenPostingStatementsWithTimestampAndAttachmentThenNoExceptionIsThrown()
      throws InterruptedException {

    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 200 OK")
        .setBody("[\"19a74a3f-7354-4254-aa4a-1c39ab4f2ca7\"]")
        .setHeader("Content-Type", "application/json"));

    final var statement = Statement.builder()

        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

        .verb(Verb.ATTEMPTED)

        .activityObject(o -> o.id("https://example.com/activity/simplestatement"))

        .addAttachment(a -> a.content(new byte[] {64, 65, 66, 67, 68, 69}).length(6)
            .contentType("application/octet-stream")
            .usageType(URI.create("http://example.com/attachment"))
            .addDisplay(Locale.ENGLISH, "binary attachment"))

        .timestamp(Instant.now())

        .build();

    // When Posting Statements With Timestamp And Attachment

    // Then No Exception Is Thrown
    assertDoesNotThrow(() -> client.postStatements(r -> r.statements(statement)).block());

  }



}
