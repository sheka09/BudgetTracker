package io.dawn.budget.entity;



import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties 
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="expenses")
public class Expense implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="expense_id")
    @NotNull
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name="expense_price")
    private BigDecimal price;
    @Column(name="expense_category")
    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;
    @Column(name="expense_note")
    private String note;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="expense_date")
    private LocalDate date;
    
    @JsonBackReference
    @JoinColumn(name="account_id", nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private Account account;
    
  }
