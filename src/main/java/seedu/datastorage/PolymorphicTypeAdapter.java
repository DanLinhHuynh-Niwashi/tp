package seedu.datastorage;

import com.google.gson.TypeAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import seedu.message.ErrorMessages;

import java.io.IOException;
import java.util.Map;

/**
 * A custom TypeAdapter implementation for handling polymorphic serialization and deserialization.
 *
 * @param <R> The type for which this TypeAdapter is used.
 */
public class PolymorphicTypeAdapter<R> extends TypeAdapter<R> {
    private final String typeFieldName;
    private final Map<Class<?>, String> subtypeToLabel;
    private final Map<String, TypeAdapter<?>> labelToDelegate;
    private final Map<Class<?>, TypeAdapter<?>> subtypeToDelegate;
    private final Gson gson;

    PolymorphicTypeAdapter(String typeFieldName,
                           Map<Class<?>, String> subtypeToLabel,
                           Map<String, TypeAdapter<?>> labelToDelegate,
                           Map<Class<?>, TypeAdapter<?>> subtypeToDelegate,
                           Gson gson) {
        this.typeFieldName = typeFieldName;
        this.subtypeToLabel = subtypeToLabel;
        this.labelToDelegate = labelToDelegate;
        this.subtypeToDelegate = subtypeToDelegate;
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, R value) throws IOException {
        Class<?> srcType = value.getClass();
        String label = subtypeToLabel.get(srcType);
        if (label == null) {
            throw new JsonParseException(
            String.format(ErrorMessages.UNKNOWN_SUBTYPE_SERIALIZATION, srcType.getName()));
        }
        @SuppressWarnings("unchecked")
        TypeAdapter<R> delegate = (TypeAdapter<R>) subtypeToDelegate.get(srcType);
        if (delegate == null) {
            throw new JsonParseException(
            String.format(ErrorMessages.UNKNOWN_SUBTYPE_SERIALIZATION, srcType.getName()));
        }

        // Serialize the object using the delegate
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(typeFieldName, label);

        JsonElement delegateElement = delegate.toJsonTree(value);
        if (delegateElement.isJsonObject()) {
            delegateElement.getAsJsonObject().entrySet().forEach(entry ->
                jsonObject.add(entry.getKey(), entry.getValue()));
        } else {
            throw new JsonParseException(ErrorMessages.MISSING_TYPE_INFORMATION);
        }

        // Write the jsonObject to the output
        gson.toJson(jsonObject, out);
    }

    @Override
    public R read(JsonReader in) throws IOException {
        JsonElement jsonElement = com.google.gson.JsonParser.parseReader(in);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement labelJsonElement = jsonObject.remove(typeFieldName);
        if (labelJsonElement == null) {
            throw new JsonParseException(ErrorMessages.MISSING_TYPE_INFORMATION);
        }
        String label = labelJsonElement.getAsString();


        TypeAdapter<R> delegate = (TypeAdapter<R>) labelToDelegate.get(label);
        if (delegate == null) {
            throw new JsonParseException(
            String.format(ErrorMessages.UNKNOWN_TYPE_LABEL, label));
        }
        return delegate.fromJsonTree(jsonObject);
    }
}
