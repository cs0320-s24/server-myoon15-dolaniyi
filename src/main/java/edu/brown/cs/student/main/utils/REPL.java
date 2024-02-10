package edu.brown.cs.student.main.utils;

import edu.brown.cs.student.main.search.CSVParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class REPL {
  private String filePath; // filepath from user input
  private String word; //
  private String col;

  private ErrorLogger logger;

  public REPL() throws FileNotFoundException {
    // instantiate ErrorLogger object to write errors to ErrorLog.txt
    this.logger = new ErrorLogger();

    // load-up info
    this.init();

    // prompt for users
    this.promptCommandLine();

    // search method
    this.search();
  }

  /** Constructor only for testing purposes */
  public REPL(String path, String word, String col) {
    this.logger = new ErrorLogger();

    this.filePath = path;
    this.word = word;
    this.col = col;
  }

  /** Prints welcome message */
  public void init() {
    System.out.println("CSV Searcher");
    System.out.println("Type \"exit\" to exit at any time.");
    System.out.println("(Assumes capitalization does NOT matter)\n");
  }

  /** Prompts the user for information and stores as instance vars */
  public void promptCommandLine() {
    Scanner input = new Scanner(System.in);
    this.filePath = getFilePath(input);
    this.word = this.getWord(input);
    this.col = this.getCol(input);
  }

  /** Only for testing purposes */

  /** Creates CSV parser object, which initiates the parsing and searching */
  public void search() throws FileNotFoundException {
    try {
      FileReader f = new FileReader(this.filePath);
      // what i want to do: create parser; store CSV as arraylist of arrays; send this into Searcher
      // send FileReader into parser
      ArrayCreator arrCreator = new ArrayCreator();

      // create CSVParser here, taking in Reader and CreatorFromRow<T> to use type T
      // calls CSVSearcher from CSVParser constructor
      new CSVParser(f, arrCreator, this.word, this.col);

    } catch (FileNotFoundException e) {
      System.out.println("File not found: " + e.getMessage());
      this.logger.logError("File not found: " + e.getMessage());
      throw new FileNotFoundException("");
    }
  }

  /**
   * Prompts user for CSV filepath and error checks for correct directory and filetype Also checks
   * for invalid file
   */
  public String getFilePath(Scanner input) {
    while (true) {
      System.out.println(
          "Input file path of CSV\n(directory must start with \"data/\" and end in \".csv\"): ");
      String fPath = input.nextLine();

      // check if special command
      if (this.checkExit(fPath)) {
        return null;
      }
      try {
        if (fPath.startsWith("data/") && fPath.toLowerCase().endsWith(".csv")) {
          new FileReader(fPath).close(); // checks if file exists
          return fPath;
        } else {
          System.out.println(
              "Invalid file path syntax! Ensure path starts with \"data/\" and ends with \".csv\"\n\nTry again:");
          this.logger.logError("Invalid file path: " + fPath + ". Please ensure file exists. ");
        }
      } catch (IllegalArgumentException | IOException e) {
        this.logger.logError(
            "Invalid file path: " + fPath + ". Please ensure file exists. " + e.getMessage());
        System.out.println("Invalid file path. Please ensure file exists\n\nTry again:");
      }
    }
  }

  /** Takes user input for searchable word */
  public String getWord(Scanner input) {
    while (true) {
      System.out.println("Input word to be searched: ");
      this.word = input.nextLine();
      if (this.checkExit(this.word) || !this.word.equals("")) {
        return this.word;
      } else {
        System.out.println("Enter a valid word: ");
      }
    }
  }

  /** Prompts user for column number or column header, or empty for search all */
  public String getCol(Scanner input) {
    while (true) {
      System.out.println("Input column (index or name), or press \"enter\" to search all cols");
      this.col = input.nextLine();
      if (this.checkExit(col)) {
        return this.col;
      }
      return this.col;
    }
  }

  /** Checks if the input string is "exit," and if so, exits program */
  public boolean checkExit(String input) {
    if ("exit".equalsIgnoreCase(input)) {
      System.exit(0);
      return true;
    }
    return false;
  }
}
