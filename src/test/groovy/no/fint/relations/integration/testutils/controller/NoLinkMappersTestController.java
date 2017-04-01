package no.fint.relations.integration.testutils.controller;

import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/noLinkMappers", method = RequestMethod.GET, produces = {"application/hal+json"})
public class NoLinkMappersTestController {

    @RequestMapping
    public ResponseEntity getResponseEntity() {
        return ResponseEntity.ok(new Person("test123"));
    }
}
