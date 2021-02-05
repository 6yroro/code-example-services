package com.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Alexander Zubkov
 */
@Data
@AllArgsConstructor
public class AuditMessage implements Serializable {

    private static final long serialVersionUID = 1;

    private String username;
    private String action;
    private String params;
    private String result;
    private LocalDateTime date;

}
