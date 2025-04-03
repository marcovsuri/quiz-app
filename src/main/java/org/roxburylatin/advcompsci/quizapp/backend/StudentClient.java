package org.roxburylatin.advcompsci.quizapp.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;

/**
 * A Student client interface that sends requests to the teacher's server and receives responses.
 */
public class StudentClient {
  private final String firstName;
  private final String lastName;
  private final String serverAddress;
  private final int serverPort;

  /**
   * Creates a new StudentClient with the given first name, last name, server address, and server
   * port.
   *
   * @param firstName the first name of the student
   * @param lastName the last name of the student
   * @param serverAddress the address of the server
   * @param serverPort the port number of the server
   */
  public StudentClient(String firstName, String lastName, String serverAddress, int serverPort) {
    this.firstName = firstName;
    this.lastName = lastName;
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
  public String send(RequestType requestType, JSONObject jsonObject) throws Exception {
    // TODO - deal with errors
    try (Socket socket = new Socket(serverAddress, serverPort)) {
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

      // Send request headers
      out.println(firstName);
      out.println(lastName);
      out.println(requestType);

      // Send the JSON object
      out.println(jsonObject.toString());

      // Receive the response from the server => read response
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      String responseStatus = in.readLine();
      if (responseStatus.equals("ERROR")) {
        throw new Exception(in.readLine());
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
