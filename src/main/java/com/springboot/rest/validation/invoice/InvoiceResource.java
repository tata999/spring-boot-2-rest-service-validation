package com.springboot.rest.validation.invoice;

import com.springboot.rest.validation.invoice.Invoice;
import com.springboot.rest.validation.invoice.InvoiceNotFoundException;
import com.springboot.rest.validation.invoice.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class InvoiceResource {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping("/invoices")
    public List<Invoice> retrieveAllInvoices() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/invoices/{id}")
    public Resource<Invoice> retrieveInvoice(@PathVariable long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);

        if (!invoice.isPresent())
            throw new InvoiceNotFoundException("id-" + id);

        Resource<Invoice> resource = new Resource<Invoice>(invoice.get());

        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllInvoices());

        resource.add(linkTo.withRel("all-invoices"));

        return resource;
    }

    @DeleteMapping("/invoices/{id}")
    public void deleteInvoice(@PathVariable long id) {
        invoiceRepository.deleteById(id);
    }

    @PostMapping("/invoices")
    public ResponseEntity<Object> createInvoice(@Valid @RequestBody Invoice invoice) {
        Invoice savedInvoice = invoiceRepository.save(invoice);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedInvoice.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<Object> updateInvoice(@Valid @RequestBody Invoice invoice, @PathVariable long id) {

        Optional<Invoice> invoiceOptional = invoiceRepository.findById(id);

        if (!invoiceOptional.isPresent())
            return ResponseEntity.notFound().build();

        invoice.setId(id);

        invoiceRepository.save(invoice);

        return ResponseEntity.noContent().build();
    }
}
