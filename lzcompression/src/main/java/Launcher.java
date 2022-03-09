import lzw.Lzw;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class Launcher {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("C:\\Users\\adm-sabitovka\\Desktop\\1.txt");
        FileOutputStream fos = new FileOutputStream("C:\\Users\\adm-sabitovka\\Desktop\\2.txt");

        Lzw.compress(fis, fos);

        fis.close();
        fos.close();

        /*FileInputStream*/ fis = new FileInputStream("C:\\Users\\adm-sabitovka\\Desktop\\2.txt");
        /*FileOutputStream*/ fos = new FileOutputStream("C:\\Users\\adm-sabitovka\\Desktop\\3.txt");

        Lzw.decode(fis, fos);

        fis.close();
        fos.close();
    }
}
