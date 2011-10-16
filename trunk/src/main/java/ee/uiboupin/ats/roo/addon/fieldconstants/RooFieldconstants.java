package ee.uiboupin.ats.roo.addon.fieldconstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Trigger annotation for this add-on.
 * 
 * @author Ats Uiboupin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RooFieldconstants {
  /**
   * @return name of the inner class to be created inside annotated class that should contain constants
   */
  String innerClassName() default FieldConstantsAnnotationValues.DEFAULT_INNER_CLASS_NAME;

  boolean includeSuperClasses() default FieldConstantsAnnotationValues.DEFAULT_INCLUDE_SUPER_CLASSES;

}
