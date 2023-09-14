package TrabalhoJavaFinalPoo.Model;

public class Curso {

	private Integer idCurso;
	private String nomeCurso;

	public Curso(Integer idCurso, String nomeCurso) {

		this.idCurso = idCurso;
		this.nomeCurso = nomeCurso;
	}

	public Integer getId() {
		return idCurso;
	}

	public void setId(Integer idCurso) {
		this.idCurso = idCurso;
	}

	public String getNome() {
		return nomeCurso;
	}

	public void setNome(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	@Override
	public String toString() {
		return "Código: " + this.idCurso + " - Nome: " + this.nomeCurso;
	}

}
