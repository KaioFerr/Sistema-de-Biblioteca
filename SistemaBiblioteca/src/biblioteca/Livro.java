package biblioteca;

/**
 * Classe modelo que representa um Livro no sistema de biblioteca.
 *
 * Atributos encapsulados com modificador private.
 * Acesso controlado via getters e setters (encapsulamento).
 * O atributo 'disponivel' controla se o livro pode ser emprestado.
 */
public class Livro {

    // Atributos privados - encapsulamento
    private String titulo;
    private String autor;
    private int ano;
    private String isbn;
    private boolean disponivel; // true = pode ser emprestado

    /**
     * Construtor: inicializa um livro como disponível para empréstimo.
     */
    public Livro(String titulo, String autor, int ano, String isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.isbn = isbn;
        this.disponivel = true; // todo livro começa disponível
    }

    // ---- Getters e Setters ----

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    /**
     * Representação textual do livro usada nos JComboBox das telas de empréstimo.
     */
    @Override
    public String toString() {
        return titulo + " - " + autor;
    }
}
