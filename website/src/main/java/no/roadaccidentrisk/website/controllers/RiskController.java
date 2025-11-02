package no.roadaccidentrisk.website.controllers;

import jakarta.validation.Valid;
import no.roadaccidentrisk.website.dto.PredictRequest;
import no.roadaccidentrisk.website.services.MlClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class RiskController {

    private final MlClient ml;

    public RiskController(MlClient ml) {
        this.ml = ml;
    }

    @GetMapping("/")
    public String form(Model model) {
        model.addAttribute("req", new PredictRequest());
        return "form";
    }

    @PostMapping("/predict")
    @ResponseBody
    public Map<String, Double> predict(@RequestBody PredictRequest req) {
        double risk = ml.predict(req);
        double percent = Math.round(risk * 1000.0) / 10.0;
        return Map.of("risk", risk, "percent", percent);
    }
}