package edu.brown.cs.student.main.interfaces;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DataSource {

  public String requestData(String state, String county)
      throws URISyntaxException, IOException, InterruptedException;
}
