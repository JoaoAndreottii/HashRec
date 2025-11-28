# Tabela Hash - Encadeamento Separado

## Sobre

Implementação de tabela hash com encadeamento separado em Java. Analisa desempenho de 3 funções de hash diferentes: divisão, multiplicação e dobramento.

**Autor:** João Otávio Andreotti

## Arquivos

- `Registro.java` - classe do registro com codigo
- `No.java` - nó da lista encadeada
- `TabelaHash.java` - tabela hash e funções de hashing
- `ResultadoBusca.java` - guarda resultados das buscas
- `Principal.java` - roda os experimentos
- `GeradorGraficos.java` - gera os graficos HTML

## Como usar

Compilar tudo:

```bash
javac *.java
```

Executar:

```bash
java Principal
```

Isso vai gerar o `resultados.csv` com todos os dados.

Depois rodar:

```bash
java GeradorGraficos
```

Vai criar o `graficos.html` e abrir no navegador automaticamente.

## Configurações

**Tamanhos de tabela (m):**

- 1009
- 10007
- 100003

**Tamanhos de dataset (n):**

- 1.000
- 10.000
- 100.000

**Seeds:**

- 137
- 271828
- 314159

**Funções de hash:**

- H_DIV - divisão (k mod m)
- H_MUL - multiplicação (usa constante A = 0.618...)
- H_FOLD - dobramento (divide em blocos de 3 digitos)

## Resultados

O arquivo CSV tem essas colunas:

```
m,n,func,seed,ins_ms,coll_tbl,coll_lst,find_us_hits,find_us_misses,cmp_hits,cmp_misses,checksum
```

Os graficos mostram:

1. Colisões na tabela vs fator de carga
2. Comparações em buscas bem-sucedidas
3. Comparações em buscas mal-sucedidas
4. Tempo de busca (hits)
5. Tempo de busca (misses)

## Observações

- Cada experimento roda 5 vezes pra pegar a media
- Tem aquecimento da JVM antes dos testes
- As seeds são fixas pra garantir reprodutibilidade
- O checksum é calculado somando os 10 primeiros valores de hash

## Problemas comuns

Se der erro ao gerar graficos, verifique se o arquivo `resultados.csv` existe na pasta.

Se não abrir o navegador automatico, abra o arquivo `graficos.html` manualmente.

## Frase importante

"Distribuições mais uniformes reduzem o custo médio no encadeamento separado."
