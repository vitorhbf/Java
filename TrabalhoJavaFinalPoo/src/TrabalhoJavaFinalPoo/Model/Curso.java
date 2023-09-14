package TrabalhoJavaFinalPoo.Model;

public class Curso {

	private Integer idCurso; // ID do curso
	private String nomeCurso; // Nome do curso

	// Construtor da classe Curso
	public Curso(Integer idCurso, String nomeCurso) {
		this.idCurso = idCurso; // Inicializa o ID do curso
		this.nomeCurso = nomeCurso; // Inicializa o nome do curso
	}

	public Integer getId() {
		return idCurso; // Retorna o ID do curso
	}

	public void setId(Integer idCurso) {
		this.idCurso = idCurso; // Define o ID do curso
	}

	public String getNome() {
		return nomeCurso; // Retorna o nome do curso
	}

	public void setNome(String nomeCurso) {
		this.nomeCurso = nomeCurso; // Define o nome do curso
	}

	@Override
	public String toString() {
		// Gera uma representação em formato de string do objeto Curso
		return "Código: " + this.idCurso + " - Nome: " + this.nomeCurso;
	}
}
