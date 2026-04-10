import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FatorialParalelo {

    // Classe que define a tarefa dividida entre os núcleos
    static class TarefaFatorial extends RecursiveTask<BigInteger> {
        private final long inicio;
        private final long fim;

        public TarefaFatorial(long inicio, long fim) {
            this.inicio = inicio;
            this.fim = fim;
        }

        @Override
        protected BigInteger compute() {
            // Se o intervalo for pequeno, calcula sequencialmente (Aglomeração)
            if (fim - inicio <= 1000) {
                return calcularSequencial(inicio, fim);
            }

            // Divide a tarefa em duas (Particionamento)
            long meio = (inicio + fim) / 2;
            TarefaFatorial tarefa1 = new TarefaFatorial(inicio, meio);
            TarefaFatorial tarefa2 = new TarefaFatorial(meio + 1, fim);

            // Dispara as tarefas em paralelo (Comunicacao)
            tarefa1.fork();
            BigInteger resultado2 = tarefa2.compute();
            BigInteger resultado1 = tarefa1.join();

            // Combina os resultados (Comunicação)

            return resultado1.multiply(resultado2);
        }

        private BigInteger calcularSequencial(long start, long end) {
            BigInteger res = BigInteger.ONE;
            for (long i = start; i <= end; i++) {
                res = res.multiply(BigInteger.valueOf(i));
            }
            return res;
        }
    }

    public static void main(String[] args) {
        int n = 500_000;

        System.out.println("Calculando " + n + "! em paralelo...");
        long startTime = System.currentTimeMillis();

        // Cria um pool de threads baseado no número de núcleos da sua CPU (Mapping)
        ForkJoinPool pool = new ForkJoinPool();
        BigInteger resultado = pool.invoke(new TarefaFatorial(1, n));

        long endTime = System.currentTimeMillis();

        System.out.println("Cálculo concluído em: " + (endTime - startTime) + " ms");
        System.out.println("Total de dígitos: " + resultado.toString().length());

    }
}