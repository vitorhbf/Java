package TrabalhoJavaFinalPoo.View;

import java.sql.SQLException;

import java.util.List;
import java.util.Scanner;
import TrabalhoJavaFinalPoo.SistemaGerenciamentoEstudantes;
import TrabalhoJavaFinalPoo.Data.BancoDeDados;
import TrabalhoJavaFinalPoo.Model.Curso;
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
				listarCurso();
				break;
			case "8":
				System.out.println("Programa encerrado! Até logo!");
				break;
			default:
				System.out.println("Opção inválida. Tente novamente.");
			}
		} while (!opcao.equals("8"));

		scanner.close();

	}

	private void adicionarEstudante() {
		System.out.print("Nome do estudante: ");
		String nome = scanner.nextLine();

		List<Curso> cursosDisponíveis;
		try {
			cursosDisponíveis = bancoDeDados.listarCursos();

			if (cursosDisponíveis.isEmpty()) {
				System.out.println("\nNenhum curso cadastrado. Não é possível adicionar o estudante.");
				return;
			}

			System.out.println("\nCursos disponíveis:");
			for (Curso curso : cursosDisponíveis) {
				System.out.println(curso);
			}

			int escolhaCurso = bancoDeDados.lerInteiro("Escolha o número do curso: ");

			for (Curso curso : cursosDisponíveis) {

				int idCurso = curso.getId();

				if (idCurso == escolhaCurso) {
					Estudante estudante = new Estudante(idCurso, curso.getNome(), nome);
					bancoDeDados.inserirEstudante(estudante);
					return;
				}
			}

			System.out.println("Escolha de curso inválida.");
		} catch (SQLException e) {
			System.out.println("Erro ao adicionar estudante: " + e.getMessage());
		}
	}

	private void editarEstudante() {
		List<Estudante> estudantes;
		try {
			estudantes = bancoDeDados.listarEstudantes();
		} catch (SQLException e) {
			System.out.println("Erro ao listar estudantes: " + e.getMessage());
			return;
		}

		if (estudantes.isEmpty()) {
			System.out.println("Nenhum estudante cadastrado para editar.");
			return;
		}

		System.out.println("Lista de Estudantes:\n");
		for (Estudante estudante : estudantes) {
			System.out.println("Codigo: " + estudante.getIdAluno() + " - Nome: " + estudante.getNomeAluno());
		}

		int id = bancoDeDados.lerInteiro("\nDigite o Codigo do estudante a ser editado: ");

		Estudante estudanteExistente = buscarEstudante(id, estudantes);
		if (estudanteExistente == null) {
			System.out.println("Estudante não encontrado.");
			return;
		}

		System.out.print("Novo nome do estudante: ");
		String novoNome = scanner.nextLine();

		List<Curso> cursosDisponíveis;
		try {
			cursosDisponíveis = bancoDeDados.listarCursos();
			if (cursosDisponíveis.isEmpty()) {
				System.out.println("Nenhum curso cadastrado. Não é possível editar o curso do estudante.");
			} else {
				System.out.println("Cursos disponíveis:");

				int i = 0;
				for (Curso curso : cursosDisponíveis) {

					i++;
					System.out.println(curso);
				}

				System.out.print("Escolha o número do novo curso (ou pressione Enter para manter o curso atual): ");
				String escolhaCursoStr = scanner.nextLine();

				int novoIdCurso = -1;

				if (!escolhaCursoStr.isEmpty()) {
					try {
						int escolhaCurso = Integer.parseInt(escolhaCursoStr);
						if (escolhaCurso >= 1
								&& escolhaCurso <= cursosDisponíveis.get(cursosDisponíveis.size() - 1).getId()) {
							Curso cursoEscolhido = new Curso(1, "Teste");
							for (Curso curso : cursosDisponíveis) {
								if (curso.getId() == escolhaCurso) {

									cursoEscolhido = curso;

								}

							}
							novoIdCurso = cursoEscolhido.getId();
						} else {
							System.out.println("Escolha de curso inválida. O curso atual será mantido.");
						}
					} catch (NumberFormatException e) {
						System.out.println("Entrada inválida. O curso atual será mantido.");
					}
				}

				gerenciamento.editarEstudante(id, novoNome, novoIdCurso);
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

		System.out.println("Lista de Estudantes:\n");
		for (Estudante estudante : estudantes) {
			System.out.println("Codigo: " + estudante.getIdAluno() + " - Nome: " + estudante.getNomeAluno());
		}

		int id = bancoDeDados.lerInteiro("\nDigite o Codigo do estudante a ser removido: ");

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
			estudantes = bancoDeDados.listarEstudantes();
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

	private void listarCurso() {
		List<Curso> cursos;
		try {
			cursos = bancoDeDados.listarCursos();

		} catch (SQLException e) {
			System.out.println("Erro ao listar Cursos: " + e.getMessage());
			return;
		}

		if (cursos.isEmpty()) {
			System.out.println("Nenhum Curso cadastrado.");
		} else {
			System.out.println("Lista de Cursos:\n");
			for (Curso curso : cursos) {
				System.out.println(curso);
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