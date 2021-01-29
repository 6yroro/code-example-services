package com.test.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Alexander Zubkov
 */
@Entity
@Table(name = "data", schema = "data")
@Data
public class DataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "date")
    private LocalDateTime date;

}
