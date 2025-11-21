package com.sistema_reservas.service;

import com.sistema_reservas.controller.dto.*;
import com.sistema_reservas.dao.UsuarioDAO;
import com.sistema_reservas.model.RefreshToken;
import com.sistema_reservas.model.Usuario;
import com.sistema_reservas.repository.UserRepository;
import com.sistema_reservas.security.JwtUtil;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceimpl implements UsuarioService {

    private final UserRepository userRepository;
    private final UsuarioDAO usuarioDAO;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JavaMailSender mailSender;

    // Lista de dominios permitidos para AFILIADO
    private final List<String> afiliadoDomains = List.of("@empresa.com", "@universidad.edu");

    @Override
    public UserResponseRegisterDTO registerUser(UserRegisterDTO dto) {
        if (dto.getRol() != null && dto.getRol().equalsIgnoreCase("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No está permitido crear administradores desde el registro público.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }

        String email = dto.getEmail().toLowerCase();
        String rol = "USER"; // Rol por defecto

        // Dominios permitidos para AFILIADO
        List<String> afiliadoDomains = List.of("@empresa.com", "@universidad.edu.co", "@test.com");


        if (afiliadoDomains.stream().anyMatch(email::endsWith)) {
            rol = "AFILIADO";
        }
        Usuario user = new Usuario();
        user.setNombre(dto.getNombre());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRol(rol);

        userRepository.save(user);
        UserResponseRegisterDTO response = new UserResponseRegisterDTO();
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setEmail(user.getEmail());
        response.setRol(user.getRol());

        return response;
    }

    public UserResponseRegisterDTO upgradeToAfiliado(String email) {
        Usuario user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!user.getRol().equals("USER")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo usuarios normales pueden convertirse en AFILIADO");
        }

        boolean validDomain = afiliadoDomains.stream().anyMatch(email::endsWith);
        if (!validDomain) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no pertenece a un dominio válido de afiliado");
        }

        user.setRol("AFILIADO");
        userRepository.save(user);

        UserResponseRegisterDTO response = new UserResponseRegisterDTO();
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setEmail(user.getEmail());
        response.setRol(user.getRol());

        return response;
    }

    @Override
    public LoginResponseDTO login(UserLoginDTO dto) {
        try {
            //comprueba si el correo existe
            Usuario user = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new RuntimeException("EMAIL_NOT_FOUND"));
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(user.getEmail());
            RefreshToken refresh = refreshTokenService.crearRefreshToken(user);

            LoginResponseDTO response = new LoginResponseDTO();
            response.setId(user.getId());
            response.setNombre(user.getNombre());
            response.setEmail(user.getEmail());
            response.setRol(user.getRol().toUpperCase());
            response.setToken(token);
            response.setRefreshToken(refresh.getToken());
            response.setMensaje("Inicio de sesión exitoso");

            return response;

        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("INVALID_PASSWORD");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("LOGIN_ERROR");
        }
    }



    @Override
    public void OlvideContraseña(OlvideContraseñaDTO request) {
        Usuario usuario = usuarioDAO.buscarPorCorreo(request.getEmail());
        if (usuario == null) {
            throw new RuntimeException("No se encontró ningún usuario con ese correo");
        }

        // Generar token de recuperación
        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuario.setResetTokenExpiration(LocalDateTime.now().plusHours(1));
        usuarioDAO.actualizar(usuario);
        sendResetEmail(usuario.getEmail(), token);
    }

    private void sendResetEmail(String email, String token) {
        String resetLink = "http://localhost:4200/auth/reset-password?token=" + token;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Restablecer contraseña");

            String html = "<p>Haz click en el siguiente enlace para restablecer tu contraseña:</p>"
                    + "<p><a href=\"" + resetLink + "\">Restablecer contraseña</a></p>"
                    + "<br>"
                    + "<p>Si el enlace no funciona copia y pega este link en el navegador:</p>"
                    + "<p>" + resetLink + "</p>";

            helper.setText(html, true); // IMPORTANTE → es HTML

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando correo: " + e.getMessage());
        }
    }

    @Override
    public void RestaurarContraseña(RestaurarContraseñaDTO request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        Usuario usuario = usuarioDAO.buscarPorResetToken(request.getToken());
        if (usuario == null) {
            throw new RuntimeException("Token inválido");
        }

        if (usuario.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usuario.setResetToken(null);
        usuario.setResetTokenExpiration(null);
        usuarioDAO.actualizar(usuario);
    }

    // Métodos de usuario admin
    private UsuarioResponseDTO mapToResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                "listado",
                usuario.getTelefono()
        );
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioDAO.listarTodos().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        return usuario != null ? mapToResponseDTO(usuario) : null;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO actualizarUsuario(Long id, usuarioDTO dto) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) return null;
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        return mapToResponseDTO(usuarioDAO.actualizar(usuario));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public boolean eliminarUsuario(Long id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario != null) {
            usuarioDAO.eliminar(id);
            return true;
        }
        return false;
    }

    @Override
    public UsuarioResponseDTO obtenerPorEmail(String email) {
        Usuario usuario = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                "perfil",
                usuario.getTelefono()
        );
    }

    @Override
    public UsuarioResponseDTO actualizarMiUsuario(String email, usuarioDTO dto) {
        Usuario usuario = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());

        userRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                "perfil actualizado",
                usuario.getTelefono()
        );
    }

    @Override
    public void cambiarMiPassword(CambioPasswordDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña actual incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(dto.getNuevaPassword()));
        userRepository.save(usuario);
    }

    @Override
    public void cerrarSesionesActuales() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        try {
            refreshTokenService.eliminarTokensDeUsuario(usuario.getId());
        } catch (Exception e) {
            System.out.println("No existe el método eliminarTokensDeUsuario, ignorando...");
        }
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public long contarUsuarios() {
        return userRepository.count();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public long contarAfiliados() {
        return usuarioDAO.listarTodos()
                .stream()
                .filter(u -> "AFILIADO".equalsIgnoreCase(u.getRol()))
                .count();
    }


}
