package com.test.database.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Alexander Zubkov
 */
@Entity
@Table(name = "record", schema = "audit")
@DynamicInsert
@Data
public class AuditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "action")
    private String action;

    @Column(name = "params")
    private String params;

    @Column(name = "result")
    private String result;

    @Column(name = "date")
    private LocalDateTime date;

}
