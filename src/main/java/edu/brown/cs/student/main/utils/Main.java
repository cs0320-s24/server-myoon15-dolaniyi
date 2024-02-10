package edu.brown.cs.student.main.utils;

import java.io.FileNotFoundException;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    new Main(args).run();
  }

  private Main(String[] args) {}

  private void run() throws FileNotFoundException {
    REPL advanceToGoCollect200 = new REPL();
    //    ErrorLogger test = new ErrorLogger();
    //    test.logError("hi");
  }
}
