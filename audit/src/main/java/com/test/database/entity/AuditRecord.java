package com.test.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
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

    public AuditRecord(String username, String action, String params, String result, LocalDateTime date) {
        this.username = username;
        this.action = action;
        this.params = params;
        this.result = result;
        this.date = date;
    }
}
