package no.fint.relations.integration.testutils.mapper;

import no.fint.relation.model.Relation;
import no.fint.relations.FintLinkMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class TestLinkMapper implements FintLinkMapper {

    @Override
    public Link createRelation(Relation relation) {
        return null;
    }

    @Override
    public Class<?> type() {
        return null;
    }
}
