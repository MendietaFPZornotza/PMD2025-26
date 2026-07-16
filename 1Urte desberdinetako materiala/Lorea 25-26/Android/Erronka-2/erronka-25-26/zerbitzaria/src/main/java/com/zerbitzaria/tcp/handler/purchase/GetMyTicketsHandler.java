package com.zerbitzaria.tcp.handler.purchase;

import com.zerbitzaria.sarrera.entity.ErositakoSarrera;
import com.zerbitzaria.sarrera.repository.ErositakoSarreraRepository;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class GetMyTicketsHandler implements TcpHandler {

    private final ErositakoSarreraRepository repo;

    public GetMyTicketsHandler(ErositakoSarreraRepository repo) {
        this.repo = repo;
    }

    @Override
    public String command() {
        return "MY_TICKETS";
    } //GET_MY_TICKETS

    @Override
    @Transactional(readOnly = true)
    public String handle(TcpRequest req) {
        if (req.args().length != 1) return "MY_TICKETS_FAIL;BAD_ARGS";

        Integer userId;
        try {
            userId = Integer.parseInt(req.args()[0].trim());
        } catch (Exception e) {
            return "MY_TICKETS_FAIL;BAD_USER";
        }

        List<ErositakoSarrera> all = repo.findUserTicketsWithEvent(userId);
        if (all.isEmpty()) return "MY_TICKETS_EMPTY";

        // Agrupar por deskargaKodea
        Map<String, List<ErositakoSarrera>> grouped = new LinkedHashMap<>();
        for (ErositakoSarrera s : all) {
            String code = s.getDeskargaKodea();
            if (code == null) continue;
            grouped.computeIfAbsent(code, k -> new ArrayList<>()).add(s);
        }

        if (grouped.isEmpty()) return "MY_TICKETS_EMPTY";

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        StringBuilder sb = new StringBuilder();
        sb.append("MY_TICKETS_OK;").append(grouped.size());

        for (Map.Entry<String, List<ErositakoSarrera>> entry : grouped.entrySet()) {
            String deskargaKodea = entry.getKey();
            List<ErositakoSarrera> list = entry.getValue();

            // Tomamos el primero como “cabecera”
            ErositakoSarrera first = list.get(0);

            var e = first.getEkitaldia();
            String title = safe(e.getIzenburua()); // ajusta si tu campo se llama distinto
            String date = first.getErosketarenData() != null ? first.getErosketarenData().format(fmt) : "-";

            // Si en tu Ekitaldia tienes hasiera:
            String eDate = (e.getHasiera() != null) ? e.getHasiera().toLocalDate().toString() : "-";
            String eTime = (e.getHasiera() != null) ? e.getHasiera().toLocalTime().toString().substring(0,5) : "-";

            String room = (e.getEszenatokia() != null) ? safe(e.getEszenatokia().getIzena()) : "-";

            int seatCount = list.size();

            sb.append(";")
                    .append(safe(deskargaKodea)).append("|")
                    .append(e.getId()).append("|")
                    .append(title).append("|")
                    .append(eDate).append("|")
                    .append(eTime).append("|")
                    .append(room).append("|")
                    .append(seatCount).append("|")
                    .append(safe(date));
        }

        return sb.toString();
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replace(";", " ").replace("|", " ");
    }
}
