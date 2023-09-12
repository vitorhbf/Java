package TrabalhoJavaFinalPoo.Data;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import TrabalhoJavaFinalPoo.Model.Estudante;

public class BancoDeDados extends Conexao {
	Connection conexao;
	Scanner scanner = new Scanner(System.in);

	public void criarTabelas() throws SQLException {
		criarTabelaCurso();
		criarTabelaEstudante();
	}

	public BancoDeDados() throws SQLException {

		try {
			conexao = DriverManager.getConnection(Conexao.getUrl(), Conexao.getUsuario(), Conexao.getSenha());
			System.out.println("Conexão com banco de dados estabelecida com sucesso!");
		} catch (SQLException e) {
			System.out.println("Não foi possível obter a conexão com o banco de dados!");
			e.printStackTrace();
			throw e;
		}
	}

	public void criarTabelaCurso() throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS curso (id SERIAL PRIMARY KEY, nome VARCHAR(255))";
		try (Statement stmt = conexao.createStatement()) {
			int linhasAfetadas;
			linhasAfetadas = stmt.executeUpdate(sql);
			if (linhasAfetadas == 1) {

				System.out.println("Tabela 'curso' criada com sucesso!");

			} else {

				System.out.println("Conexão Realizada a tabela existente");
			}

		} catch (SQLException e) {
			System.out.println("Erro ao criar tabela 'curso': " + e.getMessage());
			throw e;
		}
	}

	public void criarTabelaEstudante() throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS estudante (id SERIAL PRIMARY KEY, nome VARCHAR(255), curso_id INT)";
		try (Statement stmt = conexao.createStatement()) {
			int linhasAfetadas;
			linhasAfetadas = stmt.executeUpdate(sql);
			if (linhasAfetadas == 1) {
				System.out.println("Tabela 'estudante' criada com sucesso!");
			} else {
				System.out.println("Conexão Realizada a tabela existente");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao criar tabela 'estudante': " + e.getMessage());
			throw e;
		}
	}

	public void fecharConexao() throws SQLException {
		if (conexao != null) {
			conexao.close();
		}
	}

	public void inserirCurso(String nomeCurso) throws SQLException {
		String sql = "INSERT INTO curso (nome) VALUES (?)";
		try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, nomeCurso);
			int rowsInserted = stmt.executeUpdate();
			if (rowsInserted > 0) {
				ResultSet generatedKeys = stmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					int idCurso = generatedKeys.getInt(1);
					System.out.println("Curso cadastrado com sucesso! ID: " + idCurso);
				} else {
					System.out.println("Erro ao obter o ID do curso após a inserção.");
				}
			} else {
				System.out.println("Erro ao cadastrar curso.");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao cadastrar curso: " + e.getMessage());
			throw e;
		}
	}

	public void inserirEstudante(Estudante estudante) throws SQLException {
		String sql = "INSERT INTO estudante (nome, curso_id) VALUES (?, ?)";
		try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, estudante.getNomeAluno());
			stmt.setInt(2, estudante.getIdCurso());
			int rowsInserted = stmt.executeUpdate();
			if (rowsInserted > 0) {
				ResultSet generatedKeys = stmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					estudante.setIdAluno(generatedKeys.getInt(1));
					System.out.println("Estudante cadastrado com sucesso! ID: " + estudante.getIdAluno());
				} else {
					System.out.println("Erro ao obter o ID do estudante após a inserção.");
				}
			} else {
				System.out.println("Erro ao cadastrar estudante.");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao cadastrar estudante: " + e.getMessage());
			throw e;
		}
	}

	public  List<String> listarCursos() throws SQLException {
		List<String> cursosDisponíveis = new ArrayList<>();
		String sql = "SELECT * FROM curso";

		try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet resultado = stmt.executeQuery()) {

			while (resultado.next()) {
				int id = resultado.getInt("id");
				String nome = resultado.getString("nome");

				System.out.println("ID: " + id + ", Nome: " + nome);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao listar cursos: " + e.getMessage());
			throw e;
		}

		return cursosDisponíveis;
	}

	public int getIdCursoPeloNome(String nomeCursoEscolhido) throws SQLException {
		String sql = "SELECT id FROM curso WHERE nome = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setString(1, nomeCursoEscolhido);
			ResultSet resultado = stmt.executeQuery();
			if (resultado.next()) {
				return resultado.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao obter o ID do curso: " + e.getMessage());
			throw e;
		}
		return -1;
	}

	public List<Estudante> listarEstudantes() throws SQLException {
		List<Estudante> estudantes = new ArrayList<>();
		String sql = "SELECT id, nome, curso_id FROM estudante";

		try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet resultado = stmt.executeQuery()) {

			while (resultado.next()) {
				int id = resultado.getInt("id");
				String nome = resultado.getString("nome");
				int cursoId = resultado.getInt("curso_id");

				Estudante estudante = new Estudante(nome, "", cursoId);
				estudante.setIdAluno(id);

				estudantes.add(estudante);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao listar estudantes: " + e.getMessage());
			throw e;
		}

		for (Estudante estudante : estudantes) {
			String nomeCurso = getNomeCursoPeloId(estudante.getIdCurso());
			estudante.setNomeCurso(nomeCurso);
		}

		return estudantes;
	}
	
	

	public String getNomeCursoPeloId(int idCurso) throws SQLException {
		String sql = "SELECT nome FROM curso WHERE id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setInt(1, idCurso);
			ResultSet resultado = stmt.executeQuery();
			if (resultado.next()) {
				return resultado.getString("nome");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao obter o nome do curso: " + e.getMessage());
			throw e;
		}
		return "";
	}

	public void atualizarNomeEstudante(int idEstudante, String novoNome, int novoIdCurso) throws SQLException {
		String sql = "UPDATE estudante SET nome = ?, curso_id = ? WHERE id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setString(1, novoNome);
			stmt.setInt(2, novoIdCurso);
			stmt.setInt(3, idEstudante);
			int rowsUpdated = stmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Estudante atualizado com sucesso!");
			} else {
				System.out.println("Nenhum estudante foi atualizado.");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao atualizar o estudante: " + e.getMessage());
			throw e;
		}
	}

	public void removerEstudante(int idEstudante) throws SQLException {
		String sql = "DELETE FROM estudante WHERE id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setInt(1, idEstudante);
			int rowsDeleted = stmt.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Estudante removido com sucesso!");
			} else {
				System.out.println("Nenhum estudante foi removido.");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao remover o estudante: " + e.getMessage());
			throw e;
		}
	}

	public void removerCurso() throws SQLException {
		String sql = "SELECT * FROM curso";
		try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet resultado = stmt.executeQuery()) {

			while (resultado.next()) {
				int id = resultado.getInt("id");
				String nome = resultado.getString("nome");

				System.out.println("ID: " + id + ", Nome: " + nome);
			}
		} catch (SQLException e) {
			System.out.println("Não foi possível acessar a tabela curso!!!");
			throw e;
		}

		int idCurso = lerInteiro("Qual curso deseja remover: ");
		sql = "DELETE FROM curso WHERE id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setInt(1, idCurso);
			int rowsDeleted = stmt.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Curso removido com sucesso!");
			} else {
				System.out.println("Nenhum Curso foi removido.");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao remover Curso: " + e.getMessage());
			throw e;
		}
	}

	public int lerInteiro(String mensagem) {
		while (true) {
			System.out.print(mensagem);
			try {
				int valor = Integer.parseInt(scanner.nextLine());
				return valor;
			} catch (NumberFormatException e) {
				System.out.println("Entrada inválida! Digite um número inteiro válido, referente ao ID.");
			}
		}
	}
}
