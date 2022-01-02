package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.GlobalSettings;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.SensorSettings;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.TempMeasurement;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.GlobalSettingsFormData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.SensorSettingsFormData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.TemperatureFormData;

@Component
public class SensorsMapper extends AbstractMapper{

    @Override
    protected void configureMappings(ModelMapper modelMapper) {
        configureSensorSettingsFromData(modelMapper);
        configureTemperatureFormData(modelMapper);
        configureGlobalSettingsMapper(modelMapper);
    }

    private void configureSensorSettingsFromData(ModelMapper modelMapper) {
        modelMapper.createTypeMap(SensorSettingsFormData.class, SensorSettings.class).addMappings(new PropertyMap<SensorSettingsFormData, SensorSettings>() {
            @Override
            protected void configure() {
                skip().set_id(null);
            }
        }).setPostConverter(mappingContext -> {
            SensorSettings destination = mappingContext.getDestination();
            destination.set_id(mappingContext.getSource().getId());
            return destination;
        });

        modelMapper.createTypeMap(SensorSettings.class, SensorSettingsFormData.class).addMappings(new PropertyMap<SensorSettings, SensorSettingsFormData>() {
            @Override
            protected void configure() {
                skip().setId(null);
            }
        }).setPostConverter(mappingContext -> {
            SensorSettingsFormData destination = mappingContext.getDestination();
            destination.setId(mappingContext.getSource().get_id());
            return destination;
        });
    }

    private void configureTemperatureFormData(ModelMapper modelMapper) {
        modelMapper.createTypeMap(TempMeasurement.class, TemperatureFormData.class);
    }

    private void configureGlobalSettingsMapper(ModelMapper modelMapper) {
        modelMapper.createTypeMap(GlobalSettingsFormData.class, GlobalSettings.class).addMappings(new PropertyMap<GlobalSettingsFormData, GlobalSettings>() {
            @Override
            protected void configure() {
                skip().set_id(null);
                skip().setCreatedOn(null);
                skip().setLastModifiedOn(null);
            }
        });

        modelMapper.createTypeMap(GlobalSettings.class, GlobalSettingsFormData.class);
    }
}
