package biblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tela de Gerenciamento de Livros.
 *
 * CONSTRUÇÃO DA TELA:
 *   - Layout principal BorderLayout(10, 10).
 *   - NORTH: rótulo de título da tela.
 *   - WEST: painel de formulário (250 px) com campos de entrada e botões.
 *     * Campos organizados em GridLayout(4, 2): rótulo + campo lado a lado.
 *     * Botões em GridLayout(4, 1): empilhados verticalmente.
 *   - CENTER: JScrollPane contendo a JTable com os livros cadastrados.
 *
 * CONEXÃO AO MODELO:
 *   - Cria objetos Livro e os armazena em Dados.livros (ArrayList estático).
 *   - A tabela (DefaultTableModel) é reconstruída a cada alteração a partir de Dados.livros.
 *   - A edição usa um índice (indiceEditando) para localizar o objeto na lista e atualizá-lo via setters.
 */
public class TelaLivros extends JFrame {

    // Campos do formulário
    private JTextField campTitulo;
    private JTextField campAutor;
    private JTextField campAno;
    private JTextField campIsbn;

    // Botões de ação
    private JButton btnAdicionar;
    private JButton btnEditar;
    private JButton btnRemover;
    private JButton btnLimpar;

    // Tabela para listar livros
    private JTable tabelaLivros;
    private DefaultTableModel modeloTabela;

    // Controla o modo de edição: -1 = não está editando nenhum livro
    private int indiceEditando = -1;

    public TelaLivros() {
        setTitle("Gerenciamento de Livros");
        setSize(820, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // não encerra o programa
        inicializarComponentes();
        atualizarTabela(); // exibe livros já cadastrados ao abrir
    }

    // -------------------------------------------------------------------------
    // Montagem dos componentes visuais
    // -------------------------------------------------------------------------

    private void inicializarComponentes() {
        JPanel painelRaiz = new JPanel(new BorderLayout(10, 10));
        painelRaiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- NORTE: título ----
        JLabel lblTitulo = new JLabel("Gerenciamento de Livros", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // ---- OESTE: formulário + botões ----
        JPanel painelEsquerdo = new JPanel(new BorderLayout(0, 10));
        painelEsquerdo.setBorder(BorderFactory.createTitledBorder("Dados do Livro"));
        painelEsquerdo.setPreferredSize(new Dimension(255, 0));

        // Campos em duas colunas: rótulo | campo de texto
        campTitulo = new JTextField();
        campAutor  = new JTextField();
        campAno    = new JTextField();
        campIsbn   = new JTextField();

        JPanel painelCampos = new JPanel(new GridLayout(4, 2, 5, 6));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        painelCampos.add(new JLabel("Titulo:"));  painelCampos.add(campTitulo);
        painelCampos.add(new JLabel("Autor:"));   painelCampos.add(campAutor);
        painelCampos.add(new JLabel("Ano:"));     painelCampos.add(campAno);
        painelCampos.add(new JLabel("ISBN:"));    painelCampos.add(campIsbn);

        // Quatro botões empilhados
        btnAdicionar = new JButton("Adicionar Livro");
        btnEditar    = new JButton("Editar Selecionado");
        btnRemover   = new JButton("Remover Selecionado");
        btnLimpar    = new JButton("Limpar Campos");

        estilizar(btnAdicionar, new Color(41, 128, 185));
        estilizar(btnEditar,    new Color(230, 126, 34));
        estilizar(btnRemover,   new Color(192, 57, 43));
        estilizar(btnLimpar,    new Color(127, 140, 141));

        btnAdicionar.addActionListener(e -> acaoAdicionar());
        btnEditar.addActionListener(e    -> acaoEditar());
        btnRemover.addActionListener(e   -> acaoRemover());
        btnLimpar.addActionListener(e    -> limparCampos());

        JPanel painelBotoes = new JPanel(new GridLayout(4, 1, 0, 6));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnLimpar);

        painelEsquerdo.add(painelCampos, BorderLayout.NORTH);
        painelEsquerdo.add(painelBotoes, BorderLayout.CENTER);

        // ---- CENTRO: tabela de livros ----
        // Colunas: Titulo | Autor | Ano | ISBN | Disponivel
        String[] colunas = {"Titulo", "Autor", "Ano", "ISBN", "Disponivel"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // somente leitura
        };

        tabelaLivros = new JTable(modeloTabela);
        tabelaLivros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaLivros.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabelaLivros.setRowHeight(22);
        tabelaLivros.getColumnModel().getColumn(0).setPreferredWidth(180);
        tabelaLivros.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabelaLivros.getColumnModel().getColumn(2).setPreferredWidth(45);
        tabelaLivros.getColumnModel().getColumn(3).setPreferredWidth(115);
        tabelaLivros.getColumnModel().getColumn(4).setPreferredWidth(80);

        JScrollPane scroll = new JScrollPane(tabelaLivros);
        scroll.setBorder(BorderFactory.createTitledBorder("Livros Cadastrados"));

        // Monta a tela
        painelRaiz.add(lblTitulo, BorderLayout.NORTH);
        painelRaiz.add(painelEsquerdo, BorderLayout.WEST);
        painelRaiz.add(scroll, BorderLayout.CENTER);
        add(painelRaiz);
    }

    // -------------------------------------------------------------------------
    // Lógica das ações dos botões
    // -------------------------------------------------------------------------

    /**
     * Adiciona um novo livro OU salva a edição em andamento.
     * Distingue os dois modos pelo valor de indiceEditando.
     */
    private void acaoAdicionar() {
        if (!validarCampos()) return;

        String titulo = campTitulo.getText().trim();
        String autor  = campAutor.getText().trim();
        String isbn   = campIsbn.getText().trim();
        int ano;

        try {
            ano = Integer.parseInt(campAno.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O campo Ano deve conter um numero valido.", "Erro de validacao", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (indiceEditando == -1) {
            // Modo ADICIONAR: cria novo objeto Livro e insere na lista
            Dados.livros.add(new Livro(titulo, autor, ano, isbn));
            JOptionPane.showMessageDialog(this, "Livro adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Modo EDITAR: atualiza o objeto existente via setters (encapsulamento)
            Livro livro = Dados.livros.get(indiceEditando);
            livro.setTitulo(titulo);
            livro.setAutor(autor);
            livro.setAno(ano);
            livro.setIsbn(isbn);

            // Restaura o modo normal
            indiceEditando = -1;
            btnAdicionar.setText("Adicionar Livro");
            btnEditar.setEnabled(true);
            btnRemover.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Livro atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }

        limparCampos();
        atualizarTabela();
    }

    /**
     * Carrega os dados do livro selecionado nos campos para edição.
     * O índice da linha selecionada corresponde ao índice em Dados.livros.
     */
    private void acaoEditar() {
        int linha = tabelaLivros.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Recupera o objeto pelo índice e preenche os campos
        Livro livro = Dados.livros.get(linha);
        campTitulo.setText(livro.getTitulo());
        campAutor.setText(livro.getAutor());
        campAno.setText(String.valueOf(livro.getAno()));
        campIsbn.setText(livro.getIsbn());

        // Entra no modo de edição
        indiceEditando = linha;
        btnAdicionar.setText("Salvar Alteracoes");
        btnEditar.setEnabled(false);
        btnRemover.setEnabled(false);
    }

    /**
     * Remove o livro selecionado da lista.
     * Impede a remoção se o livro estiver emprestado.
     */
    private void acaoRemover() {
        int linha = tabelaLivros.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro na tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Livro livro = Dados.livros.get(linha);

        if (!livro.isDisponivel()) {
            JOptionPane.showMessageDialog(this, "Nao e possivel remover um livro que esta emprestado!", "Operacao negada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this,
                "Confirmar remocao do livro:\n\"" + livro.getTitulo() + "\"?",
                "Confirmar Remocao", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            Dados.livros.remove(linha);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Livro removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Limpa os campos do formulário e cancela qualquer edição em andamento.
     */
    private void limparCampos() {
        campTitulo.setText("");
        campAutor.setText("");
        campAno.setText("");
        campIsbn.setText("");

        if (indiceEditando != -1) {
            indiceEditando = -1;
            btnAdicionar.setText("Adicionar Livro");
            btnEditar.setEnabled(true);
            btnRemover.setEnabled(true);
        }
    }

    /** Verifica se todos os campos foram preenchidos antes de salvar. */
    private boolean validarCampos() {
        if (campTitulo.getText().trim().isEmpty() || campAutor.getText().trim().isEmpty()
                || campAno.getText().trim().isEmpty() || campIsbn.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos antes de continuar.", "Campos obrigatorios", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Reconstrói a JTable a partir de Dados.livros.
     * Chamado após cada operação de inclusão, edição ou remoção.
     */
    private void atualizarTabela() {
        modeloTabela.setRowCount(0); // remove todas as linhas existentes
        for (Livro l : Dados.livros) {
            modeloTabela.addRow(new Object[]{
                l.getTitulo(), l.getAutor(), l.getAno(), l.getIsbn(),
                l.isDisponivel() ? "Sim" : "Nao"
            });
        }
    }

    /** Aplica estilo visual padronizado aos botões. */
    private void estilizar(JButton botao, Color cor) {
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(true);
    }
}
