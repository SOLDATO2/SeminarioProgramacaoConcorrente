import java.util.concurrent.Callable;
class GetClienteTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(1000);
        return "👤 Cliente: Ricardo";
    }
}
