/**
 * Classe ResultadoBusca armazena metricas de busca.
 * Separa resultados de hits (sucesso) e misses (fracasso).
 */
public class ResultadoBusca {
    public long tempoHits;
    public long tempoMisses;
    public int comparacoesHits;
    public int comparacoesMisses;
    public int colisoesLstHits;
    public int colisoesLstMisses;
    
    /**
     * Construtor inicializa valores em zero.
     */
    public ResultadoBusca() {
        this.tempoHits = 0;
        this.tempoMisses = 0;
        this.comparacoesHits = 0;
        this.comparacoesMisses = 0;
        this.colisoesLstHits = 0;
        this.colisoesLstMisses = 0;
    }
}