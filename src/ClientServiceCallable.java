import java.util.concurrent.*;

public class ClientServiceCallable {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Callable<String> getCliente = () -> {
            Thread.sleep(1000);
            return " Cliente: Ricardo";
        };

        Callable<String> getPedidos = () -> {
            Thread.sleep(1500);
            return " Pedidos: [Mouse, Livro]";
        };

        Callable<String> getRecomendacoes = () -> {
            Thread.sleep(1200);
            return " Recomendações: [Headset, Teclado]";
        };

        Future<String> f1 = executor.submit(getCliente);
        Future<String> f2 = executor.submit(getPedidos);
        Future<String> f3 = executor.submit(getRecomendacoes);

        // Espera os resultados
        String relatorio = String.join("\n", f1.get(), f2.get(), f3.get());

        System.out.println("📄 Relatório final:\n" + relatorio);
        executor.shutdown();
    }
}