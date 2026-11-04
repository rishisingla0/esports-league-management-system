
package com.esports.api.service;

import com.esports.api.dto.CreateOrganizationRequest;
import com.esports.api.dto.OrganizationResponse;
import com.esports.api.entity.Organization;
import com.esports.api.repository.OrganizationRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(
            OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    // GET: Retrieve all organizations
    @Transactional(readOnly = true)
    public List<OrganizationResponse> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET: Retrieve organization by ID
    @Transactional(readOnly = true)
    public OrganizationResponse getOrganizationById(Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Organization not found with ID: " + id));

        return convertToResponse(organization);
    }

    // POST: Create organization
    @Transactional
    public OrganizationResponse createOrganization(
            CreateOrganizationRequest request) {

        Organization organization = new Organization();

        organization.setOrganizationName(
                request.organizationName().trim());
        organization.setCountry(request.country());
        organization.setFoundedDate(request.foundedDate());
        organization.setWebsite(request.website());

        Organization savedOrganization =
                organizationRepository.save(organization);

        return convertToResponse(savedOrganization);
    }

    // PUT: Update organization
    @Transactional
    public OrganizationResponse updateOrganization(
            Long id, CreateOrganizationRequest request) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Organization not found with ID: " + id));

        organization.setOrganizationName(
                request.organizationName().trim());
        organization.setCountry(request.country());
        organization.setFoundedDate(request.foundedDate());
        organization.setWebsite(request.website());

        Organization updatedOrganization =
                organizationRepository.save(organization);

        return convertToResponse(updatedOrganization);
    }

    // DELETE: Delete organization
    @Transactional
    public void deleteOrganization(Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Organization not found with ID: " + id));

        organizationRepository.delete(organization);
    }

    // Convert Entity into Response DTO
    private OrganizationResponse convertToResponse(
            Organization organization) {

        return new OrganizationResponse(
                organization.getOrganizationId(),
                organization.getOrganizationName(),
                organization.getCountry(),
                organization.getFoundedDate(),
                organization.getWebsite()
        );
    }
}
