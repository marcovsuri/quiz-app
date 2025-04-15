package org.roxburylatin.advcompsci.quizapp.backend;

/** A custom exception class for server-related errors. */
public class ServerException extends Exception {
  /**
   * Constructs a new ServerException with the specified error message.
   *
   * @param message The error message describing the exception
   */
  public ServerException(String message) {
    super(message);
  }
}
