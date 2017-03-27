package no.fint.relations.relations.hal

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import no.fint.relations.AspectMetadata
import no.fint.relations.annotations.FintRelation
import no.fint.relations.annotations.FintSelf
import no.fint.relations.integration.testutils.controller.PersonRelationController
import no.fint.relations.integration.testutils.dto.Person
import org.slf4j.LoggerFactory
import org.springframework.hateoas.Link
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ReflectionUtils
import spock.lang.Specification

class FintRelationHalSpec extends Specification {
    private FintRelationHal fintRelationHal
    private AspectMetadata metadata
    private SpringHateoasIntegration springHateoasIntegration
    private HalResourceLinks halResourceLinks

    void setup() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)
        logger.setLevel(Level.INFO)

        metadata = Mock(AspectMetadata) {
            getCallingClass() >> PersonRelationController
            getMethod() >> ReflectionUtils.findMethod(PersonRelationController, 'getResponseEntityNoInput')
            getArguments() >> new Object[0]
            getFintSelf() >> Mock(FintSelf) {
                self() >> Person
                id() >> 'name'
            }
        }
        springHateoasIntegration = Mock(SpringHateoasIntegration) {
            getSelfLink(metadata) >> new Link('http://localhost', Link.REL_SELF)
            getSelfLinkCollection(metadata, _ as String) >> new Link('http://localhost', Link.REL_SELF)
        }
        halResourceLinks = Mock(HalResourceLinks)
        fintRelationHal = new FintRelationHal(springHateoasIntegration: springHateoasIntegration, halResourceLinks: halResourceLinks)
    }

    def "Add self relation to single resource"() {
        given:
        def response = ResponseEntity.ok(new Person(name: 'test'))
        def relations = [] as FintRelation[]

        when:
        def returnValue = fintRelationHal.addRelations(metadata, relations, response)
        def responseEntity = (ResponseEntity) returnValue
        def body = (Resource) responseEntity.getBody()

        then:
        responseEntity.statusCode == HttpStatus.OK
        body.links.size() == 1
        body.links[0].rel == Link.REL_SELF
    }


    def "Add self relation to collection resource"() {
        given:
        def response = ResponseEntity.ok([new Person(name: 'test')])
        def relations = [] as FintRelation[]

        when:
        def returnValue = fintRelationHal.addRelations(metadata, relations, response)
        def responseEntity = (ResponseEntity) returnValue
        def body = (Resources) responseEntity.getBody()

        then:
        responseEntity.statusCode == HttpStatus.OK
        body.links.size() == 1
        body.links[0].rel == Link.REL_SELF
    }

    def "Add self and relation to other resource for single resource response"() {
        given:
        def response = ResponseEntity.ok(new Person(name: 'test'))
        def relations = [Mock(FintRelation)] as FintRelation[]

        when:
        def returnValue = fintRelationHal.addRelations(metadata, relations, response)
        def responseEntity = (ResponseEntity) returnValue
        def body = (Resource) responseEntity.getBody()

        then:
        1 * halResourceLinks.getLinks(_ as Object, _ as AspectMetadata, _ as FintRelation) >> [new Link('http://localhost', 'test')]
        responseEntity.statusCode == HttpStatus.OK
        body.links.size() == 2
    }

    def "Add self and relation to other resource for collection resource response"() {
        given:
        def response = ResponseEntity.ok([new Person(name: 'test')])
        def relations = [Mock(FintRelation)] as FintRelation[]

        when:
        def returnValue = fintRelationHal.addRelations(metadata, relations, response)
        def responseEntity = (ResponseEntity) returnValue
        def body = (Resources) responseEntity.getBody()

        then:
        1 * halResourceLinks.getLinks(_ as Object, _ as AspectMetadata, _ as FintRelation) >> [new Link('http://localhost', 'test')]
        responseEntity.statusCode == HttpStatus.OK
        body.links.size() == 1
        body.links[0].rel == Link.REL_SELF
        ((Resource) body[0]).links.size() == 2
    }

}
