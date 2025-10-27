import java.util.Scanner;

public class ParseInts {
    public static void main(String[] args) {
        int val, sum = 0;
        Scanner scan = new Scanner(System.in);

        System.out.println("Masukkan satu baris teks:");
        Scanner scanLine = new Scanner(scan.nextLine());

        // Tahap 2: try-catch di dalam loop agar program tidak berhenti di tengah
        while (scanLine.hasNext()) {
            try {
                val = Integer.parseInt(scanLine.next());
                sum += val;
            } catch (NumberFormatException e) {
                // Token bukan angka, diabaikan
            }
        }

        System.out.println("Jumlah bilangan bulat dalam baris ini adalah " + sum + ".");
    }
}