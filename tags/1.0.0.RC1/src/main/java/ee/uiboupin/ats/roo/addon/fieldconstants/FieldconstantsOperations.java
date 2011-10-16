package ee.uiboupin.ats.roo.addon.fieldconstants;

import org.springframework.roo.model.JavaType;

/**
 * Interface of operations this add-on offers.
 * 
 * @author Ats Uiboupin
 */
public interface FieldconstantsOperations {

  /**
   * Indicate commands should be available
   * 
   * @return true if project is created and this add-on is set up
   */
  boolean isAnnotatingCommandsAvailable();

  /**
   * @return true if project is created and dependency of this add-on is not jet added to the project
   */
  boolean isSetupCommandAvailable();

  /**
   * Annotate the provided Java type with the trigger of this add-on
   * 
   * @param type - class that should be annotated with {@link RooFieldconstants} annotation
   * @param innerClassName - optional name for the inner class to be created by this plugin
   * @param includeSuperClasses - 
   */
  void annotateType(JavaType type, String innerClassName, boolean includeSuperClasses);

  /**
   * Annotate all Java types with the trigger of this add-on
   * 
   * @param innerClassName - optional name for the inner class to be created by this plugin
   */
  void annotateAll(String innerClassName);

  /**
   * Setup add-on dependencies
   */
  void setup();
}