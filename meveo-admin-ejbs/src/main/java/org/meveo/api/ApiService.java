package org.meveo.api;

import java.util.List;

import org.meveo.admin.exception.BusinessException;
import org.meveo.api.dto.BaseEntityDto;
import org.meveo.api.dto.module.MeveoModuleItemDto;
import org.meveo.api.exception.EntityDoesNotExistsException;
import org.meveo.api.exception.InvalidParameterException;
import org.meveo.api.exception.MeveoApiException;
import org.meveo.api.exception.MissingParameterException;
import org.meveo.model.IEntity;

/**
 * An interface of CRUD API service class
 * 
 * @author Andrius Karpavicius
 * 
 * @param <E> Entity class
 * @param <T> Dto class
 */
public interface ApiService<E extends IEntity, T extends BaseEntityDto> {
	
	/**
	 * Remove the entity corresponding to the DTO if it exists
	 * 
	 * @param dto The entity to remove
	 * @throws MeveoApiException if an exception occurs
	 * @throws BusinessException if an exception occurs
	 */
	void remove(T dto) throws MeveoApiException, BusinessException;
	
    /**
     * Find entity identified by code.
     * 
     * @param code Entity code
     * 
     * @return A DTO of entity
     * @throws EntityDoesNotExistsException Entity was not found
     * @throws InvalidParameterException Some search parameter is incorrect
     * @throws MissingParameterException A parameter, necessary to find an entity, was not provided
     * @throws MeveoApiException Any other exception is wrapped to MeveoApiException
     * @throws org.meveo.exceptions.EntityDoesNotExistsException 
     */
    T find(String code) throws EntityDoesNotExistsException, MissingParameterException, InvalidParameterException, MeveoApiException, org.meveo.exceptions.EntityDoesNotExistsException;

    /**
     * Find entity identified by code. Return null if not found
     * 
     * @param code Entity code
     * 
     * @return A DTO of entity or NULL if not found
     * @throws InvalidParameterException Some search parameter is incorrect
     * @throws MissingParameterException A parameter, necessary to find an entity, was not provided
     * @throws MeveoApiException Any other exception is wrapped to MeveoApiException
     * @throws org.meveo.exceptions.EntityDoesNotExistsException 
     */
    T findIgnoreNotFound(String code) throws MissingParameterException, InvalidParameterException, MeveoApiException, org.meveo.exceptions.EntityDoesNotExistsException;

    /**
     * Create or update an entity from DTO.
     * 
     * @param dtoData DTO data
     * @return created or updated entity.
     * @throws MeveoApiException meveo api exception
     * @throws BusinessException business exception.
     */
    E createOrUpdate(T dtoData) throws MeveoApiException, BusinessException;
    
    default int compareDtos (T obj1, T obj2, List<MeveoModuleItemDto> dtos) {
    	if (obj1 instanceof Comparable && obj2 instanceof Comparable) {
        	return ((Comparable<T>) obj1).compareTo(obj2);
    	} else {
    		return 0;
    	}
    }

}
