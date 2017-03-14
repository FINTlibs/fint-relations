package no.fint.relations.integration.testutils.mapper;

import no.fint.relation.model.Relation;
import no.fint.relations.annotations.FintLinkMapper;
import no.fint.relations.annotations.FintLinkRelation;
import no.fint.relations.integration.testutils.dto.Address;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@FintLinkMapper
@Component
public class AddressLinkMapper {

    @FintLinkRelation(leftObject = Person.class, leftId = "name", rightObject = Address.class, rightId = "street")
    public Link createLink(Relation relation) {
        return new Link("http://localhost/address/" + relation.getLeftKey(), "address");
    }
}
