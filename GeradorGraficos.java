import java.util.Scanner;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;

/**
 * Classe GeradorGraficos le resultados_experimentos.csv e gera visualizacao HTML interativa.
 * Executa automaticamente apos os experimentos para criar graficos profissionais.
 */
public class GeradorGraficos {
    
    public static final int MAX_LINHAS = 1000;
    public static final int MAX_COLUNAS = 20;
    public static final int MAX_STRING = 500;
    
    /**
     * Classe interna para armazenar dados de uma linha do CSV.
     */
    public static class DadosLinha {
        public int m;
        public int n;
        public String func;
        public long seed;
        public double insMs;
        public double collTbl;
        public double collLst;
        public double findMsHits;
        public double findMsMisses;
        public int cmpHits;
        public int cmpMisses;
        public int checksum;
        public double alpha;
    }
    
    /**
     * Divide string por virgula.
     */
    public static String[] dividirPorVirgula(String linha) {
        String[] resultado = new String[MAX_COLUNAS];
        int indiceResultado = 0;
        String acumulador = "";
        
        for (int i = 0; i < linha.length(); i = i + 1) {
            char c = linha.charAt(i);
            
            if (c == ',') {
                resultado[indiceResultado] = acumulador;
                indiceResultado = indiceResultado + 1;
                acumulador = "";
            } else {
                acumulador = acumulador + c;
            }
        }
        
        if (indiceResultado < MAX_COLUNAS) {
            resultado[indiceResultado] = acumulador;
        }
        
        return resultado;
    }
    
    /**
     * Converte string para inteiro.
     */
    public static int stringParaInt(String texto) {
        int resultado = 0;
        int negativo = 0;
        int inicio = 0;
        
        if (texto.length() > 0) {
            char primeiro = texto.charAt(0);
            if (primeiro == '-') {
                negativo = 1;
                inicio = 1;
            }
        }
        
        for (int i = inicio; i < texto.length(); i = i + 1) {
            char c = texto.charAt(i);
            
            if (c >= '0' && c <= '9') {
                int digito = (int) c - (int) '0';
                resultado = resultado * 10 + digito;
            }
        }
        
        if (negativo == 1) {
            resultado = resultado * -1;
        }
        
        return resultado;
    }
    
    /**
     * Converte string para long.
     */
    public static long stringParaLong(String texto) {
        long resultado = 0;
        
        for (int i = 0; i < texto.length(); i = i + 1) {
            char c = texto.charAt(i);
            
            if (c >= '0' && c <= '9') {
                int digito = (int) c - (int) '0';
                resultado = resultado * 10 + (long) digito;
            }
        }
        
        return resultado;
    }
    
    /**
     * Converte string para double.
     */
    public static double stringParaDouble(String texto) {
        double parteInteira = 0.0;
        double parteFracionaria = 0.0;
        int achouPonto = 0;
        int casasDecimais = 0;
        
        for (int i = 0; i < texto.length(); i = i + 1) {
            char c = texto.charAt(i);
            
            if (c == '.') {
                achouPonto = 1;
            } else if (c >= '0' && c <= '9') {
                int digito = (int) c - (int) '0';
                
                if (achouPonto == 0) {
                    parteInteira = parteInteira * 10.0 + (double) digito;
                } else {
                    parteFracionaria = parteFracionaria * 10.0 + (double) digito;
                    casasDecimais = casasDecimais + 1;
                }
            }
        }
        
        double divisor = 1.0;
        for (int j = 0; j < casasDecimais; j = j + 1) {
            divisor = divisor * 10.0;
        }
        
        double resultado = parteInteira;
        if (divisor > 1.0) {
            resultado = resultado + (parteFracionaria / divisor);
        }
        
        return resultado;
    }
    
    /**
     * Le o arquivo CSV e retorna array de dados usando Scanner.
     */
    public static DadosLinha[] lerCSV(String nomeArquivo) {
        DadosLinha[] dados = new DadosLinha[MAX_LINHAS];
        int contador = 0;
        
        Scanner scanner = null;
        
        try {
            scanner = new Scanner(new FileInputStream(nomeArquivo));
            
            // Pula cabecalho
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            // Le dados
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                
                if (linha.trim().length() > 0) {
                    String[] partes = dividirPorVirgula(linha);
                    
                    DadosLinha dado = new DadosLinha();
                    dado.m = stringParaInt(partes[0]);
                    dado.n = stringParaInt(partes[1]);
                    dado.func = partes[2];
                    dado.seed = stringParaLong(partes[3]);
                    dado.insMs = stringParaDouble(partes[4]) / 1000000.0;
                    dado.collTbl = stringParaDouble(partes[5]);
                    dado.collLst = stringParaDouble(partes[6]);
                    dado.findMsHits = stringParaDouble(partes[7]) / 1000000.0;
                    dado.findMsMisses = stringParaDouble(partes[8]) / 1000000.0;
                    dado.cmpHits = stringParaInt(partes[9]);
                    dado.cmpMisses = stringParaInt(partes[10]);
                    dado.checksum = stringParaInt(partes[11]);
                    dado.alpha = (double) dado.n / (double) dado.m;
                    
                    dados[contador] = dado;
                    contador = contador + 1;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo CSV");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        
        DadosLinha[] resultado = new DadosLinha[contador];
        for (int i = 0; i < contador; i = i + 1) {
            resultado[i] = dados[i];
        }
        
        return resultado;
    }
    
    /**
     * Verifica se string contem substring.
     */
    public static int contem(String texto, String busca) {
        int encontrado = 0;
        
        for (int i = 0; i <= texto.length() - busca.length(); i = i + 1) {
            int match = 1;
            
            for (int j = 0; j < busca.length(); j = j + 1) {
                if (texto.charAt(i + j) != busca.charAt(j)) {
                    match = 0;
                }
            }
            
            if (match == 1) {
                encontrado = 1;
            }
        }
        
        return encontrado;
    }
    
    /**
     * Gera string com dados JSON para grafico de colisoes vs alpha.
     */
    public static String gerarDadosColisoesAlpha(DadosLinha[] dados) {
        String jsonDiv = "[";
        String jsonMul = "[";
        String jsonFold = "[";
        
        for (int i = 0; i < dados.length; i = i + 1) {
            DadosLinha d = dados[i];
            String ponto = "{x:" + d.alpha + ",y:" + d.collTbl + "}";
            
            if (contem(d.func, "DIV") == 1) {
                if (jsonDiv.length() > 1) jsonDiv = jsonDiv + ",";
                jsonDiv = jsonDiv + ponto;
            } else if (contem(d.func, "MUL") == 1) {
                if (jsonMul.length() > 1) jsonMul = jsonMul + ",";
                jsonMul = jsonMul + ponto;
            } else {
                if (jsonFold.length() > 1) jsonFold = jsonFold + ",";
                jsonFold = jsonFold + ponto;
            }
        }
        
        jsonDiv = jsonDiv + "]";
        jsonMul = jsonMul + "]";
        jsonFold = jsonFold + "]";
        
        return jsonDiv + "|" + jsonMul + "|" + jsonFold;
    }
    
    /**
     * Gera string com dados JSON para grafico de comparacoes.
     */
    public static String gerarDadosComparacoes(DadosLinha[] dados, int tipoHits) {
        String jsonDiv = "[";
        String jsonMul = "[";
        String jsonFold = "[";
        
        for (int i = 0; i < dados.length; i = i + 1) {
            DadosLinha d = dados[i];
            int valor = 0;
            
            if (tipoHits == 1) {
                valor = d.cmpHits;
            } else {
                valor = d.cmpMisses;
            }
            
            String ponto = "{x:" + d.alpha + ",y:" + valor + "}";
            
            if (contem(d.func, "DIV") == 1) {
                if (jsonDiv.length() > 1) jsonDiv = jsonDiv + ",";
                jsonDiv = jsonDiv + ponto;
            } else if (contem(d.func, "MUL") == 1) {
                if (jsonMul.length() > 1) jsonMul = jsonMul + ",";
                jsonMul = jsonMul + ponto;
            } else {
                if (jsonFold.length() > 1) jsonFold = jsonFold + ",";
                jsonFold = jsonFold + ponto;
            }
        }
        
        jsonDiv = jsonDiv + "]";
        jsonMul = jsonMul + "]";
        jsonFold = jsonFold + "]";
        
        return jsonDiv + "|" + jsonMul + "|" + jsonFold;
    }
    
    /**
     * Gera string com dados JSON para grafico de tempos.
     */
    public static String gerarDadosTempos(DadosLinha[] dados, int tipoHits) {
        String jsonDiv = "[";
        String jsonMul = "[";
        String jsonFold = "[";
        
        for (int i = 0; i < dados.length; i = i + 1) {
            DadosLinha d = dados[i];
            double valor = 0.0;
            
            if (tipoHits == 1) {
                valor = d.findMsHits;
            } else {
                valor = d.findMsMisses;
            }
            
            String ponto = "{x:" + d.alpha + ",y:" + valor + "}";
            
            if (contem(d.func, "DIV") == 1) {
                if (jsonDiv.length() > 1) jsonDiv = jsonDiv + ",";
                jsonDiv = jsonDiv + ponto;
            } else if (contem(d.func, "MUL") == 1) {
                if (jsonMul.length() > 1) jsonMul = jsonMul + ",";
                jsonMul = jsonMul + ponto;
            } else {
                if (jsonFold.length() > 1) jsonFold = jsonFold + ",";
                jsonFold = jsonFold + ponto;
            }
        }
        
        jsonDiv = jsonDiv + "]";
        jsonMul = jsonMul + "]";
        jsonFold = jsonFold + "]";
        
        return jsonDiv + "|" + jsonMul + "|" + jsonFold;
    }
    
    /**
     * Divide string por pipe.
     */
    public static String[] dividirPorPipe(String texto) {
        String[] resultado = new String[10];
        int indiceResultado = 0;
        String acumulador = "";
        
        for (int i = 0; i < texto.length(); i = i + 1) {
            char c = texto.charAt(i);
            
            if (c == '|') {
                resultado[indiceResultado] = acumulador;
                indiceResultado = indiceResultado + 1;
                acumulador = "";
            } else {
                acumulador = acumulador + c;
            }
        }
        
        if (indiceResultado < 10) {
            resultado[indiceResultado] = acumulador;
        }
        
        return resultado;
    }
    
    /**
     * Gera arquivo HTML com graficos interativos usando PrintWriter.
     */
    public static void gerarHTML(DadosLinha[] dados) {
        String dadosColisoes = gerarDadosColisoesAlpha(dados);
        String[] partsCol = dividirPorPipe(dadosColisoes);
        
        String dadosCmpHits = gerarDadosComparacoes(dados, 1);
        String[] partsCmpH = dividirPorPipe(dadosCmpHits);
        
        String dadosCmpMisses = gerarDadosComparacoes(dados, 0);
        String[] partsCmpM = dividirPorPipe(dadosCmpMisses);
        
        String dadosTempoHits = gerarDadosTempos(dados, 1);
        String[] partsTmpH = dividirPorPipe(dadosTempoHits);
        
        String dadosTempoMisses = gerarDadosTempos(dados, 0);
        String[] partsTmpM = dividirPorPipe(dadosTempoMisses);
        
        PrintWriter escritor = null;
        
        try {
            escritor = new PrintWriter(new FileOutputStream("graficos.html"));
            
            escritor.println("<!DOCTYPE html>");
            escritor.println("<html lang='pt-BR'>");
            escritor.println("<head>");
            escritor.println("<meta charset='UTF-8'>");
            escritor.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            escritor.println("<title>Análise de Tabela Hash - Gráficos Interativos</title>");
            escritor.println("<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>");
            escritor.println("<style>");
            escritor.println("body{font-family:Arial,sans-serif;background:#f5f5f5;padding:20px;margin:0}");
            escritor.println("h1{text-align:center;color:#333;margin-bottom:10px}");
            escritor.println("h2{text-align:center;color:#666;font-size:18px;font-weight:normal;margin-top:0}");
            escritor.println(".container{max-width:1400px;margin:0 auto}");
            escritor.println(".chart-box{background:white;padding:20px;margin:20px 0;border-radius:8px;box-shadow:0 2px 4px rgba(0,0,0,0.1)}");
            escritor.println(".chart-box h3{margin-top:0;color:#444;border-bottom:2px solid #4285f4;padding-bottom:10px}");
            escritor.println("canvas{max-height:400px}");
            escritor.println(".footer{text-align:center;margin-top:40px;color:#999;font-size:14px}");
            escritor.println("</style>");
            escritor.println("</head>");
            escritor.println("<body>");
            escritor.println("<div class='container'>");
            escritor.println("<h1>Análise de Tabela Hash por Encadeamento Separado</h1>");
            escritor.println("<h2>João Otávio Andreotti</h2>");
            
            escritor.println("<div class='chart-box'>");
            escritor.println("<h3>Gráfico 1: Colisões na Tabela vs Fator de Carga (α)</h3>");
            escritor.println("<canvas id='chart1'></canvas>");
            escritor.println("</div>");
            
            escritor.println("<div class='chart-box'>");
            escritor.println("<h3>Gráfico 2: Comparações em Buscas Bem-Sucedidas vs α</h3>");
            escritor.println("<canvas id='chart2'></canvas>");
            escritor.println("</div>");
            
            escritor.println("<div class='chart-box'>");
            escritor.println("<h3>Gráfico 3: Comparações em Buscas Malsucedidas vs α</h3>");
            escritor.println("<canvas id='chart3'></canvas>");
            escritor.println("</div>");
            
            escritor.println("<div class='chart-box'>");
            escritor.println("<h3>Gráfico 4: Tempo de Busca (Hits) vs α (ms)</h3>");
            escritor.println("<canvas id='chart4'></canvas>");
            escritor.println("</div>");
            
            escritor.println("<div class='chart-box'>");
            escritor.println("<h3>Gráfico 5: Tempo de Busca (Misses) vs α (ms)</h3>");
            escritor.println("<canvas id='chart5'></canvas>");
            escritor.println("</div>");
            
            escritor.println("<div class='footer'>Gerado automaticamente por GeradorGraficos.java</div>");
            escritor.println("</div>");
            
            escritor.println("<script>");
            escritor.println("const config={responsive:true,maintainAspectRatio:true,plugins:{legend:{display:true,position:'top'}}};");
            
            escritor.println("new Chart(document.getElementById('chart1'),{type:'scatter',data:{datasets:[");
            escritor.println("{label:'H_DIV',data:" + partsCol[0] + ",borderColor:'#4285f4',backgroundColor:'#4285f4',showLine:true},");
            escritor.println("{label:'H_MUL',data:" + partsCol[1] + ",borderColor:'#ea4335',backgroundColor:'#ea4335',showLine:true},");
            escritor.println("{label:'H_FOLD',data:" + partsCol[2] + ",borderColor:'#34a853',backgroundColor:'#34a853',showLine:true}");
            escritor.println("]},options:{...config,scales:{x:{title:{display:true,text:'Fator de Carga (α)'}},y:{title:{display:true,text:'Colisões'}}}}});");
            
            escritor.println("new Chart(document.getElementById('chart2'),{type:'scatter',data:{datasets:[");
            escritor.println("{label:'H_DIV',data:" + partsCmpH[0] + ",borderColor:'#4285f4',backgroundColor:'#4285f4',showLine:true},");
            escritor.println("{label:'H_MUL',data:" + partsCmpH[1] + ",borderColor:'#ea4335',backgroundColor:'#ea4335',showLine:true},");
            escritor.println("{label:'H_FOLD',data:" + partsCmpH[2] + ",borderColor:'#34a853',backgroundColor:'#34a853',showLine:true}");
            escritor.println("]},options:{...config,scales:{x:{title:{display:true,text:'Fator de Carga (α)'}},y:{title:{display:true,text:'Comparações (Hits)'}}}}});");
            
            escritor.println("new Chart(document.getElementById('chart3'),{type:'scatter',data:{datasets:[");
            escritor.println("{label:'H_DIV',data:" + partsCmpM[0] + ",borderColor:'#4285f4',backgroundColor:'#4285f4',showLine:true},");
            escritor.println("{label:'H_MUL',data:" + partsCmpM[1] + ",borderColor:'#ea4335',backgroundColor:'#ea4335',showLine:true},");
            escritor.println("{label:'H_FOLD',data:" + partsCmpM[2] + ",borderColor:'#34a853',backgroundColor:'#34a853',showLine:true}");
            escritor.println("]},options:{...config,scales:{x:{title:{display:true,text:'Fator de Carga (α)'}},y:{title:{display:true,text:'Comparações (Misses)'}}}}});");
            
            escritor.println("new Chart(document.getElementById('chart4'),{type:'scatter',data:{datasets:[");
            escritor.println("{label:'H_DIV',data:" + partsTmpH[0] + ",borderColor:'#4285f4',backgroundColor:'#4285f4',showLine:true},");
            escritor.println("{label:'H_MUL',data:" + partsTmpH[1] + ",borderColor:'#ea4335',backgroundColor:'#ea4335',showLine:true},");
            escritor.println("{label:'H_FOLD',data:" + partsTmpH[2] + ",borderColor:'#34a853',backgroundColor:'#34a853',showLine:true}");
            escritor.println("]},options:{...config,scales:{x:{title:{display:true,text:'Fator de Carga (α)'}},y:{title:{display:true,text:'Tempo (ms)'}}}}});");
            
            escritor.println("new Chart(document.getElementById('chart5'),{type:'scatter',data:{datasets:[");
            escritor.println("{label:'H_DIV',data:" + partsTmpM[0] + ",borderColor:'#4285f4',backgroundColor:'#4285f4',showLine:true},");
            escritor.println("{label:'H_MUL',data:" + partsTmpM[1] + ",borderColor:'#ea4335',backgroundColor:'#ea4335',showLine:true},");
            escritor.println("{label:'H_FOLD',data:" + partsTmpM[2] + ",borderColor:'#34a853',backgroundColor:'#34a853',showLine:true}");
            escritor.println("]},options:{...config,scales:{x:{title:{display:true,text:'Fator de Carga (α)'}},y:{title:{display:true,text:'Tempo (ms)'}}}}});");
            
            escritor.println("</script>");
            escritor.println("</body>");
            escritor.println("</html>");
            
            System.out.println("Arquivo graficos.html gerado com sucesso!");
            
        } catch (Exception e) {
            System.out.println("Erro ao gerar HTML");
        } finally {
            if (escritor != null) {
                escritor.close();
            }
        }
    }
    
    /**
     * Metodo principal.
     */
    public static void main(String[] args) {
        System.out.println("Gerando graficos a partir de resultados_experimentos.csv...");
        
        DadosLinha[] dados = lerCSV("resultados_experimentos.csv");
        
        System.out.println("Total de linhas lidas: " + dados.length);
        
        gerarHTML(dados);
        
        System.out.println("Processo concluido!");
        System.out.println("Abra o arquivo graficos.html no navegador para visualizar os graficos.");
    }
}