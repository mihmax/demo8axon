package ua.dp.maxym.demo8.inventory.query;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public final class Warehouse {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(mappedBy = "warehouse")
    private Set<SKU> skus = new HashSet<>();

    public Warehouse() {
    }

    public Warehouse(String name) {
        this.name = name;
    }

}
