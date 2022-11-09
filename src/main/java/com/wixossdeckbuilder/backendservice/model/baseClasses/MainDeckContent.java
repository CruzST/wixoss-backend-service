package com.wixossdeckbuilder.backendservice.model.baseClasses;

import com.wixossdeckbuilder.backendservice.model.entities.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MainDeckContent {
    Card card;
    int amount;
}
