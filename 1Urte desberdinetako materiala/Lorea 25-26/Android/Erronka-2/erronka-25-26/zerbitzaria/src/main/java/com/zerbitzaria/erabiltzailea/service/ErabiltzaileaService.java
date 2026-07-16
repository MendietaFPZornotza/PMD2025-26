package com.zerbitzaria.erabiltzailea.service;

import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import com.zerbitzaria.erabiltzailea.repository.ErabiltzaileaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ErabiltzaileaService {

    private final ErabiltzaileaRepository erabiltzaileaRepository;
    private final PasswordEncoder passwordEncoder;

    public ErabiltzaileaService(ErabiltzaileaRepository erabiltzaileaRepository,
                                PasswordEncoder passwordEncoder) {
        this.erabiltzaileaRepository = erabiltzaileaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Métodos de login/seguridad (ya los tenías)
    public boolean login(String email, String rawPassword) {
        return erabiltzaileaRepository.findByEmaila(email)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPasahitzaHash()) && user.isAktibo())
                .orElse(false);
    }

    // CRUD para el controller REST
    public List<Erabiltzailea> findAll() {
        return erabiltzaileaRepository.findAll();
    }

    public Optional<Erabiltzailea> findById(Long id) {
        return erabiltzaileaRepository.findById(id);
    }

    public Erabiltzailea create(Erabiltzailea erabiltzailea) {
        // Si viene con contraseña en plano, la hasheamos
        if (erabiltzailea.getPasahitzaHash() != null &&
                !erabiltzailea.getPasahitzaHash().startsWith("$2a$")) {
            erabiltzailea.setPasahitzaHash(
                    passwordEncoder.encode(erabiltzailea.getPasahitzaHash()));
        }
        return erabiltzaileaRepository.save(erabiltzailea);
    }

    public Erabiltzailea update(Long id, Erabiltzailea updated) {
        return erabiltzaileaRepository.findById(id)
                .map(user -> {
                    user.setEmaila(updated.getEmaila() != null ?
                            updated.getEmaila() : user.getEmaila());
                    // Si cambian la contraseña, hashearla
                    if (updated.getPasahitzaHash() != null) {
                        user.setPasahitzaHash(passwordEncoder.encode(updated.getPasahitzaHash()));
                    }
                    return erabiltzaileaRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void delete(Long id) {
        erabiltzaileaRepository.deleteById(id);
    }


    @Transactional
    public void deactivateUser(int userId) {
        Erabiltzailea u = erabiltzaileaRepository.findById((long) userId)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        // Soft delete
        u.setAktibo(false);

        // Opcional recomendado: “matar” credenciales para que ni a tiros vuelva a entrar
        // (aunque con aktibo=false ya no entra)
        u.setPasahitzaHash("DELETED");

        // Opcional si quieres permitir que se vuelva a registrar con el mismo email:
        // u.setEmaila("deleted_" + u.getId() + "@deleted.local");

        erabiltzaileaRepository.save(u);
    }
}
