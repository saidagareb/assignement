package com.example.assignement.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
@Table(name = "bank_account", schema = "assignement")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")

    private Customer customer;

    private BigDecimal balance;

    @JsonIgnore
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransferHistory> transferHistories = new ArrayList<>();

    public void addTransaction(TransferHistory transfer) {
        transferHistories.add(transfer);
        transfer.setBankAccount(this);
    }

    public void removeTransaction(TransferHistory transfer) {
        transferHistories.remove(transfer);
        transfer.setBankAccount(null);
    }

    @Override
    public String toString() {
        return "BankAccount{" + "id=" + id + ", customerId=" + (customer != null ? customer.getId() : "null") + // Avoid full toString call
                '}';
    }
}
