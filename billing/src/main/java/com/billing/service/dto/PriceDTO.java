package com.billing.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.billing.domain.Price} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PriceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String location;

    @NotNull
    private String description;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10000")
    private BigDecimal cost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceDTO)) {
            return false;
        }

        PriceDTO priceDTO = (PriceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, priceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            ", description='" + getDescription() + "'" +
            ", cost=" + getCost() +
            "}";
    }
}
