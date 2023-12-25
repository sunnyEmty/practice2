package task1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLSyntaxErrorException;

public class Main {
    public static void main(String[] args) throws IOException {

        String buff_dir = "buff";

        new File(System.getProperty("user.dir")+"/"+buff_dir).mkdir();

        File newFile = makeFile("test_file.txt",
                                Paths.get(System.getProperty("user.dir"), buff_dir).toString(),
                                0);

        String strToWrite = "Hello! !!!";

        Files.write(newFile.toPath(), strToWrite.getBytes());


        String readStr = new String(Files.readAllBytes(newFile.toPath()));

        System.out.println("File " + newFile.getName() + " contains " + readStr);

    }


    public static File makeFile(String fileName, String path, long size) throws IOException {
        File file = new File(path + File.separator + fileName);
        file.createNewFile();

        RandomAccessFile randAcess = new RandomAccessFile(file, "rw");
        randAcess.setLength(size);
        randAcess.close();
        return file;
    }


}
