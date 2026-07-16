package com.zerbitzaria.tcp.handler.purchase;

import com.zerbitzaria.common.enums.SarreraEgoera;
import com.zerbitzaria.sarrera.entity.ErositakoSarrera;
import com.zerbitzaria.sarrera.repository.ErositakoSarreraRepository;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TakenSeatsHandler implements TcpHandler {

    private final ErositakoSarreraRepository repo;

    public TakenSeatsHandler(ErositakoSarreraRepository repo) {
        this.repo = repo;
    }

    @Override
    public String command() {
        return "GET_TAKEN_SEATS";
    }

    @Override
    public String handle(TcpRequest req) {
        // GET_TAKEN_SEATS necesita 1 arg: ekitaldiaId
        if (req.args().length != 1) return "TAKEN_FAIL;BAD_ARGS";

        Integer ekitaldiaId;
        try {
            ekitaldiaId = Integer.parseInt(req.args()[0].trim());
        } catch (Exception e) {
            return "TAKEN_FAIL;BAD_EVENT_ID";
        }

        List<ErositakoSarrera> list =
                repo.findByEkitaldia_IdAndEgoeraNot(ekitaldiaId, SarreraEgoera.EZEZTATUA);

        if (list.isEmpty()) return "TAKEN_OK"; // sin ocupadas

        // Respuesta: TAKEN_OK;A1,A2,B4...
        StringBuilder sb = new StringBuilder("TAKEN_OK;");
        for (int i = 0; i < list.size(); i++) {
            ErositakoSarrera s = list.get(i);
            String code = rowLabel(s.getFila()) + s.getEserlekua(); // A1
            if (i > 0) sb.append(",");
            sb.append(code);
        }
        return sb.toString();
    }


    private String rowLabel(int r) {
        // 1->A, 2->B ... 26->Z, 27->AA...
        int n = r;
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            n--;
            sb.append((char) ('A' + (n % 26)));
            n /= 26;
        }
        return sb.reverse().toString();
    }
}
