package edu.brown.cs.student.main.search;

import edu.brown.cs.student.main.utils.ErrorLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVSearcher {
  private List<String[]> elts;
  private String word;
  private int col;

  private ErrorLogger logger;

  // maintains a list of all found results
  private List<Coordinate> allMatches;

  public CSVSearcher(List elts, String word, int col) {
    this.logger = new ErrorLogger();

    this.elts = elts;
    this.word = word;
    this.col = col;

    // stores all found elements
    this.allMatches = new ArrayList<>();

    // populates this.allMatches
    this.searchCSV();

  }

  /** Goes through CSV and adds all matches to this.allMatches as Coordinate record class */
  public void searchCSV() {
    switch (this.col) {
        // case when invalid column is provided
      case -2:
        System.out.println("Error: invalid column");
        this.logger.logError("Error: invalid column");

        break;

        // no input for col is given (search all cols)
      case -1:
        for (int i = 0; i < this.elts.size(); i += 1) {
          String[] temp = this.elts.get(i);
          if (temp == null) continue;

          for (int j = 0; j < temp.length; j += 1) {
            if (temp[j] != null && temp[j].equalsIgnoreCase(this.word)) {
              this.allMatches.add(new Coordinate(i, j));
            }
          }
        }
        break;

        // specific column given
      default:
        for (int i = 0; i < this.elts.size(); i += 1) {
          String[] temp = this.elts.get(i);
          // skip if row is empty, or row is shorter than col we're searching
          if (temp == null || temp.length <= this.col) {
            continue;
          }

          String elt = temp[this.col];
          if (elt != null && elt.equalsIgnoreCase(this.word)) {
            this.allMatches.add(new Coordinate(i, this.col));
          }
        }
        break;
    }
  }

  public void printMatches() {
    if (this.allMatches.isEmpty()) {
      System.out.println("\nNo matches found");
    } else {
      System.out.println("\nTuple formatted as (row, col):");
      System.out.println(Arrays.toString(this.allMatches.toArray()));
    }
  }

  public List<Coordinate> returnList() {
    return this.allMatches;
  }
}
