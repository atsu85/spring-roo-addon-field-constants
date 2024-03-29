package ee.uiboupin.ats.roo.addon.fieldconstants;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.classpath.PhysicalTypeDetails;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;

/**
 * Provides {@link FieldconstantsMetadata}. This type is called by Roo to retrieve the metadata for this add-on.
 * Use this type to reference external types and services needed by the metadata type. Register metadata triggers and
 * dependencies here. Also define the unique add-on ITD identifier.
 * 
 * @author Ats Uiboupin
 */
@Component
// Using Apache Felix annotations to register command class in the Roo container
@Service
public final class FieldconstantsMetadataProvider extends AbstractItdMetadataProvider {
  private Logger log = Logger.getLogger(getClass().getName());

  /**
   * The activate method for this OSGi component, this will be called by the OSGi container upon bundle activation
   * (result of the 'addon install' command)
   * 
   * @param context the component context can be used to get access to the OSGi container (ie find out if certain bundles are active)
   */
  protected void activate(ComponentContext context) {
    metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
    addMetadataTrigger(new JavaType(RooFieldconstants.class.getName()));
  }

  /**
   * The deactivate method for this OSGi component, this will be called by the OSGi container upon bundle deactivation
   * (result of the 'addon uninstall' command)
   * 
   * @param context the component context can be used to get access to the OSGi container (ie find out if certain bundles are active)
   */
  protected void deactivate(ComponentContext context) {
    metadataDependencyRegistry.deregisterDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
    removeMetadataTrigger(new JavaType(RooFieldconstants.class.getName()));
  }

  /**
   * Return an instance of the Metadata offered by this add-on
   */
  protected ItdTypeDetailsProvidingMetadataItem getMetadata(String metadataIdentificationString
      , JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, String itdFilename) {
    System.out.println("getMetadata");
    Set<FieldMetadata> declaredFields = new LinkedHashSet<FieldMetadata>();
    PhysicalTypeDetails physicalTypeDetails = governorPhysicalTypeMetadata.getMemberHoldingTypeDetails();
    FieldConstantsAnnotationValues annotationValues = new FieldConstantsAnnotationValues(governorPhysicalTypeMetadata);
    collectFields(declaredFields, physicalTypeDetails, annotationValues.isIncludeSuperClasses());

    // Pass dependencies required by the metadata in through its constructor
    return new FieldconstantsMetadata(metadataIdentificationString, aspectName, governorPhysicalTypeMetadata, declaredFields);
  }

  private void collectFields(Set<FieldMetadata> declaredFields, PhysicalTypeDetails physicalTypeDetails, boolean includeSuperClasses) {
    log.fine("collecting fields from " + physicalTypeDetails.getName());
    if (physicalTypeDetails != null && physicalTypeDetails instanceof ClassOrInterfaceTypeDetails) {
      System.out.println("physicalTypeDetails instanceof ClassOrInterfaceTypeDetails");
      ClassOrInterfaceTypeDetails governorTypeDetails = (ClassOrInterfaceTypeDetails) physicalTypeDetails;
      for (FieldMetadata field : governorTypeDetails.getDeclaredFields()) {
        declaredFields.add(field);
      }
      if (includeSuperClasses) {
        ClassOrInterfaceTypeDetails superclass = governorTypeDetails.getSuperclass();
        if (superclass != null) {
          log.fine(physicalTypeDetails.getName() + " extends " + superclass.getName());
          collectFields(declaredFields, superclass, includeSuperClasses);
        }
      }
    }
  }

  /**
   * Define the unique ITD file name extension, here the resulting file name will be **_ROO_Fieldconstants.aj
   */
  public String getItdUniquenessFilenameSuffix() {
    return "Fieldconstants";
  }

  protected String getGovernorPhysicalTypeIdentifier(String metadataIdentificationString) {
    JavaType javaType = FieldconstantsMetadata.getJavaType(metadataIdentificationString);
    Path path = FieldconstantsMetadata.getPath(metadataIdentificationString);
    return PhysicalTypeIdentifier.createIdentifier(javaType, path);
  }

  protected String createLocalIdentifier(JavaType javaType, Path path) {
    return FieldconstantsMetadata.createIdentifier(javaType, path);
  }

  public String getProvidesType() {
    return FieldconstantsMetadata.getMetadataIdentiferType();
  }
}