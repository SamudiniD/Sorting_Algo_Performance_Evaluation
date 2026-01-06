package sorting;

import java.util.HashMap;
import java.util.Map;

/**
 * Here Run every sorting algorithm (each class must provide a static `sort(int[])` method).
 * Returns a LinkedHashMap of algorithm name -> elapsed time in nanoseconds.
 */
// Update 
public class SortRunner {

    /**
     * Runs all sorting algorithms and returns a Map with execution times (ns)
     */
    public static Map<String, Long> runAll(int[] original) {
        Map<String, Long> results = new HashMap<>();

        // results.put("Bubble Sort", time(original, BubbleSort::sort));
        // results.put("Selection Sort", time(original, SelectionSort::sort));
        results.put("Insertion Sort", time(original, InsertionSort::sort));
        results.put("Merge Sort", time(original, MergeSort::sort));
        results.put("Quick Sort", time(original, QuickSort::sort));
        results.put("Heap Sort", time(original, HeapSort::sort));
        results.put("Shell Sort", time(original, ShellSort::sort));

        return results;
    }

    /**
     * Runs a single sorting method and returns time taken in nanoseconds.
     */
    public static long time(int[] original, SortMethod method) {
        int[] arr = original.clone();

        long start = System.nanoTime();
        method.sort(arr);
        long end = System.nanoTime();

        return end - start;
    }

    @FunctionalInterface
    public interface SortMethod {
        void sort(int[] arr);
    }

}
