package TrabalhoJavaFinalPoo.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static String url = "jdbc:postgresql://localhost:5432/TrabalhoJavaFinalPoo";
    private static String usuario = "postgres";
    private static String senha = "@Vitorhb10";
    private static Connection conexao;

    // Método para obter a conexão com o banco de dados
    public static Connection obterConexao() {
        if (conexao == null) {
            try {
                // Estabelece a conexão com o banco de dados usando a URL, nome de usuário e senha
                conexao = DriverManager.getConnection(url, usuario, senha);
                System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            } catch (SQLException e) {
                System.out.println("Não foi possível obter a conexão com o banco de dados!");
                e.printStackTrace();
            }
        }
        return conexao;
    }

    // Métodos protegidos para acessar informações de conexão
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
