package zad1;


import java.nio.file.*;
import java.nio.charset.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil implements FileVisitor<Path>{
    private static Path out;
    private static Path in;
    public Futil(String nazwaPliku) throws IOException {
        out = Paths.get(nazwaPliku);
        
    }
    public static void processDir(String inputName,String outputName){
        try {
            in = Paths.get(inputName);
            out = Paths.get(outputName);
            Futil futill = new Futil(outputName);
            Files.walkFileTree(in,futill);
        }catch (IOException e) {
            e.getCause();
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        try {
            FileChannel outputChannel = FileChannel.open(out, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            Charset cp1250 = Charset.forName("Cp1250");
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            try (FileChannel inputChannel = FileChannel.open(file, StandardOpenOption.READ)) {
                while (inputChannel.read(byteBuffer) != -1) {
                    byteBuffer.flip();
                    CharBuffer decoded = StandardCharsets.UTF_8.decode(byteBuffer);
                    outputChannel.write(cp1250.encode(decoded));
                    byteBuffer.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputChannel.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}



