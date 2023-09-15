package TrabalhoJavaFinalPoo.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import TrabalhoJavaFinalPoo.Model.Curso;
import TrabalhoJavaFinalPoo.Model.Estudante;

public class BancoDeDados {
	Connection conexao;

	public BancoDeDados() throws SQLException {
		// Inicializa a conexão com o banco de dados no construtor
		conexao = Conexao.obterConexao();
	}

	// Método para verificar se a tabela existe no banco de dados
	public boolean verificarTabelaExistente(String nomeTabela) throws SQLException {
		String sql = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = ?)";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setString(1, nomeTabela);
			ResultSet resultado = stmt.executeQuery();
			if (resultado.next()) {
				return resultado.getBoolean(1);
			}
		}
		return false;
	}

	// Método para criar a tabela de cursos no banco de dados
	public boolean criarTabelaCurso() {
		String sql = "CREATE TABLE IF NOT EXISTS curso (id SERIAL PRIMARY KEY, nome VARCHAR(255))";
		try (Statement stmt = conexao.createStatement()) {
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Método para criar a tabela de estudantes no banco de dados
	public boolean criarTabelaEstudante() throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS estudante (id SERIAL PRIMARY KEY, nome VARCHAR(255), curso_id INT)";
		try (Statement stmt = conexao.createStatement()) {
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Método para fechar a conexão com o banco de dados
	public void fecharConexao() throws SQLException {
		if (conexao != null) {
			conexao.close();
		}
	}

	// Método para inserir um novo curso no banco de dados
	public int inserirCurso(String nomeCurso) throws SQLException {
		String sql = "INSERT INTO curso (nome) VALUES (?)";
		try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, nomeCurso);
			stmt.executeUpdate();

			ResultSet generatedKeys = stmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			}
			return -1;
		}
	}

	// Método para inserir um novo estudante no banco de dados
	public void inserirEstudante(Estudante estudante) throws SQLException {
		String sql = "INSERT INTO estudante (nome, curso_id) VALUES (?, ?)";
		try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, estudante.getNomeAluno());
			stmt.setInt(2, estudante.getId());
			stmt.executeUpdate();
		}
	}

	// Método para listar todos os cursos disponíveis no banco de dados
	public List<Curso> listarCursos() throws SQLException {
		List<Curso> cursosDisponiveis = new ArrayList<>();
		String sql = "SELECT * FROM curso";

		try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet resultado = stmt.executeQuery()) {
			while (resultado.next()) {
				int id = resultado.getInt("id");
				String nome = resultado.getString("nome");
				Curso curso = new Curso(id, nome);
				cursosDisponiveis.add(curso);
			}
		}

		return cursosDisponiveis;
	}

	// Método para listar todos os estudantes no banco de dados
	public List<Estudante> listarEstudantes() throws SQLException {
		List<Estudante> estudantes = new ArrayList<>();
		String sql = "SELECT id, nome, curso_id FROM estudante ORDER BY id";

		try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet resultado = stmt.executeQuery()) {
			while (resultado.next()) {
				int id = resultado.getInt("id");
				String nome = resultado.getString("nome");
				int cursoId = resultado.getInt("curso_id");

				Estudante estudante = new Estudante(cursoId, "", nome);
				estudante.setIdAluno(id);

				estudantes.add(estudante);
			}
		}

		for (Estudante estudante : estudantes) {
			// Obtém o nome do curso associado a cada estudante
			String nomeCurso = getNomeCursoPeloId(estudante.getId());
			estudante.setNome(nomeCurso);
		}

		return estudantes;
	}

	// Método para obter o nome de um curso pelo seu ID
	public String getNomeCursoPeloId(int idCurso) {
		String sql = "SELECT nome FROM curso WHERE id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setInt(1, idCurso);
			ResultSet resultado = stmt.executeQuery();
			if (resultado.next()) {
				return resultado.getString("nome");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao obter o nome do curso: " + e.getMessage());
		}
		return "";
	}

	// Método para atualizar o nome e o curso de um estudante no banco de dados
	public boolean atualizarNomeEstudante(int idEstudante, String novoNome, int novoIdCurso) throws SQLException {
		String sql = "UPDATE estudante SET nome = ?, curso_id = ? WHERE id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setString(1, novoNome);
			stmt.setInt(2, novoIdCurso);
			stmt.setInt(3, idEstudante);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Método para remover um estudante do banco de dados
	public void removerEstudante(int idEstudante) throws SQLException {
		String sql = "DELETE FROM estudante WHERE id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setInt(1, idEstudante);
			stmt.executeUpdate();
		}
	}

	// Método para remover um curso do banco de dados
	public boolean removerCurso(int idCurso) throws SQLException {
		String sql = "DELETE FROM curso WHERE id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setInt(1, idCurso);
			int linhasAfetadas = stmt.executeUpdate();
			return linhasAfetadas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Método para verificar se existem estudantes associados a um curso
	public List<String> verificarEstudantesAssociados(int idCurso) throws SQLException {
		List<String> estudantesAssociados = new ArrayList<>();

		// Verifique se há estudantes associados a este curso
		String consulta = "SELECT nome FROM estudante WHERE curso_id = ?";
		try (PreparedStatement stmtConsulta = conexao.prepareStatement(consulta)) {
			stmtConsulta.setInt(1, idCurso);
			ResultSet resultado = stmtConsulta.executeQuery();

			while (resultado.next()) {
				String nomeEstudante = resultado.getString("nome");
				estudantesAssociados.add(nomeEstudante);
			}
		}

		return estudantesAssociados;
	}

	public boolean atualizarCursoEstudante(int idEstudante, int novoIdCurso) throws SQLException {
		String sql = "UPDATE estudante SET curso_id = ? WHERE id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setInt(1, novoIdCurso);
			stmt.setInt(2, idEstudante);
			int linhasAfetadas = stmt.executeUpdate();
			return linhasAfetadas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
