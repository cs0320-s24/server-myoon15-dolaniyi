package edu.brown.cs.student.main.server;

public record SearchFail(String response_type) {
  public SearchFail() {
    this("error");
  }
}
