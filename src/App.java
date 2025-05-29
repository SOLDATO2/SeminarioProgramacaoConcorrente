import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
public class App {
    public static void main(String[] args) throws Exception {
        //exemplo base da utilização do Future
        // https://download.java.net/java/early_access/valhalla/docs/api/java.base/java/util/concurrent/Future.html
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<String> f1 = executor.submit(new GetClienteTask());
        Future<String> f2 = executor.submit(new GetPedidosTask());
        Future<String> f3 = executor.submit(new GetRecomendacoesTask());

        String relatorio = String.join("\n", f1.get(), f2.get(), f3.get());
        System.out.println("Relatório final:\n" + relatorio);

        executor.shutdown();

        System.out.println("Exemplo terminou. Pressione Enter para continuar...");
        new java.util.Scanner(System.in).nextLine();
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();



        //exemplos de metodos do Future
        //Exemplo 1: .cancel() + .isCancelled() e get padrao
        ExecutorService executorEx1 = Executors.newFixedThreadPool(3);

        f1 = executorEx1.submit(new GetClienteTask());
        f2 = executorEx1.submit(new GetPedidosTask()); // irá ser cancelado, pois não precisa mais por um motivo hipotetico.
        f3 = executorEx1.submit(new GetRecomendacoesTask());

        f2.cancel(true); //cancelado por algum motivo hipotetico

        String resultado1 = "Nada";
        String resultado2 = "Nada";
        String resultado3 = "Nada";

        resultado1 = f1.get(); //realisticamente, .isCancelled() seria aplicado para f1 e f3 tambem antes de tentar pegar o resultado
        resultado3 = f3.get();

        if(f2.isCancelled()){
            resultado2 = "Tarefa cancelada";
        }else{
            resultado2 = f2.get();
        }

        relatorio = String.join("\n", resultado1, resultado2, resultado3);
        System.out.println("Relatório final:\n" + relatorio);
        executorEx1.close();



        System.out.println("Exemplo terminou. Pressione Enter para continuar...");
        new java.util.Scanner(System.in).nextLine();
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();


        



        //Exemplo 2: .isDone() + getNow()
        ExecutorService executorEx2 = Executors.newFixedThreadPool(1);

        Future<String> futuro = executorEx2.submit(() -> {
            Thread.sleep(2000); // tarefa q demora 2 segundos
            return "Resultado concluído";
        });

        System.out.println("Antes da conclusão, isDone(): " + futuro.isDone()); //antes da conclusao
        String resultadoAntes;
        try {
            resultadoAntes = futuro.resultNow(); // Tenta obter sem esperar, lança Exception se ainda n terminou
        } catch (IllegalStateException e) {
            resultadoAntes = "Ainda sem resultado...";
        }
        System.out.println("Resultado com resultNow() antes da conclusão: " + resultadoAntes);

        Thread.sleep(2500);        // espera tarefa terminar

        // dps da conclusao
        System.out.println("Depois da conclusão, isDone(): " + futuro.isDone());
        String resultadoDepois = futuro.resultNow();  // agora já deve retornar o valor real
        System.out.println("Resultado com resultNow() após a conclusão: " + resultadoDepois);

        executorEx2.shutdown();

        System.out.println("Exemplo terminou. Pressione Enter para continuar...");
        new java.util.Scanner(System.in).nextLine();
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();






        //Exemplo 3: exceptionNow() + state() + get(timeout)
        ExecutorService executorEx3 = Executors.newFixedThreadPool(2);

        // Duas tarefas: uma que conclui normalmente e outra que lança exceção
        Future<String> task1 = executorEx3.submit(() -> {
            Thread.sleep(1000);
            return "Task 1 concluída";
        });
        Future<String> task2 = executorEx3.submit(() -> {
            Thread.sleep(1500);
            throw new IllegalStateException("Erro na Task 2");
        });

        // 1. get(timeout): tenta obter o resultado em até 500 ms
        try {
            String r1 = task1.get(500, TimeUnit.MILLISECONDS);
            System.out.println("Resultado de task1 (com timeout): " + r1);
        } catch (TimeoutException e) {
            System.out.println("Timeout obtendo task1: " + e.getMessage());
        }

        // 2. state(): imprime o estado atual de cada Future
        System.out.println("Estado de task1: " + task1.state());
        System.out.println("Estado de task2: " + task2.state());

        // Espera para garantir que ambas tarefas tenham terminado
        Thread.sleep(2000);

        // Reimprime estados após término
        System.out.println("Estado de task1 após conclusão: " + task1.state());
        System.out.println("Estado de task2 após conclusão: " + task2.state());

        // 3. exceptionNow(): obtém imediatamente a exceção de task2
        try {
            Throwable ex = task2.exceptionNow();
            System.out.println("Exceção de task2: " + ex);
        } catch (IllegalStateException ise) {
            System.out.println("Não houve exceção em task2: " + ise.getMessage());
        }

        executorEx3.shutdown();

    }
}
