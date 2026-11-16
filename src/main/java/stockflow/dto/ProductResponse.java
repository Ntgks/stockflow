package stockflow.dto;

import java.math.BigDecimal;

public record ProductResponse(
    Long id,
    String name,
    String sku,
    int quantity,
    BigDecimal price
) 
{    
}
