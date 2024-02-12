package edu.brown.cs.student.main.utils;

public class CountyData {
  private String NAME;
  private String S2802_C03_022E;
  private String state;
  private String county;

  public CountyData() {}

  @Override
  public String toString() {
    return this.NAME + ", " + this.S2802_C03_022E + ", " + this.state + ", " + this.county;
  }
}
