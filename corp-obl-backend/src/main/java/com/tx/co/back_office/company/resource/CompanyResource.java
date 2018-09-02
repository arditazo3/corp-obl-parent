package com.tx.co.back_office.company.resource;

import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.service.ICompanyService;
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
import static org.springframework.util.ObjectUtils.isEmpty;


@Component
@Path(BACK_OFFICE)
public class CompanyResource {

    private static final Logger logger = LogManager.getLogger(CompanyResource.class);

    @Context
    private UriInfo uriInfo;

    private ICompanyService companyService;

    @Autowired
    public CompanyResource(ICompanyService companyService) {
        this.companyService = companyService;
    }

    @GET
    @Path(COMPANY_LIST)
    @Produces(MediaType.APPLICATION_JSON)
//    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getCompanies() {

        Iterable<Company> companyIterable = companyService.findAllCompany();
        List<CompanyResult> queryDetailsList =
                StreamSupport.stream(companyIterable.spliterator(), false)
                        .map(this::toQueryResult)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
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

            CompanyResult result = toQueryResult(company.get());
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

        return Response.ok(companyStored).build();
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

    /**
     * Map a {@link Company} instance to a {@link CompanyResult} instance.
     *
     * @param company
     * @return UserResult
     */
    private CompanyResult toQueryResult(Company company) {
        CompanyResult result = new CompanyResult();
        result.setIdCompany(company.getIdCompany());
        result.setDescription(company.getDescription());
        result.setCreatedBy(company.getCreatedBy());
        result.setModifiedBy(company.getModifiedBy());
        return result;
    }

    private Company toCompany(CompanyResult companyResult) {
        Company company = new Company();
        if(isEmpty(companyResult)) {
            throw new GeneralException("The form is empty");
        }
        if(!isEmpty(companyResult.getIdCompany())) {
            company.setIdCompany(companyResult.getIdCompany());
        }
        if(!isEmpty(companyResult.getDescription())) {
            company.setDescription(companyResult.getDescription().trim());
        }

        return company;
    }
}
