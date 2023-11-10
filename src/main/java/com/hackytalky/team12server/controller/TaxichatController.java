package com.hackytalky.team12server.controller;

import com.hackytalky.team12server.dto.IdDto;
import com.hackytalky.team12server.service.TaxichatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/taxichat")
@RequiredArgsConstructor
public class TaxichatController {
    private final TaxichatService taxichatService;

    @PostMapping("/{taxichatId}/completed")
    public ResponseEntity complete(@PathVariable Long taxichatId, @RequestBody IdDto userId) {
        taxichatService.completeTaxichat(userId.getUserId(), taxichatId);

        return new ResponseEntity(HttpStatus.OK);
    }
}
