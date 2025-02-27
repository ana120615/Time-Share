**# Time-Share**
**Repositório destinado ao projeto final da disciplina de Introdução à Programação II **

# Projeto FlexShare

Ideias que inspiram o projeto: 
 * https://www.webuyandselltimeshares.com/
 * https://www.sellmytimesharenow.com/
 * https://gavresorts.com.br/

## Integrantes do grupo 
 * Cauã Wallacy Gomes Teodoro - caua.wallacy@ufrpe.br
 * Laila Samara da Costa Lemos - laila.samara@ufrpe.br
 * Samara Monteiro Xavier - samara.xavier@ufrpe.br

## Descrição geral do projeto 
O sistema será uma aplicação voltada para o gerenciamento de bens compartilhados no modelo de time share. Ele permitirá que administradores ofertem produtos de alto valor, em frações de tempo específicas. Usuários poderão adquirir direitos de uso dessas frações, realizar reservas dentro de seus períodos contratados e, opcionalmente, fora desses períodos mediante pagamento de taxas adicionais.

O sistema automatiza o deslocamento das semanas adquiridas a cada ano, assegurando um uso justo ao longo do tempo e possibilita a revenda ou transferência de direitos entre usuários. Além disso, oferece funcionalidades de consulta e relatórios, promovendo praticidade e transparência para todos os envolvidos.

O objetivo do sistema é gerenciar e otimizar a compra, venda, reserva e uso compartilhado de bens, garantindo que múltiplos usuários possam usufruir de produtos de alto valor de forma justa, prática e organizada.

Objetivos Específicos:
Facilitar o cadastro, gerenciamento e oferta de bens pelos administradores.
Proporcionar aos usuários ferramentas para adquirir, reservar e transferir direitos de uso.
Automatizar o deslocamento de frações de tempo para garantir equilíbrio no uso anual.
Permitir a reserva no período contratado e aplicar taxas para reservas fora dele.
Oferecer relatórios e estatísticas para auxiliar no controle e na tomada de decisões.
O sistema busca atender às necessidades de administradores e usuários de forma eficiente, promovendo um modelo eficaz de compartilhamento de bens ao longo do tempo.

## Requisitos do projeto 
Requisitos para o sistema FlexShare

  * **REQ1** - Gerenciamento de administradores, que podem cadastrar e ofertar bens para venda.
   
  * **REQ2** - Gerenciamento de bens ofertados, incluindo informações como nome, descrição, capacidade de uso (ex.: número de pessoas), localização, preço e frações disponíveis para venda.
   
  * **REQ3** - Gerenciamento de usuários, que podem adquirir direitos de uso, realizar reservas e revender direitos.
   
  * **REQ4** - Registro de direitos de uso adquiridos por usuários, incluindo informações como bem associado, fração comprada (ex.: 1/52) e período móvel correspondente ao ano atual.

   
   **Operações de venda**
   * **REQ5** Compra de frações de bens, com controle automático de disponibilidade de frações e do deslocamento anual da semana adquirida.

   * **REQ6** Transferência ou revenda de direitos de uso entre usuários cadastrados, com atualização dos registros.


   **Operações de Reserva**
   * **REQ7** Consulta de períodos disponíveis para reserva, considerando o deslocamento da fração adquirida a cada ano.
     
   * **REQ8** Criação de reservas gratuitas dentro do intervalo permitido pelo direito adquirido, respeitando o período correspondente à fração comprada.
   * **REQ9** Criação de reservas fora do intervalo permitido, aplicando automaticamente uma taxa extra calculada com base no período.
     
   * **REQ10** Cancelamento de reservas pelos usuários ou administradores.

  
   **Consultas e Relatórios**

   * **REQ11** Consulta ao histórico de uso de um bem, incluindo reservas realizadas por diferentes usuários.

   * **REQ12** Estatísticas de vendas e reservas por bem, incluindo frações mais populares e períodos mais reservados.


   **Validação e Controle**
   * **REQ13** Validação automática de conflitos de datas para reservas e controle de taxas adicionais para períodos fora do intervalo permitido.
         
   * **REQ14** Ajuste automático do deslocamento da fração de semanas no início de cada ano (ex.: a fração de uma semana de 2024, de 1 a 7 de março, passará para 8 a 15 de março em 2025, e assim sucessivamente).


 
## Cronograma de MVPs com seleção de requisitos

MVP1 - Cadastros Básicos - 
[REQ1, REQ2, REQ3, REQ4]

Descrição: Implementação das funcionalidades para gerenciamento de administradores, usuários e bens ofertados.
Justificativa: Esses cadastros são fundamentais para estruturar o sistema e permitir a entrada e organização dos dados necessários para operações futuras.

MVP2 - Operações de Venda - 
[REQ5, REQ6]

Descrição: Desenvolvimento das funcionalidades de compra e venda das cotas, com controle automático de disponibilidade e atualização dos registros.
Justificativa: Após o cadastro dos br.ufrpe.time_share.dados, é essencial viabilizar a principal operação do sistema, que é a comercialização de direitos de uso.

MVP3 - Operações de Reserva -
[REQ7, REQ8, REQ9, REQ10]

Descrição: Implementação de funcionalidades relacionadas à reserva de bens, incluindo consulta de períodos disponíveis, criação de reservas, e cancelamento de reservas.
Justificativa: As reservas são o principal ponto de interação dos usuários com os bens adquiridos, sendo necessário estabelecer um controle robusto para esta funcionalidade.

MVP4 - Consultas e Relatórios - [REQ11, REQ12]

Descrição: Criação de funcionalidades para consulta do histórico de uso e estatísticas de vendas e reservas.
Justificativa: As consultas e relatórios fornecem informações estratégicas para usuários e administradores, promovendo transparência e auxiliando na tomada de decisões.

MVP5 - Validação e Controle - [REQ13, REQ14]

Descrição: Desenvolvimento de mecanismos automáticos para validar conflitos de datas, aplicar taxas adicionais e ajustar o deslocamento de frações de semanas no início de cada ano.
Justificativa: Esse módulo garante que o sistema mantenha a consistência e integridade dos br.ufrpe.time_share.dados ao longo do tempo, corrigindo automaticamente as mudanças de períodos.
