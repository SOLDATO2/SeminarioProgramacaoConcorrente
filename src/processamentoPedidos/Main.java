import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        exemploRunnable();
        exemploCallable();
        exemploCompletedFuture();
        exemploFailedFuture();
        exemploSupplyAsync();
        exemploRunAsync();
        exemploThenApply();
        exemploThenCompose();
        exemploThenCombine();
        exemploThenAcceptBoth();
        exemploRunAfterBoth();
        exemploApplyToEither();
        exemploAcceptEither();
        exemploRunAfterEither();
        exemploWhenComplete();
        exemploHandle();
        exemploExceptionally();
        exemploTimeouts();
        exemploAllOfAnyOf();
        exemploObtrude();
        exemploCancelamento();
        exemploMinimalCompletionStage();
        exemploCustomExecutor();
    }

    // ==== Runnable com Pedido ====
    static void exemploRunnable() throws InterruptedException {
        System.out.println("\n=== Runnable com Pedido ===");
        Pedido p = new Pedido(1, "Pizza");
        Thread t = new Thread(new ProcessadorRunnable(p));
        t.start();
        t.join();
    }

    // ==== Callable com Pedido ====
    static void exemploCallable() throws Exception {
        System.out.println("\n=== Callable com Pedido ===");
        Pedido p = new Pedido(2, "Lasanha");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> resultado = executor.submit(new ProcessadorCallable(p));
        System.out.println(resultado.get());
        executor.shutdown();
    }

    // ==== completedFuture com Pedido ====
    static void exemploCompletedFuture() throws Exception {
        System.out.println("\n=== completedFuture com Pedido ===");
        Pedido p = new Pedido(3, "Esfiha");
        CompletableFuture<Pedido> cf = CompletableFuture.completedFuture(p);
        System.out.println("Pedido pronto: " + cf.get());
    }

    // ==== failedFuture com Pedido ====
    static void exemploFailedFuture() {
        System.out.println("\n=== failedFuture com Pedido ===");
        Pedido p = new Pedido(4, "Coxinha");
        CompletableFuture<Pedido> cf = CompletableFuture.failedFuture(new RuntimeException("Erro ao processar " + p));
        cf.exceptionally(ex -> {
            System.out.println("Erro tratado: " + ex.getMessage());
            return null;
        });
    }

    // ==== supplyAsync com Pedido ====
    static void exemploSupplyAsync() throws Exception {
        System.out.println("\n=== supplyAsync com Pedido ===");
        Pedido p = new Pedido(5, "Hambúrguer");
        CompletableFuture<Pedido> cf = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(150); } catch (InterruptedException ignored) {}
            System.out.println("Preparando " + p);
            return p;
        });
        System.out.println("Pedido retornado: " + cf.get());
    }

    // ==== runAsync com Pedido ====
    static void exemploRunAsync() {
        System.out.println("\n=== runAsync com Pedido ===");
        Pedido p = new Pedido(6, "Quibe");
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            System.out.println("Produzindo: " + p);
        });
        cf.join();
    }

    // ==== thenApply ====
    static void exemploThenApply() {
        System.out.println("\n=== thenApply ===");
        Pedido p = new Pedido(7, "Pastel");
        CompletableFuture.completedFuture(p)
            .thenApply(ped -> {
                System.out.println("Fritando: " + ped);
                return new Pedido(ped.getId(), ped.getDescricao() + " Frito");
            })
            .thenAccept(res -> System.out.println("Final: " + res));
    }

    // ==== thenCompose ====
    static void exemploThenCompose() {
        System.out.println("\n=== thenCompose ===");
        Pedido p = new Pedido(8, "Empada");
        CompletableFuture.completedFuture(p)
            .thenCompose(ped -> CompletableFuture.supplyAsync(() -> {
                System.out.println("Assando: " + ped);
                return new Pedido(ped.getId(), ped.getDescricao() + " Assada");
            }))
            .thenAccept(res -> System.out.println("Pronto: " + res));
    }

    // ==== thenCombine ====
    static void exemploThenCombine() {
        System.out.println("\n=== thenCombine ===");
        Pedido p1 = new Pedido(9, "Refrigerante");
        Pedido p2 = new Pedido(10, "Lanche");
        CompletableFuture<Pedido> f1 = CompletableFuture.completedFuture(p1);
        CompletableFuture<Pedido> f2 = CompletableFuture.completedFuture(p2);

        f1.thenCombine(f2, (a, b) -> "Combo: " + a + " + " + b)
          .thenAccept(System.out::println);
    }

    // ==== thenAcceptBoth ====
    static void exemploThenAcceptBoth() {
        System.out.println("\n=== thenAcceptBoth ===");
        Pedido p1 = new Pedido(11, "Suco");
        Pedido p2 = new Pedido(12, "Torta");
        CompletableFuture<Pedido> f1 = CompletableFuture.completedFuture(p1);
        CompletableFuture<Pedido> f2 = CompletableFuture.completedFuture(p2);

        f1.thenAcceptBoth(f2, (a, b) -> System.out.println("Servindo juntos: " + a + " e " + b));
    }

    // ==== runAfterBoth ====
    static void exemploRunAfterBoth() {
        System.out.println("\n=== runAfterBoth ===");
        Pedido p1 = new Pedido(13, "Batata");
        Pedido p2 = new Pedido(14, "Molho");
        CompletableFuture<Pedido> f1 = CompletableFuture.completedFuture(p1);
        CompletableFuture<Pedido> f2 = CompletableFuture.completedFuture(p2);

        f1.runAfterBoth(f2, () -> System.out.println("Batata e Molho prontos para servir!"));
    }

    // ==== applyToEither ====
    static void exemploApplyToEither() {
        System.out.println("\n=== applyToEither ===");
        Pedido p1 = new Pedido(15, "Pão");
        Pedido p2 = new Pedido(16, "Bolo");
        CompletableFuture<Pedido> f1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            return p1;
        });
        CompletableFuture<Pedido> f2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            return p2;
        });
        f1.applyToEither(f2, p -> "Primeiro pronto: " + p)
          .thenAccept(System.out::println);
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}
    }

    // ==== acceptEither ====
    static void exemploAcceptEither() {
        System.out.println("\n=== acceptEither ===");
        Pedido p1 = new Pedido(17, "Tapioca");
        Pedido p2 = new Pedido(18, "Café");
        CompletableFuture<Pedido> f1 = CompletableFuture.supplyAsync(() -> p1);
        CompletableFuture<Pedido> f2 = CompletableFuture.supplyAsync(() -> p2);
        f1.acceptEither(f2, p -> System.out.println("Primeiro servido: " + p));
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
    }

    // ==== runAfterEither ====
    static void exemploRunAfterEither() {
        System.out.println("\n=== runAfterEither ===");
        Pedido p1 = new Pedido(19, "Pudim");
        Pedido p2 = new Pedido(20, "Sorvete");
        CompletableFuture<Pedido> f1 = CompletableFuture.supplyAsync(() -> p1);
        CompletableFuture<Pedido> f2 = CompletableFuture.supplyAsync(() -> p2);
        f1.runAfterEither(f2, () -> System.out.println("Um doce já está pronto!"));
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
    }

    // ==== whenComplete ====
    static void exemploWhenComplete() {
        System.out.println("\n=== whenComplete ===");
        Pedido p = new Pedido(21, "Milkshake");
        CompletableFuture<Pedido> f = CompletableFuture.completedFuture(p);
        f.whenComplete((res, ex) -> System.out.println("Pedido finalizado: " + res));
    }

    // ==== handle ====
    static void exemploHandle() {
        System.out.println("\n=== handle ===");
        Pedido p = new Pedido(22, "Wrap");
        CompletableFuture<Pedido> f = CompletableFuture.completedFuture(p);
        f.handle((res, ex) -> res != null ? res : new Pedido(-1, "Pedido vazio"))
         .thenAccept(System.out::println);
    }

    // ==== exceptionally ====
    static void exemploExceptionally() {
        System.out.println("\n=== exceptionally ===");
        Pedido p = new Pedido(23, "Brownie");
        CompletableFuture<Pedido> f = CompletableFuture.failedFuture(new Exception("Faltou ingrediente"));
        f.exceptionally(ex -> {
            System.out.println("Erro tratado: " + ex.getMessage());
            return new Pedido(-1, "Pedido substituto");
        }).thenAccept(System.out::println);
    }

    // ==== Timeout ====
    static void exemploTimeouts() throws InterruptedException {
        System.out.println("\n=== orTimeout / completeOnTimeout ===");
        Pedido p = new Pedido(24, "Yakissoba");
        CompletableFuture<Pedido> cf = new CompletableFuture<>();
        cf.orTimeout(300, TimeUnit.MILLISECONDS)
          .exceptionally(ex -> {
              System.out.println("Timeout ao preparar: " + p);
              return new Pedido(-2, "Timeout: " + p.getDescricao());
          }).thenAccept(System.out::println);
        Thread.sleep(350);

        CompletableFuture<Pedido> cf2 = new CompletableFuture<>();
        cf2.completeOnTimeout(p, 200, TimeUnit.MILLISECONDS)
            .thenAccept(res -> System.out.println("Entrega forçada: " + res));
        Thread.sleep(250);
    }

    // ==== allOf / anyOf ====
    static void exemploAllOfAnyOf() {
        System.out.println("\n=== allOf / anyOf ===");
        Pedido p1 = new Pedido(25, "Petit Gateau");
        Pedido p2 = new Pedido(26, "Chocolate Quente");
        CompletableFuture<Pedido> f1 = CompletableFuture.supplyAsync(() -> p1);
        CompletableFuture<Pedido> f2 = CompletableFuture.supplyAsync(() -> p2);

        CompletableFuture.allOf(f1, f2).thenRun(() -> System.out.println("Sobremesas prontas!")).join();
        CompletableFuture.anyOf(f1, f2).thenAccept(res -> System.out.println("Primeira sobremesa pronta: " + res)).join();
    }

    // ==== obtrudeValue / obtrudeException ====
    static void exemploObtrude() {
        System.out.println("\n=== obtrudeValue / obtrudeException ===");
        Pedido p = new Pedido(27, "Chá Gelado");
        CompletableFuture<Pedido> cf = new CompletableFuture<>();
        cf.obtrudeValue(p);
        System.out.println("Forçado pronto: " + cf.join());

        CompletableFuture<Pedido> cf2 = new CompletableFuture<>();
        cf2.obtrudeException(new RuntimeException("Cancelado"));
        try { cf2.join(); } catch (Exception e) { System.out.println("Pedido cancelado: " + e); }
    }

    // ==== cancel / isCancelled / isDone ====
    static void exemploCancelamento() {
        System.out.println("\n=== cancel / isCancelled / isDone ===");
        CompletableFuture<Pedido> cf = new CompletableFuture<>();
        System.out.println("Antes: isDone=" + cf.isDone() + ", isCancelled=" + cf.isCancelled());
        cf.cancel(true);
        System.out.println("Depois: isDone=" + cf.isDone() + ", isCancelled=" + cf.isCancelled());
    }

    // ==== minimalCompletionStage / copy ====
    static void exemploMinimalCompletionStage() {
        System.out.println("\n=== minimalCompletionStage / copy ===");
        Pedido p = new Pedido(28, "Salada");
        CompletableFuture<Pedido> cf = CompletableFuture.completedFuture(p);
        CompletionStage<Pedido> stage = cf.minimalCompletionStage();
        System.out.println("minimalCompletionStage: " + stage.toCompletableFuture().join());

        CompletableFuture<Pedido> cfCopy = cf.copy();
        System.out.println("copy: " + cfCopy.join());
    }

    // ==== Executor customizado com Pedido ====
    static void exemploCustomExecutor() {
        System.out.println("\n=== supplyAsync com Executor customizado ===");
        Pedido p = new Pedido(29, "Bolo de Cenoura");
        ExecutorService pool = Executors.newFixedThreadPool(2);
        CompletableFuture.supplyAsync(() -> p, pool)
            .thenAccept(res -> System.out.println("Processado com pool: " + res))
            .join();
        pool.shutdown();
    }
}
