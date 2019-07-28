package com.connectgroup;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class DataFilterer {
    public static Collection<?> filterByCountry(Reader source, String country) {
        return stream(getRequestLogs(source))
            .filter((requestLog) -> {
                return requestLog.getCountry().equals(country);
            })
            .collect(toList());
    }

    public static Collection<?> filterByCountryWithResponseTimeAboveLimit(Reader source, String country, long limit) {
        return stream(getRequestLogs(source))
            .filter((requestLog) -> {
                return requestLog.getCountry().equals(country) && requestLog.getResponseTime() > limit;
            })
            .collect(toList());
    }

    public static Collection<?> filterByResponseTimeAboveAverage(Reader source) {
        RequestLog[] requestLogs = getRequestLogs(source);
        int totalResponseTime = stream(requestLogs)
            .mapToInt((requestLog) -> requestLog.getResponseTime())
            .sum();
        int averageResponseTime = totalResponseTime / requestLogs.length;
        return stream(requestLogs)
            .filter((requestLog) -> {
                return requestLog.getResponseTime() > averageResponseTime;
            })
            .collect(toList());
    }

    private static RequestLog[] getRequestLogs(Reader source) {
        try {
            return new RequestLogReader(source).get();
        } catch (IOException e) {
            //TODO: make this a checked exception (not allowed to change original signatures)
            throw new RuntimeException(e);
        }
    }


    //TODO: move to separate class
    public static class RequestLog {

        private long timestamp;

        private String country;

        private int responseTime;

        public RequestLog(long timestamp, String country, int responseTime) {
            this.timestamp = timestamp;
            this.country = country;
            this.responseTime = responseTime;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getCountry() {
            return country;
        }

        public int getResponseTime() {
            return responseTime;
        }
    }

    //TODO: move to separate class
    public static class RequestLogReader {

        private static final String COLUMN_DELIMITER = ",";


        private Reader source;

        public RequestLogReader(Reader source) {
            this.source = source;
        }

        public RequestLog[] get() throws IOException {
            Scanner scanner = new Scanner(source);
            List<RequestLog> requestLogs = new ArrayList<RequestLog>(4);

            //skip headers if they exist
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while(scanner.hasNextLine()) {
                String[] columns = scanner.nextLine().split(COLUMN_DELIMITER);
                requestLogs.add(new RequestLog(parseLong(columns[0]), columns[1], parseInt(columns[2])));
            }

            scanner.close();

            return requestLogs.toArray(new RequestLog[requestLogs.size()]);
        }

    }

}