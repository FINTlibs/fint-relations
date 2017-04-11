package no.fint.relations.integration.testutils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    public enum Relasjonsnavn {
        COUNTRY
    }

    private String street;
    private String street2;
}
