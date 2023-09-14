package TrabalhoJavaFinalPoo;

import java.sql.SQLException;
import java.util.List;
import TrabalhoJavaFinalPoo.Data.BancoDeDados;
import TrabalhoJavaFinalPoo.Model.Estudante;
import TrabalhoJavaFinalPoo.View.Menu;

public class SistemaGerenciamentoEstudantes {

	private BancoDeDados bancoDeDados;

	// Construtor da classe
	public SistemaGerenciamentoEstudantes() {
		try {
			// Inicializa a conexão com o banco de dados
			bancoDeDados = new BancoDeDados();

			// Verifica se a tabela 'Estudante' já existe no banco de dados. Se não existir,
			// cria.
			if (bancoDeDados.criarTabelaEstudante() == true) {
				System.out.println("Tabela 'Estudante' criada com sucesso!");
			} else {
				System.out.println("Conexão realizada com sucesso à tabela 'Estudante'!");
			}

			// Verifica se a tabela 'curso' já existe no banco de dados. Se não existir,
			// cria.
			if (bancoDeDados.criarTabelaCurso() == true) {
				System.out.println("Tabela 'curso' criada com sucesso!");
			} else {
				System.out.println("Conexão com sucesso realizada à tabela 'curso'!");
			}
		} catch (SQLException e) {
			// Trata erros de conexão ou criação de tabelas
			System.err.println("Erro ao conectar ao banco de dados ou criar tabelas: " + e.getMessage());
			System.exit(1);
		}
	}

	public static void main(String[] args) throws Exception {
		// Inicializa a classe principal do sistema
		SistemaGerenciamentoEstudantes gerenciamento = new SistemaGerenciamentoEstudantes();

		// Cria uma instância do BancoDeDados
		BancoDeDados bd = new BancoDeDados();

		// Cria um menu e exibe o menu para o usuário
		Menu menu = new Menu(gerenciamento, gerenciamento.bancoDeDados);
		menu.exibirMenu();

		// Fecha a conexão com o banco de dados
		bd.fecharConexao();
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
