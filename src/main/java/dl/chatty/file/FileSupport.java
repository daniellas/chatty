package dl.chatty.file;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * File create/search/read/write utilities. This class provides some kind if
 * file operations functional DSL
 * 
 * @author Daniel Łaś
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileSupport {

    /**
     * Generates traversable file path
     * 
     * @param path
     *            to construct
     * @return path construction {@link Function}
     */
    public static Function<String, Path> path(String path) {
        return p -> Paths.get(p, path);
    }

    public static Function<Path, Path> append(String path) {
        return p -> Paths.get(p.toString(), path);
    }

    public static Function<Optional<Path>, Optional<Path>> appendIfPresent(String path) {
        return p -> p.map(append(path));
    }

    /**
     * Gets {@link Optional} creation time
     * 
     * @return creation time {@link Function}
     */
    public static Function<Path, Optional<Date>> getCreationTime() {
        return path -> {
            try {
                FileTime time = Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes().creationTime();

                return Optional.of(Date.from(time.toInstant()));
            } catch (IOException e) {
                return Optional.empty();
            }
        };
    }

    /**
     * Creates directory from given {@link Path}, returns {@link Path}
     * {@link Optional} in case of success, empty otherwise
     * 
     * @return directory creating {@link Function}
     */
    public static Function<Path, Optional<Path>> mkdir() {
        return path -> {
            try {
                Path dir = Files.createDirectories(path);

                return Optional.of(dir);
            } catch (IOException e) {
                return Optional.empty();
            }
        };
    }

    /**
     * Creates file from give {@link Path} returns {@link Path} {@link Optional}
     * in case of success, empty otherwise
     * 
     * @return file creating {@link Function}
     */
    public static Function<Path, Optional<Path>> mkfile() {
        return path -> {
            try {
                Path file = Files.createFile(path);

                return Optional.of(file);
            } catch (IOException e) {
                return Optional.empty();
            }
        };
    }

    public static Function<Optional<Path>, Optional<Path>> mkfileIfPresent() {
        return path -> path.flatMap(mkfile());
    }

    public static Function<Path, Optional<Path>> write(String content) {
        return path -> {
            if (path.toFile().canWrite()) {
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    writer.write(content);

                    return Optional.of(path);
                } catch (IOException e) {
                    return Optional.empty();
                }
            }

            return Optional.empty();
        };
    }

    public static Function<Optional<Path>, Optional<Path>> writeIfPresent(String content) {
        return path -> {
            return path.flatMap(write(content));
        };
    }

    public static Function<Path, Optional<Stream<String>>> readLines() {
        return path -> {
            try {
                Stream<String> lines = Files.lines(path);

                return Optional.of(lines);
            } catch (IOException e) {
                return Optional.empty();
            }
        };
    }

    public static Function<Path, Optional<String>> read() {
        return path -> {
            return readLines().apply(path)
                    .map(s -> s.collect(Collectors.joining(System.lineSeparator())));
        };
    }

    public static Function<Optional<Path>, Optional<String>> readIfPresent() {
        return path -> {
            return path.flatMap(read());
        };
    }

    public static Function<String, Stream<Path>> findFiles(Predicate<Path> filter) {
        return path -> {
            try {
                Path root = Paths.get(path);

                return Files.find(root, 1, (p, attr) -> {
                    return filter.and(isNotRoot(root)).test(p);
                });
            } catch (IOException e) {
                return Stream.empty();
            }
        };
    }

    public static Predicate<Path> isDirectory() {
        return p -> p.toFile().isDirectory();
    }

    public static Predicate<Path> isFile() {
        return p -> p.toFile().isFile();
    }

    public static Predicate<Path> nameStartsWith(String prefix) {
        return p -> p.getFileName().toString().startsWith(prefix);
    }

    public static Predicate<Path> nameEndsWith(String suffix) {
        return p -> p.getFileName().toString().endsWith(suffix);
    }

    public static Predicate<Path> nameIs(String name) {
        return p -> name.equals(p.getFileName().toString());
    }

    public static Predicate<Path> isNotRoot(Path root) {
        return path -> !path.equals(root);
    }
}
