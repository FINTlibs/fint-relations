package no.fint.relations.relations.hal;

import lombok.extern.slf4j.Slf4j;
import no.fint.relation.model.Relation;
import no.fint.relations.annotations.mapper.FintLinkMapper;
import no.fint.relations.annotations.mapper.FintLinkRelation;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Component
public class FintMappers implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private Map<String, FintRelationObjectMethod> mapperMethods;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        mapperMethods = new HashMap<>();
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(FintLinkMapper.class);
        Collection<?> linkMappers = beans.values();
        for (Object linkMapper : linkMappers) {
            addRelationObjects(linkMapper);
        }
    }

    private void addRelationObjects(Object linkMapper) {
        Method[] methods = linkMapper.getClass().getMethods();
        for (Method method : methods) {
            FintLinkRelation fintLinkRelation = AnnotationUtils.getAnnotation(method, FintLinkRelation.class);
            String relationId = getRelationId(fintLinkRelation);
            if (relationId != null) {
                validateLinkMapperMethod(method);
                mapperMethods.put(relationId, new FintRelationObjectMethod(method, linkMapper));
            }
        }
    }

    public Map<String, FintRelationObjectMethod> getLinkMappers() {
        return mapperMethods;
    }

    public Optional<FintRelationObjectMethod> getMethod(String relationId) {
        return Optional.ofNullable(mapperMethods.get(relationId));
    }

    private void validateLinkMapperMethod(Method method) {
        int parameterCount = method.getParameterCount();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        if (parameterCount != 1 || parameterTypes[0] != Relation.class || (returnType != Link.class && returnType != List.class)) {
            log.error("The method {} needs to take a Relation object as input and return a Link or a list of Link objects", method.getName());
        }
    }

    private String getRelationId(FintLinkRelation fintLinkRelation) {
        if (fintLinkRelation == null) {
            return null;
        }

        String leftKeyClass = fintLinkRelation.leftObject().getSimpleName().toLowerCase();
        String leftKeyProperty = fintLinkRelation.leftId();
        String rightKeyClass = fintLinkRelation.rightObject().getSimpleName().toLowerCase();
        String rightKeyProperty = fintLinkRelation.rightId();
        return String.format("%s.%s:%s.%s", leftKeyClass, leftKeyProperty, rightKeyClass, rightKeyProperty);
    }

}