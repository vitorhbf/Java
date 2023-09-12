package TrabalhoJavaFinalPoo.View;

import java.sql.SQLException;

import java.util.List;
import java.util.Scanner;
import TrabalhoJavaFinalPoo.SistemaGerenciamentoEstudantes;
import TrabalhoJavaFinalPoo.Data.BancoDeDados;
import TrabalhoJavaFinalPoo.Model.Estudante;

public class Menu {
	private Scanner scanner = new Scanner(System.in);
	private SistemaGerenciamentoEstudantes gerenciamento;
	private BancoDeDados bancoDeDados;

	public Menu(SistemaGerenciamentoEstudantes gerenciamento, BancoDeDados bancoDeDados) {
		this.gerenciamento = gerenciamento;
		this.bancoDeDados = bancoDeDados;
	}

	public void exibirMenu() throws SQLException {
		String opcao;

		do {
			System.out.println("\nMenu de opções:\n");
			System.out.println("1 - Cadastrar Curso");
			System.out.println("2 - Cadastrar Estudante");
			System.out.println("3 - Editar Estudante");
			System.out.println("4 - Remover Estudante");
			System.out.println("5 - Remover Curso");
			System.out.println("6 - Listar Estudantes");
			System.out.println("7 - Listar Cursos");
			System.out.println("8 - Encerrar/Sair\n");
			System.out.print("Escolha uma opção: \n");
			opcao = scanner.next();
			scanner.nextLine();

			switch (opcao) {

			case "1":
				cadastrarCursos();
				break;
			case "2":
				adicionarEstudante();
				break;
			case "3":
				editarEstudante();
				break;
			case "4":
				removerEstudante();
				break;
			case "5":
				bancoDeDados.removerCurso();
				break;
			case "6":
				listarEstudantes();
				break;
			case "7":
				bancoDeDados.listarCursos();
				break;
			case "8":
				System.out.println("Programa encerrado! Até logo!");
				break;
			default:
				System.out.println("Opção inválida. Tente novamente.");
			}
		} while (opcao != "7");

		scanner.close();

	}

	private void adicionarEstudante() {
		System.out.print("Nome do estudante: ");
		String nome = scanner.nextLine();
		if (!nome.matches("^[a-zA-Z]+$")) {
			System.out.println("Erro: O nome deve conter apenas letras.");
			return;
		}

		List<String> cursosDisponíveis;
		try {
			cursosDisponíveis = bancoDeDados.listarCursos();

			if (cursosDisponíveis.isEmpty()) {
				System.out.println("Nenhum curso cadastrado. Não é possível adicionar o estudante.");
				return;
			}

			System.out.println("Cursos disponíveis:");
			for (int i = 0; i < cursosDisponíveis.size(); i++) {
				System.out.println((i + 1) + " - " + cursosDisponíveis.get(i));
			}

			System.out.print("Escolha o número do curso: ");
			int escolhaCurso = scanner.nextInt();
			scanner.nextLine();

			if (escolhaCurso >= 1 && escolhaCurso <= cursosDisponíveis.size()) {
				String nomeCursoEscolhido = cursosDisponíveis.get(escolhaCurso - 1);
				int idCursoEscolhido = bancoDeDados.getIdCursoPeloNome(nomeCursoEscolhido);
				Estudante estudante = new Estudante(nome, nomeCursoEscolhido, idCursoEscolhido);
				gerenciamento.adicionarEstudante(estudante);

			} else {
				System.out.println("Escolha de curso inválida.");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao adicionar estudante: " + e.getMessage());
		}
	}

	private void editarEstudante() {
		List<Estudante> estudantes;
		try {
			estudantes = gerenciamento.listarEstudantes();
		} catch (SQLException e) {
			System.out.println("Erro ao listar estudantes: " + e.getMessage());
			return;
		}

		if (estudantes.isEmpty()) {
			System.out.println("Nenhum estudante cadastrado para editar.");
			return;
		}

		System.out.println("Lista de Estudantes:");
		for (Estudante estudante : estudantes) {
			System.out.println("ID: " + estudante.getIdAluno() + " - Nome: " + estudante.getNomeAluno());
		}

		int id = bancoDeDados.lerInteiro("Digite o ID do estudante a ser editado: ");

		Estudante estudanteExistente = buscarEstudante(id, estudantes);
		if (estudanteExistente == null) {
			System.out.println("Estudante não encontrado.");
			return;
		}

		System.out.print("Novo nome do estudante: ");
		String novoNome = scanner.nextLine();

		List<String> cursosDisponíveis;
		try {
			cursosDisponíveis = bancoDeDados.listarCursos();
			if (cursosDisponíveis.isEmpty()) {
				System.out.println("Nenhum curso cadastrado. Não é possível editar o curso do estudante.");
			} else {
				System.out.println("Cursos disponíveis:");
				for (int i = 0; i < cursosDisponíveis.size(); i++) {
					System.out.println((i + 1) + " - " + cursosDisponíveis.get(i));
				}

				System.out.print("Escolha o número do novo curso (ou pressione Enter para manter o curso atual): ");
				String escolhaCursoStr = scanner.nextLine();
				int novoIdCurso = -1;
				if (!escolhaCursoStr.isEmpty()) {
					int escolhaCurso = Integer.parseInt(escolhaCursoStr);
					if (escolhaCurso >= 1 && escolhaCurso <= cursosDisponíveis.size()) {
						String nomeCursoEscolhido = cursosDisponíveis.get(escolhaCurso - 1);
						novoIdCurso = bancoDeDados.getIdCursoPeloNome(nomeCursoEscolhido);
					} else {
						System.out.println("Escolha de curso inválida. O curso atual será mantido.");
					}
				} else {
					novoIdCurso = estudanteExistente.getIdCurso();
				}

				try {
					gerenciamento.editarEstudante(id, novoNome, novoIdCurso);

				} catch (SQLException e) {
					System.out.println("Erro ao editar estudante: " + e.getMessage());
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao listar cursos: " + e.getMessage());
		}
	}

	private void removerEstudante() {
		List<Estudante> estudantes;
		try {
			estudantes = gerenciamento.listarEstudantes();
		} catch (SQLException e) {
			System.out.println("Erro ao listar estudantes: " + e.getMessage());
			return;
		}

		if (estudantes.isEmpty()) {
			System.out.println("Nenhum estudante cadastrado para remover.");
			return;
		}

		System.out.println("Lista de Estudantes:");
		for (Estudante estudante : estudantes) {
			System.out.println("ID: " + estudante.getIdAluno() + " - Nome: " + estudante.getNomeAluno());
		}

		int id = bancoDeDados.lerInteiro("Digite o ID do estudante a ser removido: ");

		Estudante estudanteExistente = buscarEstudante(id, estudantes);
		if (estudanteExistente == null) {
			System.out.println("Estudante não encontrado.");
			return;
		}

		try {
			gerenciamento.removerEstudante(id);

		} catch (SQLException e) {
			System.out.println("Erro ao remover estudante: " + e.getMessage());
		}
	}

	private void listarEstudantes() {
		List<Estudante> estudantes;
		try {
			estudantes = gerenciamento.listarEstudantes();
		} catch (SQLException e) {
			System.out.println("Erro ao listar estudantes: " + e.getMessage());
			return;
		}

		if (estudantes.isEmpty()) {
			System.out.println("Nenhum estudante cadastrado.");
		} else {
			System.out.println("Lista de Estudantes:");
			for (Estudante estudante : estudantes) {
				System.out.println(estudante);
			}
		}
	}

	private void cadastrarCursos() {
		System.out.print("Nome do curso: ");
		String nomeCurso = scanner.nextLine();

		try {
			bancoDeDados.inserirCurso(nomeCurso);

		} catch (SQLException e) {
			System.out.println("Erro ao cadastrar curso: " + e.getMessage());
		}
	}

	private Estudante buscarEstudante(int id, List<Estudante> estudantes) {
		for (Estudante estudante : estudantes) {
			if (estudante.getIdAluno() == id) {
				return estudante;
			}
		}
		return null;
	}
}