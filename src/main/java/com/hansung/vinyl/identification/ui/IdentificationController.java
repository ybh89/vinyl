package com.hansung.vinyl.identification.ui;

import com.hansung.vinyl.identification.application.IdentificationService;
import com.hansung.vinyl.identification.dto.IdentificationRequest;
import com.hansung.vinyl.identification.dto.IdentificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class IdentificationController {
    private final IdentificationService identificationService;

    @ResponseBody
    @PostMapping("/v1/identifications")
    public ResponseEntity<IdentificationResponse> certify(@RequestBody IdentificationRequest identificationRequest) {
        IdentificationResponse identificationResponse = identificationService.certify(identificationRequest);
        return ResponseEntity.created(URI.create("/v1/identifications?email=" + identificationResponse.getEmail()))
                .body(identificationResponse);
    }

    @GetMapping("/v1/identifications/{token}")
    public String validate(@PathVariable String token, Model model) {
        IdentificationResponse identificationResponse = identificationService.validate(token);
        model.addAttribute("message", identificationResponse.getMessage());
        return "identification-result";
    }

    @ResponseBody
    @GetMapping("/v1/identifications")
    public ResponseEntity<IdentificationResponse> result(String email) {
        IdentificationResponse identificationResponse = identificationService.result(email);
        return ResponseEntity.ok(identificationResponse);
    }

    @ResponseBody
    @GetMapping("/emails/token")
    public ResponseEntity<UUID> token(String email) {
        UUID token = identificationService.token(email);
        return ResponseEntity.ok(token);
    }
}
