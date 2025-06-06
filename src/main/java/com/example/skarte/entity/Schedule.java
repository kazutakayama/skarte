package com.example.skarte.entity;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "schedule")
@Data
@EqualsAndHashCode(callSuper = false) // EntityBase
public class Schedule extends EntityBase {

    /** ID */
    @Id
    @Column
    @SequenceGenerator(name = "schedule_scheduleId_seq", sequenceName = "schedule_scheduleId_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_scheduleId_seq")
    private Long scheduleId = null;

    public void setId(Long scheduleId) {
        this.scheduleId = null;
    }
    
    /** 日付 */
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    /** 休日 */
    private boolean holiday = false;
    
}
