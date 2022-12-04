package com.billing.service.mapper;

import com.billing.domain.Price;
import com.billing.service.dto.PriceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Price} and its DTO {@link PriceDTO}.
 */
@Mapper(componentModel = "spring")
public interface PriceMapper extends EntityMapper<PriceDTO, Price> {}
