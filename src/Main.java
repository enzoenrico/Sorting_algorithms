
/**
 * Programa principal para análise de algoritmos de ordenação TDE 04 - Resolução
 * de Problemas Estruturados em Computação
 *
 * @author Enzo Enrico Boteon Chiuratto e Pedro Henrique Sudario da Silva
 */
public class Main {
    public static void main(String[] args) {
            System.out.println("=== ANÁLISE DE ALGORITMOS DE ORDENAÇÃO ===");
            System.out.println("TDE 04 - Resolução de Problemas Estruturados em Computação");
            System.out.println("Por: Enzo e Pedro\n");
            
            runAlgorithms();
    }

    private static void runAlgorithms() {
                System.out.println("\n--- Executando análise de algoritmos ---");
                SortingAnalysis analysis = new SortingAnalysis();
                analysis.runCompleteAnalysis();
    }
}
