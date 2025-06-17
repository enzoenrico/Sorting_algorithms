
import java.io.*;
import java.util.*;

public class SortingAnalysis {

    public static void main(String[] args) {
        SortingAnalysis analysis = new SortingAnalysis();
        analysis.runCompleteAnalysis();
    }

    public void runCompleteAnalysis() {
        System.out.println("=== ANÁLISE DE ALGORITMOS DE ORDENAÇÃO ===\n");

        String[] dataTypes = {"aleatorio", "crescente", "decrescente"};
        int[] sizes = {100, 1000, 10000};

        // tabela de resultados
        System.out.printf("%-15s %-10s %-15s %-15s %-15s%n",
                "Tipo", "Tamanho", "Bubble Sort", "Insertion Sort", "Quick Sort");
        System.out.println("-".repeat(80));

        // Para cada tipo de dado
        for (String type : dataTypes) {
            // Para cada tamanho
            for (int size : sizes) {
                String filename = String.format("data/%s_%d.csv", type, size);

                System.out.println("Running file: " + filename);

                try {
                    int[] data = readDataFromFile(filename);

                    if (data != null) {
                        long bubbleTime = measureSortingTime(data.clone(), "bubble");
                        long quickTime = measureSortingTime(data.clone(), "quick");

                        System.out.printf("%-15s %-10d %-15s %-15s %-15s%n",
                                type, size,
                                formatTime(bubbleTime),
                                "-", 
                                formatTime(quickTime));
                    }
                }catch(Error err){
                    System.err.println(err);
                }
            }
        }

    }

    private int[] readDataFromFile(String filename) {
        try {
            // Primeiro tenta encontrar o arquivo no diretório atual
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("Arquivo " + filename + " não encontrado.");
                return null;
            }

            List<Integer> numbers = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        try {
                            numbers.add(Integer.parseInt(line));
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }

            return numbers.stream().mapToInt(Integer::intValue).toArray();

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo " + filename + ": " + e.getMessage());
            return null;
        }
    }

    private long measureSortingTime(int[] data, String algorithm) {
        long startTime = System.nanoTime();

        switch (algorithm.toLowerCase()) {
            case "bubble" ->
                bubbleSort(data);
            case "quick" ->
                quickSort(data, 0, data.length - 1);
            default ->
                throw new IllegalArgumentException("Algoritmo não reconhecido: " + algorithm);
        }

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }


    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    private String formatTime(long nanoTime) {
        if (nanoTime < 1_000) {
            return nanoTime + " ns";
        } else if (nanoTime < 1_000_000) {
            return String.format("%.2f μs", nanoTime / 1_000.0);
        } else if (nanoTime < 1_000_000_000) {
            return String.format("%.2f ms", nanoTime / 1_000_000.0);
        } else {
            return String.format("%.3f s", nanoTime / 1_000_000_000.0);
        }
    }
}
