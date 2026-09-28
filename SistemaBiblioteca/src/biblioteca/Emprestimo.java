package biblioteca;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe modelo que representa um Empréstimo de livro a um membro.
 *
 * Registra o livro emprestado, o membro que retirou, as datas e o status.
 * O construtor marca o livro como indisponível automaticamente.
 * O método encerrar() registra a devolução e libera o livro novamente.
 */
public class Emprestimo {

    private Livro livro;
    private Membro membro;
    private LocalDate dataEmprestimo;  // data em que o empréstimo foi realizado
    private LocalDate dataDevolucao;   // null enquanto o empréstimo estiver ativo
    private boolean ativo;

    // Formato de data BR para exibição nas telas
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Cria um empréstimo ativo com a data atual e marca o livro como indisponível.
     */
    public Emprestimo(Livro livro, Membro membro) {
        this.livro = livro;
        this.membro = membro;
        this.dataEmprestimo = LocalDate.now();
        this.ativo = true;
        livro.setDisponivel(false); // bloqueia o livro para outros empréstimos
    }

    // ---- Getters ----

    public Livro getLivro() { return livro; }
    public Membro getMembro() { return membro; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public boolean isAtivo() { return ativo; }

    /**
     * Encerra o empréstimo: registra a data de devolução e libera o livro.
     */
    public void encerrar() {
        this.dataDevolucao = LocalDate.now();
        this.ativo = false;
        livro.setDisponivel(true); // livro volta a ficar disponível
    }

    /** Data de empréstimo formatada no padrão dd/MM/yyyy. */
    public String getDataEmprestimoFormatada() {
        return dataEmprestimo.format(FORMATO);
    }

    /** Data de devolução formatada, ou "Em aberto" se o empréstimo ainda estiver ativo. */
    public String getDataDevolucaoFormatada() {
        return dataDevolucao != null ? dataDevolucao.format(FORMATO) : "Em aberto";
    }

    /** Status do empréstimo em texto legível para exibição na tabela. */
    public String getStatusTexto() {
        return ativo ? "Ativo" : "Encerrado";
    }
}
