package ua.dp.maxym.demo8.inventory.query;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public final class Warehouse {
    private Long id;
    private String name;
    private Set<SKU> skus = new HashSet<>();

    public Warehouse(String name) {
        this.name = name;
    }

    public Warehouse() {
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "warehouse")
    public Set<SKU> getSkus() {
        return skus;
    }

    @SuppressWarnings("unused")
    public void setSkus(Set<SKU> skus) {
        this.skus = skus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Warehouse warehouse = (Warehouse) o;

        if (!id.equals(warehouse.id)) return false;
        if (!name.equals(warehouse.name)) return false;
        return skus.equals(warehouse.skus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, skus);
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", skus=" + skus +
                '}';
    }
}
