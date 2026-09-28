package biblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tela de Gerenciamento de Membros.
 *
 * CONSTRUÇÃO DA TELA:
 *   - Layout principal BorderLayout(10, 10).
 *   - NORTH: rótulo de título.
 *   - WEST: formulário (240 px) com campos nome, matrícula, contato e botões.
 *   - CENTER: JScrollPane com JTable listando os membros cadastrados.
 *
 * CONEXÃO AO MODELO:
 *   - Cria objetos Membro e os armazena em Dados.membros (ArrayList estático).
 *   - A matrícula é validada como campo único antes do cadastro.
 *   - A edição usa indiceEditando para localizar e atualizar o objeto via setters.
 */
public class TelaMembros extends JFrame {

    // Campos do formulário
    private JTextField campNome;
    private JTextField campMatricula;
    private JTextField campContato;

    // Botões de ação
    private JButton btnCadastrar;
    private JButton btnEditar;
    private JButton btnLimpar;

    // Tabela de membros
    private JTable tabelaMembros;
    private DefaultTableModel modeloTabela;

    // Controla o modo de edição (-1 = não editando)
    private int indiceEditando = -1;

    public TelaMembros() {
        setTitle("Gerenciamento de Membros");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicializarComponentes();
        atualizarTabela();
    }

    // -------------------------------------------------------------------------
    // Montagem dos componentes visuais
    // -------------------------------------------------------------------------

    private void inicializarComponentes() {
        JPanel painelRaiz = new JPanel(new BorderLayout(10, 10));
        painelRaiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- NORTE: título ----
        JLabel lblTitulo = new JLabel("Gerenciamento de Membros", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // ---- OESTE: formulário ----
        JPanel painelEsquerdo = new JPanel(new BorderLayout(0, 10));
        painelEsquerdo.setBorder(BorderFactory.createTitledBorder("Dados do Membro"));
        painelEsquerdo.setPreferredSize(new Dimension(245, 0));

        campNome       = new JTextField();
        campMatricula  = new JTextField();
        campContato    = new JTextField();

        // Campos em duas colunas: rótulo | campo de texto
        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 5, 6));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        painelCampos.add(new JLabel("Nome:"));       painelCampos.add(campNome);
        painelCampos.add(new JLabel("Matricula:")); painelCampos.add(campMatricula);
        painelCampos.add(new JLabel("Contato:"));   painelCampos.add(campContato);

        // Três botões empilhados
        btnCadastrar = new JButton("Cadastrar Membro");
        btnEditar    = new JButton("Editar Selecionado");
        btnLimpar    = new JButton("Limpar Campos");

        estilizar(btnCadastrar, new Color(39, 174, 96));
        estilizar(btnEditar,    new Color(230, 126, 34));
        estilizar(btnLimpar,    new Color(127, 140, 141));

        btnCadastrar.addActionListener(e -> acaoCadastrar());
        btnEditar.addActionListener(e    -> acaoEditar());
        btnLimpar.addActionListener(e    -> limparCampos());

        JPanel painelBotoes = new JPanel(new GridLayout(3, 1, 0, 6));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnLimpar);

        painelEsquerdo.add(painelCampos, BorderLayout.NORTH);
        painelEsquerdo.add(painelBotoes, BorderLayout.CENTER);

        // ---- CENTRO: tabela de membros ----
        String[] colunas = {"Nome", "Matricula", "Contato"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaMembros = new JTable(modeloTabela);
        tabelaMembros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaMembros.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabelaMembros.setRowHeight(22);
        tabelaMembros.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabelaMembros.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabelaMembros.getColumnModel().getColumn(2).setPreferredWidth(180);

        JScrollPane scroll = new JScrollPane(tabelaMembros);
        scroll.setBorder(BorderFactory.createTitledBorder("Membros Cadastrados"));

        painelRaiz.add(lblTitulo, BorderLayout.NORTH);
        painelRaiz.add(painelEsquerdo, BorderLayout.WEST);
        painelRaiz.add(scroll, BorderLayout.CENTER);
        add(painelRaiz);
    }

    // -------------------------------------------------------------------------
    // Lógica das ações dos botões
    // -------------------------------------------------------------------------

    /**
     * Cadastra um novo membro OU salva a edição em andamento.
     * Valida duplicidade de matrícula antes de inserir.
     */
    private void acaoCadastrar() {
        if (!validarCampos()) return;

        String nome      = campNome.getText().trim();
        String matricula = campMatricula.getText().trim();
        String contato   = campContato.getText().trim();

        if (indiceEditando == -1) {
            // Modo CADASTRAR: verifica se a matrícula já existe
            for (Membro m : Dados.membros) {
                if (m.getMatricula().equalsIgnoreCase(matricula)) {
                    JOptionPane.showMessageDialog(this, "Matricula ja cadastrada. Use uma matricula unica.", "Duplicidade", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Dados.membros.add(new Membro(nome, matricula, contato));
            JOptionPane.showMessageDialog(this, "Membro cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Modo EDITAR: atualiza via setters
            Membro membro = Dados.membros.get(indiceEditando);
            membro.setNome(nome);
            membro.setMatricula(matricula);
            membro.setContato(contato);

            indiceEditando = -1;
            btnCadastrar.setText("Cadastrar Membro");
            btnEditar.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Membro atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }

        limparCampos();
        atualizarTabela();
    }

    /**
     * Carrega os dados do membro selecionado para edição nos campos do formulário.
     */
    private void acaoEditar() {
        int linha = tabelaMembros.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um membro na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Membro membro = Dados.membros.get(linha);
        campNome.setText(membro.getNome());
        campMatricula.setText(membro.getMatricula());
        campContato.setText(membro.getContato());

        indiceEditando = linha;
        btnCadastrar.setText("Salvar Alteracoes");
        btnEditar.setEnabled(false);
    }

    /** Limpa os campos e cancela a edição em andamento. */
    private void limparCampos() {
        campNome.setText("");
        campMatricula.setText("");
        campContato.setText("");

        if (indiceEditando != -1) {
            indiceEditando = -1;
            btnCadastrar.setText("Cadastrar Membro");
            btnEditar.setEnabled(true);
        }
    }

    private boolean validarCampos() {
        if (campNome.getText().trim().isEmpty() || campMatricula.getText().trim().isEmpty()
                || campContato.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos antes de continuar.", "Campos obrigatorios", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Reconstrói a JTable a partir de Dados.membros.
     */
    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Membro m : Dados.membros) {
            modeloTabela.addRow(new Object[]{ m.getNome(), m.getMatricula(), m.getContato() });
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
