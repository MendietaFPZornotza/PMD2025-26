package com.zerbitzaria.tcp.handler.users;

import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import com.zerbitzaria.erabiltzailea.repository.ErabiltzaileaRepository;
import com.zerbitzaria.erabiltzailea.service.ErabiltzaileaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

/**
 * PROFILE_UPDATE;oldEmail;newNameOrBlank;newEmailOrBlank;newPasswordOrBlank
 * Todo opcional: si viene "" se mantiene el valor actual.
 *
 * Respuestas:
 *  UPDATE_OK;izena;email
 *  UPDATE_NOT_FOUND
 *  UPDATE_EXISTS   (si newEmail ya existe en otro usuario)
 *  UPDATE_FAIL
 */
@Component
public class UpdateUserClientHandler implements TcpHandler {

    private final ErabiltzaileaRepository repo;
    private final ErabiltzaileaService service;

    public UpdateUserClientHandler(ErabiltzaileaRepository repo, ErabiltzaileaService service) {
        this.repo = repo;
        this.service = service;
    }

    @Override public String command() { return "PROFILE_UPDATE"; }

    @Override
    public String handle(TcpRequest req) {
        if (req.args().length != 4) return "ERROR;BAD_ARGS";

        String oldEmail = safeIn(req.args()[0]).trim().toLowerCase();
        String newName  = safeIn(req.args()[1]).trim();
        String newEmail = safeIn(req.args()[2]).trim().toLowerCase();
        String newPass  = safeIn(req.args()[3]); // puede ser ""

        Erabiltzailea user = repo.findByEmaila(oldEmail).orElse(null);
        if (user == null) return "UPDATE_NOT_FOUND";

        // si quieren cambiar email, que no exista en otro usuario
        if (!newEmail.isBlank() && !newEmail.equalsIgnoreCase(oldEmail)) {
            if (repo.findByEmaila(newEmail).isPresent()) return "UPDATE_EXISTS";
            user.setEmaila(newEmail);
        }

        if (!newName.isBlank()) user.setIzena(newName);

        // pass opcional
        if (!newPass.isBlank()) user.setPasahitzaHash(newPass);

        // Reutilizamos service.create/update para hashear si hace falta
        Erabiltzailea saved = service.create(user);

        return "UPDATE_OK;" +safeOut(saved.getId().toString())+ ";"+ safeOut(saved.getIzena()) + ";" + safeOut(saved.getEmaila());
    }

    private String safeIn(String s) { return s == null ? "" : s; }
    private String safeOut(String s) { return s == null ? "" : s.replace(";", " "); }
}