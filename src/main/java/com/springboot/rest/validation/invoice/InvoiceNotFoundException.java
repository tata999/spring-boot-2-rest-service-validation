package com.springboot.rest.validation.invoice;

public class InvoiceNotFoundException extends RuntimeException {

    public InvoiceNotFoundException(String exception) {
        super(exception);
    }
}
