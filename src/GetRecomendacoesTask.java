import java.util.concurrent.Callable;
class GetRecomendacoesTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(1200);
        return "🤖 Recomendações: [Headset, Teclado]";
    }
}