package com.tx.co.back_office.office;

import static org.springframework.util.ObjectUtils.isEmpty;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.office.service.IOfficeService;
import com.tx.co.security.exception.GeneralException;

import static com.tx.co.common.constants.AppConstants.*;

@Component
@Path(BACK_OFFICE)
public class OfficeResource {
	
	private static final Logger logger = LogManager.getLogger(OfficeResource.class);
	
	@Context
    private UriInfo uriInfo; 
	
	private IOfficeService officeService;

	@Autowired
	public void setOfficeService(IOfficeService officeService) {
		this.officeService = officeService;
	}
	
    @GET
    @Path(OFFICE_LIST)
    @Produces(MediaType.APPLICATION_JSON)
//    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getCompanies() {

        Iterable<Office> officeIterable = officeService.findAllOffice();
        List<OfficeResult> queryOfficeList =
                StreamSupport.stream(officeIterable.spliterator(), false)
                        .map(this::toOfficeResult)
                        .collect(Collectors.toList());

        return Response.ok(queryOfficeList).build();
    }
    
    @POST
    @Path(OFFICE_CREATE_UPDATE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUpdateCompany(OfficeResult officeResult) {

        Office officeStored = officeService.saveUpdateOffice(toOffice(officeResult));

        return Response.ok(officeStored).build();
    }
	
    /**
     * Map a {@link Company} instance to a {@link CompanyResult} instance.
     *
     * @param company
     * @return UserResult
     */
    private OfficeResult toOfficeResult(Office office) {
    	OfficeResult result = new OfficeResult();
        result.setIdOffice(office.getIdOffice());
        result.setDescription(office.getDescription());
        result.setCreatedBy(office.getCreatedBy());
        result.setModifiedBy(office.getModifiedBy());
        return result;
    }
    
    private Office toOffice(OfficeResult officeResult) {
    	Office office = new Office();
    	if(isEmpty(officeResult)) {
    		throw new GeneralException("The form is empty");
    	}
    	if(!isEmpty(officeResult.getIdOffice())) {
    		office.setIdOffice(officeResult.getIdOffice());
        }
        if(!isEmpty(officeResult.getDescription())) {
        	office.setDescription(officeResult.getDescription().trim());
        }
        if(!isEmpty(officeResult.getDescription())) {
        	office.setCompany(officeResult.getCompany());
        }
        return office;
    }

}
