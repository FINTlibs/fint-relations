package no.fint.relations.integration.testutils.controller;

import no.fint.relations.annotations.FintRelation;
import no.fint.relations.annotations.FintSelf;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@FintSelf(self = Address.class, id = "street")
@FintRelation("unkown-property")
@RestController
@RequestMapping(value = "/noMatchingLinkMappers", method = RequestMethod.GET)
public class NoLinkMappersTestController {

    @RequestMapping
    public ResponseEntity getResponseEntity() {
        return ResponseEntity.ok(new Person("test123"));
    }

}
