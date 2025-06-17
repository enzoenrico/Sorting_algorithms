
import java.io.*;
import java.util.*;

public class SortingAnalysis {

    public static void main(String[] args) {
        SortingAnalysis analysis = new SortingAnalysis();
        analysis.runCompleteAnalysis();
    }

    /**
     * Executa a análise completa de todos os algoritmos em todos os conjuntos
     * de dados
     */
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
                    // Lê os dados do arquivo
                    int[] data = readDataFromFile(filename);

                    if (data != null) {
                        // Executa os algoritmos e mede o tempo
                        long bubbleTime = measureSortingTime(data.clone(), "bubble");
                        long insertionTime = measureSortingTime(data.clone(), "insertion");
                        long quickTime = measureSortingTime(data.clone(), "quick");

                        // Exibe os resultados
                        System.out.printf("%-15s %-10d %-15s %-15s %-15s%n",
                                type, size,
                                formatTime(bubbleTime),
                                formatTime(insertionTime),
                                formatTime(quickTime));
                    } else {
                        System.out.printf("%-15s %-10d %-15s %-15s %-15s%n",
                                type, size, "ERRO", "ERRO", "ERRO");
                    }

                } catch (Exception e) {
                    System.out.printf("%-15s %-10d %-15s %-15s %-15s%n",
                            type, size, "ARQUIVO NÃO ENCONTRADO", "", "");
                }
            }
        }

        // System.out.println("\n=== ANÁLISE ===");
    }

    /**
     * Lê dados de um arquivo CSV
     */
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
                            // Ignora linhas que não são números
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

    /**
     * Mede o tempo de execução de um algoritmo de ordenação
     */
    private long measureSortingTime(int[] data, String algorithm) {
        long startTime = System.nanoTime();

        switch (algorithm.toLowerCase()) {
            case "bubble" ->
                bubbleSort(data);
            case "insertion" ->
                insertionSort(data);
            case "quick" ->
                quickSort(data, 0, data.length - 1);
            default ->
                throw new IllegalArgumentException("Algoritmo não reconhecido: " + algorithm);
        }

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    /**
     * Implementação do Bubble Sort
     */
    private void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        // System.out.println("-> Bubble Sort <-");
        // System.out.println("Array inicial: " + Arrays.toString(arr));

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // System.out.println("Troca: " + arr[j] + " " + arr[j + 1]);
                    // Troca os elementos
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            // Se não houve troca, o array já está ordenado
            if (!swapped) {
                break;
            }
        }
        // System.out.println("Array final: " + Arrays.toString(arr));
    }

    /**
     * Implementação do Insertion Sort
     */
    private void insertionSort(int[] arr) {
        int n = arr.length;

        // System.out.println("-> Insertion Sort <-");
        // System.out.println("Array inicial: " + Arrays.toString(arr));

        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;

            // Move elementos maiores que key uma posição à frente
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
            // System.out.println("Passo " + i + ": " + Arrays.toString(arr));
        }
        // System.out.println("Array final: " + Arrays.toString(arr));
    }

    /**
     * Implementação do Quick Sort
     */
    private void quickSort(int[] arr, int low, int high) {
        // System.out.println("-> Quick Sort <-");
        // System.out.println("Array inicial: " + Arrays.toString(arr));
        if (low < high) {
            // Encontra o índice de partição
            int pi = partition(arr, low, high);
            // System.out.println("Partição em " + pi);

            // Ordena recursivamente os elementos antes e depois da partição
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    /**
     * Função auxiliar para partição do Quick Sort
     */
    private int partition(int[] arr, int low, int high) {
        // Escolhe o último elemento como pivot
        int pivot = arr[high];
        int i = low - 1; // Índice do menor elemento

        for (int j = low; j < high; j++) {
            // Se o elemento atual é menor ou igual ao pivot
            if (arr[j] <= pivot) {
                i++;
                // Troca arr[i] e arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // Troca arr[i+1] e arr[high] (pivot)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    /**
     * Formata o tempo em nanossegundos para uma string legível
     */
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
