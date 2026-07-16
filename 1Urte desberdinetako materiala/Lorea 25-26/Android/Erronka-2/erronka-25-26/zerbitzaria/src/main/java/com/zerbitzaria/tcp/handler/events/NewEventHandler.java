package com.zerbitzaria.tcp.handler.events;

import com.zerbitzaria.common.enums.EkitaldiMota;
import com.zerbitzaria.ekitaldia.entity.Ekitaldia;
import com.zerbitzaria.ekitaldia.service.EkitaldiaService;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Handler TCP para crear un nuevo evento (NEW_EVENT)
 */
@Component
public class NewEventHandler implements TcpHandler {

    private final EkitaldiaService ekitaldiaService;

    // Spring inyecta el servicio automáticamente
    public NewEventHandler(EkitaldiaService ekitaldiaService) {
        this.ekitaldiaService = ekitaldiaService;
    }

    @Override
    public String command() {
        return "NEW_EVENT";
    }

    @Override
    public String handle(TcpRequest req) {
        String[] args = req.args(); // args viene como String[]

        // Validación de cantidad de argumentos
        if (args.length != 9) {
            return "ERROR;BAD_ARGS"; // cliente envió campos insuficientes
        }

        // Mapear los argumentos a variables
        String title = args[0];
        String typeStr = args[1];
        String genre = args[2];
        String startStr = args[3];
        String endStr = args[4];
        String synopsis = args[5];
        String scenarioIdStr = args[6];
        String price = args[7];
        String imagePath = args[8];

        // Validación mínima
        if (title.isBlank()) return "ERROR;TITLE_EMPTY";

        LocalDateTime start;
        LocalDateTime end;

        try {
            start = LocalDateTime.parse(startStr);
            end = LocalDateTime.parse(endStr);
        } catch (DateTimeParseException e) {
            return "ERROR;BAD_DATE_FORMAT"; // formato incorrecto
        }

        if (end.isBefore(start)) {
            return "ERROR;DATE_ORDER"; // la fecha de fin es anterior a la de inicio
        }

        try {
            // Crear entidad Ekitaldia
            Ekitaldia ek = new Ekitaldia();
            ek.setIzenburua(title);
            try {
                ek.setMota(EkitaldiMota.valueOf(typeStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                return "ERROR;BAD_TYPE"; // si el valor no coincide con el enum
            }
            ek.setGeneroa(genre);
            ek.setHasiera(start);
            ek.setAmaiera(end);
            ek.setSinopsia(synopsis);
            ek.setAktibo(true);

            int scenarioId;
            try {
                scenarioId = Integer.parseInt(scenarioIdStr);
            } catch (NumberFormatException e) {
                return "ERROR;BAD_ESZENATOKIA";
            }
            BigDecimal prezioa;
            try {
                prezioa = new BigDecimal(price);
                if (prezioa.compareTo(BigDecimal.ZERO) < 0) {
                    return "ERROR;BAD_PRICE";
                }
            } catch (NumberFormatException e) {
                return "ERROR;BAD_PRICE";
            }
            ek.setPrezioa(prezioa);
            ek.setArgazkia(imagePath);
            // Guardar en base de datos
            Ekitaldia created = ekitaldiaService.create(ek, scenarioId);

            // Respuesta exitosa
            return "OK;" + created.getId();

        } catch (RuntimeException e) {
            // Captura validaciones de EkitaldiaService
            return "ERROR;" + e.getMessage();
        } catch (Exception e) {
            // Captura cualquier otro error inesperado
            return "ERROR;INTERNAL";
        }
    }
}
