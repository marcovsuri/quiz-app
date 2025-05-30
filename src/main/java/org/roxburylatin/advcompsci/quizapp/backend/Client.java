package org.roxburylatin.advcompsci.quizapp.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * A generic client class that sends requests to a server and receives responses.
 *
 * @param <T> The enum type representing different request types the client can handle
 * @see Server
 */
public class Client<T extends Enum<T>> {
  private final @NotNull String serverAddress;
  private final int serverPort;

  /**
   * Constructs a new Client instance with the specified server address and port.
   *
   * @param serverAddress the address of the server
   * @param serverPort the port number of the server
   */
  public Client(@NotNull String serverAddress, int serverPort) {
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
  }

  /**
   * Sends a request to the server and receives a response.
   *
   * @param requestType the type of request to send
   * @param jsonObject the JSON object to send
   * @return the response from the server
   * @throws ServerException if there is an error sending or receiving the message
   */
  public @NotNull String send(@NotNull T requestType, @NotNull JSONObject jsonObject)
      throws ServerException {
    try (Socket socket = new Socket(serverAddress, serverPort)) {
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

      // Send request headers
      out.println(requestType);

      // Send the JSON object
      out.println(jsonObject);

      // Receive the response from the server => read response
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      String responseStatus = in.readLine();
      if (responseStatus == null) {
        throw new ServerException("Server closed connection unexpectedly (no status line).");
      }

      if (responseStatus.equals("ERROR")) {
        String errorMessage = in.readLine();
        if (errorMessage == null) {
          throw new ServerException("Server reported error but gave no message.");
        }
        throw new ServerException(errorMessage);
      }

      // Otherwise read full response
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = in.readLine()) != null) {
        response.append(line).append('\n');
      }

      return response.toString();
    } catch (IOException e) {
      throw new ServerException("Unable to connect to server");
    }
  }
}
