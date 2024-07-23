package com.test.gson;

import com.test.errorprone.annotations.CanIgnoreReturnValue;
import com.test.errorprone.annotations.InlineMe;
import com.test.gson.internal.$Gson$Preconditions;
import com.test.gson.internal.Excluder;
import com.test.gson.internal.bind.DefaultDateTypeAdapter;
import com.test.gson.internal.bind.TreeTypeAdapter;
import com.test.gson.internal.bind.TypeAdapters;
import com.test.gson.internal.sql.SqlTypesSupport;
import com.test.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class GsonBuilder {
    private Excluder excluder = Excluder.DEFAULT;

    private LongSerializationPolicy longSerializationPolicy = LongSerializationPolicy.DEFAULT;

    private FieldNamingStrategy fieldNamingPolicy = FieldNamingPolicy.IDENTITY;

    private final Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();

    private final List<TypeAdapterFactory> factories = new ArrayList<>();

    private final List<TypeAdapterFactory> hierarchyFactories = new ArrayList<>();

    private boolean serializeNulls = false;

    private String datePattern = Gson.DEFAULT_DATE_PATTERN;

    private int dateStyle = 2;

    private int timeStyle = 2;

    private boolean complexMapKeySerialization = false;

    private boolean serializeSpecialFloatingPointValues = false;

    private boolean escapeHtmlChars = true;

    private FormattingStyle formattingStyle = Gson.DEFAULT_FORMATTING_STYLE;

    private boolean generateNonExecutableJson = false;

    private Strictness strictness = Gson.DEFAULT_STRICTNESS;

    private boolean useJdkUnsafe = true;

    private ToNumberStrategy objectToNumberStrategy = Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY;

    private ToNumberStrategy numberToNumberStrategy = Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY;

    private final ArrayDeque<ReflectionAccessFilter> reflectionFilters = new ArrayDeque<>();

    public GsonBuilder() {}

    GsonBuilder(Gson gson) {
        this.excluder = gson.excluder;
        this.fieldNamingPolicy = gson.fieldNamingStrategy;
        this.instanceCreators.putAll(gson.instanceCreators);
        this.serializeNulls = gson.serializeNulls;
        this.complexMapKeySerialization = gson.complexMapKeySerialization;
        this.generateNonExecutableJson = gson.generateNonExecutableJson;
        this.escapeHtmlChars = gson.htmlSafe;
        this.formattingStyle = gson.formattingStyle;
        this.strictness = gson.strictness;
        this.serializeSpecialFloatingPointValues = gson.serializeSpecialFloatingPointValues;
        this.longSerializationPolicy = gson.longSerializationPolicy;
        this.datePattern = gson.datePattern;
        this.dateStyle = gson.dateStyle;
        this.timeStyle = gson.timeStyle;
        this.factories.addAll(gson.builderFactories);
        this.hierarchyFactories.addAll(gson.builderHierarchyFactories);
        this.useJdkUnsafe = gson.useJdkUnsafe;
        this.objectToNumberStrategy = gson.objectToNumberStrategy;
        this.numberToNumberStrategy = gson.numberToNumberStrategy;
        this.reflectionFilters.addAll(gson.reflectionFilters);
    }

    @CanIgnoreReturnValue
    public GsonBuilder setVersion(double version) {
        if (Double.isNaN(version) || version < 0.0D)
            throw new IllegalArgumentException("Invalid version: " + version);
        this.excluder = this.excluder.withVersion(version);
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder excludeFieldsWithModifiers(int... modifiers) {
        Objects.requireNonNull(modifiers);
        this.excluder = this.excluder.withModifiers(modifiers);
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder generateNonExecutableJson() {
        this.generateNonExecutableJson = true;
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder excludeFieldsWithoutExposeAnnotation() {
        this.excluder = this.excluder.excludeFieldsWithoutExposeAnnotation();
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder serializeNulls() {
        this.serializeNulls = true;
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder enableComplexMapKeySerialization() {
        this.complexMapKeySerialization = true;
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder disableInnerClassSerialization() {
        this.excluder = this.excluder.disableInnerClassSerialization();
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder setLongSerializationPolicy(LongSerializationPolicy serializationPolicy) {
        this.longSerializationPolicy = Objects.<LongSerializationPolicy>requireNonNull(serializationPolicy);
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder setFieldNamingPolicy(FieldNamingPolicy namingConvention) {
        return setFieldNamingStrategy(namingConvention);
    }

    @CanIgnoreReturnValue
    public GsonBuilder setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        this.fieldNamingPolicy = Objects.<FieldNamingStrategy>requireNonNull(fieldNamingStrategy);
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder setObjectToNumberStrategy(ToNumberStrategy objectToNumberStrategy) {
        this.objectToNumberStrategy = Objects.<ToNumberStrategy>requireNonNull(objectToNumberStrategy);
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder setNumberToNumberStrategy(ToNumberStrategy numberToNumberStrategy) {
        this.numberToNumberStrategy = Objects.<ToNumberStrategy>requireNonNull(numberToNumberStrategy);
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder setExclusionStrategies(ExclusionStrategy... strategies) {
        Objects.requireNonNull(strategies);
        for (ExclusionStrategy strategy : strategies)
            this.excluder = this.excluder.withExclusionStrategy(strategy, true, true);
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder addSerializationExclusionStrategy(ExclusionStrategy strategy) {
        Objects.requireNonNull(strategy);
        this.excluder = this.excluder.withExclusionStrategy(strategy, true, false);
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder addDeserializationExclusionStrategy(ExclusionStrategy strategy) {
        Objects.requireNonNull(strategy);
        this.excluder = this.excluder.withExclusionStrategy(strategy, false, true);
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder setPrettyPrinting() {
        return setFormattingStyle(FormattingStyle.PRETTY);
    }

    @CanIgnoreReturnValue
    public GsonBuilder setFormattingStyle(FormattingStyle formattingStyle) {
        this.formattingStyle = Objects.<FormattingStyle>requireNonNull(formattingStyle);
        return this;
    }

    @Deprecated
    @InlineMe(replacement = "this.setStrictness(Strictness.LENIENT)", imports = {"com.google.gson.Strictness"})
    @CanIgnoreReturnValue
    public GsonBuilder setLenient() {
        return setStrictness(Strictness.LENIENT);
    }

    @CanIgnoreReturnValue
    public GsonBuilder setStrictness(Strictness strictness) {
        this.strictness = Objects.<Strictness>requireNonNull(strictness);
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder disableHtmlEscaping() {
        this.escapeHtmlChars = false;
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder setDateFormat(String pattern) {
        if (pattern != null)
            try {
                new SimpleDateFormat(pattern);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("The date pattern '" + pattern + "' is not valid", e);
            }
        this.datePattern = pattern;
        return this;
    }

    @Deprecated
    @CanIgnoreReturnValue
    public GsonBuilder setDateFormat(int dateStyle) {
        this.dateStyle = checkDateFormatStyle(dateStyle);
        this.datePattern = null;
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder setDateFormat(int dateStyle, int timeStyle) {
        this.dateStyle = checkDateFormatStyle(dateStyle);
        this.timeStyle = checkDateFormatStyle(timeStyle);
        this.datePattern = null;
        return this;
    }

    private static int checkDateFormatStyle(int style) {
        if (style < 0 || style > 3)
            throw new IllegalArgumentException("Invalid style: " + style);
        return style;
    }

    @CanIgnoreReturnValue
    public GsonBuilder registerTypeAdapter(Type type, Object typeAdapter) {
        Objects.requireNonNull(type);
        $Gson$Preconditions.checkArgument((typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer || typeAdapter instanceof InstanceCreator || typeAdapter instanceof TypeAdapter));
        if (isTypeObjectOrJsonElement(type))
            throw new IllegalArgumentException("Cannot override built-in adapter for " + type);
        if (typeAdapter instanceof InstanceCreator)
            this.instanceCreators.put(type, (InstanceCreator)typeAdapter);
        if (typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer) {
            TypeToken<?> typeToken = TypeToken.get(type);
            this.factories.add(TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, typeAdapter));
        }
        if (typeAdapter instanceof TypeAdapter) {
            TypeAdapterFactory factory = TypeAdapters.newFactory(TypeToken.get(type), (TypeAdapter)typeAdapter);
            this.factories.add(factory);
        }
        return this;
    }

    private static boolean isTypeObjectOrJsonElement(Type type) {
        return (type instanceof Class && (type == Object.class || JsonElement.class
                .isAssignableFrom((Class)type)));
    }

    @CanIgnoreReturnValue
    public GsonBuilder registerTypeAdapterFactory(TypeAdapterFactory factory) {
        Objects.requireNonNull(factory);
        this.factories.add(factory);
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder registerTypeHierarchyAdapter(Class<?> baseType, Object typeAdapter) {
        Objects.requireNonNull(baseType);
        $Gson$Preconditions.checkArgument((typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer || typeAdapter instanceof TypeAdapter));
        if (JsonElement.class.isAssignableFrom(baseType))
            throw new IllegalArgumentException("Cannot override built-in adapter for " + baseType);
        if (typeAdapter instanceof JsonDeserializer || typeAdapter instanceof JsonSerializer)
            this.hierarchyFactories.add(TreeTypeAdapter.newTypeHierarchyFactory(baseType, typeAdapter));
        if (typeAdapter instanceof TypeAdapter) {
            TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(baseType, (TypeAdapter)typeAdapter);
            this.factories.add(factory);
        }
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder serializeSpecialFloatingPointValues() {
        this.serializeSpecialFloatingPointValues = true;
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder disableJdkUnsafe() {
        this.useJdkUnsafe = false;
        return this;
    }

    @CanIgnoreReturnValue
    public GsonBuilder addReflectionAccessFilter(ReflectionAccessFilter filter) {
        Objects.requireNonNull(filter);
        this.reflectionFilters.addFirst(filter);
        return this;
    }

    public Gson create() {
        List<TypeAdapterFactory> factories = new ArrayList<>(this.factories.size() + this.hierarchyFactories.size() + 3);
        factories.addAll(this.factories);
        Collections.reverse(factories);
        List<TypeAdapterFactory> hierarchyFactories = new ArrayList<>(this.hierarchyFactories);
        Collections.reverse(hierarchyFactories);
        factories.addAll(hierarchyFactories);
        addTypeAdaptersForDate(this.datePattern, this.dateStyle, this.timeStyle, factories);
        return new Gson(this.excluder, this.fieldNamingPolicy, new HashMap<>(this.instanceCreators), this.serializeNulls, this.complexMapKeySerialization, this.generateNonExecutableJson, this.escapeHtmlChars, this.formattingStyle, this.strictness, this.serializeSpecialFloatingPointValues, this.useJdkUnsafe, this.longSerializationPolicy, this.datePattern, this.dateStyle, this.timeStyle, new ArrayList<>(this.factories), new ArrayList<>(this.hierarchyFactories), factories, this.objectToNumberStrategy, this.numberToNumberStrategy, new ArrayList<>(this.reflectionFilters));
    }

    private static void addTypeAdaptersForDate(String datePattern, int dateStyle, int timeStyle, List<TypeAdapterFactory> factories) {
        TypeAdapterFactory dateAdapterFactory;
        boolean sqlTypesSupported = SqlTypesSupport.SUPPORTS_SQL_TYPES;
        TypeAdapterFactory sqlTimestampAdapterFactory = null;
        TypeAdapterFactory sqlDateAdapterFactory = null;
        if (datePattern != null && !datePattern.trim().isEmpty()) {
            dateAdapterFactory = DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(datePattern);
            if (sqlTypesSupported) {
                sqlTimestampAdapterFactory = SqlTypesSupport.TIMESTAMP_DATE_TYPE.createAdapterFactory(datePattern);
                sqlDateAdapterFactory = SqlTypesSupport.DATE_DATE_TYPE.createAdapterFactory(datePattern);
            }
        } else if (dateStyle != 2 || timeStyle != 2) {
            dateAdapterFactory = DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(dateStyle, timeStyle);
            if (sqlTypesSupported) {
                sqlTimestampAdapterFactory = SqlTypesSupport.TIMESTAMP_DATE_TYPE.createAdapterFactory(dateStyle, timeStyle);
                sqlDateAdapterFactory = SqlTypesSupport.DATE_DATE_TYPE.createAdapterFactory(dateStyle, timeStyle);
            }
        } else {
            return;
        }
        factories.add(dateAdapterFactory);
        if (sqlTypesSupported) {
            factories.add(sqlTimestampAdapterFactory);
            factories.add(sqlDateAdapterFactory);
        }
    }
}
