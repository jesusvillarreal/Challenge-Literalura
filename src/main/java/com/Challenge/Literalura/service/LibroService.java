package com.Challenge.Literalura.service;

import com.Challenge.Literalura.model.Autor;
import com.Challenge.Literalura.model.Libro;
import com.Challenge.Literalura.repository.AutorRepository;
import com.Challenge.Literalura.repository.LibroRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LibroService {

    private static final Logger logger = LoggerFactory.getLogger(LibroService.class);

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String API_URL = "https://gutendex.com/books/";

    public Libro fetchAndSaveBook(String title) throws JsonProcessingException {
        String url = API_URL + "?search=" + title;
        String response = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        JsonNode bookNode = rootNode.path("results").get(0);

        String bookTitle = bookNode.path("title").asText();
        String autorName = bookNode.path("authors").get(0).path("name").asText();

        // Verificar si el libro ya existe
        Optional<Libro> existingLibro = libroRepository.findByTitleAndAutorName(bookTitle, autorName);
        if (existingLibro.isPresent()) {
            logger.info("El libro ya existe en la base de datos: " + existingLibro.get());
            return existingLibro.get();
        }

        Libro libro = new Libro();
        libro.setTitle(bookTitle);
        libro.setLanguage(bookNode.path("languages").get(0).asText());
        libro.setDownloadCount(bookNode.path("download_count").asInt());

        JsonNode authorNode = bookNode.path("authors").get(0);
        Autor autor = new Autor();
        autor.setName(autorName);
        autor.setBirthYear(authorNode.path("birth_year").asInt());
        autor.setDeathYear(authorNode.path("death_year").asInt());

        // Verificar si el autor ya existe
        Optional<Autor> existingAutor = autorRepository.findByNameAndBirthYearAndDeathYear(
                autor.getName(), autor.getBirthYear(), autor.getDeathYear()
        );

        if (existingAutor.isPresent()) {
            autor = existingAutor.get();
            logger.info("El autor ya existe en la base de datos: " + autor);
        } else {
            autor = autorRepository.save(autor);
            logger.info("Nuevo autor guardado en la base de datos: " + autor);
        }

        libro.setAutor(autor);
        libro = libroRepository.save(libro);
        logger.info("Nuevo libro guardado en la base de datos: " + libro);

        return libro;
    }

    public List<Libro> getAllBooks() {
        List<Libro> libros = libroRepository.findAll();
        logger.info("Libros encontrados en la base de datos: " + libros.size());
        return libros;
    }

    public List<Libro> getBooksByLanguage(String language) {
        List<Libro> libros = libroRepository.findByLanguage(language);
        logger.info("Libros encontrados en la base de datos por idioma '" + language + "': " + libros.size());
        return libros;
    }

    public List<Autor> getAuthorsAliveInYear(int year) {
        List<Autor> autores = autorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
        logger.info("Autores vivos en el año " + year + " encontrados en la base de datos: " + autores.size());
        return autores;
    }
}
