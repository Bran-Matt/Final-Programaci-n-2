package com.columbiaviajesweb.columbia_springboot.service;

import com.columbiaviajesweb.columbia_springboot.dto.RankingVendedor;
import com.columbiaviajesweb.columbia_springboot.dto.UsuarioForm;
import com.columbiaviajesweb.columbia_springboot.entity.*;
import com.columbiaviajesweb.columbia_springboot.exception.ReglaNegocioException;
import com.columbiaviajesweb.columbia_springboot.repository.ClienteRepository;
import com.columbiaviajesweb.columbia_springboot.repository.UsuarioRepository;
import com.columbiaviajesweb.columbia_springboot.repository.VendedorRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Alta, baja, modificación y autenticación de usuarios de todos los roles.
 */
@Service
@Transactional
public class UsuarioService extends ServicioBase<Usuario> {

    private final UsuarioRepository repo;
    private final ClienteRepository clienteRepo;
    private final VendedorRepository vendedorRepo;
    private final SucursalService sucursalService;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository repo, ClienteRepository clienteRepo,
                          VendedorRepository vendedorRepo, SucursalService sucursalService) {
        this.repo = repo;
        this.clienteRepo = clienteRepo;
        this.vendedorRepo = vendedorRepo;
        this.sucursalService = sucursalService;
    }

    @Override protected JpaRepository<Usuario, Long> repositorio() { return repo; }
    @Override protected String nombreEntidad() { return "el usuario"; }

    @Override
    public List<Usuario> listar() {
        List<Usuario> todos = new ArrayList<>(repo.findAll());
        todos.sort(Comparator.comparing((Usuario u) -> u.getRol().ordinal()).reversed()
                .thenComparing(Usuario::getApellidos));
        return todos;
    }

    /** Devuelve el usuario si el código y la clave son correctos. */
    @Transactional(readOnly = true)
    public Optional<Usuario> autenticar(String codigo, String clave) {
        return repo.findByCodigoIgnoreCase(codigo.trim())
                .filter(u -> encoder.matches(clave, u.getClave()));
    }

    public List<Cliente> listarClientes() { return clienteRepo.findAllByOrderByApellidosAscNombreAsc(); }
    public List<Vendedor> listarVendedores() { return vendedorRepo.findAllByOrderByApellidosAscNombreAsc(); }

    public Cliente buscarCliente(Long id) {
        return clienteRepo.findById(id).orElseThrow(() -> new ReglaNegocioException("No se encontró el cliente"));
    }

    public Vendedor buscarVendedor(Long id) {
        return vendedorRepo.findById(id).orElseThrow(() -> new ReglaNegocioException("No se encontró el vendedor"));
    }

    /**
     * Crea o actualiza un usuario a partir del formulario.
     * Usa la fábrica del rol para instanciar la subclase correcta.
     */
    public Usuario guardar(UsuarioForm form) {
        if (repo.existsByCodigoIgnoreCaseAndIdNot(form.getCodigo().trim(), idONinguno(form.getId()))) {
            throw new ReglaNegocioException("Ya existe un usuario con el código " + form.getCodigo());
        }
        Usuario u;
        if (form.getId() == null) {
            if (form.getClave() == null || form.getClave().isBlank()) {
                throw new ReglaNegocioException("La clave es obligatoria para un usuario nuevo");
            }
            u = form.getRol().crearUsuario();
        } else {
            u = buscar(form.getId());
            if (u.getRol() != form.getRol()) {
                throw new ReglaNegocioException("No se puede cambiar el rol de un usuario existente");
            }
        }
        u.setCodigo(form.getCodigo().trim());
        u.setNombre(form.getNombre().trim());
        u.setApellidos(form.getApellidos().trim());
        u.setDireccion(form.getDireccion());
        u.setEmail(form.getEmail());
        u.setTelefono(form.getTelefono());
        if (form.getClave() != null && !form.getClave().isBlank()) {
            u.setClave(encoder.encode(form.getClave()));
        }
        Sucursal sucursal = form.getSucursalId() == null ? null : sucursalService.buscar(form.getSucursalId());
        if (u instanceof Cliente c) c.setSucursal(sucursal);
        if (u instanceof Vendedor v) v.setSucursal(sucursal);
        return repo.save(u);
    }

    /** Crea un usuario directamente (lo usa la carga inicial de datos). */
    public <U extends Usuario> U crear(U u, String codigo, String clave, String nombre, String apellidos,
                                       String direccion, String email, String telefono) {
        u.setCodigo(codigo);
        u.setClave(encoder.encode(clave));
        u.setNombre(nombre);
        u.setApellidos(apellidos);
        u.setDireccion(direccion);
        u.setEmail(email);
        u.setTelefono(telefono);
        return repo.save(u);
    }

    /** Elimina un usuario, impidiendo que alguien se borre a sí mismo. */
    public void eliminar(Long id, Usuario actual) {
        if (id.equals(actual.getId())) {
            throw new ReglaNegocioException("No podés eliminar tu propio usuario");
        }
        eliminar(id);
    }

    /** Listado de vendedores ordenado de mayor a menor facturación. */
    @Transactional(readOnly = true)
    public List<RankingVendedor> rankingVendedores() {
        List<Vendedor> vendedores = new ArrayList<>(vendedorRepo.findAll());
        vendedores.sort(Comparator.comparingDouble(Vendedor::getTotalFacturado).reversed());
        double maximo = vendedores.isEmpty() ? 0 : vendedores.get(0).getTotalFacturado();
        List<RankingVendedor> ranking = new ArrayList<>();
        for (int i = 0; i < vendedores.size(); i++) {
            Vendedor v = vendedores.get(i);
            double pct = maximo == 0 ? 0 : v.getTotalFacturado() * 100 / maximo;
            ranking.add(new RankingVendedor(i + 1, v, v.getTotalFacturado(), v.getCantidadReservas(), pct));
        }
        return ranking;
    }
}
