package biblioteca;

import javax.swing.*;
import java.awt.*;

/**
 * Tela Principal — menu inicial do Sistema de Biblioteca.
 *
 * CONSTRUÇÃO DA TELA:
 *   - Estende JFrame diretamente para ser a janela principal da aplicação.
 *   - Layout BorderLayout: título no NORTH, botões no CENTER, rodapé no SOUTH.
 *   - Os três botões do centro usam GridLayout(3,1) para ficarem empilhados.
 *
 * NAVEGAÇÃO:
 *   - Cada botão cria e exibe uma nova janela (JFrame) da tela correspondente.
 *   - A TelaPrincipal usa EXIT_ON_CLOSE; as demais usam DISPOSE_ON_CLOSE
 *     para não encerrar o programa ao fechar uma tela secundária.
 */
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema de Biblioteca");
        setSize(420, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fecha o programa ao fechar esta janela
        setLocationRelativeTo(null); // centraliza na tela
        setResizable(false);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Painel raiz com margens internas
        JPanel painelRaiz = new JPanel(new BorderLayout(10, 15));
        painelRaiz.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));

        // ---- NORTE: título e subtítulo ----
        JPanel painelTopo = new JPanel(new GridLayout(2, 1, 0, 4));
        JLabel lblTitulo = new JLabel("Sistema de Biblioteca", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(30, 80, 150));

        JLabel lblSubtitulo = new JLabel("Selecione uma opcao para continuar", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblSubtitulo.setForeground(Color.GRAY);

        painelTopo.add(lblTitulo);
        painelTopo.add(lblSubtitulo);

        // Separador horizontal abaixo do título
        JSeparator separador = new JSeparator();
        JPanel painelNorth = new JPanel(new BorderLayout(0, 8));
        painelNorth.add(painelTopo, BorderLayout.NORTH);
        painelNorth.add(separador, BorderLayout.SOUTH);

        // ---- CENTRO: três botões de navegação ----
        JPanel painelBotoes = new JPanel(new GridLayout(3, 1, 0, 12));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botão que abre a tela de Livros
        JButton btnLivros = criarBotao("Gerenciamento de Livros", new Color(41, 128, 185));
        btnLivros.addActionListener(e -> new TelaLivros().setVisible(true));

        // Botão que abre a tela de Membros
        JButton btnMembros = criarBotao("Gerenciamento de Membros", new Color(39, 174, 96));
        btnMembros.addActionListener(e -> new TelaMembros().setVisible(true));

        // Botão que abre a tela de Empréstimos
        JButton btnEmprestimos = criarBotao("Gerenciamento de Emprestimos", new Color(142, 68, 173));
        btnEmprestimos.addActionListener(e -> new TelaEmprestimos().setVisible(true));

        painelBotoes.add(btnLivros);
        painelBotoes.add(btnMembros);
        painelBotoes.add(btnEmprestimos);

        // ---- SUL: rodapé ----
        JLabel lblRodape = new JLabel("UNIFAGOC - Laboratorio de Programacao", SwingConstants.CENTER);
        lblRodape.setFont(new Font("Arial", Font.PLAIN, 10));
        lblRodape.setForeground(Color.GRAY);

        // Monta a tela
        painelRaiz.add(painelNorth, BorderLayout.NORTH);
        painelRaiz.add(painelBotoes, BorderLayout.CENTER);
        painelRaiz.add(lblRodape, BorderLayout.SOUTH);

        add(painelRaiz);
    }

    /**
     * Cria um botão estilizado com cor de fundo, texto branco e efeito hover.
     * Reutilizado para os três botões do menu principal.
     */
    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(true);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito de destaque ao passar o mouse sobre o botão
        Color corEscura = cor.darker();
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { botao.setBackground(corEscura); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { botao.setBackground(cor); }
        });
        return botao;
    }
}
