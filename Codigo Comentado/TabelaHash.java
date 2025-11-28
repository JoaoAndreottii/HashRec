/**
 * Classe TabelaHash implementa tabela hash com encadeamento separado.
 * Usa tres funcoes de hashing: divisao, multiplicacao e dobramento.
 */
public class TabelaHash {
    public static final double CONSTANTE_A = 0.6180339887;
    public static final int TAMANHO_CHECKSUM = 10;
    
    public No[] tabela;
    public int tamanhoTabela;
    public int colisoesTbl;
    public int colisoesLst;
    public int[] primeirosHashes;
    public int contadorHashes;
    
    /**
     * Construtor inicializa a tabela hash com tamanho especificado.
     */
    public TabelaHash(int tamanho) {
        this.tamanhoTabela = tamanho;
        this.tabela = new No[tamanho];
        this.colisoesTbl = 0;
        this.colisoesLst = 0;
        this.primeirosHashes = new int[TAMANHO_CHECKSUM];
        this.contadorHashes = 0;
        
        int i = 0;
        while (i < tamanho) {
            this.tabela[i] = null;
            i = i + 1;
        }
        
        int j = 0;
        while (j < TAMANHO_CHECKSUM) {
            this.primeirosHashes[j] = 0;
            j = j + 1;
        }
    }
    
    /**
     * Funcao hash por divisao: h(k) = k mod m.
     */
    public int hashDivisao(int chave) {
        int resultado = chave % this.tamanhoTabela;
        if (resultado < 0) {
            resultado = resultado + this.tamanhoTabela;
        }
        return resultado;
    }
    
    /**
     * Funcao hash por multiplicacao: h(k) = piso(m * ((k * A) mod 1)).
     */
    public int hashMultiplicacao(int chave) {
        double produto = (double) chave * CONSTANTE_A;
        double fracao = produto - (long) produto;
        if (fracao < 0.0) {
            fracao = fracao + 1.0;
        }
        double resultado = (double) this.tamanhoTabela * fracao;
        return (int) resultado;
    }
    
    /**
     * Funcao hash por dobramento: particiona k em blocos de 3 digitos, soma e aplica mod m.
     */
    public int hashDobramento(int chave) {
        int soma = 0;
        int resto = chave;
        
        while (resto > 0) {
            int bloco = resto % 1000;
            soma = soma + bloco;
            resto = resto / 1000;
        }
        
        int resultado = soma % this.tamanhoTabela;
        if (resultado < 0) {
            resultado = resultado + this.tamanhoTabela;
        }
        return resultado;
    }
    
    /**
     * Insere um registro na tabela hash no compartimento indicado.
     */
    public void inserir(Registro reg, int hash) {
        if (this.contadorHashes < TAMANHO_CHECKSUM) {
            this.primeirosHashes[this.contadorHashes] = hash;
            this.contadorHashes = this.contadorHashes + 1;
        }
        
        No novo = new No(reg);
        
        if (this.tabela[hash] == null) {
            this.tabela[hash] = novo;
        } else {
            this.colisoesTbl = this.colisoesTbl + 1;
            
            No atual = this.tabela[hash];
            int tamanhoCadeia = 1;
            
            while (atual.proximo != null) {
                atual = atual.proximo;
                tamanhoCadeia = tamanhoCadeia + 1;
            }
            
            atual.proximo = novo;
            this.colisoesLst = this.colisoesLst + tamanhoCadeia;
        }
    }
    
    /**
     * Busca uma chave no compartimento especificado e retorna numero de comparacoes.
     */
    public int buscar(int chave, int hash) {
        int comparacoes = 0;
        No atual = this.tabela[hash];
        
        while (atual != null) {
            comparacoes = comparacoes + 1;
            if (atual.registro.codigo == chave) {
                atual = null;
            } else {
                atual = atual.proximo;
            }
        }
        
        return comparacoes;
    }
    
    /**
     * Retorna o numero total de colisoes na tabela.
     */
    public int obterColisoesTbl() {
        return this.colisoesTbl;
    }
    
    /**
     * Retorna o numero total de colisoes nas listas.
     */
    public int obterColisoesLst() {
        return this.colisoesLst;
    }
    
    /**
     * Calcula o checksum como soma dos primeiros 10 hashes mod 1000003.
     */
    public int calcularChecksum() {
        int soma = 0;
        int i = 0;
        
        while (i < TAMANHO_CHECKSUM) {
            soma = soma + this.primeirosHashes[i];
            i = i + 1;
        }
        
        int resultado = soma % 1000003;
        if (resultado < 0) {
            resultado = resultado + 1000003;
        }
        return resultado;
    }
}