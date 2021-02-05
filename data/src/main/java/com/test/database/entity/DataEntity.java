package com.test.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.aop.AuditData;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Alexander Zubkov
 */
@Entity
@Table(name = "data", schema = "data")
@Data
public class DataEntity implements AuditData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "username")
    @JsonIgnore
    private String username;

    @Override
    public String get() {
        return id.toString();
    }
}
