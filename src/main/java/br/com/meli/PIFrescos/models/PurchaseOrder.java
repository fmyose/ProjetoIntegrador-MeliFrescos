package br.com.meli.PIFrescos.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Ana Preis
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User field can't be empty")
    @ManyToOne
    private User user;
    private LocalDate date;
    @NotNull(message = "OrderStatus field can't be empty")
    private OrderStatus orderStatus;
}