package edu.brown.cs.student.main.search;

public record Coordinate(int row, int col) {
  @Override
  public String toString() {
    //        return "(row: " + row + ", col: " + col +")";
    return "(" + row + ", " + col + ")";
  }
}
