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
				removerCurso();
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
		String nome = lerApenasLetrasComEspacos(scanner);

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

			int escolhaCurso = lerInteiro("\nEscolha o número do curso: ");

			Curso cursoEscolhido = null;
			for (Curso curso : cursosDisponíveis) {
				if (curso.getId() == escolhaCurso) {
					cursoEscolhido = curso;
					break;
				}
			}

			if (cursoEscolhido != null) {
				Estudante estudante = new Estudante(cursoEscolhido.getId(), cursoEscolhido.getNome(), nome);
				bancoDeDados.inserirEstudante(estudante);
				System.out.println("\nEstudante adicionado com sucesso!");
			} else {
				System.out.println("\nCurso escolhido não existe. Não foi possível adicionar o estudante.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("\nErro ao adicionar o estudante.");
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

		int id = lerInteiro("\nDigite o Codigo do estudante a ser editado: ");

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
				System.out.println("Cursos disponíveis:\n");

				int i = 0;
				for (Curso curso : cursosDisponíveis) {
					i++;
					System.out.println(curso);
				}

				System.out.print("\nEscolha o número do novo curso (ou pressione Enter para manter o curso atual): ");
				String escolhaCursoStr = scanner.nextLine();

				int novoIdCurso = -1;

				if (!escolhaCursoStr.isEmpty()) {
					try {
						int escolhaCurso = Integer.parseInt(escolhaCursoStr);
						if (escolhaCurso >= 1
								&& escolhaCurso <= cursosDisponíveis.get(cursosDisponíveis.size() - 1).getId()) {
							Curso cursoEscolhido = new Curso(1, escolhaCursoStr);
							for (Curso curso : cursosDisponíveis) {
								if (curso.getId() == escolhaCurso) {

									cursoEscolhido = curso;
								}

							}
							novoIdCurso = cursoEscolhido.getId();

						} else {

							System.out.println("Escolha de curso inválida.");
						}
					} catch (NumberFormatException e) {
						System.out.println("Entrada inválida. O curso atual será mantido.");
					}
				}

				gerenciamento.editarEstudante(id, novoNome, novoIdCurso);
				boolean sucesso = bancoDeDados.atualizarNomeEstudante(id, novoNome, novoIdCurso);

				if (sucesso) {
					System.out.println("\nEstudante editado com sucesso!");
				} else {
					System.out.println("\nFalha ao editar o estudante.");
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

		System.out.println("Lista de Estudantes:\n");
		for (Estudante estudante : estudantes) {
			System.out.println("Codigo: " + estudante.getIdAluno() + " - Nome: " + estudante.getNomeAluno());
		}

		int id = lerInteiro("\nDigite o Codigo do estudante a ser removido: ");

		Estudante estudanteExistente = buscarEstudante(id, estudantes);
		if (estudanteExistente == null) {
			System.out.println("Estudante não encontrado.");
			return;
		} else {

			System.out.println("Estudante Removido com Sucesso!");
		}

		try {
			gerenciamento.removerEstudante(id);

		} catch (SQLException e) {
			System.out.println("Erro ao remover estudante: " + e.getMessage());
		}
	}

	private void removerCurso() {
		try {
			listarCurso();
			System.out.println("\nCursos Listados com Sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\nNão foi possível Listar Cursos!");
		}

		try {
			int idCurso = lerInteiro("\nQual curso deseja remover: ");
			List<String> estudantesAssociados = bancoDeDados.verificarEstudantesAssociados(idCurso);

			if (!estudantesAssociados.isEmpty()) {
				System.out.println("\nNão é possível remover o curso, pois está associado aos seguintes estudantes:\n");
				for (String nomeEstudante : estudantesAssociados) {
					System.out.println(nomeEstudante);
				}
			} else {
				boolean removidoComSucesso = bancoDeDados.removerCurso(idCurso);

				if (removidoComSucesso) {
					System.out.println("\nCurso removido com sucesso!");
				} else {
					System.out.println("\nNão foi possível remover o curso.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\nNão foi possível Acessar Cursos!");
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
			int idCurso = bancoDeDados.inserirCurso(nomeCurso);
			if (idCurso != -1) {
				System.out.println("\nCurso cadastrado com sucesso! nº: " + idCurso);
			} else {
				System.out.println("\nErro ao cadastrar curso.");
			}
		} catch (SQLException e) {
			System.out.println("\nErro ao cadastrar curso: " + e.getMessage());
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

	private int lerInteiro(String mensagem) {
		while (true) {
			System.out.print(mensagem);
			try {
				int valor = Integer.parseInt(scanner.nextLine());
				return valor;
			} catch (NumberFormatException e) {
				System.out.println("Entrada inválida! Digite um número inteiro válido.");
			}
		}

	}

	public static String lerApenasLetrasComEspacos(Scanner scanner) {
		String entrada = "";
		boolean contemApenasLetras = false;

		while (!contemApenasLetras) {
			entrada = scanner.nextLine();

			if (entrada.matches("^[a-zA-Z\\s]+$")) {
				contemApenasLetras = true;
			} else {
				System.out.println("Por favor, insira apenas letras e espaços: ");
			}
		}

		return entrada;
	}
}
