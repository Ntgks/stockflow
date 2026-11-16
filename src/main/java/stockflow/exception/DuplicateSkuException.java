package stockflow.exception;

public class DuplicateSkuException extends RuntimeException{
    
    public DuplicateSkuException(String sku){
        super("Υπάρχει ήδη προϊόν με SKU: " + sku);
    }
}
