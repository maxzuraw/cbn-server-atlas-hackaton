package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.mappers;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import pl.krejnstudio.smarttools.coldbedroomnotifier.utils.LocalDateUtils;
import pl.krejnstudio.smarttools.coldbedroomnotifier.utils.ZonedDateTimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class AbstractMapper implements Mapper {

    private ModelMapper modelMapper;

    public AbstractMapper(boolean validateExplicitMappings) {
        initModelMapper(validateExplicitMappings, true);
    }

    public AbstractMapper() {
        initModelMapper(true, true);
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        return getModelMapper(source.getClass(), destinationType).map(source, destinationType);
    }

    @Override
    public void map(Object source, Object destination) {
        getModelMapper(source.getClass(), destination.getClass()).map(source, destination);
    }

    @Override
    public <S, D> List<D> mapList(List<S> sourceList, Class<D> destinationType) {
        if (null == sourceList) {
            return null;
        }
        List<D> result = new ArrayList<>();
        for (S source : sourceList) {
            result.add(map(source, destinationType));
        }
        return result;
    }

    @Override
    public <S, D> void mapList(List<S> sourceList, List<D> destinationList) {
        if (null == sourceList && null == destinationList) {
            return;
        }
        validateSourceAndDestinationLists(sourceList, destinationList);

        Iterator<S> sourceIter = sourceList.iterator();
        Iterator<D> destinationIter = destinationList.iterator();
        while (sourceIter.hasNext()) {
            map(sourceIter.next(), destinationIter.next());
        }
    }

    protected abstract void configureMappings(ModelMapper modelMapper2);

    private void initModelMapper(boolean validateExplicitMappings, boolean removeWithspaces) {

        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        if (removeWithspaces) {
            modelMapper.createTypeMap(String.class, String.class).setConverter(new AbstractConverter<String, String>() {
                @Override
                protected String convert(String source) {
                    return StringUtils.trimToNull(source);
                }
            });
        }

        modelMapper.createTypeMap(java.util.Date.class, java.util.Date.class)
            .setConverter(new AbstractConverter<java.util.Date, java.util.Date>() {
                @Override
                protected java.util.Date convert(java.util.Date source) {
                    if (source == null) {
                        return null;
                    }
                    return new java.util.Date(source.getTime());
                }
            });

        modelMapper.createTypeMap(java.time.LocalDate.class, java.util.Date.class)
                .setConverter(new AbstractConverter<java.time.LocalDate, java.util.Date>() {
                    @Override
                    protected java.util.Date convert(java.time.LocalDate source) {
                        return LocalDateUtils.from(source);
                    }
                });


        modelMapper.createTypeMap(java.util.Date.class, java.time.LocalDate.class)
                .setConverter(new AbstractConverter<java.util.Date, java.time.LocalDate>() {
                    @Override
                    protected java.time.LocalDate convert(java.util.Date source) {
                        if (source == null) {
                            return null;
                        }
                        return LocalDateUtils.from(new java.util.Date(source.getTime()));
                    }
                });

        modelMapper.createTypeMap(java.time.ZonedDateTime.class, java.util.Date.class)
                .setConverter(new AbstractConverter<java.time.ZonedDateTime, java.util.Date>() {
                    @Override
                    protected java.util.Date convert(java.time.ZonedDateTime source) {
                        return ZonedDateTimeUtils.from(source);
                    }
                });

        modelMapper.createTypeMap(java.util.Date.class, java.time.ZonedDateTime.class)
                .setConverter(new AbstractConverter<java.util.Date, java.time.ZonedDateTime>() {
                    @Override
                    protected java.time.ZonedDateTime convert(java.util.Date source) {
                        if (source == null) {
                            return null;
                        }
                        return ZonedDateTimeUtils.from(new java.util.Date(source.getTime()));
                    }
                });

        configureMappings(modelMapper);

        if (validateExplicitMappings) {
            modelMapper.validate();
        }
    }

    private <S, D> ModelMapper getModelMapper(Class<S> sourceClass, Class<D> destinationClass) {
        if (null == modelMapper.getTypeMap(sourceClass, destinationClass)) {
            String errorMsg = "No mapping found from source class: " + sourceClass.getName() + " to destination " +
                "class: " + destinationClass.getName();
            throw new IllegalArgumentException(errorMsg);
        }
        return modelMapper;
    }

    private <S, D> void validateSourceAndDestinationLists(List<S> sourceList, List<D> destinationList) {
        Preconditions.checkArgument(sourceList != null, "Source list is null!");
        Preconditions.checkArgument(destinationList != null, "Destination list is null!");
        if (sourceList.size() != destinationList.size()) {
            String msg = String.format("Source list and destination list do not have the same size! Source size: %d, destination size: %d",
                sourceList.size(), destinationList.size());
            throw new IllegalArgumentException(msg);
        }
    }

    public String fromObjectId(ObjectId id) {
        return id.toHexString();
    }

    public ObjectId toObjectId(String id) {
        return new ObjectId(id);
    }

}
