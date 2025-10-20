import java.util.Scanner;

public class CountLetters {
    public static void main(String[] args) {
        int[] counts = new int[26];
        Scanner scan = new Scanner(System.in);

        // Meminta input dari pengguna
        System.out.print("Masukkan sebuah kata atau kalimat: ");
        String word = scan.nextLine();

        // Ubah semua huruf menjadi huruf besar
        word = word.toUpperCase();

        // Hitung frekuensi huruf
        for (int i = 0; i < word.length(); i++) {
            try {
                counts[word.charAt(i) - 'A']++;
            } catch (ArrayIndexOutOfBoundsException e) {
                // Tahap 2: Tangkap karakter non-huruf
                System.out.println("Not a letter: " + word.charAt(i));
            }
        }

        // Cetak hasil
        System.out.println("\nFrekuensi huruf:");
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] != 0)
                System.out.println((char) (i + 'A') + ": " + counts[i]);
        }
    }
}
