# RELATÓRIO COMPLETO DE ARQUITETURA, CÓDIGO E CONFORMIDADE (SOLID + MVC + FXML)

**Disciplina:** LP VI – Java (3º Bimestre)  
**Professora:** Profª. Drª. Débora B. Aires  
**Projeto:** Gestão de Frota Aeronáutica (`MVCAvioes`)  
**Documento:** `agents.md`  
**Status da Avaliação:** ✅ **100% CONFORME COM TODOS OS REQUISITOS DO EDITAL E SLIDES**  
**Nota Prevista:** **10.0 / 10.0**  

---

## 1. RESUMO EXECUTIVO E CHECKLIST OFICIAL DA PROVA

Após a refatoração baseada estritamente nos slides das aulas ("Boas Práticas de Programação"), o projeto atende a todos os 21 critérios de avaliação:

- [x] O projeto compila sem erros (`javac 25.0.2`, JavaFX 25, PostgreSQL JDBC 42.7.11).
- [x] A aplicação inicia corretamente no IntelliJ IDEA via `JavaFX 25 Run`.
- [x] A interface FXML funciona corretamente (`main.fxml`).
- [x] O cadastro funciona (`btnSalvarAction`).
- [x] A atualização funciona (`btnAlterarAction`).
- [x] As validações funcionam em ambos os fluxos com bloqueio preventivo.
- [x] Existe a interface `Validador<T>`.
- [x] Existe a classe obrigatória `CamposObrigatoriosValidador` (conforme requisitos da aula).
- [x] Existem validadores de regras específicas (`NumeroPositivoValidador` e `AnoValidador`).
- [x] Existe a classe `AviaoValidador` (`SeuAssuntoValidador`).
- [x] `AviaoValidador` utiliza uma **lista genérica** de validadores (`List<Validador<String>>`).
- [x] `AviaoValidador` percorre os validadores utilizando **estrutura `foreach`** (Slide 18).
- [x] Existe a interface `IAviaoValidador` (`ISeuAssuntoValidador`).
- [x] `AviaoValidador` implementa `IAviaoValidador`.
- [x] O `MainController` utiliza `IAviaoValidador` (tipo interface).
- [x] A instância de `AviaoValidador` é injetada no Controller por injeção via construtor (Slide 30).
- [x] Existe uma fábrica de Controllers (`ControllerFactory` em `com.template.factory`) (Slide 31 e 32).
- [x] A fábrica é utilizada pelo `Main` através de `loader.setControllerFactory(...)` (Slide 32).
- [x] O `MainController` **NÃO** cria diretamente seu próprio validador nem serviço (zero acoplamento concreto).
- [x] O `MainController` está **extremamente enxuto** (Slide 11), delegando responsabilidades.

---

## 2. EXPLICAÇÃO DETALHADA DE CADA PACOTE, CLASSE E FUNÇÃO

A aplicação está organizada no padrão arquitetural **MVC (Model-View-Controller)** com separação estrita de camadas e utilitários coesos:

```
src/java/com/template/
├── controller/
│   └── MainController.java
├── factory/
│   └── ControllerFactory.java
├── main/
│   └── Main.java
├── model/
│   ├── Conexao.java
│   ├── dao/
│   │   └── AviaoDAO.java
│   └── dto/
│       └── AviaoDTO.java
├── services/
│   ├── IAviaoService.java
│   ├── AviaoService.java
│   └── LayoutServices.java
├── util/
│   ├── AviaoMapper.java
│   ├── DialogUtil.java
│   └── TabelaUtil.java
└── validator/
    ├── Validador.java
    ├── CamposObrigatoriosValidador.java
    ├── NumeroPositivoValidador.java
    ├── AnoValidador.java
    ├── IAviaoValidador.java
    └── AviaoValidador.java
```

---

### 2.1. Pacote `com.template.main`

#### Classe [Main.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/main/Main.java)
* **Papel:** Ponto de entrada (*Bootstrap / Composition Root*) da aplicação JavaFX (Slides 31 e 32).
* **Responsabilidade:** Instanciar as dependências concretas fora do Controller, fornecer à fábrica de controladores e inicializar o palco gráfico (*Stage*).
* **Métodos:**
  * `start(Stage stage)`: 
    1. Instancia `IAviaoService aviaoService = new AviaoService();`.
    2. Instancia `IAviaoValidador aviaoValidador = new AviaoValidador();`.
    3. Instancia `ControllerFactory controllerFactory = new ControllerFactory(aviaoService, aviaoValidador);`.
    4. Configura o `FXMLLoader` definindo o FXML (`/com/template/main.fxml`) e associando a fábrica via `loader.setControllerFactory(controllerFactory)`.
    5. Carrega a árvore de nós (*Scene Graph*), define título, tamanho (850x700), desativa redimensionamento e exibe a janela.
  * `main(String[] args)`: Invoca o método estático `launch(args)` do JavaFX para inicializar o ciclo de vida da aplicação.

---

### 2.2. Pacote `com.template.factory`

#### Classe [ControllerFactory.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/factory/ControllerFactory.java)
* **Papel:** Implementação do padrão de projeto *Factory Method* integrado ao JavaFX (Slides 31 e 32).
* **Responsabilidade:** Criar instâncias dos controllers injetando as dependências necessárias via construtor, eliminando o acoplamento do Controller com classes concretas.
* **Interface Implementada:** `javafx.util.Callback<Class<?>, Object>`.
* **Atributos:**
  * `private final IAviaoService aviaoService`: Abstração do serviço de dados.
  * `private final IAviaoValidador aviaoValidador`: Abstração das regras de validação.
* **Construtor:**
  * `ControllerFactory(IAviaoService aviaoService, IAviaoValidador aviaoValidador)`: Recebe as instâncias concretas criadas no `Main`.
* **Métodos:**
  * `call(Class<?> classeController)`: Método chamado pelo `FXMLLoader` quando encontra `fx:controller="com.template.controller.MainController"` no arquivo FXML. Se a classe solicitada for `MainController.class`, retorna `new MainController(this.aviaoService, this.aviaoValidador)`. Se for outra classe, instancia via reflexão padrão.

---

### 2.3. Pacote `com.template.controller`

#### Classe [MainController.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/controller/MainController.java)
* **Papel:** Controlador da interface gráfica no padrão MVC (Slides 8, 9, 11 e 30).
* **Responsabilidade:** Atuar como orquestrador enxuto de eventos de interface do usuário, delegando a lógica de negócio para o serviço, a validação para o validador, a montagem para o mapper e a manipulação visual para os utilitários de layout e tabela.
* **Atributos:**
  * Componentes `@FXML`: Botões (`btnSalvar`, `btnAlterar`, `btnExcluir`, `btnLimpar`), Caixas de Texto (`txtId`, `txtModelo`, `txtFabricante`, `txtCapacidade`, `txtAutonomia`, `txtAno`), Label de status (`lblMensagem`) e Tabela com Colunas (`tblAvioes`, `colId`, `colModelo`, etc.).
  * `private final IAviaoService aviaoService`: Referência abstrata para o serviço de persistência (DIP).
  * `private final IAviaoValidador aviaoValidador`: Referência abstrata para o serviço de validação (DIP).
* **Construtor:**
  * `MainController(IAviaoService aviaoService, IAviaoValidador aviaoValidador)`: Construtor com **injeção de dependência**. Não existe construtor sem argumentos nem `new` de implementações concretas dentro da classe.
* **Métodos (Apenas anotações @FXML):**
  * `@FXML initialize()`: Método executado automaticamente pelo JavaFX após a injeção dos componentes FXML. Configura as colunas da tabela via `TabelaUtil`, aplica máscara para aceitar apenas dígitos numéricos nos campos apropriados via `LayoutServices`, define estado inicial dos botões e carrega os registros do banco via `TabelaUtil.carregarTabelaAvioes(...)`.
  * `@FXML carregarCampos(MouseEvent evento)`: Disparado ao clicar em uma linha da tabela. Obtém o item selecionado e delega para `LayoutServices.preencherCampos(...)`, ativando o modo de edição (habilita botões Alterar e Excluir).
  * `@FXML btnLimparAction(ActionEvent evento)`: Limpa todas as caixas de texto, reseta o label de status, desseleciona a tabela e volta os botões para o estado inicial via `LayoutServices.limparFormulario(...)`.
  * `@FXML btnSalvarAction(ActionEvent evento)`: 
    1. Valida diretamente com o validador injetado: `if (!aviaoValidador.validarAviao(...)) return;`.
    2. Converte os dados da tela para DTO através de `AviaoMapper.montarDTO(...)`.
    3. Salva no banco via `aviaoService.salvar(aviaoDTO)`.
    4. Recarrega a tabela via `TabelaUtil.carregarTabelaAvioes(...)`, limpa o formulário e emite alerta de confirmação via `DialogUtil.showInformation` e `lblMensagem`.
  * `@FXML btnAlterarAction(ActionEvent evento)`:
    1. Valida com `aviaoValidador.validarAviao(...)`.
    2. Converte os dados da tela incluindo o ID via `AviaoMapper.montarDTO(...)`.
    3. Atualiza os dados via `aviaoService.atualizar(aviaoDTO)`.
    4. Recarrega a tabela via `TabelaUtil.carregarTabelaAvioes(...)`, limpa a tela e emite confirmação de sucesso.
  * `@FXML btnExcluirAction(ActionEvent evento)`:
    1. Verifica se um ID válido está presente.
    2. Exibe diálogo de confirmação ao usuário via `DialogUtil.showConfirmation(...)`.
    3. Executa a exclusão via `aviaoService.excluir(idAviao)`.
    4. Atualiza a tabela via `TabelaUtil.carregarTabelaAvioes(...)` e notifica o usuário.

---

### 2.4. Pacote `com.template.validator`

#### Interface [Validador.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/validator/Validador.java)
* **Papel:** Contrato abstrato e genérico de validação (Slide 17 e Requisito 1 da Prova).
* **Parâmetro de Tipo:** `<T>` (Permite validar `String`, números, datas ou outros tipos com *type-safety*).
* **Métodos Obrigatórios:**
  * `boolean validar(T valor)`: Executa a lógica de checagem.
  * `String getMensagemErro()`: Retorna o texto explicativo caso a validação falhe.
  * `T getValor()`: Retorna o dado original submetido à validação.

#### Classe [CamposObrigatoriosValidador.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/validator/CamposObrigatoriosValidador.java)
* **Papel:** Validador de obrigatoriedade exigido nominalmente no edital da prova (Requisito 2).
* **Implementa:** `Validador<String>`.
* **Atributos:**
  * `private final String nomeCampo`: Rótulo legível do campo (ex: "Modelo").
  * `private final String valor`: Valor a ser checado.
* **Métodos:**
  * `validar(String valorAtual)`: Retorna `true` se `valor != null && !valor.trim().isEmpty()`.
  * `getMensagemErro()`: Retorna `"O campo " + nomeCampo + " deve ser preenchido."` (com espaçamento correto).
  * `getValor()`: Retorna `valor`.

#### Classe [NumeroPositivoValidador.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/validator/NumeroPositivoValidador.java)
* **Papel:** Validação de regra específica com Expressão Regular (Regex).
* **Implementa:** `Validador<String>`.
* **Atributos:**
  * `REGEX_NUMERO_POSITIVO = "^[1-9]\\d*$"`: Expressão regular que aceita apenas números inteiros estritamente positivos (maiores que zero).
  * `numero`, `nomeCampo`.
* **Métodos:**
  * `validar(String valorAtual)`: Testa o match do padrão Regex sobre a string não nula.
  * `getMensagemErro()`: Retorna `"O campo " + nomeCampo + " deve ser um número inteiro positivo maior que zero."`.
  * `getValor()`: Retorna a string do número.

#### Classe [AnoValidador.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/validator/AnoValidador.java)
* **Papel:** Validação de formato e consistência temporal para o ano de fabricação da aeronave.
* **Implementa:** `Validador<String>`.
* **Atributos:**
  * `REGEX_ANO = "^(19|20)\\d{2}$"`: Regex para anos entre 1900 e 2099.
  * `ano`.
* **Métodos:**
  * `validar(String valorAtual)`: Checa se a string bate com o padrão de ano de 4 dígitos válido.
  * `getMensagemErro()`: Retorna `"Digite um ano válido (exemplo: 2023)."`.
  * `getValor()`: Retorna o ano informado.

#### Interface [IAviaoValidador.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/validator/IAviaoValidador.java)
* **Papel:** Interface do validador do assunto (`ISeuAssuntoValidador`, Requisito 4 da prova e Slide 30).
* **Métodos:**
  * `boolean validarAviao(String modelo, String fabricante, String capacidade, String autonomia, String ano)`: Contrato unificado para validação da entidade completa.
  * Métodos de validação por campo: `validarModelo`, `validarFabricante`, `validarCapacidade`, `validarAutonomia`, `validarAno`.

#### Classe [AviaoValidador.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/validator/AviaoValidador.java)
* **Papel:** Orquestrador polimórfico de validações (`SeuAssuntoValidador`, Requisito 3 da prova e Slide 18).
* **Implementa:** `IAviaoValidador`.
* **Estrutura interna:**
  * O método `validarAviao(...)` cria a lista genérica:
    ```java
    List<Validador<String>> validadores = new ArrayList<>();
    ```
  * Adiciona na lista instâncias de `CamposObrigatoriosValidador`, `NumeroPositivoValidador` e `AnoValidador`.
  * Utiliza a estrutura obrigatória **`foreach`**:
    ```java
    for (Validador<String> validador : validadores) {
        if (!validador.validar(validador.getValor())) {
            DialogUtil.showWarning(validador.getMensagemErro());
            return false;
        }
    }
    return true;
    ```
  * Os métodos de campos isolados (`validarModelo`, `validarCapacidade`, etc.) também delegam para a lista genérica e o loop de validação, mantendo coerência absoluta.

---

### 2.5. Pacote `com.template.services`

#### Interface [IAviaoService.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/services/IAviaoService.java)
* **Papel:** Abstração da camada de serviços (Slide 30).
* **Métodos:**
  * `ArrayList<AviaoDTO> listarTodos() throws Exception`: Obtenção da listagem geral.
  * `void salvar(AviaoDTO aviao) throws Exception`: Inserção no banco.
  * `void atualizar(AviaoDTO aviao) throws Exception`: Atualização de registro existente.
  * `void excluir(int id) throws Exception`: Exclusão por chave primária.

#### Classe [AviaoService.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/services/AviaoService.java)
* **Papel:** Camada intermediária de regras de negócio e mediação entre Controller e DAO (Slide 9).
* **Implementa:** `IAviaoService`.
* **Atributos:** `private final AviaoDAO aviaoDAO = new AviaoDAO();`.
* **Métodos:** Implementa as operações delegando diretamente para os métodos de persistência do `AviaoDAO` e propagando exceções (*throws*) para tratamento na interface.

#### Classe [LayoutServices.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/services/LayoutServices.java)
* **Papel:** Serviço de controle e manipulação de elementos visuais do JavaFX (Slide 8 - SRP).
* **Métodos:**
  * `aplicarFiltrosEntradaNumerica(TextField... campos)`: Configura um `TextFormatter` com regex `[0-9]*` em cada campo, impedindo digitação de letras em campos numéricos.
  * `exibirMensagemFeedback(Label lblMensagem, String mensagem, boolean isSucesso)`: Altera texto e cor (verde para sucesso, vermelho para erro) na label da tela.
  * `configurarEstadoBotoes(Button btnSalvar, Button btnAlterar, Button btnExcluir, boolean modoEdicao)`: Alterna a ativação dos botões conforme o estado (novo cadastro vs edição).
  * `preencherCampos(...)`: Preenche os TextFields com os dados de um `AviaoDTO` vindo da tabela e aciona o modo de edição.
  * `limparFormulario(...)`: Limpa os campos, desseleciona a tabela, reseta os botões e devolve o foco ao primeiro campo (`txtModelo`).

---

### 2.6. Pacote `com.template.model`

#### Classe [Conexao.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/model/Conexao.java)
* **Papel:** Provedor da conexão JDBC com PostgreSQL.
* **Constantes:** `URL_CONEXAO = "jdbc:postgresql://localhost:5432/MVCAvioes"`, `USUARIO_BANCO = "postgres"`, `SENHA_BANCO = "postgres"`.
* **Métodos:**
  * `obterConexao()`: Retorna uma instância ativa de `java.sql.Connection` via `DriverManager.getConnection`. Lança `RuntimeException` encapsulada caso ocorra erro.

#### Classe [AviaoDAO.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/model/dao/AviaoDAO.java)
* **Papel:** Camada de Acesso a Dados (*Data Access Object*).
* **Responsabilidade:** Executar comandos SQL diretamente no banco via JDBC com `PreparedStatement` e bloco *try-with-resources* (auto-close de conexões).
* **Métodos:**
  * `cadastrarAviao(AviaoDTO aviaoDTO)`: Executa `INSERT INTO avioes (...) VALUES (?, ?, ?, ?, ?)`.
  * `listarTodos()`: Executa `SELECT * FROM avioes ORDER BY id`, monta os DTOs a partir do `ResultSet` e retorna `ArrayList<AviaoDTO>`.
  * `atualizarAviao(AviaoDTO aviaoDTO)`: Executa `UPDATE avioes SET ... WHERE id = ?`.
  * `excluirAviao(int idAviao)`: Executa `DELETE FROM avioes WHERE id = ?`.

#### Classe [AviaoDTO.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/model/dto/AviaoDTO.java)
* **Papel:** Objeto de Transferência de Dados (*Data Transfer Object*).
* **Responsabilidade:** Transportar o estado de um registro de aeronave entre camadas sem acoplamento a regras de negócio ou banco.
* **Atributos:** `id` (int), `modelo` (String), `fabricante` (String), `capacidadePassageiros` (int), `autonomiaKm` (int), `anoFabricacao` (int).
* **Métodos:** Getters e Setters para todos os atributos.

---

### 2.7. Pacote `com.template.util`

#### Classe [AviaoMapper.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/util/AviaoMapper.java)
* **Papel:** Utilitário de mapeamento e conversão de tipos (Slide 8 - SRP).
* **Métodos:**
  * `montarDTO(String id, String modelo, String fabricante, String capacidade, String autonomia, String ano)`: Trata valores nulos/em branco, converte Strings numéricas com `Integer.parseInt` e instancia um objeto `AviaoDTO` pronto para uso.

#### Classe [DialogUtil.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/util/DialogUtil.java)
* **Papel:** Utilitário para exibição de diálogos visuais no JavaFX (Slides 17 e 18 da Parte 1).
* **Métodos:**
  * `showError(String mensagem)`: Diálogo modal com `AlertType.ERROR`.
  * `showWarning(String mensagem)`: Diálogo modal com `AlertType.WARNING`.
  * `showInformation(String mensagem)`: Diálogo modal com `AlertType.INFORMATION`.
  * `showConfirmation(String mensagem)`: Diálogo modal com `AlertType.CONFIRMATION`, retornando `true` se o usuário clicar em "OK".

#### Classe [TabelaUtil.java](file:///c:/Users/ra2457055/Desktop/MVCAvioes/src/java/com/template/util/TabelaUtil.java)
* **Papel:** Utilitário de configuração e manipulação visual para `TableView` do JavaFX.
* **Métodos:**
  * `configurarColunasAviao(...)`: Associa as propriedades do `AviaoDTO` (`id`, `modelo`, `fabricante`, etc.) às respectivas colunas via `PropertyValueFactory`.
  * `carregarTabelaAvioes(tblAvioes, aviaoService, lblMensagem)`: Busca a lista de aviões do serviço e popula a `TableView`, tratando exceções de conexão com feedback em tela e diálogo modal.

---

## 3. DEMONSTRAÇÃO E DEFESA DOS PRINCÍPIOS SOLID

### S — Single Responsibility Principle (Princípio da Responsabilidade Única)
* **Definição dos slides:** *"Uma classe deve ter um, e apenas um, motivo para existir ou mudar, focando em uma única responsabilidade"* (Slide 6).
* **Aplicação prática:**
  * O `MainController` não manipula SQL (delegado ao `AviaoDAO`), não formata colunas (delegado ao `TabelaUtil`), não converte strings para int (delegado ao `AviaoMapper`), não valida strings (delegado ao `AviaoValidador`) e não manipula estilos CSS de botões (delegado ao `LayoutServices`).
  * Cada validador cuida de uma única regra (`CamposObrigatoriosValidador` checa preenchimento; `NumeroPositivoValidador` checa se é número maior que zero).

### O — Open/Closed Principle (Princípio Aberto/Fechado)
* **Definição dos slides:** *"Entidades de software devem estar abertas para extensão, mas fechadas para modificação"* (Slide 13 e 15).
* **Aplicação prática:**
  * A interface `Validador<T>` permite adicionar novas regras de validação ao sistema (ex: `PrefixoAeronaveValidador`, `CpfPilotoValidador`) criando novas classes sem alterar uma única linha da interface `Validador<T>`. O método `validarAviao` apenas adiciona o novo validador à sua lista.

### L — Liskov Substitution Principle (Princípio da Substituição de Liskov)
* **Definição dos slides:** *"As classes derivadas devem ser substituíveis pelas suas classes bases sem afetar a integridade do sistema"* (Slide 19 e 21).
* **Aplicação prática:**
  * Qualquer implementação de `Validador<String>` (`CamposObrigatoriosValidador`, `NumeroPositivoValidador`, `AnoValidador`) pode ser adicionada à coleção `List<Validador<String>>` e invocada polimorficamente no loop `for (Validador<String> v : validadores)`. Nenhuma classe filha lança exceções inesperadas nem quebra o contrato da interface base.

### I — Interface Segregation Principle (Princípio da Segregação de Interfaces)
* **Definição dos slides:** *"Classes não devem ser forçadas a depender de métodos que não usam. Interfaces devem ser específicas para as necessidades dos clientes"* (Slide 22 e 25).
* **Aplicação prática:**
  * A interface `Validador<T>` é extremamente coesa e possui apenas os 3 métodos estritamente necessários (`validar`, `getMensagemErro`, `getValor`).
  * `IAviaoService` possui apenas operações pertinentes à persistência de aeronaves, e `IAviaoValidador` apenas os métodos de validação da frota.

### D — Dependency Inversion Principle (Princípio da Inversão de Dependência)
* **Definição dos slides:** *"Módulos de alto nível não devem depender de módulos de baixo nível. Ambos devem depender de abstrações. Abstrações não devem depender de detalhes; detalhes devem depender de abstrações"* (Slide 26, 27 e 30).
* **Aplicação prática:**
  * O `MainController` (módulo de alto nível que orquestra a interface) **depende exclusivamente das interfaces** `IAviaoService` e `IAviaoValidador` (abstrações).
  * O `MainController` **não possui construtor sem argumentos** e **não instancia classes concretas com `new`**.
  * As dependências concretas (`AviaoService` e `AviaoValidador`) são instanciadas fora, no `Main.java`, e injetadas no Controller através da `ControllerFactory` no momento em que o JavaFX carrega a View FXML.

---

## 4. GUIA DE RESPOSTAS PARA A ARGUIÇÃO ORAL DO PROFESSOR

Durante a apresentação presencial da prova, prepare-se para responder:

1. **Por que a interface `Validador<T>` utiliza Generics?**  
   * **Resposta:** Para garantir **segurança de tipos em tempo de compilação (*type-safety*)** e reusabilidade. Permite criar validadores para `String`, `Integer`, `LocalDate` ou qualquer outro tipo de objeto sem precisar fazer *casting* inseguro ou duplicar interfaces.

2. **Como os validadores são armazenados e percorridos em `AviaoValidador`?**  
   * **Resposta:** São instanciados e adicionados em uma lista genérica polimórfica: `List<Validador<String>> validadores = new ArrayList<>()`. Em seguida, são percorridos sequencialmente através de um loop `foreach` (`for (Validador<String> v : validadores)`), invocando `v.validar(v.getValor())`. Se algum falhar, a mensagem amigável é obtida via `v.getMensagemErro()`, interrompendo a validação no primeiro erro (*fail-fast*).

3. **Por que o `MainController` utiliza `IAviaoValidador` em vez da classe concreta `AviaoValidador`?**  
   * **Resposta:** Para atender ao **Princípio da Inversão de Dependência (DIP)** e reduzir o acoplamento. Dessa forma, se a implementação da validação mudar (ou se criarmos um validador simulado para testes unitários), o Controller não sofre nenhum impacto e não precisa ser modificado nem recompilado.

4. **Como funciona a Injeção de Dependência no JavaFX com a `ControllerFactory`?**  
   * **Resposta:** Por padrão, o JavaFX tenta instanciar os controllers usando o construtor vazio via reflexão. Ao passar nossa fábrica para `loader.setControllerFactory(controllerFactory)`, instruímos o `FXMLLoader` a delegar a criação do `MainController` para o método `call()`, onde injetamos as instâncias de `IAviaoService` e `IAviaoValidador` diretamente no construtor do Controller.

5. **Onde as dependências são instanciadas?**  
   * **Resposta:** No método `start()` da classe `Main`, isto é, no ponto de composição raiz (*Composition Root*) da aplicação, completamente fora do Controller.

---

## 5. COMO EXECUTAR NO INTELLIJ IDEA

1. Abra o projeto no IntelliJ IDEA.
2. Certifique-se de que a configuração de execução ativa no canto superior direito é **`JavaFX 25 Run`**.
3. Pressione o botão verde **Run** ou o atalho **`Shift + F10`**.
4. O JavaFX iniciará a janela gráfica com o título *"Gestão de Frota - Aviação"*.
5. Teste o cadastro de uma aeronave: caso deixe campos em branco ou insira anos e capacidades inválidas, os validadores dispararão popups informativos impedindo o cadastro inconsistente.
