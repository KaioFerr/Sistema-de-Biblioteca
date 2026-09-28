package biblioteca;

import java.util.ArrayList;

/**
 * Classe de repositório central — armazena os dados da aplicação em memória.
 *
 * As três listas estáticas são acessadas diretamente por todas as telas
 * (TelaLivros, TelaMembros, TelaEmprestimos) para ler e gravar dados.
 * Os dados existem enquanto o programa estiver em execução.
 */
public class Dados {

    /** Lista de todos os livros cadastrados no sistema. */
    public static ArrayList<Livro> livros = new ArrayList<>();

    /** Lista de todos os membros cadastrados no sistema. */
    public static ArrayList<Membro> membros = new ArrayList<>();

    /** Lista de todos os empréstimos, incluindo ativos e encerrados. */
    public static ArrayList<Emprestimo> emprestimos = new ArrayList<>();
}
