public class MathUtils {

    // Menghitung faktorial dari bilangan bulat positif
    public static int factorial(int n) throws IllegalArgumentException {
        if (n < 0)
            throw new IllegalArgumentException("Negatif tidak diperbolehkan untuk faktorial.");
        if (n > 16)
            throw new IllegalArgumentException("Nilai terlalu besar (overflow pada int).");

        int fac = 1;
        for (int i = n; i > 0; i--)
            fac *= i;

        return fac;
    }
}