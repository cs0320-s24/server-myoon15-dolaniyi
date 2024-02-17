package edu.brown.cs.student.main.server;

public record FailResponse(String response_type) {
  public FailResponse() {
    this("error");
  }
}
