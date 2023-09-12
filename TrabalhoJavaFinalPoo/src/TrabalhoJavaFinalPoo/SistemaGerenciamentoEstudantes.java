package TrabalhoJavaFinalPoo;

import java.sql.SQLException;
import java.util.List;
import TrabalhoJavaFinalPoo.Data.BancoDeDados;
import TrabalhoJavaFinalPoo.Model.Estudante;
import TrabalhoJavaFinalPoo.View.Menu;

public class SistemaGerenciamentoEstudantes {

	private BancoDeDados bancoDeDados;

	public SistemaGerenciamentoEstudantes() {
		try {
			bancoDeDados = new BancoDeDados();
			bancoDeDados.criarTabelas();
		} catch (SQLException e) {
			System.err.println("Erro ao conectar ao banco de dados ou criar tabelas: " + e.getMessage());
			System.exit(1);
		}
	}

	public static void main(String[] args) throws SQLException {
		SistemaGerenciamentoEstudantes gerenciamento = new SistemaGerenciamentoEstudantes();

		Menu menu = new Menu(gerenciamento, gerenciamento.bancoDeDados);
		menu.exibirMenu();
	}

	public List<Estudante> listarEstudantes() throws SQLException {
		return bancoDeDados.listarEstudantes();
	}

	public void adicionarEstudante(Estudante estudante) throws SQLException {
		bancoDeDados.inserirEstudante(estudante);
	}

	public void removerEstudante(int id) throws SQLException {
		bancoDeDados.removerEstudante(id);
	}

	public void editarEstudante(int id, String novoNome, int novoIdCurso) throws SQLException {
		bancoDeDados.atualizarNomeEstudante(id, novoNome, novoIdCurso);
	}

}
