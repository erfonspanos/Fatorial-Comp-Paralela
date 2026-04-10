# Fatorial 500.000 (Metodologia de Foster)

# Alunos: Erfon Spanos e Rodrigo Cirino

## Performance
* **Input:** 500.000!
* **Resultado:** Um número com mais de **2,6 milhões de dígitos**.
* **Tempo de Execução:** Na casa dos **2,4 segundos** (explorando todos os núcleos da CPU).

---

## (Conceitos Aplicados)

Para estruturarmos o algoritmo, seguimos os quatro pilares da Metodologia de Foster:

### 1. Particionamento (Partitioning)
Aqui a gente quebra o problema pra achar o paralelismo. 
* **Como fizemos:** Usamos **Divisão e Conquista**, quebrando o intervalo recursivamente em subtarefas menores.
* **Alternativa:** Como o senhor comentou em sala, daria pra fazer via **Empilhamento**. Usar uma pilha explícita daria um controle ainda maior sobre a memória *Stack* da JVM, evitando gargalos em escalas muito maiores.
* **No código:** Esta presente na criação das instâncias de `TarefaFatorial` dividindo o intervalo pelo `meio`.

### 2. Aglomeração (Agglomeration)
Criar thread demais gera "bagunça" (overhead). O segredo é agrupar.
* **No código:** Definimos um limite (limiar) de **1.000 números**. Se o intervalo for menor que isso, o Java para de dividir e resolve de forma sequencial pra ser mais eficiente.

### 3. Comunicação (Communication)
As tarefas precisam "conversar" pra chegar no resultado final.
* **No código:** Usamos o `fork()` pra disparar as tarefas e o `join()` pra sincronizar tudo. No fim, os resultados parciais se fundem através da multiplicação de `BigInteger`.

### 4. Mapeamento (Mapping)
É a hora de distribuirmos o trabalho pros núcleos da CPU.
* **No código:** O `ForkJoinPool` faz o trabalho pesado. Ele identifica quantas CPUs temos disponíveis e usa a técnica de **Work-Stealing** — se um núcleo termina rápido, ele "rouba" tarefa de outro que está sobrecarregado, mantendo o processamento sempre em 100%.
