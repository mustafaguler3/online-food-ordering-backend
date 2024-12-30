package com.example.food_ordering.controller;

import com.example.food_ordering.dto.SavedCardDto;
import com.example.food_ordering.entities.SavedCard;
import com.example.food_ordering.entities.User;
import com.example.food_ordering.repository.SavedCardRepository;
import com.example.food_ordering.service.UserDetailsImpl;
import com.example.food_ordering.util.CreditCardValidator;
import com.example.food_ordering.util.CurrentUserProvider;
import com.example.food_ordering.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/cards")
public class SavedCardController {

    @Autowired
    private SavedCardRepository savedCardRepository;
    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private DTOConverter dtoConverter;

    @PostMapping("/save")
    public ResponseEntity<?> addCreditCard(@RequestBody SavedCardDto savedCardDto) throws ParseException {
        SavedCard newCard = new SavedCard();

        // Kart numarasının geçerli olup olmadığını kontrol et
        if (!CreditCardValidator.isValidCardNumber(savedCardDto.getCardNumber())) {
            return new ResponseEntity<>("Invalid card number. Card number must be 16 digits and valid.", HttpStatus.BAD_REQUEST);
        }

        // Kart numarası geçerli ise devam et
        newCard.setCardHolderName(savedCardDto.getCardHolderName());
        newCard.setMaskedCardNumber(maskedCardNumber(savedCardDto.getCardNumber()));

        // Expiry date'ini al
        if (savedCardDto.getExpiryDate() != null) {
            String expiryDateStr = savedCardDto.getExpiryDate();

            // MM/yy formatını doğrulama
            if (expiryDateStr.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                newCard.setExpiryDate(expiryDateStr);  // Doğrudan String olarak sakla
            } else {
                return new ResponseEntity<>("Invalid expiration date format. Expected MM/yy.", HttpStatus.BAD_REQUEST);
            }
        }

        // CVV doğrulaması ve maskelenmesi
        if (!CreditCardValidator.isValidCvv(savedCardDto.getCvv())) {
            return new ResponseEntity<>("Invalid CVV. CVV must be 3 digits.", HttpStatus.BAD_REQUEST);
        }
        newCard.setCvv(maskCVV(savedCardDto.getCvv()));

        // Kullanıcı bilgisini al ve ilişkilendir
        UserDetailsImpl userDetails = currentUserProvider.getCurrentUserDetails();
        newCard.setUser(userDetails.user);

        // Kartı veritabanına kaydet
        savedCardRepository.save(newCard);
        return new ResponseEntity<>(newCard, HttpStatus.CREATED);
    }

    public String maskCVV(String cvv) {
        if (cvv != null && cvv.length() == 3) {
            return "***";
        }
        return cvv;
    }
    public static String maskedCardNumber(String cardNumber) {
        if (cardNumber != null && cardNumber.length() == 16) {
            return cardNumber.substring(0, 6) + "******" + cardNumber.substring(12);
        }
        return cardNumber;
    }

    @GetMapping("/saved-cards")
    public ResponseEntity<?> getSavedCards(){
        UserDetailsImpl userDetails = currentUserProvider.getCurrentUserDetails();
        List<SavedCard> savedCards = savedCardRepository.findSavedCardsByUser(userDetails.user);

        List<SavedCardDto> savedCardDtos = dtoConverter.toSavedCardDtoList(savedCards);

        return ResponseEntity.ok(savedCardDtos);
    }
}
