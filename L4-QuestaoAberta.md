## READ.md: Transações Simultâneas em Autorizadores
Introdução

Este documento discute a problemática de transações simultâneas em sistemas de autorização de pagamentos, focando na garantia de que apenas uma transação por conta seja processada em um determinado momento. A discussão se baseia no contexto de um autorizador completo, considerando as restrições de tempo e a natureza síncrona das solicitações.

Problema

Em um cenário onde o mesmo cartão de crédito pode ser utilizado em diferentes serviços online, existe a possibilidade de que duas ou mais solicitações de transação sejam feitas simultaneamente para a mesma conta. Essa situação pode levar a problemas como:

Duplicidade de cobranças: O valor da transação pode ser cobrado mais de uma vez.
Exaustão de limites: Transações podem ser recusadas indevidamente devido a um cálculo incorreto do limite disponível.
Inconsistências de dados: O estado da conta pode se tornar inconsistente, gerando dificuldades na reconciliação e na geração de relatórios.
Solução Proposta

Para garantir a consistência e a integridade das transações, é fundamental implementar um mecanismo de controle de concorrência. A seguir, são apresentadas algumas estratégias que podem ser utilizadas:

1. Lock Pessimista (Pessimistic Locking)

Funcionamento: Antes de processar uma transação, um lock é adquirido sobre a conta. Se outro processo já estiver processando uma transação para a mesma conta, a solicitação atual será bloqueada até que o lock seja liberado.
Vantagens: Garante a exclusão mútua, evitando que múltiplas transações sejam processadas simultaneamente para a mesma conta.
Desvantagens: Pode levar a um alto nível de concorrência, especialmente em cenários de alta carga, impactando o desempenho do sistema.
2. Lock Otimista (Optimistic Locking)

Funcionamento: A transação é processada sem adquirir um lock. Antes de confirmar a transação, uma verificação é feita para garantir que nenhum outra transação tenha modificado os dados da conta desde o início da operação. Se houver alguma modificação, a transação é abortada e o cliente é notificado.
Vantagens: Geralmente apresenta um melhor desempenho do que o lock pessimista, pois evita bloqueios desnecessários.
Desvantagens: Requer um mecanismo de detecção de conflitos, o que pode adicionar complexidade ao sistema.
3. Transações ACID

Funcionamento: Utilizar um sistema de gerenciamento de banco de dados relacional (RDBMS) que suporte transações ACID (Atomicidade, Consistência, Isolamento e Durabilidade). As transações ACID garantem a consistência dos dados, mesmo em caso de falhas.
Vantagens: Oferece um alto nível de isolamento e durabilidade, garantindo a integridade dos dados.
Desvantagens: Pode ser mais complexo de implementar e configurar do que outras soluções.
4. Padrões de Projeto Concorrentes

Funcionamento: Utilizar padrões de projeto como Monitor, Semaforo ou Mutex para controlar o acesso a recursos compartilhados.
Vantagens: Flexibilidade para implementar diferentes estratégias de controle de concorrência.
Desvantagens: Requer um profundo conhecimento dos padrões de projeto e das suas implicações.
Considerações Adicionais

Escalabilidade: A solução escolhida deve ser capaz de escalar para atender a um grande volume de transações.
Desempenho: A latência das transações deve ser mantida dentro dos limites estabelecidos (menos de 100 ms).
Disponibilidade: O sistema deve ser altamente disponível, garantindo que as transações sejam processadas mesmo em caso de falhas.
Segurança: É fundamental garantir a segurança das informações financeiras, implementando mecanismos de autenticação e autorização robustos.