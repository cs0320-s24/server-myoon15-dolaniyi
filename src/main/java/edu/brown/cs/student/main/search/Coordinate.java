package edu.brown.cs.student.main.search;

public record Coordinate(int row, int col) {
  @Override
  public String toString() {
    //        return "(row: " + row + ", col: " + col +")";
    return "(" + row + ", " + col + ")";
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }
}
