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

			// Verifica se a tabela 'Estudante' já existe no banco de dados
			if (bancoDeDados.verificarTabelaExistente("estudante")) {
				System.out.println("Conexão realizada com sucesso à tabela 'Estudante'!");
			} else {
				// Se a tabela 'Estudante' não existe, cria-a
				if (bancoDeDados.criarTabelaEstudante()) {
					System.out.println("Tabela 'Estudante' criada com sucesso!");
				} else {
					System.out.println("Erro ao criar a tabela 'Estudante'!");
				}
			}

			// Verifica se a tabela 'curso' já existe no banco de dados
			if (bancoDeDados.verificarTabelaExistente("curso")) {
				System.out.println("Conexão com sucesso realizada à tabela 'curso'!");
			} else {
				// Se a tabela 'curso' não existe, cria-a
				if (bancoDeDados.criarTabelaCurso()) {
					System.out.println("Tabela 'curso' criada com sucesso!");
				} else {
					System.out.println("Erro ao criar a tabela 'curso'!");
				}
			}
		} catch (SQLException e) {
			System.err.println("Erro ao conectar ao banco de dados ou criar tabelas: " + e.getMessage());
			System.exit(1);
		}
	}

	public static void main(String[] args) throws Exception {
		// Inicializa a classe principal do sistema
		SistemaGerenciamentoEstudantes gerenciamento = new SistemaGerenciamentoEstudantes();

		// Cria um menu e exibe o menu para o usuário
		Menu menu = new Menu(gerenciamento, gerenciamento.bancoDeDados);
		menu.exibirMenu();

	}

	// Método para listar estudantes
	public List<Estudante> listarEstudantes() throws SQLException {
		return bancoDeDados.listarEstudantes();
	}

	// Método para remover um estudante pelo ID
	public void removerEstudante(int id) throws SQLException {
		bancoDeDados.removerEstudante(id);
	}

	// Método para editar um estudante pelo ID, atualizando o nome e o ID do curso
	public void editarEstudante(int id, String novoNome, int novoIdCurso) throws SQLException {
		bancoDeDados.atualizarNomeEstudante(id, novoNome, novoIdCurso);
	}
}
