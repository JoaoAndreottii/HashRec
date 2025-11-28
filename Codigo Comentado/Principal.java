import java.io.PrintWriter;
import java.io.FileNotFoundException;

/**
 * Classe Principal executa experimentos da tabela hash.
 * Gera datasets, mede tempos, conta colisoes e imprime resultados.
 */
public class Principal {
    
    public static final int TAM_TAB_1 = 1009;
    public static final int TAM_TAB_2 = 10007;
    public static final int TAM_TAB_3 = 100003;
    
    public static final int TAM_DS_1 = 1000;
    public static final int TAM_DS_2 = 10000;
    public static final int TAM_DS_3 = 100000;
    
    public static final long SEED_1 = 137;
    public static final long SEED_2 = 271828;
    public static final long SEED_3 = 314159;
    
    public static final String NOME_AUTOR = "João Otávio Andreotti";
    public static final int TAM_NOME_AUTOR = 21;
    
    public static final int NUM_REPETICOES = 5;
    
    public static final int MIN_CHAVE = 100000000;
    public static final int MAX_CHAVE = 999999999;
    
    public static final int QTD_TAM_TAB = 3;
    public static final int QTD_TAM_DS = 3;
    public static final int QTD_SEEDS = 3;
    public static final int QTD_FUNCOES = 3;
    
    public static final String NOME_ARQUIVO_CSV = "resultados_experimentos.csv";
    
    public static final int VERDADEIRO = 1;
    public static final int FALSO = 0;
    
    // -----------------------------------------------------------------------------------------
    // Escritor CSV
    // -----------------------------------------------------------------------------------------
    
    public static class EscritorCSV {
        private PrintWriter escritor;
        private int aberto;
        
        public EscritorCSV() {
            this.escritor = null;
            this.aberto = FALSO;
        }
        
        public int abrir(String nomeArquivo) {
            int sucesso = FALSO;
            
            // Unico try-catch permitido: para criar o arquivo CSV
            try {
                escritor = new PrintWriter(nomeArquivo);
                this.aberto = VERDADEIRO;
                sucesso = VERDADEIRO;
            } catch (FileNotFoundException e) {
                escritor = null;
                this.aberto = FALSO;
                sucesso = FALSO;
            }
            
            return sucesso;
        }
        
        public void escreverLinha(String linha) {
            if (aberto == VERDADEIRO) {
                escritor.println(linha);
                escritor.flush();
            }
        }
        
        public void fechar() {
            if (aberto == VERDADEIRO) {
                if (escritor != null) {
                    escritor.close();
                }
                aberto = FALSO;
            }
        }
    }
    
    // -----------------------------------------------------------------------------------------
    // Gerador Aleatório
    // -----------------------------------------------------------------------------------------
    
    public static class GeradorAleatorio {
        private long estado;
        
        public GeradorAleatorio(long seed) {
            this.estado = seed;
        }
        
        public int proximo(int limite) {
            long a = 1103515245L;
            long c = 12345L;
            long m = 2147483648L;
            
            estado = (a * estado + c) % m;
            long temp = estado % limite;
            int valor = (int) temp;
            
            if (valor < 0) {
                valor = -valor;
            }
            
            return valor;
        }
    }
    
    // -----------------------------------------------------------------------------------------
    // Funções auxiliares (ABS, String, Hash do nome)
    // -----------------------------------------------------------------------------------------
    
    public static int valorAbsoluto(int x) {
        if (x < 0) {
            return -x;
        }
        return x;
    }
    
    public static double valorAbsolutoDouble(double x) {
        if (x < 0) {
            return -x;
        }
        return x;
    }
    
    public static int calcularHashAutor(String nome) {
        int soma = 0;
        int i = 0;
        
        while (i < TAM_NOME_AUTOR) {
            soma = soma + nome.charAt(i);
            i = i + 1;
        }
        
        return soma % 1000003;
    }
    
    public static int compararStrings(String s1, String s2, int tamanho) {
        int i = 0;
        
        while (i < tamanho) {
            if (s1.charAt(i) != s2.charAt(i)) {
                return FALSO;
            }
            i = i + 1;
        }
        
        return VERDADEIRO;
    }
    
    public static String doubleParaString(double valor, int casasDecimais) {
        int parteInteira = (int) valor;
        double parteFracionaria = valorAbsolutoDouble(valor - parteInteira);
        
        int multiplicador = 1;
        int i = 0;
        
        while (i < casasDecimais) {
            multiplicador = multiplicador * 10;
            i = i + 1;
        }
        
        int fracaoInteira = (int) (parteFracionaria * multiplicador + 0.5);
        String resultado = "";
        resultado = concatenarInt(resultado, parteInteira);
        resultado = concatenar(resultado, ".");
        
        String fracaoStr = "";
        fracaoStr = concatenarInt(fracaoStr, fracaoInteira);
        
        int tam = obterTamanho(fracaoStr);
        while (tam < casasDecimais) {
            resultado = concatenar(resultado, "0");
            tam = tam + 1;
        }
        
        return concatenar(resultado, fracaoStr);
    }
    
    public static String concatenar(String s1, String s2) {
        return s1 + s2;
    }
    
    public static String concatenarInt(String s, int num) {
        return s + num;
    }
    
    public static int obterTamanho(String s) {
        int tam = 0;
        int i = 0;
        int continuar = VERDADEIRO;
        
        while (continuar == VERDADEIRO) {
            if (i < 1000) {
                tam = i;
                i = i + 1;
            } else {
                continuar = FALSO;
            }
        }
        
        return tam;
    }
    
    // -----------------------------------------------------------------------------------------
    // Gerar Dataset
    // -----------------------------------------------------------------------------------------
    
    public static int[] gerarDataset(int tamanho, long seed) {
        GeradorAleatorio gerador = new GeradorAleatorio(seed);
        int[] dataset = new int[tamanho];
        
        int i = 0;
        
        while (i < tamanho) {
            int intervalo = MAX_CHAVE - MIN_CHAVE + 1;
            int chave = MIN_CHAVE + gerador.proximo(intervalo);
            
            int repetido = FALSO;
            int j = 0;
            
            while (j < i) {
                if (dataset[j] == chave) {
                    repetido = VERDADEIRO;
                    j = i;
                }
                j = j + 1;
            }
            
            if (repetido == FALSO) {
                dataset[i] = chave;
                i = i + 1;
            }
        }
        
        return dataset;
    }
    
    // -----------------------------------------------------------------------------------------
    // Embaralhar
    // -----------------------------------------------------------------------------------------
    
    public static void embaralhar(int[] array, int tamanho, GeradorAleatorio gerador) {
        int i = tamanho - 1;
        
        while (i > 0) {
            int j = gerador.proximo(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            i = i - 1;
        }
    }
    
    // -----------------------------------------------------------------------------------------
    // Medir Inserção
    // -----------------------------------------------------------------------------------------
    
    public static long medirInsercao(TabelaHash tabela, int[] dataset, int n, String funcao) {
        long inicio = System.nanoTime();
        int i = 0;
        
        while (i < n) {
            int chave = dataset[i];
            Registro reg = new Registro(chave);
            int hash = 0;
            
            if (compararStrings(funcao, "H_DIV", 5) == VERDADEIRO) {
                hash = tabela.hashDivisao(chave);
            } else {
                if (compararStrings(funcao, "H_MUL", 5) == VERDADEIRO) {
                    hash = tabela.hashMultiplicacao(chave);
                } else {
                    hash = tabela.hashDobramento(chave);
                }
            }
            
            tabela.inserir(reg, hash);
            i = i + 1;
        }
        
        return System.nanoTime() - inicio;
    }
    
    // -----------------------------------------------------------------------------------------
    // Preparar lote de busca (HIT/MISS)
    // -----------------------------------------------------------------------------------------
    
    public static int[][] prepararLoteBusca(int[] dataset, int n, long seed) {
        GeradorAleatorio gerador = new GeradorAleatorio(seed + 1000);
        
        int metade = n / 2;
        int[] lote = new int[n];
        int[] flags = new int[n];
        
        int[] copia = copiarArray(dataset, n);
        embaralhar(copia, n, gerador);
        
        int i = 0;
        while (i < metade) {
            lote[i] = copia[i];
            flags[i] = VERDADEIRO;
            i = i + 1;
        }
        
        i = metade;
        while (i < n) {
            int chave = MIN_CHAVE + gerador.proximo(MAX_CHAVE - MIN_CHAVE + 1);
            int existe = FALSO;
            int j = 0;
            
            while (j < n) {
                if (dataset[j] == chave) {
                    existe = VERDADEIRO;
                    j = n;
                }
                j = j + 1;
            }
            
            if (existe == FALSO) {
                lote[i] = chave;
                flags[i] = FALSO;
                i = i + 1;
            }
        }
        
        embaralhar(lote, n, gerador);
        
        int[][] resultado = new int[2][];
        resultado[0] = lote;
        resultado[1] = flags;
        
        return resultado;
    }
    
    public static int[] copiarArray(int[] original, int tamanho) {
        int[] copia = new int[tamanho];
        int i = 0;
        
        while (i < tamanho) {
            copia[i] = original[i];
            i = i + 1;
        }
        
        return copia;
    }
    
    // -----------------------------------------------------------------------------------------
    // Resultado busca
    // -----------------------------------------------------------------------------------------
    
    public static class ResultadoBusca {
        long tempoHits;
        long tempoMisses;
        int comparacoesHits;
        int comparacoesMisses;
        int colisoesLstHits;
        int colisoesLstMisses;
    }
    
    // -----------------------------------------------------------------------------------------
    // Medir busca
    // -----------------------------------------------------------------------------------------
    
    public static ResultadoBusca medirBusca(TabelaHash tabela, int[][] lote, int n, String funcao) {
        ResultadoBusca r = new ResultadoBusca();
        
        long somaTH = 0;
        long somaTM = 0;
        int somaCH = 0;
        int somaCM = 0;
        int somaCLH = 0;
        int somaCLM = 0;
        int hits = 0;
        int misses = 0;
        int i = 0;
        
        while (i < n) {
            int chave = lote[0][i];
            int flag = lote[1][i];
            int hash = 0;
            
            if (compararStrings(funcao, "H_DIV", 5) == VERDADEIRO) {
                hash = tabela.hashDivisao(chave);
            } else {
                if (compararStrings(funcao, "H_MUL", 5) == VERDADEIRO) {
                    hash = tabela.hashMultiplicacao(chave);
                } else {
                    hash = tabela.hashDobramento(chave);
                }
            }
            
            long ini = System.nanoTime();
            int cmp = tabela.buscar(chave, hash);
            long fim = System.nanoTime();
            
            if (flag == VERDADEIRO) {
                somaTH = somaTH + (fim - ini);
                somaCH = somaCH + cmp;
                hits = hits + 1;
                if (cmp > 0) {
                    somaCLH = somaCLH + (cmp - 1);
                }
            } else {
                somaTM = somaTM + (fim - ini);
                somaCM = somaCM + cmp;
                misses = misses + 1;
                if (cmp > 0) {
                    somaCLM = somaCLM + (cmp - 1);
                }
            }
            
            i = i + 1;
        }
        
        if (hits > 0) {
            r.tempoHits = somaTH;
            r.comparacoesHits = somaCH / hits;
            r.colisoesLstHits = somaCLH / hits;
        }
        
        if (misses > 0) {
            r.tempoMisses = somaTM;
            r.comparacoesMisses = somaCM / misses;
            r.colisoesLstMisses = somaCLM / misses;
        }
        
        return r;
    }
    
    // -----------------------------------------------------------------------------------------
    // Aquecimento
    // -----------------------------------------------------------------------------------------
    
    public static void aquecimento() {
        TabelaHash t = new TabelaHash(TAM_TAB_1);
        int[] ds = gerarDataset(TAM_DS_1, SEED_1);
        int i = 0;
        int tam = TAM_DS_1;
        
        while (i < tam) {
            t.inserir(new Registro(ds[i]), t.hashDivisao(ds[i]));
            i = i + 1;
        }
    }
    
    // -----------------------------------------------------------------------------------------
    // Obter parâmetros
    // -----------------------------------------------------------------------------------------
    
    public static int obterTamanhoTabela(int i) {
        if (i == 0) {
            return TAM_TAB_1;
        }
        if (i == 1) {
            return TAM_TAB_2;
        }
        return TAM_TAB_3;
    }
    
    public static int obterTamanhoDataset(int i) {
        if (i == 0) {
            return TAM_DS_1;
        }
        if (i == 1) {
            return TAM_DS_2;
        }
        return TAM_DS_3;
    }
    
    public static long obterSeed(int i) {
        if (i == 0) {
            return SEED_1;
        }
        if (i == 1) {
            return SEED_2;
        }
        return SEED_3;
    }
    
    public static String obterNomeFuncao(int i) {
        if (i == 0) {
            return "H_DIV";
        }
        if (i == 1) {
            return "H_MUL";
        }
        return "H_FOLD";
    }
    
    // -----------------------------------------------------------------------------------------
    // EXECUTAR EXPERIMENTOS
    // -----------------------------------------------------------------------------------------
    
    public static void executarExperimentos() {
        System.err.println("Iniciando experimentos...");
        int hashAutor = calcularHashAutor(NOME_AUTOR);
        System.err.println("Autor: " + NOME_AUTOR + " (hash_autor=" + hashAutor + ")");
        
        aquecimento();
        
        EscritorCSV escritor = new EscritorCSV();
        if (escritor.abrir(NOME_ARQUIVO_CSV) == VERDADEIRO) {
            System.err.println("Arquivo CSV criado: " + NOME_ARQUIVO_CSV);
        } else {
            System.err.println("ERRO: Nao foi possivel criar o arquivo CSV");
        }
        
        String cab = "m,n,func,seed,ins_ms,coll_tbl,coll_lst,find_ms_hits,find_ms_misses,cmp_hits,cmp_misses,checksum";
        System.out.println(cab);
        escritor.escreverLinha(cab);
        
        int it = 0;
        while (it < QTD_TAM_TAB) {
            int m = obterTamanhoTabela(it);
            int ifu = 0;
            
            while (ifu < QTD_FUNCOES) {
                String funcao = obterNomeFuncao(ifu);
                int ids = 0;
                
                while (ids < QTD_TAM_DS) {
                    int n = obterTamanhoDataset(ids);
                    int iseed = 0;
                    
                    while (iseed < QTD_SEEDS) {
                        long seed = obterSeed(iseed);
                        
                        System.err.println(funcao);
                        System.err.println("Tamanho da tabela (m): " + m);
                        System.err.println("Numero Teste: " + seed);
                        
                        int[] dataset = gerarDataset(n, seed);
                        int[][] loteBusca = prepararLoteBusca(dataset, n, seed);
                        
                        TabelaHash tabela = new TabelaHash(m);
                        long tempoInsercao = medirInsercao(tabela, dataset, n, funcao);
                        
                        ResultadoBusca rb = medirBusca(tabela, loteBusca, n, funcao);
                        
                        int checksum = hashAutor + m + n;
                        
                        String linha = "";
                        linha = concatenarInt(linha, m);
                        linha = concatenar(linha, ",");
                        linha = concatenarInt(linha, n);
                        linha = concatenar(linha, ",");
                        linha = concatenar(linha, funcao);
                        linha = concatenar(linha, ",");
                        linha = concatenarLong(linha, seed);
                        linha = concatenar(linha, ",");
                        linha = concatenarLong(linha, tempoInsercao);
                        linha = concatenar(linha, ",");
                        linha = concatenarInt(linha, tabela.colisoesTbl);
                        linha = concatenar(linha, ",");
                        linha = concatenarInt(linha, tabela.colisoesLst);
                        linha = concatenar(linha, ",");
                        linha = concatenarLong(linha, rb.tempoHits);
                        linha = concatenar(linha, ",");
                        linha = concatenarLong(linha, rb.tempoMisses);
                        linha = concatenar(linha, ",");
                        linha = concatenarInt(linha, rb.comparacoesHits);
                        linha = concatenar(linha, ",");
                        linha = concatenarInt(linha, rb.comparacoesMisses);
                        linha = concatenar(linha, ",");
                        linha = concatenarInt(linha, checksum);
                        
                        System.out.println(linha);
                        escritor.escreverLinha(linha);
                        
                        iseed = iseed + 1;
                    }
                    ids = ids + 1;
                }
                ifu = ifu + 1;
            }
            it = it + 1;
        }
        
        escritor.fechar();
    }
    
    public static String concatenarLong(String s, long num) {
        return s + num;
    }
    
    public static void main(String[] args) {
        executarExperimentos();
    }
}