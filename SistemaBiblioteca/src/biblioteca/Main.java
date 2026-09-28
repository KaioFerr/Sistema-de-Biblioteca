package biblioteca;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Ponto de entrada da aplicação Sistema de Biblioteca.
 *
 * Utiliza SwingUtilities.invokeLater para garantir que a interface gráfica
 * seja criada e exibida na Event Dispatch Thread (EDT), conforme boas práticas Swing.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Aplica o visual nativo do sistema operacional (Windows, macOS, Linux)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Em caso de erro, usa o visual padrão do Java (Metal)
            }
            // Cria e exibe a janela principal do sistema
            new TelaPrincipal().setVisible(true);
        });
    }
}
