package task3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    // Compute a 16-bit checksum for all the remaining bytes
    // in the given byte buffer
    private static int sum(ByteBuffer bb) {
        int sum = 0;
        while (bb.hasRemaining()) {
            if ((sum & 1) != 0)
                sum = (sum >> 1) + 0x8000;
            else
                sum >>= 1;
            sum += bb.get() & 0xff;
            sum &= 0xffff;
        }
        return sum;
    }

    // Compute and print a checksum for the given file
    public static void sum(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();

        // Get the file's size and then map it into memory
        int sz = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);

        // Compute and print the checksum
        int sum = sum(bb);
        int kb = (sz + 1023) / 1024;
        String s = Integer.toString(sum);
        System.out.println(file.getName()+": FileSize "+kb+" KB, CheckSum="+s);

        // Close the channel and the stream
        fc.close();
        fis.close();
    }

    public static void main(String[] args) throws IOException {
        String currentDir = System.getProperty("user.dir");
        new File(currentDir+"/tmp").mkdir();
        Path tmpDir = Paths.get(currentDir, "tmp");
        System.out.println("Working directory is - "+tmpDir);

        Scanner in = new Scanner(System.in);
        System.out.print("Input a name of your file with extension: ");
        File myFile = new File(tmpDir + File.separator + in.nextLine());

        try {
            sum(myFile);
        } catch (IOException e) {
            System.err.println(myFile + ": " + e);
        }
    }
}