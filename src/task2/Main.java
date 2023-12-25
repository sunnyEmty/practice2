package task2;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import static edu.mirea.rksp.pr2.task1.Main.createFile;

public class Main {
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            // 65536 .. 8192 4096
            byte[] buffer = new byte[65536];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally{
            sourceChannel.close();
            destChannel.close();
        }
    }

    private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }

    private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        String currentDir = System.getProperty("user.dir");
        new File(currentDir+"/tmp").mkdir();
        Path tmpDir = Paths.get(currentDir, "tmp");
        System.out.println("Working directory is - "+tmpDir);

        // removing previous files
        File directory = new File(tmpDir.toString());
        FileUtils.cleanDirectory(directory);

        // creating FILE_SIZE(mb) file
        final int FILE_SIZE = 100;
        File myFile = createFile("FILE", tmpDir.toString(),FILE_SIZE * 1024 * 1024);

        // copy using Stream
        File dest = new File(tmpDir+"/FILE_copy1");
        long start = System.nanoTime();
        copyFileUsingStream(myFile, dest);
        System.out.println("[IOStreams] Time - "+(System.nanoTime()-start));

        // copy using java.nio.FileChannel
        dest = new File(tmpDir+"/FILE_copy2");
        start = System.nanoTime();
        copyFileUsingChannel(myFile, dest);
        System.out.println("[FileChannel] Time - "+(System.nanoTime()-start));

        // copy using Apache Commons io
        dest = new File(tmpDir+"/FILE_copy3");
        start = System.nanoTime();
        copyFileUsingApacheCommonsIO(myFile, dest);
        System.out.println("[Apache Commons IO] Time - "+(System.nanoTime()-start));

        // copy using Files class
        dest = new File(tmpDir+"/FILE_copy4");
        start = System.nanoTime();
        copyFileUsingJava7Files(myFile, dest);
        System.out.println("[Files class] Time - "+(System.nanoTime()-start));
    }
}