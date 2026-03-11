package com.educonnect.controller;

import com.educonnect.dto.parent.ParentAccessRequestDTO;
import com.educonnect.dto.parent.ParentAccessResponseDTO;
import com.educonnect.service.contract.ParentAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that manages parent access permissions for student information.
 * This controller exposes endpoints to:
 *  1.Grant a parent access to view a student's information
 *  2.Retrieve existing access permissions between a parent and a student
 *
 *
 *  All endpoints are exposed under the base path: /v1/api/parent/access
 *
 *   @author abramya975
 *   @version 1.0
 *   @since 1.0
 *
 */
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/parent/access")
@RequiredArgsConstructor
public class ParentAccessController {
    private final ParentAccessService parentAccessService;

    /**
     * Grants a parent access to a student’s information.
     *
     * This method receives a {@link ParentAccessRequestDTO} that contains
     * the parent and student identifiers along with required access details.
     * It delegates the access creation logic to {@link ParentAccessService#grantAccess(ParentAccessRequestDTO)}
     * and returns the result.
     *
     * @param request the request body containing parent ID, student ID,
     *                and additional access-related information
     * @return a {@link ParentAccessResponseDTO} representing the granted access details
     */

    @PostMapping
    public ParentAccessResponseDTO grantAccess(@RequestBody ParentAccessRequestDTO request){
        return parentAccessService.grantAccess(request);
    }

    /**
     * Retrieves an existing parent–student access permission.
     *
     * Clients must supply both a parentId and studentId as query parameters.
     * The method delegates to {@link ParentAccessService#getAccess(UUID, UUID)}
     * and returns the stored permission details.
     *
     * @param parentId  the unique identifier of the parent
     * @param studentId the unique identifier of the student
     * @return a {@link ParentAccessResponseDTO} containing access permission details
     */

    @GetMapping
    public ParentAccessResponseDTO getAccess(@RequestParam UUID parentId,@RequestParam UUID studentId){
        return parentAccessService.getAccess(parentId, studentId);
    }
}
