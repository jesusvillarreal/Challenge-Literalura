package com.Challenge.Literalura;

import com.Challenge.Literalura.model.Autor;
import com.Challenge.Literalura.model.Libro;
import com.Challenge.Literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private LibroService libroService;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		boolean running = true;

		while (running) {
			System.out.println("1. Buscar libro por título");
			System.out.println("2. Listar todos los libros");
			System.out.println("3. Listar libros por idioma");
			System.out.println("4. Listar autores vivos en un año específico");
			System.out.println("5. Salir");
			System.out.print("Elija una opción: ");
			int option = scanner.nextInt();
			scanner.nextLine(); // Consumir nueva línea

			switch (option) {
				case 1:
					System.out.print("Ingrese el título del libro: ");
					String title = scanner.nextLine();
					try {
						Libro libro = libroService.fetchAndSaveBook(title);
						if (libro != null) {
							System.out.println("Libro encontrado: " + libro);
						} else {
							System.out.println("No se encontró el libro.");
						}
					} catch (Exception e) {
						System.out.println("Error al buscar el libro: " + e.getMessage());
						e.printStackTrace();
					}
					break;
				case 2:
					List<Libro> libros = libroService.getAllBooks();
					if (libros.isEmpty()) {
						System.out.println("No hay libros registrados.");
					} else {
						libros.forEach(System.out::println);
					}
					break;
				case 3:
					System.out.print("Ingrese el idioma: ");
					String language = scanner.nextLine();
					List<Libro> librosPorIdioma = libroService.getBooksByLanguage(language);
					if (librosPorIdioma.isEmpty()) {
						System.out.println("No hay libros en ese idioma.");
					} else {
						librosPorIdioma.forEach(System.out::println);
					}
					break;
				case 4:
					System.out.print("Ingrese el año: ");
					int year = scanner.nextInt();
					List<Autor> autoresVivos = libroService.getAuthorsAliveInYear(year);
					if (autoresVivos.isEmpty()) {
						System.out.println("No hay autores vivos en ese año.");
					} else {
						autoresVivos.forEach(System.out::println);
					}
					break;
				case 5:
					running = false;
					break;
				default:
					System.out.println("Opción inválida");
			}
		}
	}
}
