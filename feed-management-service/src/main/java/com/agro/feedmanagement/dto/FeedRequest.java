package com.agro.feedmanagement.dto;

import java.time.LocalDateTime;

import com.agro.feedmanagement.entity.FeedType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FeedRequest(
		 @NotBlank(message = "animalId é obrigatório")
         String animalId,

         @NotNull(message = "feedType é obrigatório")
         FeedType feedType,

         @NotNull(message = "quantity é obrigatória")
         @Positive(message = "quantity deve ser maior que zero")
         Double quantity,

         LocalDateTime recordedAt
		) {

}
