package edu.brown.cs.student.main.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ErrorLogger {
  private static final String FILE_PATH = "ErrorLog.txt";

  public ErrorLogger() {}

  /**
   * Writes to ErrorLog.txt for any errors that happened during runtime
   *
   * @param errorMessage String error message
   */
  public void logError(String errorMessage) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
      // 'true' in the FileWriter constructor appends to the existing file
      writer.println("Error: " + errorMessage);
    } catch (IOException e) {
      // Handle exception if unable to write to the file
      e.printStackTrace();
    }
  }
}
