package com.mercari.solution.util.schema;

import org.apache.beam.sdk.transforms.SerializableFunction;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SchemaUtil {

    public interface SchemaConverter<InputSchemaT, RuntimeSchemaT> extends Serializable {
        RuntimeSchemaT convert(InputSchemaT schema);
    }

    public interface StringGetter<ElementT> extends Serializable {
        String getAsString(ElementT element, String field);
    }

    public interface FloatGetter<ElementT> extends Serializable {
        Float getAsFloat(ElementT element, String field);
    }

    public interface DoubleGetter<ElementT> extends Serializable {
        Double getAsDouble(ElementT element, String field);
    }

    public interface MapConverter<ElementT> extends Serializable {
        Map<String, Object> convert(ElementT element);
    }

    public interface ValuesSetter<SchemaT, ElementT> extends Serializable {
        ElementT setValues(SchemaT schema, ElementT element, Map<String, ? extends Object> values);
    }

    public interface ValueCreator<SchemaT, ElementT> extends Serializable {
        ElementT create(final SchemaT schema, final Map<String, Object> values);
    }

    public static <ElementT> SerializableFunction<ElementT, String> createGroupKeysFunction(
            final StringGetter<ElementT> stringGetter, final List<String> groupFields) {

        return (ElementT t) -> {
            final StringBuilder sb = new StringBuilder();
            for(final String fieldName : groupFields) {
                final String fieldValue = stringGetter.getAsString(t, fieldName);
                sb.append(fieldValue == null ? "" : fieldValue);
                sb.append("#");
            }
            if(sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        };
    }

}
