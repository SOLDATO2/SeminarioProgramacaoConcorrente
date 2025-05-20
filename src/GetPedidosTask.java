import java.util.concurrent.Callable;
class GetPedidosTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(5000);
        return "📦 Pedidos: [Mouse, Livro]";
    }
}