package edu.brown.cs.student.main.interfaces;

import java.io.IOException;
import java.net.URISyntaxException;


public interface Broadband {
  String sendRequest(String stateID, String countyID)
      throws URISyntaxException, IOException, InterruptedException;
}
