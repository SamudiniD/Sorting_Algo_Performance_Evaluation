package sorting;

import java.util.Arrays;

public class TestSorting {

    public static void main(String[] args) {

        int[] sample = {5, 2, 9, 1, 7};

        System.out.println("Original: " + Arrays.toString(sample));

        // Test MergeSort
        int[] arr1 = sample.clone();
        MergeSort.sort(arr1);
        System.out.println("MergeSort: " + Arrays.toString(arr1));

        // You can later test other algorithms:
        // int[] arr2 = sample.clone();
        // QuickSort.sort(arr2);
        // System.out.println("QuickSort: " + Arrays.toString(arr2));
    }
}
