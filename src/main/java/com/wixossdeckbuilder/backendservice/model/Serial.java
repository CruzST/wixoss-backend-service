package com.wixossdeckbuilder.backendservice.model;

import com.wixossdeckbuilder.backendservice.model.enums.CardSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class Serial {
    private String serialNumber;
    private String formatSet;
    private CardSet cardSet;
    private int cardNumber;
    private String cardSetName;
}
