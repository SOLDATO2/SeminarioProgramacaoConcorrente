package processamentoPedidos;
import java.util.concurrent.Callable;

public class ProcessadorCallable implements Callable<String> {
    private final Pedido pedido;

    public ProcessadorCallable(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public String call() throws Exception {
        System.out.println("[Callable] Processando " + pedido);
        Thread.sleep(700);
        return "[Callable] Finalizado " + pedido;
    }
}
