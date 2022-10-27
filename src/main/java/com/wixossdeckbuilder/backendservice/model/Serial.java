package com.wixossdeckbuilder.backendservice.model;

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
