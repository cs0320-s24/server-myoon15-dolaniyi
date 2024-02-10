package edu.brown.cs.student;

import edu.brown.cs.student.main.search.CSVParser;
import edu.brown.cs.student.main.search.Coordinate;
import edu.brown.cs.student.main.utils.ArrayCreator;
import edu.brown.cs.student.main.utils.REPL;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class CSVTest {
  public CSVTest() {}

  /** Tests parse by checking equality on entire row */
  @Test
  public void testParse() throws FileNotFoundException {
    FileReader f =
        new FileReader(
            "/Users/matthewyoon/Documents/cs32/csv-ymatthew2466/data/census/income_by_race.csv");

    ArrayCreator arrCreator = new ArrayCreator();

    // create CSVParser here, taking in Reader and CreatorFromRow<T> to use type T
    // declare type String[] to avoid generics error
    CSVParser<String[]> parser = new CSVParser<>(f, arrCreator);
    List<String[]> m_data = parser.getElts();

    // checks number of rows
    Assert.assertEquals(m_data.size(), 324);

    // testing entire row equality
    ArrayList<String> reference =
        new ArrayList<>(
            Arrays.asList(
                "0",
                "Total",
                "2020",
                "2020",
                "84282",
                "2629",
                "\"Newport County",
                " RI\"",
                "05000US44005",
                "newport-county-ri"));
    Assert.assertEquals(Arrays.asList(m_data.get(3)), reference);

    // testing individual element
    String refElement = m_data.get(5)[4];
    Assert.assertEquals(refElement, "86970");
  }

  /** Tests basic case for all columns */
  @Test
  public void testSearchAllCols() throws FileNotFoundException {
    FileReader f =
        new FileReader(
            "/Users/matthewyoon/Documents/cs32/csv-ymatthew2466/data/census/income_by_race.csv");

    ArrayCreator arrCreator = new ArrayCreator();

    // create CSVParser here, taking in Reader and CreatorFromRow<T> to use type T
    // declare type String[] to avoid generics error
    CSVParser<String[]> parser = new CSVParser<>(f, arrCreator, "88147", "");
    List<String[]> m_data = parser.getElts();

    // reference list
    List<Coordinate> reference = new ArrayList<>();
    reference.add(new Coordinate(10, 4));

    // matching coordinates from parser
    List<Coordinate> matchesFromData = parser.getMatches();
    Assert.assertEquals(reference, matchesFromData);
  }

  /** Tests on the malformed CSV to check for parsing errors */
  @Test
  public void testMalformed() throws FileNotFoundException {
    FileReader f =
        new FileReader(
            "/Users/matthewyoon/Documents/cs32/csv-ymatthew2466/data/malformed/malformed_signs.csv");

    ArrayCreator arrCreator = new ArrayCreator();

    // create CSVParser here, taking in Reader and CreatorFromRow<T> to use type T
    // declare type String[] to avoid generics error
    CSVParser<String[]> parser = new CSVParser<>(f, arrCreator, "Nick", "");

    // reference list
    List<Coordinate> reference = new ArrayList<>();
    reference.add(new Coordinate(3, 2));

    // matching coordinates from parser
    List<Coordinate> matchesFromData = parser.getMatches();

    Assert.assertEquals(reference, matchesFromData);
  }

  /** Tests when searching for a nonexistant word */
  @Test
  public void doesNotExist() throws FileNotFoundException {
    FileReader f =
        new FileReader(
            "/Users/matthewyoon/Documents/cs32/csv-ymatthew2466/data/malformed/malformed_signs.csv");

    ArrayCreator arrCreator = new ArrayCreator();

    // create CSVParser here, taking in Reader and CreatorFromRow<T> to use type T
    // declare type String[] to avoid generics error
    CSVParser<String[]> parser = new CSVParser<>(f, arrCreator, "Johnson", "");

    // empty list
    List<Coordinate> reference = new ArrayList<>();

    // empty matching coordinates
    List<Coordinate> matchesFromData = parser.getMatches();

    Assert.assertEquals(reference, matchesFromData);
  }
  /** Test empty file returns empty data from parser */
  @Test
  public void empty() {
    StringReader sReader = new StringReader("");
    ArrayCreator arrCreator = new ArrayCreator();
    CSVParser<String[]> parser = new CSVParser<>(sReader, arrCreator);
    List<String[]> m_data = parser.getElts();

    Assert.assertEquals(m_data.size(), 0);
  }
  /** Test exception for search in REPL */
  @Test
  public void testSearch() {
    REPL test = new REPL("notdata/notCSV.jar", "", "");
    //    test.search
    Assert.assertThrows(FileNotFoundException.class, test::search);
  }
  /** Test interpret column method in */
  @Test
  public void testInterpretColumn() throws FileNotFoundException {
    FileReader f =
        new FileReader(
            "/Users/matthewyoon/Documents/cs32/csv-ymatthew2466/data/census/income_by_race.csv");

    ArrayCreator arrCreator = new ArrayCreator();

    CSVParser<String[]> parser = new CSVParser<>(f, arrCreator);
    Assert.assertEquals(parser.interpretCol("3"), 3);
    Assert.assertEquals(parser.interpretCol(""), -1);
    Assert.assertEquals(parser.interpretCol("Geography"), 6);
  }
}
