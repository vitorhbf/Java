package TrabalhoJavaFinalPoo.Model;

public class Estudante extends Curso {
	private String nomeAluno;
	protected int idAluno;

	public Estudante(Integer id, String nome, String nomeAluno) {
		super(id, nome);
		this.nomeAluno = nomeAluno;

	}

	@Override
	public Integer getId() {

		return super.getId();
	}

	@Override
	public String getNome() {

		return super.getNome();
	}

	@Override
	public void setNome(String nome) {
		// TODO Auto-generated method stub
		super.setNome(nome);
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public int getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(int idAluno) {
		this.idAluno = idAluno++;
	}

	@Override
	public String toString() {
		return "\nId do Aluno: " + idAluno + "\nNome do Aluno : " + nomeAluno + "\nNome do Curso: " + super.getNome()
				+ "\n";
	}
}
