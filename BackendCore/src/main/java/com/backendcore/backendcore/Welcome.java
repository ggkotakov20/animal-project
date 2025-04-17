package com.backendcore.backendcore;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.sentry.Sentry;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class Welcome {
    @GetMapping("/online")
    public ResponseEntity<String> getAllAccountCategories() {
        return ResponseEntity.ok(null);
    }
}
