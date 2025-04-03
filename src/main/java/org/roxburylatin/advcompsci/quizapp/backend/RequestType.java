package org.roxburylatin.advcompsci.quizapp.backend;

/** Enum representing the different types of requests that can be sent to the server. */
public enum RequestType {
  /** Request to get the quiz data for a specific chapter. */
  GET_QUIZ_DATA,
  /** Request to submit the quiz results for a specific chapter. */
  SUBMIT_QUIZ
}
