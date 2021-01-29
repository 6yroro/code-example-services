package com.test.database.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Alexander Zubkov
 */
@Entity
@Table(name = "records", schema = "audit")
@DynamicInsert
@Data
public class AuditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String user;

    @Column(name = "action")
    private String action;

    @Column(name = "result")
    private String result;

    @Column(name = "date")
    private LocalDateTime date;

}