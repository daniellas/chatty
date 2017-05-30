package dl.chatty.file;

import static dl.chatty.FileTestUtil.*;
import static dl.chatty.file.FileSupport.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileSupportTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void shouldCrateFolderStructure() {
        Path path = path("a").andThen(append("b")).apply("/");

        assertThat(path.toString(), is("/a/b"));
    }

    @Test
    public void shouldCreateFolder() {
        Optional<Path> folder = path("a")
                .andThen(mkdir())
                .apply(absolutePath(tmp.getRoot()));

        assertThat(folder.isPresent(), is(true));
        assertFolderExists(folder.get().toAbsolutePath().toString());
    }

    @Test
    public void shouldReturnEmptyOnFolderCreationError() {
        Optional<Path> folder = path(createReadOnlyFolder(tmp.getRoot()))
                .andThen(append("folder"))
                .andThen(mkdir())
                .apply(absolutePath(tmp.getRoot()));

        assertFalse(folder.isPresent());
    }

    @Test
    public void shouldCreateFile() {
        Optional<Path> file = path("file")
                .andThen(mkfile())
                .apply(tmp.getRoot().getAbsolutePath());

        assertFileExists(file.get().toAbsolutePath().toString());
    }

    @Test
    public void shouldReturnEmptyOnFileCreationError() {
        Optional<Path> file = path(createReadOnlyFolder(tmp.getRoot()))
                .andThen(append("file"))
                .andThen(mkfile())
                .apply(absolutePath(tmp.getRoot()));

        assertFalse(file.isPresent());
    }

    @Test
    public void shouldReturnCreationTime() {
        Optional<Path> folder = path("folder")
                .andThen(mkdir())
                .apply(absolutePath(tmp.getRoot()));

        Optional<Date> time = folder.flatMap(creationTime());

        assertTrue(time.isPresent());
    }

    @Test
    public void shouldReturnEmptyCreationTimeOnMissingFile() {
        Optional<Date> time = path(createReadOnlyFolder(tmp.getRoot()))
                .andThen(append("folder"))
                .andThen(creationTime())
                .apply(absolutePath(tmp.getRoot()));

        assertFalse(time.isPresent());
    }

    @Test
    public void shouldWriteToReadFromFile() {
        Optional<String> content = path("file")
                .andThen(mkfile())
                .andThen(flatWrite("line"))
                .andThen(flatRead())
                .apply(absolutePath(tmp.getRoot()));

        assertFileExists(absolutePath(tmp.getRoot()) + "/file");
        assertTrue(content.isPresent());
        assertThat(content.get(), is("line"));
    }

    @Test
    public void shouldWriteToReadFromFileMultipleLines() {
        Optional<String> content = path("file")
                .andThen(mkfile())
                .andThen(flatWrite("line1\nline2"))
                .andThen(flatRead())
                .apply(absolutePath(tmp.getRoot()));

        assertFileExists(absolutePath(tmp.getRoot()) + "/file");
        assertTrue(content.isPresent());
        assertThat(content.get(), is("line1\nline2"));
    }

    @Test
    public void shouldFailWriteToReadOnlyFile() {
        Optional<Path> file = path(createReadOnlyFile(tmp.getRoot(), "file"))
                .andThen(write("line"))
                .apply(absolutePath(tmp.getRoot()));

        assertFalse(file.isPresent());
    }

    @Test
    public void shouldFailToReadFromNonExistingFile() {
        Optional<String> content = path("file")
                .andThen(read())
                .apply(absolutePath(tmp.getRoot()));

        assertFalse(content.isPresent());
    }

    @Test
    public void shouldCreateFolderFileWriteRead() {
        Optional<String> content = path("folder")
                .andThen(mkdir())
                .andThen(flatAppend("file"))
                .andThen(flatMkfile())
                .andThen(flatWrite("line"))
                .andThen(flatRead())
                .apply(absolutePath(tmp.getRoot()));

        assertThat(content.get(), is("line"));
    }

    @Test
    public void shouldFindAllFoldersExceptRoot() {
        path("id1-customer1")
                .andThen(mkdir())
                .apply(absolutePath(tmp.getRoot()));
        path("id2-customer2")
                .andThen(mkdir())
                .apply(absolutePath(tmp.getRoot()));

        List<String> files = findFiles(isDirectory())
                .apply(absolutePath(tmp.getRoot()))
                .map(f -> f.getFileName().toString())
                .collect(Collectors.toList());

        assertThat(files, containsInAnyOrder("id1-customer1", "id2-customer2"));
    }

    @Test
    public void shouldFindAllFiles() {
        path("id1-customer1")
                .andThen(mkdir())
                .apply(absolutePath(tmp.getRoot()));
        path("id2-customer2")
                .andThen(mkdir())
                .apply(absolutePath(tmp.getRoot()));

        path("file1")
                .andThen(mkfile())
                .apply(absolutePath(tmp.getRoot()));
        path("file2")
                .andThen(mkfile())
                .apply(absolutePath(tmp.getRoot()));

        List<String> files = findFiles(isFile())
                .apply(absolutePath(tmp.getRoot()))
                .map(f -> f.getFileName().toString())
                .collect(Collectors.toList());

        assertThat(files, containsInAnyOrder("file1", "file2"));
    }

    @Test
    public void shouldFindFolderByStartsWith() {
        path("id1-customer1")
                .andThen(mkdir())
                .apply(absolutePath(tmp.getRoot()));
        path("id2-customer2")
                .andThen(mkdir())
                .apply(absolutePath(tmp.getRoot()));

        List<String> files = findFiles(isDirectory().and(nameStartsWith("id1")))
                .apply(absolutePath(tmp.getRoot()))
                .map(f -> f.getFileName().toString())
                .collect(Collectors.toList());

        assertThat(files, containsInAnyOrder("id1-customer1"));
    }

}
