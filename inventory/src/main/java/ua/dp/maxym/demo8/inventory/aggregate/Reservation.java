package ua.dp.maxym.demo8.inventory.aggregate;

import java.util.List;

public record Reservation(String id, List<SKU> skuList) {
}
