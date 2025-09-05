package me.artm2000.hibernatecache.database.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notes")
@Cache(
    usage = CacheConcurrencyStrategy.READ_WRITE,
    region = "entity.notes"
)
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @Column(nullable = false)
    private Boolean archived = false;
}
