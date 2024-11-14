package org.moultdb.api.controller;

import org.moultdb.api.model.ReleaseInfo;
import org.moultdb.api.model.ReleaseVersion;
import org.moultdb.api.service.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.moultdb.api.controller.ResponseHandler.generateErrorResponse;
import static org.moultdb.api.controller.ResponseHandler.generateValidResponse;

/**
 * @author Valentine Rech de Laval
 * @since 2024-11-14
 */
@RestController
@RequestMapping(path="/release")
public class ReleaseController {
    
    @Autowired
    ReleaseService releaseService;

    @GetMapping("/info")
    public ResponseEntity<Map<String, ReleaseInfo>> getReleaseInfo() {
        return generateValidResponse(releaseService.getReleaseInfo());
    }
    
    @GetMapping("/new-release-version")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addNewReleaseVersion() {
        ReleaseVersion newVersion;
        try {
            newVersion = releaseService.createNewVersion();
        } catch (Exception e) {
            return generateErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return generateValidResponse("New release version created: " + newVersion.name());
    }
}
