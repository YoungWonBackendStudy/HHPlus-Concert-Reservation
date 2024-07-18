package io.hhplus.concert.infra.concert;

import io.hhplus.concert.domain.concert.Concert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "concert")
@NoArgsConstructor
public class ConcertEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    
    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    public ConcertEntity(Concert domain) {
        this.id = domain.getId();
        this.name = domain.getName();
        this.description = domain.getDescription();
    }

    public Concert toDomain(){
        return new Concert(this.id, this.name, this.description);
    }
}
