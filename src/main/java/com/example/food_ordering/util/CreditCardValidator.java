package com.example.food_ordering.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreditCardValidator {
    public static boolean isValidCardNumber(String cardNumber) {
        // Kart numarasının yalnızca 16 rakamdan oluştuğunu kontrol et
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            return false;
        }

        // Luhn algoritması ile kart numarasını doğrulama
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(cardNumber.charAt(i));

            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }

            sum += n;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }

    public static boolean isValidCvv(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }

    public static boolean isValidExpirationDate(String expirationDate) {
        if (expirationDate == null || !expirationDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            return false;
        }

        // Tarih formatını kontrol et
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        LocalDate expiration = LocalDate.parse("20" + expirationDate.substring(3, 5) + "-" + expirationDate.substring(0, 2) + "-01", formatter);

        // Geçerlilik tarihinin gelecekte olup olmadığını kontrol et
        return !expiration.isBefore(LocalDate.now());
    }
}
