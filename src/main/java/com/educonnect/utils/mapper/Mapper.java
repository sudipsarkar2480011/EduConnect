package com.educonnect.utils.mapper;

public interface Mapper <E, Req,Res>{
    E toEntity(Req requestDTO);
    Res toResponseDTO(E entity);
}
