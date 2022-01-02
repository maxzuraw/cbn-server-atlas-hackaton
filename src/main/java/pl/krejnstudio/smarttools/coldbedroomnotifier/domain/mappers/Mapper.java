package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.mappers;

import java.util.List;

public interface Mapper {

    <D> D map(Object source, Class<D> destinationType);

    void map(Object source, Object destination);

    <S, D> List<D> mapList(List<S> sourceList, Class<D> destinationType);

    <S, D> void mapList(List<S> sourceList, List<D> destinationList);

}