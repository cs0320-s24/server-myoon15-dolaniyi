package edu.brown.cs.student.main.utils;

import edu.brown.cs.student.main.interfaces.CreatorFromRow;
import java.util.List;

public class ArrayCreator implements CreatorFromRow<String[]> {
  @Override
  public String[] create(List<String> row) {
    return row.toArray(new String[0]);
  }
}
