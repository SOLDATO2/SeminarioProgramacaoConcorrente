import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // #region ========== Criando pedidos ===========
        List<Pedido> pedidos = Arrays.asList(
            new Pedido(1, "Bolo de chocolate"),
            new Pedido(2, "Pizza calabresa"),
            new Pedido(3, "Sanduíche de frango")
        );
        // #endregion

        // #region ========== Usando Runnable com Thread ===========
        System.out.println("\n=== Runnable com Thread ===");
        for (Pedido p : pedidos) {
            Thread t = new Thread(new ProcessadorRunnable(p));
            t.start();
            t.join();
        }
    
        // #endregion

        // #region ========== Usando Callable e Future com ExecutorService ==========
        System.out.println("\n=== Callable com Future ===");
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<String>> resultados = new ArrayList<>();
        for (Pedido p : pedidos) {
            resultados.add(executor.submit(new ProcessadorCallable(p)));
        }
        for (Future<String> f : resultados) {
            System.out.println(f.get()); // Aguarda e pega o resultado
        }
        executor.shutdown();
        // #endregion

        // #region ========== Usando CompletableFuture ==========
        System.out.println("\n=== CompletableFuture Assíncrono ===");
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Pedido p : pedidos) {
            CompletableFuture<Void> cf = CompletableFuture
                .supplyAsync(() -> "[CompletableFuture] Processando " + p)
                .thenApply(msg -> {
                    System.out.println(msg);
                    try { Thread.sleep(400); } catch (InterruptedException ignored) {}
                    return "[CompletableFuture] Finalizado " + p;
                })
                .thenAccept(System.out::println);
            futures.add(cf);
        }
        // Espera todos terminarem
        
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        System.out.println("Todos os pedidos processados com CompletableFuture!");
        // #endregion

    }
}
