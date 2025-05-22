public class ProcessadorRunnable implements Runnable {
    private final Pedido pedido;

    public ProcessadorRunnable(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void run() {
        System.out.println("[Runnable] Processando " + pedido);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[Runnable] Finalizado " + pedido);
    }
}
