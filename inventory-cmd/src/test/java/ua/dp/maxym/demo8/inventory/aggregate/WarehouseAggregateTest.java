package ua.dp.maxym.demo8.inventory.aggregate;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.dp.maxym.demo8.inventory.command.AddSKUCommand;
import ua.dp.maxym.demo8.inventory.command.CreateWarehouseCommand;
import ua.dp.maxym.demo8.inventory.event.SKUCreatedEvent;
import ua.dp.maxym.demo8.inventory.event.SKUQuantityChangedEvent;
import ua.dp.maxym.demo8.inventory.event.WarehouseCreatedEvent;

import java.util.Random;
import java.util.UUID;

public class WarehouseAggregateTest {

    private static final String WAREHOUSE_ID = UUID.randomUUID().toString();
    private static final String SKU_NAME_1 = UUID.randomUUID().toString();
    private static final String SKU_NAME_2 = UUID.randomUUID().toString();
    private static final Integer SKU_QUANTITY_1 = new Random().nextInt(1000);
    private static final Integer SKU_QUANTITY_2 = new Random().nextInt(1000);
    private static final Double SKU_PRICE_1 = new Random().nextDouble();
    private static final Double SKU_PRICE_2 = new Random().nextDouble();
    private FixtureConfiguration<WarehouseAggregate> fixture;

    @BeforeEach
    void setup() {
        fixture = new AggregateTestFixture<>(WarehouseAggregate.class);
    }

    @Test
    void shouldCreateWarehouse() {
        fixture.givenNoPriorActivity()
               .when(new CreateWarehouseCommand(WAREHOUSE_ID))
               .expectEvents(new WarehouseCreatedEvent(WAREHOUSE_ID));
    }

    @Test
    void testHandleAddSKUCommand() {
        fixture.givenCommands(new CreateWarehouseCommand(WAREHOUSE_ID))
               .when(new AddSKUCommand(WAREHOUSE_ID, SKU_NAME_1, SKU_QUANTITY_1, SKU_PRICE_1))
               .expectEvents(new SKUCreatedEvent(WAREHOUSE_ID, SKU_NAME_1, SKU_QUANTITY_1, SKU_PRICE_1));

        fixture.givenCommands(new CreateWarehouseCommand(WAREHOUSE_ID))
               .andGiven(new SKUCreatedEvent(WAREHOUSE_ID, SKU_NAME_1, SKU_QUANTITY_1, SKU_PRICE_1))
               .when(new AddSKUCommand(WAREHOUSE_ID, SKU_NAME_1, SKU_QUANTITY_2, SKU_PRICE_1))
               .expectEvents(new SKUQuantityChangedEvent(WAREHOUSE_ID, SKU_NAME_1, SKU_QUANTITY_1 + SKU_QUANTITY_2))
               .expectState(w -> {
                   assert w.getSkuMap().size() == 1;
                   assert w.getSkuMap().containsKey(SKU_NAME_1);
                   assert w.getSkuMap().get(SKU_NAME_1)
                           .equals(new SKU(SKU_NAME_1, SKU_QUANTITY_1 + SKU_QUANTITY_2, SKU_PRICE_1));
               });

        fixture.givenCommands(new CreateWarehouseCommand(WAREHOUSE_ID)
                       , new AddSKUCommand(WAREHOUSE_ID, SKU_NAME_1, SKU_QUANTITY_1, SKU_PRICE_1))
               .when(new AddSKUCommand(WAREHOUSE_ID, SKU_NAME_2, SKU_QUANTITY_2, SKU_PRICE_2))
               .expectEvents(new SKUCreatedEvent(WAREHOUSE_ID, SKU_NAME_2, SKU_QUANTITY_2, SKU_PRICE_2))
               .expectState(w -> {
                   assert w.getWarehouseId().equals(WAREHOUSE_ID);
                   assert w.getSkuMap().size() == 2;
                   assert w.getSkuMap().containsKey(SKU_NAME_1);
                   assert w.getSkuMap().containsKey(SKU_NAME_2);
                   assert w.getSkuMap().get(SKU_NAME_1).equals(new SKU(SKU_NAME_1, SKU_QUANTITY_1, SKU_PRICE_1));
                   assert w.getSkuMap().get(SKU_NAME_2).equals(new SKU(SKU_NAME_2, SKU_QUANTITY_2, SKU_PRICE_2));
               });
    }

}
