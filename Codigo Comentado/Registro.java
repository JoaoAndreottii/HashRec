/**
 * Classe Registro representa um elemento na tabela hash.
 * Contem apenas um codigo inteiro de 9 digitos usado como chave.
 */
public class Registro {
    public int codigo;
    
    /**
     * Construtor inicializa o registro com um codigo.
     */
    public Registro(int codigo) {
        this.codigo = codigo;
    }
}