package sorting;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Here Run every sorting algorithm (each class must provide a static `sort(int[])` method).
 * Returns a LinkedHashMap of algorithm name -> elapsed time in nanoseconds.
 */
public class SortRunner {

    public static Map<String, Long> runAll(int[] data) {
        Map<String, Long> results = new LinkedHashMap<>();


        results.put("Merge Sort",     time(data, MergeSort::sort));
        // results.put("Insertion Sort", time(data, InsertionSort::sort));
        // results.put("Shell Sort",     time(data, ShellSort::sort));
        // results.put("Quick Sort",     time(data, QuickSort::sort));
        // results.put("Heap Sort",      time(data, HeapSort::sort));

        return results;
    }

    // Make a copy of the original array so each sort gets the same input
    private static long time(int[] original, SortMethod method) {
        int[] arr = original.clone();
        long start = System.nanoTime();
        method.sort(arr);
        long end = System.nanoTime();
        return end - start;
    }

    // functional interface for passing method references of `sort(int[])`
    private interface SortMethod {
        void sort(int[] arr);
    }
}
