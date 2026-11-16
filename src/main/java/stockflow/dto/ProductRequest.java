package stockflow.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(
    @NotBlank(message = "Το όνομα είναι υποχρεωτικό")
    @Size(max = 200, message = "Το όνομα δεν μπορεί να ξεπερνά τους 200 χαρακτήρες")
    String name,

    @NotBlank(message = "Το SKU είναι υποχρεωτικό")
    @Size(max = 50, message = "Το SKU δεν μπορεί να ξεπερνά τους 50 χαρακτήρες")
    String sku,

    @NotNull(message = "Η ποσότητα είναι υποχρεωτική")
    @Min(value = 0, message = "Η ποσότητα δεν μπορεί να είναι αρνητική")
    Integer quantity,

    @NotNull(message = "Η τιμή είναι υποχρεωτική")
    @DecimalMin(value = "0.0", inclusive = true, message = "Η τιμή δεν μπορεί να είναι αρνητική")
    BigDecimal price
){
}
