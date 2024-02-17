package edu.brown.cs.student.main.search;

import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.interfaces.CreatorFromRow;
import edu.brown.cs.student.main.utils.ErrorLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.regex.Pattern;

public class CSVParser<T> {
  private final Reader file;
  private final CreatorFromRow<T> creator;
  private CSVSearcher searcher;

  private List<T> elts;

  private int numCols; // stores number of columns for index error-checking
  private String[] header; // stores header info for col searching

  private ErrorLogger logger;

  private boolean malformed = false;
  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  /**
   * Constructor
   *
   * @param file: CSV file as Reader
   * @param creator: need this to create T objects
   */
  public CSVParser(Reader file, CreatorFromRow<T> creator, String word, String col) {
    this.logger = new ErrorLogger();

    this.file = file;
    this.creator = creator;
    //        this.word = word;
    //        this.col = col;

    this.elts = new ArrayList<>();

    // parses CSV and populates this.elts
    this.parseCSV();

    // calls CSVSearcher b/c can't directly change this.elts b/c of type T
    // uses this.interpretCol to convert string col to int
    this.searcher = new CSVSearcher(this.elts, word, this.interpretCol(col));
  }

  /** Alternate constructor for testing AND Server purposes */
  public CSVParser(Reader file, CreatorFromRow<T> creator) {
    this.file = file;
    this.creator = creator;
    this.elts = new ArrayList<>();
    this.parseCSV();
  }

  /** Searches CSV for Server implementation */
  public CSVSearcher searchCSV(String word, String col) {
    //    CSVSearcher searcher = new CSVSearcher(this.elts, word, this.interpretCol(col));
    //
    //    List<Coordinate> matches = searcher.returnList();
    //    List<T> elements = getElts();
    //    List<List<String>> mut = new ArrayList<>();
    //
    //    String[][] out = new String[matches.size()][2];
    //
    //    for (int i = 0; i < matches.size(); i+=1){
    //
    //      int x = matches.get(i).getRow();
    //      int y = matches.get(i).getCol();
    //
    //      out[i] = elements.get(x);
    //      out[i][1] = elements.get(y).toString();
    //
    //    }

    return new CSVSearcher(this.elts, word, this.interpretCol(col));
  }

  /** Updates this.elts with CSV information */
  public void parseCSV() {
    // tracks if first row
    int headerCount = 0;
    int staticHeaderLength = -1;

    try (BufferedReader br = new BufferedReader(this.file)) {
      int headerLimit = 0;
      String brRow;
      while ((brRow = br.readLine()) != null) {
        try {
          /*
           * algorithm: when a quote is open (odd number), removes all commas inside
           * the element. This fixes erroneous regex splitting
           */
          String noCommas = "";
          int quoteCount = 0;
          for (int i = 0; i < brRow.length(); i += 1) {
            // increments quoteCount
            if (brRow.charAt(i) == '\"') {
              quoteCount += 1;
            }
            // if the quotes are open and currently comma
            if (quoteCount % 2 == 1 && brRow.charAt(i) == ',') {
              continue;
            } else {
              noCommas += "" + brRow.charAt(i);
            }
          }
          brRow = noCommas.replaceAll("\"", "");
          System.out.println(brRow);

          // stores the number of cols in row for index checking later
          this.numCols = Arrays.asList(regexSplitCSVRow.split(brRow)).size();
          // stores header as String[] for index calculation
          if (headerLimit == 0) {
            this.header = regexSplitCSVRow.split(brRow);
            headerLimit += 1;
          }

          String[] row = regexSplitCSVRow.split(brRow); /*.split(",");*/ // array of row
          // stores header length to detect malformed
          if (headerCount == 0) {
            staticHeaderLength = row.length;
            headerCount += 1;
          }

          // prints malformed rows
          //          if (row.length != staticHeaderLength){
          //            this.malformed = true;
          //            System.out.println("malformed row: "+ Arrays.toString(row));
          //          }

          // convert to ARRAY OF STRINGS, as type T
          T rowT = creator.create(Arrays.asList(regexSplitCSVRow.split(brRow)));
          this.elts.add(rowT);

        } catch (FactoryFailureException e) {
          // creator can't create T
          System.out.println("Error creating row: " + e.getMessage());
          this.logger.logError("Error creating row: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
      this.logger.logError("Error reading file: " + e.getMessage());
    }
  }

  /**
   * Takes in a string and interprets what the user means Between column index ; column name ; or
   * invalid column name
   */
  public int interpretCol(String col) {
    if (col.equals("")) {
      return -1;
    }
    // try: convert string to int
    try {
      int i = Integer.parseInt(col);
      // using stored numCols from parsing to error-check
      if (i > -1 && i < this.numCols) {
        return i;
      }
    }
    // col is a string -> assuming header name
    catch (NumberFormatException e) {
      // using stored header array to find index
      for (int i = 0; i < this.header.length; i += 1) {
        if (this.header[i].equalsIgnoreCase(col)) {
          return i;
        }
      }
    }
    return -2;
  }

  /** Getter method for all elements */
  public List<T> getElts() {
    return this.elts;
  }

  /** Getter method for all matches */
  public List<Coordinate> getMatches() {
    return this.searcher.returnList();
  }

  /** Returns true if CSV is malformed */
  public boolean isMalformed() {
    return this.malformed;
  }
}
