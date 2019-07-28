package com.connectgroup;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static com.connectgroup.DataFilterer.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataFiltererTest {

    @Test
    public void shouldReturnEmptyCollection_WhenFilterByCountryIsAppliedToEmptyLogFile() throws FileNotFoundException {
        assertTrue(filterByCountry(openFile("src/test/resources/empty"), "GB").isEmpty());
    }

    @Test
    public void shouldReturnCorrectCollection_WhenFilterByCountryIsAppliedToSingleLineLogFile() throws FileNotFoundException {
        assertEquals(1, filterByCountry(openFile("src/test/resources/single-line"), "GB").size());
    }

    @Test
    public void shouldReturnCorrectCollection_WhenFilterByCountryIsAppliedToMultiLineLogFile() throws FileNotFoundException {
        assertEquals(3, filterByCountry(openFile("src/test/resources/multi-lines"), "US").size());
    }

    @Test
    public void shouldReturnEmptyCollection_WhenAverageResponseTimeFilterIsAppliedToEmptyLogFile() throws FileNotFoundException {
        assertTrue(filterByCountry(openFile("src/test/resources/empty"), "GB").isEmpty());
    }

    @Test
    public void shouldReturnCorrectCollection_WhenAverageResponseTimeFilterIsAppliedToSingleLineLogFile() throws FileNotFoundException {
        assertTrue(filterByResponseTimeAboveAverage(openFile("src/test/resources/single-line")).isEmpty());
    }

    @Test
    public void shouldReturnCorrectCollection_WhenAverageResponseTimeFilterIsAppliedToMultiLineLogFile() throws FileNotFoundException {
        assertEquals(3, filterByResponseTimeAboveAverage(openFile("src/test/resources/multi-lines")).size());
    }

    @Test
    public void shouldReturnEmptyCollection_WhenCountryAndResponseTimeFilterIsAppliedToEmptyLogFile() throws FileNotFoundException {
        assertTrue(filterByCountryWithResponseTimeAboveLimit(openFile("src/test/resources/empty"), "GB", 0).isEmpty());
    }

    @Test
    public void shouldReturnCorrectCollection_WhenCountryAndResponseTimeFilterIsAppliedToSingleLineLogFile() throws FileNotFoundException {
        assertEquals(1, filterByCountryWithResponseTimeAboveLimit(openFile("src/test/resources/single-line"), "GB", 0).size());
    }

    @Test
    public void shouldReturnCorrectCollection_WhenCountryAndResponseTimeFilterIsAppliedToMultiLineLogFile() throws FileNotFoundException {
        assertEquals(2, filterByCountryWithResponseTimeAboveLimit(openFile("src/test/resources/multi-lines"), "US", 539).size());
    }

    private FileReader openFile(String filename) throws FileNotFoundException {
        return new FileReader(new File(filename));
    }

    //TODO: test RequestLog and RequestLogReader
}
