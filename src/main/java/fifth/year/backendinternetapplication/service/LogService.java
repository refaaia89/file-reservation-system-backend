package fifth.year.backendinternetapplication.service;

import fifth.year.backendinternetapplication.dto.response.LogResponse;
import fifth.year.backendinternetapplication.dto.response.fullData.FullLogResponse;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.dto.response.main.SuccessResponse;
import fifth.year.backendinternetapplication.repository.LogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LogService {
    private final LogRepository repository;

    public LogService(LogRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<Response> getAll(int page, int pageSize) {
        return ResponseEntity.ok(new SuccessResponse("Logs Retrieved successfully",
                repository.findAll(PageRequest.of(page, pageSize))
                        .stream()
                        .map(log -> new LogResponse(log.getId(), log.getType().getType(), log.getData(),
                                log.getUserId(), log.getCreated_at(), log.getUpdated_at()))
                        .toList()));
    }

    public ResponseEntity<Response> getById(Long id) {
        return ResponseEntity.ok(new SuccessResponse("Log Data Retrieved successfully",
                repository.findById(id)
                        .map(FullLogResponse::new)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Log Not Found"))
        ));
    }
}
