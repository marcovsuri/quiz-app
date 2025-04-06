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
   * @throws Exception if there is an error sending or receiving the message
   */
  public @NotNull String send(@NotNull T requestType, @NotNull JSONObject jsonObject)
      throws ServerException {
    // TODO - deal with errors
    try (Socket socket = new Socket(serverAddress, serverPort)) {
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

      // Send request headers
      out.println(requestType);

      // Send the JSON object
      out.println(jsonObject);

      // Receive the response from the server => read response
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      String responseStatus = in.readLine();
      if (responseStatus.equals("ERROR")) {
        throw new ServerException(in.readLine());
      }

      // Read the response
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = in.readLine()) != null) {
        response.append(line);
      }

      return response.toString();
    } catch (IOException e) {
      // TODO - deal with errors
      System.err.println("Error sending message: " + e.getMessage());
      return null;
    }
  }
}
