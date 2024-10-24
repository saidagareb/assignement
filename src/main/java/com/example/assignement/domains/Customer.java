package com.example.assignement.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "customer", schema = "assignement")
public class Customer  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BankAccount> accounts = new HashSet<>();

    public Customer(String name) {
        this.name = name;
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
        account.setCustomer(this);
    }

    public void removeAccount(BankAccount account) {
        accounts.remove(account);
        account.setCustomer(null);
    }

    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accountsCount=" + accounts.size() + // Just showing the count to avoid recursion
                '}';
    }
}