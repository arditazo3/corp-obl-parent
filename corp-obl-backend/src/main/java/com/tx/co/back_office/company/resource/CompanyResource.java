package com.tx.co.back_office.company.resource;

import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.company.api.model.CompanyUserResult;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyUser;
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

import java.util.ArrayList;
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
    
    @POST
    @Path(ASSOC_USER_COMPANY)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
 //   @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response associateUserToCompany(CompanyResult companyUserListResult) {

        companyService.associateUserToCompany(toCompany(companyUserListResult));

        return Response.noContent().build();
    }

    /**
     * Map a {@link Company} instance to a {@link CompanyResult} instance.
     *
     * @param company
     * @return UserResult
     */
    private CompanyResult toCompanyResult(Company company) {
        CompanyResult result = new CompanyResult();
        result.setIdCompany(company.getIdCompany());
        result.setDescription(company.getDescription());
        
        if(!isEmpty(company.getCompanyUsers())) {
        	result.setUsersAssociated(new ArrayList<>());
        	for (CompanyUser companyUser : company.getCompanyUsers()) {
        		result.getUsersAssociated().add(toCompanyUserResult(companyUser));
			}
        }
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
        if(!isEmpty(companyResult.getUsersAssociated())) {
        	for (CompanyUserResult companyUserResylt : companyResult.getUsersAssociated()) {
        		company.getCompanyUsers().add(toCompanyUser(company, companyUserResylt));
			}
        }

        return company;
    }
    
    private CompanyUserResult toCompanyUserResult(CompanyUser companyUser) {
    	CompanyUserResult result = new CompanyUserResult();
    	result.setIdCompanyUser(companyUser.getIdCompanyUser());
    	result.setUsername(companyUser.getUsername());
    	result.setCompanyAdmin(companyUser.getCompanyAdmin());
    	return result;
    }
    
    private CompanyUser toCompanyUser(Company company, CompanyUserResult companyUserResult) {
    	CompanyUser companyUser = new CompanyUser();
    	companyUser.setIdCompanyUser(companyUserResult.getIdCompanyUser());
    	companyUser.setUsername(companyUserResult.getUsername());
    	companyUser.setCompany(company);
    	if(isEmpty(companyUserResult.getCompanyAdmin())) {
    		companyUser.setCompanyAdmin(false);
    	} else {
    		companyUser.setCompanyAdmin(companyUserResult.getCompanyAdmin());
    	}
    	return companyUser;
    }
    
    private List<CompanyUser> toCompanyUserList(Company company, List<CompanyUserResult> companyUserResultList) {
    	List<CompanyUser> companyUserList = new ArrayList<>();
    	if(!isEmpty(companyUserResultList)) {
    		for (CompanyUserResult companyUserResult : companyUserResultList) {
    			companyUserList.add(toCompanyUser(company, companyUserResult));
			}
    	}
    	return companyUserList;
    }
}
