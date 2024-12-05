**# Time-Share**
**Repositório destinado ao projeto final da disciplina de Introdução à Programação II **

# Projeto FlexShare

Ideias que inspiram o projeto (sites, blogs, sistemas, fontes de dados): 
 * https://dados.gov.br/
 * https://play.google.com
 * https://www.apple.com/br/app-store/
 * https://gtagenda2030.org.br/ods/ (17 Objetivos de Desenvolvimento Sustentável - ODS)

## Integrantes do grupo 
 * Ana Beatriz Gomes - ana.beatriz@ufrpe.br
 * Ana Raquel Rodrigues da Silva - ana.raquel@ufrpe.br
 * Cauã Wallacy Gomes Teodoro - caua.wallacy@ufrpe.br
 * Laila Samara da Costa Lemos - laila.samara@ufrpe.br
 * Samara Monteiro Xavier - samara.xavier@ufrpe.br

## Descrição geral do projeto 
Nesta descrição, apresente um texto resumido com a(s) principal(is) funcionalidade(s) do sistema. 
É muito importante que você consiga responder em uma única frase: qual a principal funcionalidade do seu sistema? 
A partir disso, você deve detalhar as funcionalidades do sistema tentando responder às perguntas:
 1. Quem vai usar o programa?
 2. Que serviços são “necessários” (leia-se: importantes para os clientes e usuários)?
 3. Quais serviços cada usuário pode executar?

## Requisitos do projeto 
Requisitos para sistema Time Share de (escolher o que será ofertado).

  * **REQ1** - Gerenciamento de administradores, que podem cadastrar e ofertar bens para venda.
   
    * **REQ2** - Gerenciamento de bens ofertados, incluindo informações como nome, descrição, capacidade de uso (ex.: número de pessoas), localização, preço e frações disponíveis para venda.
   
    * **REQ3** - Gerenciamento de usuários, que podem adquirir direitos de uso, realizar reservas e revender direitos.
   
    * **REQ4** - Registro de direitos de uso adquiridos por usuários, incluindo informações como bem associado, fração comprada (ex.: 1/52) e período móvel correspondente ao ano atual.
   
   **Operações de venda**
    * **REQ5** Compra de frações de bens, com controle automático de disponibilidade de frações e do deslocamento anual da semana adquirida.

   * **REQ6** Transferência ou revenda de direitos de uso entre usuários cadastrados, com atualização dos registros.

   **Operações de Reserva**
   * **REQ07** Consulta de períodos disponíveis para reserva, considerando o deslocamento da fração adquirida a cada ano.
     
   * **REQ08** Criação de reservas gratuitas dentro do intervalo permitido pelo direito adquirido, respeitando o período correspondente à fração comprada.
   * **REQ09** Criação de reservas fora do intervalo permitido, aplicando automaticamente uma taxa extra calculada com base no período.
     
   * **REQ10** Cancelamento de reservas pelos usuários ou administradores.
  
     **Consultas e Relatórios**

     * **REQ11** Consulta ao histórico de uso de um bem, incluindo reservas realizadas por diferentes usuários.
       
     * **REQ12** Relatório de disponibilidade de um bem em períodos futuros, considerando frações ainda não vendidas e reservas pendentes.

     * **REQ13** Estatísticas de vendas e reservas por bem, incluindo frações mais populares e períodos mais reservados.

       **Validação e Controle**
       * **REQ14** Validação automática de conflitos de datas para reservas e controle de taxas adicionais para períodos fora do intervalo permitido.
         
       * **REQ15** Ajuste automático do deslocamento da fração de semanas no início de cada ano (ex.: a fração de uma semana de 2024, de 1 a 7 de março, passará para 8 a 15 de março em 2025, e assim sucessivamente).


 
## Cronograma de MVPs com seleção de requisitos
Liste os possíveis MVPs associando-os diretamente aos requisitos do projeto e faça uma breve descrição e justificativa de cada MVP. 
A descrição deve ser breve e justificar as escolhas. 
Exemplo de sequência de MVPs:
* **MVP1 - Cadastros** - [REQ1, REQ2]: cadastros básicos de todas as entidades
* **MVP2 - Regras** - [REQ3, REQ4]: implementação de regras mínimas para venda de produtos com cartão de crédito.
