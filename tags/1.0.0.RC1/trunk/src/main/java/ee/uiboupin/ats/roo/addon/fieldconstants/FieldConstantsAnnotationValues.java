package ee.uiboupin.ats.roo.addon.fieldconstants;

import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AbstractAnnotationValues;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaType;

/**
 * Represents a parsed {@link RooFieldconstants} annotation.
 * 
 * @author Ats Uiboupin
 */
public class FieldConstantsAnnotationValues extends AbstractAnnotationValues {
  public static final String DEFAULT_INNER_CLASS_NAME = "Constants";
  public static final boolean DEFAULT_INCLUDE_SUPER_CLASSES = false;
  @AutoPopulate
  private String innerClassName = DEFAULT_INNER_CLASS_NAME;
  @AutoPopulate
  private boolean includeSuperClasses = DEFAULT_INCLUDE_SUPER_CLASSES;

  public FieldConstantsAnnotationValues(PhysicalTypeMetadata governorPhysicalTypeMetadata) {
    super(governorPhysicalTypeMetadata, new JavaType(RooFieldconstants.class.getName()));
    AutoPopulationUtils.populate(this, annotationMetadata);
  }

  public String getInnerClassName() {
    if (innerClassName == null || innerClassName.trim().length() == 0) {
      innerClassName = DEFAULT_INNER_CLASS_NAME;
    }
    return innerClassName;
  }

  public boolean isIncludeSuperClasses() {
    return includeSuperClasses;
  }
}
