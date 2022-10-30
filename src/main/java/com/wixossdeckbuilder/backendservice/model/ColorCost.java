package com.wixossdeckbuilder.backendservice.model;


import com.wixossdeckbuilder.backendservice.model.enums.Colors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ColorCost {
    private Colors color;
    private int amount;
}
