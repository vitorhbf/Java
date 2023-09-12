package TrabalhoJavaFinalPoo.Model;

public class Estudante {
	private String nomeAluno;
	private String nomeCurso;
	private int idCurso;
	protected int idAluno;
	private static int proximoId = 1;

	public Estudante(String nomeAluno, String nomeCurso, int idCursoEscolhido) {
		this.nomeAluno = nomeAluno;
		this.nomeCurso = nomeCurso;
		this.idCurso = idCursoEscolhido;
		this.idAluno = proximoId++;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getNomeCurso() {
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public int getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}

	public int getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(int idAluno) {
		this.idAluno = idAluno;
	}

	@Override
	public String toString() {
		return "\nId do Aluno: " + idAluno + "\nNome do Aluno : " + nomeAluno + "\nNome do Curso: " + nomeCurso + "\n";
	}
}
