package TrabalhoJavaFinalPoo.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class  Conexao {

	private static String url = "jdbc:postgresql://localhost:5432/TrabalhoJavaFinalPoo";
	private static String usuario = "postgres";
	private static String senha = "@Vitorhb10";

	public static Connection obterConexao(String url, String usuario, String senha) {
		Connection conexao = null;
		try {

			conexao = DriverManager.getConnection(url, usuario, senha);
			System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
		} catch (SQLException e) {
			System.out.println("Não foi possível obter a conexão com o banco de dados!");
			e.printStackTrace();
		}
		return conexao;
		
	}

	protected static String getUrl() {
		return url;
	}

	protected static String getUsuario() {
		return usuario;
	}

	protected static String getSenha() {
		return senha;
	}
}
