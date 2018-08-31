package com.tx.co.back_office.company.resource;

import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.service.ICompanyService;
import com.tx.co.user.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.tx.co.common.constants.AppConstants.*;


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

        Company company = companyService.findByIdCompany(idCompany).get();
        if (company == null) {
            throw new NotFoundException();
        }

        CompanyResult result = toQueryResult(company);
        return Response.ok(result).build();
    }

    @POST
    @Path(COMPANY_CREATE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCompany(Company company) {

        Company companyStored = companyService.saveCompany(company);
        if(companyStored != null) {
            logger.info("Company already exits.");
            return Response.status(Response.Status.CONFLICT).build();
        }
        return Response.ok(companyStored).build();
    }

    @PUT
    @Path(COMPANY_EDIT)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editCompany(Company company) {

        Company companyEdited = companyService.saveCompany(company);
        if(companyEdited != null) {
            logger.info("Company already exits.");
            return Response.status(Response.Status.CONFLICT).build();
        }
        return Response.ok(companyEdited).build();
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
}
