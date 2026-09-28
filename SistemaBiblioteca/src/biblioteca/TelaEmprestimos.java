package biblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tela de Gerenciamento de Empréstimos.
 *
 * CONSTRUÇÃO DA TELA:
 *   - Layout principal BorderLayout(10, 10).
 *   - NORTH: painel superior com título + formulário de seleção + botões.
 *     * Formulário usa GridBagLayout com dois JComboBox (livro e membro).
 *     * Botões em FlowLayout na parte inferior do painel.
 *   - CENTER: JScrollPane com JTable exibindo todos os empréstimos.
 *
 * CONEXÃO AO MODELO:
 *   - JComboBox<Livro> exibe apenas livros disponíveis (filtragem em carregarCombos).
 *   - JComboBox<Membro> exibe todos os membros cadastrados.
 *   - Realizar Empréstimo: cria um objeto Emprestimo — o construtor da classe
 *     já marca o livro como indisponível automaticamente.
 *   - Encerrar Empréstimo: chama emprestimo.encerrar(), que registra a data de
 *     devolução e libera o livro de volta.
 *   - Os combos são recarregados sempre que a janela ganha foco, garantindo
 *     sincronismo com livros/membros adicionados nas outras telas.
 */
public class TelaEmprestimos extends JFrame {

    // Seletores de livro disponível e de membro
    private JComboBox<Livro>   comboLivros;
    private JComboBox<Membro>  comboMembros;

    // Botões de ação
    private JButton btnRealizar;
    private JButton btnEncerrar;
    private JButton btnAtualizar;

    // Tabela de empréstimos
    private JTable tabelaEmprestimos;
    private DefaultTableModel modeloTabela;

    public TelaEmprestimos() {
        setTitle("Gerenciamento de Emprestimos");
        setSize(880, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        carregarCombos();
        atualizarTabela();
    }

    // -------------------------------------------------------------------------
    // Montagem dos componentes visuais
    // -------------------------------------------------------------------------

    private void inicializarComponentes() {
        JPanel painelRaiz = new JPanel(new BorderLayout(10, 10));
        painelRaiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- PAINEL SUPERIOR: título + formulário + botões ----
        JPanel painelSuperior = new JPanel(new BorderLayout(5, 5));

        JLabel lblTitulo = new JLabel("Gerenciamento de Emprestimos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // Formulário de seleção com GridBagLayout para alinhar os combos
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Novo Emprestimo"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 8, 6, 8);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        // Linha 0: label + combo de livros disponíveis
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painelFormulario.add(new JLabel("Livro (disponiveis):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboLivros = new JComboBox<>();
        comboLivros.setPreferredSize(new Dimension(320, 25));
        painelFormulario.add(comboLivros, gbc);

        // Linha 1: label + combo de membros
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painelFormulario.add(new JLabel("Membro:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboMembros = new JComboBox<>();
        painelFormulario.add(comboMembros, gbc);

        // Botões de ação alinhados à esquerda
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));

        btnRealizar  = new JButton("Realizar Emprestimo");
        btnEncerrar  = new JButton("Encerrar Emprestimo Selecionado");
        btnAtualizar = new JButton("Atualizar Combos");

        estilizar(btnRealizar,  new Color(142, 68, 173));
        estilizar(btnEncerrar,  new Color(192, 57, 43));
        estilizar(btnAtualizar, new Color(41, 128, 185));

        btnRealizar.addActionListener(e  -> acaoRealizarEmprestimo());
        btnEncerrar.addActionListener(e  -> acaoEncerrarEmprestimo());
        btnAtualizar.addActionListener(e -> { carregarCombos(); atualizarTabela(); });

        painelBotoes.add(btnRealizar);
        painelBotoes.add(btnEncerrar);
        painelBotoes.add(btnAtualizar);

        painelSuperior.add(lblTitulo, BorderLayout.NORTH);
        painelSuperior.add(painelFormulario, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        // ---- CENTRO: tabela de empréstimos ----
        // Colunas: Livro | Membro | Data Empréstimo | Data Devolução | Status
        String[] colunas = {"Livro", "Membro", "Data Emprestimo", "Data Devolucao", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaEmprestimos = new JTable(modeloTabela);
        tabelaEmprestimos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaEmprestimos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabelaEmprestimos.setRowHeight(22);
        tabelaEmprestimos.getColumnModel().getColumn(0).setPreferredWidth(210);
        tabelaEmprestimos.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabelaEmprestimos.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabelaEmprestimos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabelaEmprestimos.getColumnModel().getColumn(4).setPreferredWidth(80);

        JScrollPane scroll = new JScrollPane(tabelaEmprestimos);
        scroll.setBorder(BorderFactory.createTitledBorder("Historico de Emprestimos (ativos e encerrados)"));

        painelRaiz.add(painelSuperior, BorderLayout.NORTH);
        painelRaiz.add(scroll, BorderLayout.CENTER);
        add(painelRaiz);
    }

    // -------------------------------------------------------------------------
    // Lógica das ações dos botões
    // -------------------------------------------------------------------------

    /**
     * Realiza um novo empréstimo com o livro e membro selecionados.
     *
     * O construtor de Emprestimo já marca o livro como indisponível.
     * Após o empréstimo, os combos são recarregados (o livro some da lista disponível).
     */
    private void acaoRealizarEmprestimo() {
        if (comboLivros.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Nao ha livros disponiveis para emprestimo.\nCadastre livros na tela de Gerenciamento de Livros.", "Sem livros disponiveis", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (comboMembros.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Nao ha membros cadastrados.\nCadastre membros na tela de Gerenciamento de Membros.", "Sem membros", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Livro  livro  = (Livro)  comboLivros.getSelectedItem();
        Membro membro = (Membro) comboMembros.getSelectedItem();

        if (livro == null || membro == null) return;

        // Cria o empréstimo — o construtor de Emprestimo marca o livro como indisponível
        Emprestimo emprestimo = new Emprestimo(livro, membro);
        Dados.emprestimos.add(emprestimo);

        // Recarrega os combos para refletir a nova disponibilidade do livro
        carregarCombos();
        atualizarTabela();

        JOptionPane.showMessageDialog(this,
                "Emprestimo realizado!\nLivro: " + livro.getTitulo() + "\nMembro: " + membro.getNome(),
                "Emprestimo Realizado", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Encerra o empréstimo selecionado na tabela.
     *
     * Somente empréstimos com status "Ativo" podem ser encerrados.
     * O método encerrar() da classe Emprestimo registra a data de devolução
     * e libera o livro de volta ao acervo.
     */
    private void acaoEncerrarEmprestimo() {
        int linha = tabelaEmprestimos.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um emprestimo na tabela para encerrar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // O índice da linha na tabela corresponde ao índice em Dados.emprestimos
        Emprestimo emprestimo = Dados.emprestimos.get(linha);

        if (!emprestimo.isAtivo()) {
            JOptionPane.showMessageDialog(this, "Este emprestimo ja foi encerrado.", "Operacao invalida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this,
                "Confirmar devolucao do livro:\n\"" + emprestimo.getLivro().getTitulo() + "\"\n" +
                "Membro: " + emprestimo.getMembro().getNome() + "?",
                "Confirmar Encerramento", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            // Chama o método encerrar() — registra data e libera o livro
            emprestimo.encerrar();

            carregarCombos();
            atualizarTabela();

            JOptionPane.showMessageDialog(this, "Emprestimo encerrado com sucesso!\nLivro devolvido ao acervo.", "Encerrado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Carrega os JComboBox com dados atualizados de Dados.livros e Dados.membros.
     * Apenas livros disponíveis (isDisponivel() == true) aparecem para empréstimo.
     */
    private void carregarCombos() {
        comboLivros.removeAllItems();
        for (Livro l : Dados.livros) {
            if (l.isDisponivel()) {
                comboLivros.addItem(l); // usa toString() do Livro para exibição
            }
        }

        comboMembros.removeAllItems();
        for (Membro m : Dados.membros) {
            comboMembros.addItem(m); // usa toString() do Membro para exibição
        }
    }

    /**
     * Reconstrói a JTable a partir de Dados.emprestimos (todos, ativos e encerrados).
     */
    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Emprestimo e : Dados.emprestimos) {
            modeloTabela.addRow(new Object[]{
                e.getLivro().getTitulo(),
                e.getMembro().getNome(),
                e.getDataEmprestimoFormatada(),
                e.getDataDevolucaoFormatada(),
                e.getStatusTexto()
            });
        }
    }

    private void estilizar(JButton botao, Color cor) {
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(true);
    }
}
