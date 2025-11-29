// Endpoint temporal para listar clientes (solo para desarrollo/testing)

@GetMapping("/admin/clientes")
@PreAuthorize("hasAuthority('ADMINISTRADOR')")
public ResponseEntity<ApiResponseDTO<List<Map<String, Object>>>> listarClientes() {
    log.info("Admin listando clientes disponibles");

    List<Cliente> clientes = clienteRepository.findAll();

    List<Map<String, Object>> clientesInfo = clientes.stream()
        .map(c -> {
            Map<String, Object> info = new HashMap<>();
            info.put("id", c.getId());
            info.put("nombre", c.getNombreCompleto());
            info.put("email", c.getUsuario().getCorreoElectronico());
            info.put("telefono", c.getTelefono());
            return info;
        })
        .collect(Collectors.toList());

    return ResponseEntity.ok(
        ApiResponseDTO.<List<Map<String, Object>>>builder()
            .success(true)
            .message("Clientes obtenidos")
            .data(clientesInfo)
            .timestamp(LocalDateTime.now())
            .build()
    );
}
