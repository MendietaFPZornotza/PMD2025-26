package com.zerbitzaria.tcp.handler.purchase;

import com.zerbitzaria.sarrera.entity.ErositakoSarrera;
import com.zerbitzaria.sarrera.repository.ErositakoSarreraRepository;
import com.zerbitzaria.tcp.handler.TcpHandler;
import com.zerbitzaria.tcp.protocol.TcpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetTicketsByCodeHandler implements TcpHandler {

    private final ErositakoSarreraRepository repo;

    public GetTicketsByCodeHandler(ErositakoSarreraRepository repo) {
        this.repo = repo;
    }

    @Override
    public String command() {
        return "GET_TICKETS_BY_CODE";
    }

    @Override
    public String handle(TcpRequest req) {
        if (req.args().length != 1) return "TICKETS_CODE_FAIL;BAD_ARGS";

        String code = req.args()[0].trim();
        if (code.isBlank()) return "TICKETS_CODE_FAIL;EMPTY";

        List<ErositakoSarrera> list = repo.findByDeskargaKodeaWithEvent(code);
        if (list.isEmpty()) return "TICKETS_CODE_EMPTY";

        StringBuilder sb = new StringBuilder();
        sb.append("TICKETS_CODE_OK;").append(list.size());

        for (ErositakoSarrera s : list) {
            var e = s.getEkitaldia();

            String eDate = (e.getHasiera() != null) ? e.getHasiera().toLocalDate().toString() : "-";
            String eTime = (e.getHasiera() != null) ? e.getHasiera().toLocalTime().toString().substring(0,5) : "-";
            String room = (e.getEszenatokia() != null) ? safe(e.getEszenatokia().getIzena()) : "-";

            String seatCode = rowLabel(s.getFila()) + s.getEserlekua(); // A1
            String path = s.getQrPath() != null ? s.getQrPath() : "";   // luego será PDF path en FTP

            sb.append(";")
                    .append(e.getId()).append("|")
                    .append(safe(e.getIzenburua())).append("|")
                    .append(eDate).append("|")
                    .append(eTime).append("|")
                    .append(room).append("|")
                    .append(seatCode).append("|")
                    .append(safe(path));
        }

        return sb.toString();
    }

    private String rowLabel(int r) {
        int n = r;
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            n--;
            sb.append((char)('A' + (n % 26)));
            n /= 26;
        }
        return sb.reverse().toString();
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replace(";", " ").replace("|", " ");
    }
}
