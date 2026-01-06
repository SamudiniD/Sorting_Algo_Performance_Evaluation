package sorting;

public class ShellSort {

    public static void sort(int[] arr) {
        int n = arr.length;

        // Start with a large gap, then reduce it
        for (int gap = n / 2; gap > 0; gap /= 2) {

            // Perform a gapped insertion sort
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;

                // Shift earlier gap-sorted elements up until the correct location is found
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }

                // Place temp at the correct position
                arr[j] = temp;
            }
        }
    }
}
