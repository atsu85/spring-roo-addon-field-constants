package ee.uiboupin.ats.roo.addon.fieldconstants;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.roo.classpath.PhysicalTypeCategory;
import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.FieldMetadataBuilder;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.springframework.roo.support.style.ToStringCreator;
import org.springframework.roo.support.util.Assert;

/**
 * This type produces metadata for a new ITD.
 * 
 * @author Ats Uiboupin
 */
public class FieldconstantsMetadata extends
    AbstractItdTypeDetailsProvidingMetadataItem {
  private static final String PROVIDES_TYPE_STRING = FieldconstantsMetadata.class
      .getName();
  private static final String PROVIDES_TYPE = MetadataIdentificationUtils
      .create(PROVIDES_TYPE_STRING);

  public FieldconstantsMetadata(String identifier, JavaType aspectName,
      PhysicalTypeMetadata governorPhysicalTypeMetadata,
      Set<FieldMetadata> declaredFields) {
    super(identifier, aspectName, governorPhysicalTypeMetadata);
    Assert.isTrue(isValid(identifier), "Metadata identification string '"
        + identifier + "' does not appear to be a valid");

    FieldConstantsAnnotationValues annotationValues = new FieldConstantsAnnotationValues(governorPhysicalTypeMetadata);
    final JavaType constantsClassName = new JavaType(annotationValues.getInnerClassName());
    builder.addInnerType(createConstantsClassType(constantsClassName,
        declaredFields));
    // Create a representation of the desired output ITD
    itdTypeDetails = builder.build();
  }

  private ClassOrInterfaceTypeDetails createConstantsClassType(
      final JavaType repositoryHolderType,
      Set<FieldMetadata> declaredFields) {
    if (MemberFindingUtils.getDeclaredInnerType(governorTypeDetails,
        repositoryHolderType) != null) {
      System.out.println("inner type not null");
      return null;
    }

    final ClassOrInterfaceTypeDetailsBuilder classBuilder = new ClassOrInterfaceTypeDetailsBuilder(
        getId(), Modifier.PUBLIC|Modifier.STATIC, repositoryHolderType,
        PhysicalTypeCategory.CLASS);
    for (FieldMetadata fieldMetadata : declaredFields) {
      String fieldName = fieldMetadata.getFieldName().getSymbolName();
      FieldMetadataBuilder fieldBuilder = new FieldMetadataBuilder(
          getId(), Modifier.PUBLIC | Modifier.STATIC
              | Modifier.FINAL,
          new ArrayList<AnnotationMetadataBuilder>(),
          symbol(fieldName), new JavaType("String"));
      fieldBuilder.setFieldInitializer("\"" + fieldName + "\"");
      classBuilder.addField(fieldBuilder);
    }

    // TODO: no need to add package to the inner class
    // JavaType memberHoldingType = governorPhysicalTypeMetadata.getMemberHoldingTypeDetails().getName();
    // classBuilder.getRegisteredImports().add(new ImportMetadataBuilder(memberHoldingType.getFullyQualifiedTypeName()).build());
    // classBuilder.putCustomData(key, value)
    return classBuilder.build();
  }

  public static JavaSymbolName symbol(String name) {
    return new JavaSymbolName(name);
  }

  public String toString() {
    ToStringCreator tsc = new ToStringCreator(this);
    tsc.append("identifier", getId());
    tsc.append("valid", valid);
    tsc.append("aspectName", aspectName);
    tsc.append("destinationType", destination);
    tsc.append("governor", governorPhysicalTypeMetadata.getId());
    tsc.append("itdTypeDetails", itdTypeDetails);
    return tsc.toString();
  }

  public static final String getMetadataIdentiferType() {
    return PROVIDES_TYPE;
  }

  public static final String createIdentifier(JavaType javaType, Path path) {
    return PhysicalTypeIdentifierNamingUtils.createIdentifier(
        PROVIDES_TYPE_STRING, javaType, path);
  }

  public static final JavaType getJavaType(String metadataIdentificationString) {
    return PhysicalTypeIdentifierNamingUtils.getJavaType(
        PROVIDES_TYPE_STRING, metadataIdentificationString);
  }

  public static final Path getPath(String metadataIdentificationString) {
    return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING,
        metadataIdentificationString);
  }

  public static boolean isValid(String metadataIdentificationString) {
    return PhysicalTypeIdentifierNamingUtils.isValid(PROVIDES_TYPE_STRING,
        metadataIdentificationString);
  }
}
