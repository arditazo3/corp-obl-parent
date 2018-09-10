package com.tx.co.back_office.company.resource;

import com.tx.co.back_office.company.api.model.CompanyConsultantResult;
import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.company.service.ICompanyConsultantService;
import com.tx.co.back_office.company.service.ICompanyService;
import com.tx.co.common.api.provider.ObjectResult;
import com.tx.co.security.exception.GeneralException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.tx.co.common.constants.AppConstants.*;


@Component
@Path(BACK_OFFICE)
public class CompanyResource extends ObjectResult {

    private static final Logger logger = LogManager.getLogger(CompanyResource.class);

    @Context
    private UriInfo uriInfo;

    private ICompanyService companyService;
    private ICompanyConsultantService companyConsultantService;

    @Autowired
    public CompanyResource(ICompanyService companyService) {
        this.companyService = companyService;
    }
    
    @Autowired
    public void setCompanyConsultantService(ICompanyConsultantService companyConsultantService) {
		this.companyConsultantService = companyConsultantService;
	}

	@GET
    @Path(COMPANY_LIST)
    @Produces(MediaType.APPLICATION_JSON)
//    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getCompanies() {

        Iterable<Company> companyIterable = companyService.findAllCompany();
        List<CompanyResult> queryCompanyList =
                StreamSupport.stream(companyIterable.spliterator(), false)
                        .map(this::toCompanyResult)
                        .collect(Collectors.toList());

        return Response.ok(queryCompanyList).build();
    }

    @GET
    @Path(COMPANY_GET_BY_ID)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getCompany(@PathParam("idCompany") Long idCompany) {

        try {
            Optional<Company> company = companyService.findByIdCompany(idCompany);

            if (!company.isPresent()) {
                throw new NotFoundException();
            }

            CompanyResult result = toCompanyResult(company.get());
            return Response.ok(result).build();
        } catch (Exception e) {
            throw new GeneralException("Company not found");
        }
    }

    @POST
    @Path(COMPANY_CREATE_UPDATE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUpdateCompany(CompanyResult companyResult) {

        Company companyStored = companyService.saveUpdateCompany(toCompany(companyResult));

        return Response.ok(toCompanyResult(companyStored)).build();
    }

    @PUT
    @Path(COMPANY_DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
 //   @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response deleteCompany(CompanyResult company) {

        companyService.deleteCompany(company.getIdCompany());

        return Response.noContent().build();
    }
    
    @POST
    @Path(ASSOC_USER_COMPANY)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
 //   @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response associateUserToCompany(CompanyResult companyUserListResult) {

        companyService.associateUserToCompany(toCompany(companyUserListResult));

        return Response.noContent().build();
    }
    
    @GET
    @Path(CONSULTANT_LIST)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
//    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getCompanyConsultant(@QueryParam("idCompany") String idCompany) {

        Iterable<CompanyConsultant> companyConsultantIterable = companyConsultantService.getCompanyConsultantByIdCompany(idCompany);
        List<CompanyConsultantResult> queryCompanyConsultantList =
                StreamSupport.stream(companyConsultantIterable.spliterator(), false)
                        .map(this::toCompanyConsultantResult)
                        .collect(Collectors.toList());

        return Response.ok(queryCompanyConsultantList).build();
    }
    
    @POST
    @Path(CONSULTANT_CREATE_UPDATE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUpdateCompany(CompanyConsultantResult companyConsultantResult) {

    	CompanyConsultant companyConsultantStored = companyConsultantService.saveUpdateCompanyConsultant(toCompanyConsultant(companyConsultantResult));

        return Response.ok(toCompanyConsultantResult(companyConsultantStored)).build();
    }
    
    @PUT
    @Path(CONSULTANT_DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
 //   @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response deleteCompany(CompanyConsultantResult companyConsultant) {

    	companyConsultantService.deleteCompanyConsultant(companyConsultant.getIdCompanyConsultant());

        return Response.noContent().build();
    }
}
