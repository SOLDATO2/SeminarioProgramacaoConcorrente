package processamentoPedidos;
public class Pedido {

    private final Integer id;
    private final String descricao;

    public Pedido(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}