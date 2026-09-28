package biblioteca;

/**
 * Classe modelo que representa um Membro (usuário) da biblioteca.
 *
 * Encapsulamento aplicado: atributos privados com getters/setters.
 * toString() retorna uma representação legível usada nos JComboBox.
 */
public class Membro {

    private String nome;
    private String matricula;
    private String contato;

    /**
     * Construtor: inicializa um novo membro com seus dados básicos.
     */
    public Membro(String nome, String matricula, String contato) {
        this.nome = nome;
        this.matricula = matricula;
        this.contato = contato;
    }

    // ---- Getters e Setters ----

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    /**
     * Usado no JComboBox da tela de empréstimos para identificar o membro.
     */
    @Override
    public String toString() {
        return nome + " (" + matricula + ")";
    }
}
