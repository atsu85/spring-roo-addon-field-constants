package ee.uiboupin.ats.roo.addon.fieldconstants;

import java.util.Set;
import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.PhysicalTypeDetails;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.PhysicalTypeMetadataProvider;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.MutableClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.DependencyScope;
import org.springframework.roo.project.DependencyType;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Repository;
import org.springframework.roo.support.util.Assert;

/**
 * Implementation of operations this add-on offers.
 * 
 * @author Ats Uiboupin
 */
@Component
// Using Apache Felix annotations to register command class in the Roo container
@Service
public class FieldconstantsOperationsImpl implements FieldconstantsOperations {
  private Logger log = Logger.getLogger(getClass().getName());

  private static final String GROUP_ID = "ee.uiboupin.ats.roo.addon";
  private static final String ARTIFACT_ID = "fieldconstants";
  private static final String VERSION = "1.0.0.RC2";
  private static final String PROJECT_NAME = "spring-roo-addon-field-constants";

  /**
   * MetadataService offers access to Roo's metadata model, use it to retrieve any available metadata by its MID
   */
  @Reference
  private MetadataService metadataService;

  /**
   * Use the PhysicalTypeMetadataProvider to access information about a physical type in the project
   */
  @Reference
  private PhysicalTypeMetadataProvider physicalTypeMetadataProvider;

  /**
   * Use ProjectOperations to install new dependencies, plugins, properties, etc into the project configuration
   */
  @Reference
  private ProjectOperations projectOperations;

  /**
   * Use TypeLocationService to find types which are annotated with a given annotation in the project
   */
  @Reference
  private TypeLocationService typeLocationService;

  public boolean isSetupCommandAvailable() {
    return isProjectAvailable() && !isAddonDependencyAddedInner();
  }

  public boolean isAnnotatingCommandsAvailable() {
    return isProjectAvailable() && isAddonDependencyAddedInner();
  }

  private boolean isProjectAvailable() {
    return projectOperations.isProjectAvailable();
  }

  private boolean isAddonDependencyAddedInner() {
    Set<Dependency> dependencies = projectOperations.getProjectMetadata().getDependencies();
    for (Dependency dependency : dependencies) {
      if (GROUP_ID.equals(dependency.getGroupId())) {
        if (ARTIFACT_ID.equals(dependency.getArtifactId())) {
          String dependencyVersion = dependency.getVersion();
          if (VERSION.equals(dependencyVersion)) {
          } else {
            log.warning("FieldConstants roo addon version installed to ROO shell doesn't match version used by the project. \n\tVersion of the addon installed to ROO: '" + VERSION
                + "',\n\tVersion of the addon that the project is depending on: " + dependencyVersion
                + "\n\tIt is encouraged to update either addon or dependency(which ever is not up to date)");
          }
          DependencyScope scope = dependency.getScope();
          if (scope != DependencyScope.PROVIDED) {
            log.warning("FieldConstants roo addon dependency should be with provided not with" + (scope == null ? "out" : " " + scope.toString().toLowerCase())
                + " scope - You don't need it for compiling nor at runtime");
          }
          return true;
        } else {
          log.fine("found correct group, but not expected artifact '" + ARTIFACT_ID + "'");
        }
      }
    }
    return false;
  }

  public void annotateType(JavaType javaType, String innerClassName, boolean includeSuperClasses) {
    // Use Roo's Assert type for null checks
    Assert.notNull(javaType, "Java type required");

    // Retrieve metadata for the Java source type the annotation is being added to
    String id = physicalTypeMetadataProvider.findIdentifier(javaType);
    if (id == null) {
      throw new IllegalArgumentException("Cannot locate source for '" + javaType.getFullyQualifiedTypeName() + "'");
    }

    // Obtain the physical type and itd mutable details
    PhysicalTypeMetadata physicalTypeMetadata = (PhysicalTypeMetadata) metadataService.get(id);
    Assert.notNull(physicalTypeMetadata, "Java source code unavailable for type " + PhysicalTypeIdentifier.getFriendlyName(id));

    // Obtain physical type details for the target type
    PhysicalTypeDetails physicalTypeDetails = physicalTypeMetadata.getMemberHoldingTypeDetails();
    Assert.notNull(physicalTypeDetails, "Java source code details unavailable for type " + PhysicalTypeIdentifier.getFriendlyName(id));

    // Test if the type is an MutableClassOrInterfaceTypeDetails instance so the annotation can be added
    Assert.isInstanceOf(MutableClassOrInterfaceTypeDetails.class, physicalTypeDetails, "Java source code is immutable for type " + PhysicalTypeIdentifier.getFriendlyName(id));
    MutableClassOrInterfaceTypeDetails mutableTypeDetails = (MutableClassOrInterfaceTypeDetails) physicalTypeDetails;

    // Test if the annotation already exists on the target type
    JavaType annotationType = new JavaType(RooFieldconstants.class.getName());
    AnnotationMetadata existingAnnotation = MemberFindingUtils.getAnnotationOfType(mutableTypeDetails.getAnnotations(), annotationType);
    if (existingAnnotation != null) {
      // remove old existing annotation
      mutableTypeDetails.removeTypeAnnotation(annotationType);
    }
    // Create JavaType instance for the add-ons trigger annotation
    JavaType rooRooFieldconstants = annotationType;
    
    // Create Annotation metadata
    AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(rooRooFieldconstants);
    if (innerClassName != null) {
      JavaSymbolName.assertJavaNameLegal(innerClassName);
      annotationBuilder.addStringAttribute("innerClassName", innerClassName);
    }
    if (includeSuperClasses != FieldConstantsAnnotationValues.DEFAULT_INCLUDE_SUPER_CLASSES) {
      annotationBuilder.addBooleanAttribute("includeSuperClasses", includeSuperClasses);
    }
    
    // Add annotation to target type
    mutableTypeDetails.addTypeAnnotation(annotationBuilder.build());
  }

  public void annotateAll(String innerClassName) {
    for (JavaType type : typeLocationService.findTypesWithAnnotation(new JavaType("org.springframework.roo.addon.javabean.RooJavaBean"))) {
      annotateType(type, innerClassName, FieldConstantsAnnotationValues.DEFAULT_INCLUDE_SUPER_CLASSES);
    }
  }

  /** {@inheritDoc} */
  public void setup() {
    // Install the add-on Google code repository needed to get the annotation
    projectOperations.addRepository(new Repository("Fieldconstants Roo add-on repository", "Fieldconstants Roo add-on repository",
        "https://" + PROJECT_NAME + ".googlecode.com/svn/repo"));

    Dependency dependency = new Dependency(GROUP_ID, ARTIFACT_ID, VERSION, DependencyType.JAR, DependencyScope.PROVIDED);
    // add dependency of this roo-addon to the project dependencies
    projectOperations.addDependency(dependency);
  }
}