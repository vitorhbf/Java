package TrabalhoJavaFinalPoo.Model;

public class Estudante extends Curso {
    // A classe Estudante herda da classe Curso

    private String nomeAluno; // Nome do estudante
    protected int idAluno; // ID do estudante

    // Construtor da classe Estudante
    public Estudante(Integer id, String nome, String nomeAluno) {
        super(id, nome); // Chama o construtor da classe pai (Curso) passando o ID e nome do curso
        this.nomeAluno = nomeAluno; // Inicializa o nome do estudante
    }

    public String getNomeAluno() {
        return nomeAluno; // Retorna o nome do estudante
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno; // Define o nome do estudante
    }

    public int getIdAluno() {
        return idAluno; // Retorna o ID do estudante
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno++; // Define o ID do estudante incrementando o valor atual
    }

    @Override
    public String toString() {
        // Gera uma representação em formato de string do objeto Estudante
        return "\nId do Aluno: " + idAluno + "\nNome do Aluno : " + nomeAluno + "\nNome do Curso: " + super.getNome() + "\n";
    }
}
