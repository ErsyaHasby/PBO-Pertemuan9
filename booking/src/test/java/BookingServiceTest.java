import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.services.BookingService;
import com.services.Movie;
import com.services.MovieRepository;
import com.services.PaymentGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test BookingService - Sistem Pemesanan Tiket Bioskop")
class BookingServiceTest {

    @Mock
    private MovieRepository movieRepo;

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private BookingService bookingService;

    private Movie sampleMovie;

    @BeforeEach
    void setUp() {
        sampleMovie = new Movie("Avengers", 10, 50000);
    }

    // ---  Test Skenario Positif ---
    @Test
    @DisplayName("  Sukses memesan tiket saat kursi cukup dan pembayaran berhasil")
    void testBookingSuccess() {
        when(movieRepo.findByTitle("Avengers")).thenReturn(sampleMovie);
        when(paymentGateway.processPayment("user123", 2 * 50000)).thenReturn(true);

        String result = bookingService.bookTicket("user123", "Avengers", 2);

        assertAll(
            () -> assertEquals("Booking successful", result),
            () -> verify(movieRepo, times(1)).findByTitle("Avengers"),
            () -> verify(paymentGateway, times(1)).processPayment("user123", 100000),
            () -> verify(movieRepo, times(1)).updateSeats("Avengers", 8)
        );
    }
    

    // ---   Test Kasus Film Tidak Ditemukan ---
    @Test
    @DisplayName("  Gagal memesan jika film tidak ditemukan")
    void testMovieNotFound() {
        when(movieRepo.findByTitle("Unknown")).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
            bookingService.bookTicket("user123", "Unknown", 2)
        );

        assertEquals("Movie not found", ex.getMessage());
        verify(movieRepo, times(1)).findByTitle("Unknown");
        verifyNoInteractions(paymentGateway);
    }

    // ---  ztest Kasus Kursi Tidak Cukup ---
    @Test
    @DisplayName("  Gagal memesan jika kursi tidak cukup")
    void testNotEnoughSeats() {
        sampleMovie.setAvailableSeats(11);
        when(movieRepo.findByTitle("Avengers")).thenReturn(sampleMovie);

        Exception ex = assertThrows(IllegalStateException.class, () ->
            bookingService.bookTicket("user123", "Avengers", 3)
        );

        assertEquals("Not enough seats available", ex.getMessage());
        verify(movieRepo, times(1)).findByTitle("Avengers");
        verifyNoInteractions(paymentGateway);
    }

    // ---   Test Kasus Pembayaran Gagal ---
    @Test
    @DisplayName("  Gagal memesan jika pembayaran gagal")
    void testPaymentFailed() {
        when(movieRepo.findByTitle("Avengers")).thenReturn(sampleMovie);
        when(paymentGateway.processPayment("user123", 100000)).thenReturn(false);

        String result = bookingService.bookTicket("user123", "Avengers", 2);

        assertEquals("Payment failed", result);
        verify(movieRepo, times(1)).findByTitle("Avengers");
        verify(paymentGateway, times(1)).processPayment("user123", 100000);
        verify(movieRepo, never()).updateSeats(anyString(), anyInt());
    }

    // ---   Test Verifikasi Interaksi Lengkap ---
    @Test
    @DisplayName("Verifikasi panggilan metode eksternal")
    void testVerifyInteractions() {
        when(movieRepo.findByTitle("Avengers")).thenReturn(sampleMovie);
        when(paymentGateway.processPayment("user123", 100000)).thenReturn(false);

        bookingService.bookTicket("user123", "Avengers", 2);

        InOrder inOrder = inOrder(movieRepo, paymentGateway);
        inOrder.verify(movieRepo).findByTitle("Avengers");
        inOrder.verify(paymentGateway).processPayment("user123", 100000);
        inOrder.verify(movieRepo).updateSeats("Avengers", 8);
    }
}

