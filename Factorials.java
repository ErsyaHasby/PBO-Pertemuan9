import java.util.Scanner;

public class Factorials {
    public static void main(String[] args) {
        String keepGoing = "y";
        Scanner scan = new Scanner(System.in);

        while (keepGoing.equals("y") || keepGoing.equals("Y")) {
            System.out.print("Masukkan bilangan bulat: ");
            int val = scan.nextInt();

            try {
                System.out.println("Factorial(" + val + ") = " + MathUtils.factorial(val));
            } catch (IllegalArgumentException e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }

            System.out.print("Hitung lagi? (y/n): ");
            keepGoing = scan.next();
        }

        System.out.println("Program selesai.");
    }
}