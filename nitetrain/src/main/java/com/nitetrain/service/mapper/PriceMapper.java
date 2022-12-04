package com.nitetrain.service.mapper;

import com.nitetrain.domain.Price;
import com.nitetrain.service.dto.PriceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Price} and its DTO {@link PriceDTO}.
 */
@Mapper(componentModel = "spring")
public interface PriceMapper extends EntityMapper<PriceDTO, Price> {}
