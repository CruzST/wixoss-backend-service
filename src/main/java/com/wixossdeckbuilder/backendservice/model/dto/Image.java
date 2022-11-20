package com.wixossdeckbuilder.backendservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Image {
    private String imageName;
    private String imageSourceURL;
}
