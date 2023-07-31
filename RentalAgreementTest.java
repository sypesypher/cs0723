import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class RentalAgreementTest {

    @Test
    public void test0() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RentalAgreement agreement = new RentalAgreement("JAKR", 0, LocalDate.of(2015, 9, 3),0);
        });
        assertTrue(exception.getMessage().contains("Rental Days must be greater than 1"));
    }

    @Test
    public void test1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RentalAgreement agreement = new RentalAgreement("JAKR", 5, LocalDate.of(2015, 9, 3),101);
        });
        assertTrue(exception.getMessage().contains("Invalid Discount Percent, Discount percentage must be between 0 and 100 (Percent %)"));
    }

    @Test
    public void test2() {
        RentalAgreement agreement = new RentalAgreement("LADW", 3, LocalDate.of(2020, 7, 2),10);
        assertEquals(LocalDate.of(2020,7,5),agreement.calculateDueDate());
        assertEquals(398, agreement.calculatePreDiscountCharge());
        assertEquals(40, agreement.calculateDiscountAmount());
        assertEquals(358, agreement.calculateFinalCharge());
    }

    @Test
    public void test3() {
        RentalAgreement agreement = new RentalAgreement("CHNS", 5, LocalDate.of(2015, 7, 2),25);
        assertEquals(LocalDate.of(2015,7,7),agreement.calculateDueDate());
        assertEquals(447, agreement.calculatePreDiscountCharge());
        assertEquals(112, agreement.calculateDiscountAmount());
        assertEquals(335, agreement.calculateFinalCharge());
    }

    @Test
    public void test4() {
        RentalAgreement agreement = new RentalAgreement("JAKD", 6, LocalDate.of(2015, 9, 3), 0);
        assertEquals(LocalDate.of(2015,9,9),agreement.calculateDueDate());
        assertEquals(897, agreement.calculatePreDiscountCharge());
        assertEquals(0, agreement.calculateDiscountAmount());
        assertEquals(897, agreement.calculateFinalCharge());
    }

    @Test
    public void test5() {
        RentalAgreement agreement = new RentalAgreement("JAKR", 9, LocalDate.of(2015, 7, 2), 0);
        assertEquals(LocalDate.of(2015,7,11),agreement.calculateDueDate());
        assertEquals(1495, agreement.calculatePreDiscountCharge());
        assertEquals(0, agreement.calculateDiscountAmount());
        assertEquals(1495, agreement.calculateFinalCharge());
    }

    @Test
    public void test6() {
        RentalAgreement agreement = new RentalAgreement("JAKR", 4, LocalDate.of(2020, 7, 2), 50);
        assertEquals(LocalDate.of(2020,7,6),agreement.calculateDueDate());
        assertEquals(299, agreement.calculatePreDiscountCharge());
        assertEquals(150, agreement.calculateDiscountAmount());
        assertEquals(149, agreement.calculateFinalCharge());
    }

    @Test
    public void test7() {
        RentalAgreement agreement = new RentalAgreement("JAKR", 365, LocalDate.of(2020, 7, 2), 10);
        assertEquals(LocalDate.of(2021,7,2),agreement.calculateDueDate());
        assertEquals(77441, agreement.calculatePreDiscountCharge());
        assertEquals(7744, agreement.calculateDiscountAmount());
        assertEquals(69697, agreement.calculateFinalCharge());
    }

    @Test
    public void test8() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RentalAgreement agreement = new RentalAgreement("NOTVALID", 5, LocalDate.of(2015, 9, 3),0);
        });
        assertTrue(exception.getMessage().contains("Invalid Tool Code/Tool Code Not found"));
    }
}