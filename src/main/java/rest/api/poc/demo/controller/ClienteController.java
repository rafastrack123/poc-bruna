package rest.api.poc.demo.controller;

import static java.lang.String.valueOf;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rest.api.poc.demo.domain.Cliente;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private List<Cliente> clientes = new ArrayList<>();

    @GetMapping
    public List<Cliente> getAll() {
        return clientes;
    }

    @GetMapping("/{id}")
    public Cliente getById(@PathVariable String id) {
        return findClienteById(id);
    }

    @PostMapping
    public Cliente create(@RequestBody Cliente cliente) {

        if (isEmpty(cliente.getNome())) {
            throw new ResponseStatusException(BAD_REQUEST, "Nome nao pode nulo ou vazio");
        }

        cliente.setId(getNextId());

        clientes.add(cliente);

        return cliente;
    }

    private boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    @PutMapping("/{id}")
    public Cliente update(@PathVariable String id, @RequestBody Cliente clienteUpdate) {
        Cliente cliente = findClienteById(id);

        cliente.setNome(clienteUpdate.getNome());
        cliente.setEmail(clienteUpdate.getEmail());

        return cliente;
    }

    @PatchMapping("/{id}")
    public Cliente patch(@PathVariable String id, @RequestBody Cliente clienteUpdate) {
        Cliente cliente = findClienteById(id);

        cliente.setEmail(clienteUpdate.getEmail());

        return cliente;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        Cliente cliente = findClienteById(id);

        clientes.remove(cliente);
    }

    private Cliente findClienteById(String id) {
        return clientes.stream()
                .filter(cliente -> cliente.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cliente nao encontrado " + id));
    }

    private String getNextId() {
        if (clientes.size() == 0) {
            return "1";
        } else {
            int greatestId = Integer.valueOf(clientes.get(clientes.size() - 1).getId());
            int nextId = greatestId + 1;
            return String.valueOf(nextId);
        }
    }
}
