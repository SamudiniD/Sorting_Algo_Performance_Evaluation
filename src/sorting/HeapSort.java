package sorting;

public class HeapSort {

    // Public method to sort an array using Heap Sort update
    public static void sort(int[] arr) {
        int n = arr.length;

        // 1. Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // 2. Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root (largest) to end
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // Heapify the reduced heap
            heapify(arr, i, 0);
        }
    }

    // Heapify a subtree rooted at index i
    private static void heapify(int[] arr, int heapSize, int i) {
        int largest = i;          // root
        int left = 2 * i + 1;     // left child
        int right = 2 * i + 2;    // right child

        // If left child is larger
        if (left < heapSize && arr[left] > arr[largest]) {
            largest = left;
        }

        // If right child is larger
        if (right < heapSize && arr[right] > arr[largest]) {
            largest = right;
        }

        // If largest is not root
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Recursively heapify affected subtree
            heapify(arr, heapSize, largest);
        }
    }
}
