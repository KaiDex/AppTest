package task01;

import com.sun.istack.internal.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemporalRows {

    @NotNull
    private Map<LocalDateTime, Object> row_1;

    //for testing purposes
    public static void main(String[] args) {
        TemporalRows rows = new TemporalRows();
        rows.row_1 = new TreeMap<>();
        rows.row_1.put(LocalDateTime.of(2018, 3, 1, 0, 0), 1);
        rows.row_1.put(LocalDateTime.of(2018, 2, 1, 0, 0), 2);
        rows.row_1.put(LocalDateTime.of(2018, 5, 1, 0, 0), 3);
        rows.row_1.put(LocalDateTime.of(2018, 3, 1, 0, 0), 4);
        rows.row_1.put(LocalDateTime.of(2018, 1, 1, 0, 0), 5);

        Map<LocalDateTime, Object> row_2 = new HashMap<>();
        row_2.put(LocalDateTime.of(2018, 7, 1, 0, 0), 6);
        row_2.put(LocalDateTime.of(2018, 4, 1, 0, 0), 7);
        row_2.put(LocalDateTime.of(2018, 8, 1, 0, 0), 8);
        row_2.put(LocalDateTime.of(2018, 3, 1, 0, 0), 9);
        row_2.put(LocalDateTime.of(2018, 1, 1, 0, 0), 10);

        Map<LocalDateTime, Object> naiveMap = rows.mergeRowsNaive(row_2);
        Map<LocalDateTime, Object> streamMap = rows.mergeRowsWithStreams(row_2);
        System.out.println(naiveMap.equals(streamMap));
        System.out.println(naiveMap.size() == 6);
        System.out.println(naiveMap.containsKey(LocalDateTime.of(2018, 1, 1, 0, 0)));
        System.out.println(naiveMap.containsKey(LocalDateTime.of(2018, 2, 1, 0, 0)));
        System.out.println(naiveMap.containsKey(LocalDateTime.of(2018, 3, 1, 0, 0)));
        System.out.println(naiveMap.containsKey(LocalDateTime.of(2018, 4, 1, 0, 0)));
        System.out.println(naiveMap.containsKey(LocalDateTime.of(2018, 5, 1, 0, 0)));
        System.out.println(naiveMap.containsKey(LocalDateTime.of(2018, 7, 1, 0, 0)));
    }

    //Stream method is appropriate for large chunk of data due to its parallel nature
    public Map<LocalDateTime, Object> mergeRowsWithStreams(Map<LocalDateTime, Object> row_2) {
        //if no values of row_2 present we cannot determine the time interval
        if (row_2 == null || row_2.entrySet().size() == 0) {
            return Collections.emptyMap();
        }
        //we already know row_2 is not empty
        ArrayList<LocalDateTime> dateTimeArray = new ArrayList<>(row_2.keySet());
        LocalDateTime firstElement = dateTimeArray.get(0);
        LocalDateTime lastElement = dateTimeArray.get(dateTimeArray.size() - 1);

        //We don't know whether row_2 represents ascending function
        LocalDateTime periodBegin = firstElement.isBefore(lastElement)
                ? firstElement : lastElement;
        LocalDateTime periodEnd = lastElement.isAfter(periodBegin)
                ? lastElement : firstElement;

        Map<LocalDateTime, Object> sorted_row_1 =
                row_1.entrySet().stream().parallel()
                        //Period offset direction is unknown so both borders check is required
                        .filter(entry -> entry.getKey().equals(periodBegin)
                                || entry.getKey().equals(periodEnd)
                                || (entry.getKey().isAfter(periodBegin) && entry.getKey().isBefore(periodEnd)))
                        .filter(entry -> !row_2.keySet().contains(entry.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<LocalDateTime, Object> sorted_row_2 =
                row_2.entrySet().stream().parallel()
                        //We don't know whether row_2 represents linear function
                        .filter(entry -> entry.getKey().equals(periodBegin)
                                || entry.getKey().equals(periodEnd)
                                || (entry.getKey().isAfter(periodBegin) && entry.getKey().isBefore(periodEnd)))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return Stream.of(sorted_row_1, sorted_row_2).parallel()
                .flatMap(row -> row.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //Naive method is better for regular use due to its simplicity
    public Map<LocalDateTime, Object> mergeRowsNaive(Map<LocalDateTime, Object> row_2) {
        //if no values of row_2 present we cannot determine the time interval
        if (row_2 == null || row_2.entrySet().size() == 0) {
            return Collections.emptyMap();
        }
        //we already know row_2 is not empty
        ArrayList<LocalDateTime> dateTimeArray = new ArrayList<>(row_2.keySet());
        LocalDateTime firstElement = dateTimeArray.get(0);
        LocalDateTime lastElement = dateTimeArray.get(dateTimeArray.size() - 1);
        //We don't know whether row_2 represents ascending function
        LocalDateTime periodBegin = firstElement.isBefore(lastElement)
                ? firstElement : lastElement;
        LocalDateTime periodEnd = lastElement.isAfter(periodBegin)
                ? lastElement : firstElement;

        Map<LocalDateTime, Object> resultMap = new TreeMap<>();

        row_2.entrySet().forEach(entry -> {
            //Period offset direction is unknown so both borders check is required
            if (entry.getKey().isEqual(periodBegin)
                    || entry.getKey().isEqual(periodEnd)
                    ||(entry.getKey().isAfter(periodBegin) && entry.getKey().isBefore(periodEnd))) {
                resultMap.putIfAbsent(entry.getKey(), entry.getValue());
            }
        });

        row_1.entrySet().forEach(entry -> {
            //Period offset direction is unknown so both borders check is required
            if (entry.getKey().isEqual(periodBegin)
                    || entry.getKey().isEqual(periodEnd)
                    ||(entry.getKey().isAfter(periodBegin) && entry.getKey().isBefore(periodEnd))) {
                resultMap.putIfAbsent(entry.getKey(), entry.getValue());
            }
        });

        return resultMap;
    }
}
