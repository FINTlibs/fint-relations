package no.fint.relations.relations.hal

import no.fint.relations.integration.testutils.dto.Person
import spock.lang.Specification

class FintRelProviderSpec extends Specification {
    private FintRelProvider fintRelProvider

    void setup() {
        fintRelProvider = new FintRelProvider()
    }

    def "Return class simple name for single resource"() {
        when:
        def rel = fintRelProvider.getItemResourceRelFor(Person)

        then:
        rel == 'person'
    }

    def "Return _entries for collection resources"() {
        when:
        def rel = fintRelProvider.getCollectionResourceRelFor(Person)

        then:
        rel == '_entries'
    }

}
