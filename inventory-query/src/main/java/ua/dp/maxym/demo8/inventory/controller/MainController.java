package ua.dp.maxym.demo8.inventory.controller;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.dp.maxym.demo8.inventory.command.AddSKUCommand;
import ua.dp.maxym.demo8.inventory.command.CreateWarehouseCommand;
import ua.dp.maxym.demo8.inventory.query.Warehouse;
import ua.dp.maxym.demo8.inventory.query.WarehouseRepository;

import java.util.List;

@RestController
public class MainController {

    public static final String WAREHOUSE_NAME = "warehouse1";
    private final WarehouseRepository warehouseRepository;
    private final CommandGateway commandGateway;

    @Autowired
    public MainController(
            WarehouseRepository warehouseRepository,
            CommandGateway commandGateway) {
        this.warehouseRepository = warehouseRepository;
        this.commandGateway = commandGateway;
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/")
    public String index() {
        return """
                Call this to create warehouse and add SKUs: <a href="/create">Create the repository / add more stuff</a></a>
                <br/>
                <a href="/list">List the warehouse</a>
                <br/><br/>
                <a href="/swagger-ui.html">Swagger for API</a>
                """;
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/create")
    public String create() {
        commandGateway.send(new CreateWarehouseCommand(WAREHOUSE_NAME));
        commandGateway.send(new AddSKUCommand(WAREHOUSE_NAME, "Item1", 10, 100.0));
        commandGateway.send(new AddSKUCommand(WAREHOUSE_NAME, "Item2", 5, 50.0));
        return """
                Created, see <a href="/list">Contents of warehouse</a>
                """;
    }

    @GetMapping("/list")
    public List<Warehouse> list() {
        return warehouseRepository.findAll();
    }
}
