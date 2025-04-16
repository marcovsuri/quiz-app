package org.roxburylatin.advcompsci.quizapp.backend;

import org.json.JSONObject;

/**
 * A functional interface for handling client requests in the server. Implementations of this
 * interface process JSON request data and return response strings.
 */
@FunctionalInterface
public interface RequestHandler {
  /**
   * Handles a client request.
   *
   * @param data The JSONObject data representing the request
   * @return The response string to be sent to the client
   * @throws ServerException If an error occurs while handling the request
   */
  String handle(JSONObject data) throws ServerException;
}
