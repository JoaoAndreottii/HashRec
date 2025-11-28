/**
 * Classe No representa um elemento da lista encadeada simples.
 * Cada no contem um registro e uma referencia para o proximo no.
 */
public class No {
    public Registro registro;
    public No proximo;
    
    /**
     * Construtor cria um no com um registro.
     * O proximo e inicializado como null.
     */
    public No(Registro registro) {
        this.registro = registro;
        this.proximo = null;
    }
}