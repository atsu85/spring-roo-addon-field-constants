package ee.uiboupin.ats.roo.addon.fieldconstants;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;

/**
 * Commands provided by this addon
 * 
 * @author Ats Uiboupin
 */
@Component
// Using Apache Felix annotations to register command class in the Roo container
@Service
public class FieldconstantsCommands implements CommandMarker {

  @Reference
  private FieldconstantsOperations operations;

  /**
   * Decides when specified commands should be available in the ROO shell
   * 
   * @return true if commands that add annotations to types should be visible at this stage of the project
   */
  @CliAvailabilityIndicator({ "fieldconstants add", "fieldconstants all" })
  public boolean isCommandAvailable() {
    return operations.isAnnotatingCommandsAvailable();
  }

  /**
   * @return true if project is set up and setup command is not jet executed
   */
  @CliAvailabilityIndicator({ "fieldconstants setup" })
  public boolean isSetupCommandAvailable() {
    return operations.isSetupCommandAvailable();
  }

  /**
   * This method registers a command with the Roo shell. It also offers a mandatory command attribute.
   * 
   * @param type
   */
  @CliCommand(value = "fieldconstants add", help = "Add @RooFieldconstants annotation to class specified with 'type' option")
  public void add(
      @CliOption(key = "type", mandatory = true, help = "The java type to apply this annotation to. Roo will generate inner classes to classes annotated by @RooFieldconstants that will contain constants for each field of the annotated class") JavaType target,
      @CliOption(key = "innerClassName", help = "Name of the inner class that will be generated for each class that is annotated by @RooFieldconstants") String innerClassName,
      @CliOption(key = "includeSuperClasses", unspecifiedDefaultValue=FieldConstantsAnnotationValues.DEFAULT_INCLUDE_SUPER_CLASSES+"",  help = "Should constants from parent classes be included in generated code?") boolean includeSuperClasses) {
    operations.annotateType(target, innerClassName, includeSuperClasses);
  }

  /**
   * This method registers a command with the Roo shell. It has no command attribute.
   * 
   */
  @CliCommand(value = "fieldconstants all", help = "Add @RooFieldconstants annotation to all classes in the project")
  public void all(
      @CliOption(key = "innerClassName", help = "Name of the inner class that will be generated for each class that is annotated by @RooFieldconstants") String innerClassName) {
    operations.annotateAll(innerClassName);
  }

  /**
   * This method registers a command with the Roo shell. It has no command attribute.
   * 
   */
  @CliCommand(value = "fieldconstants setup", help = "Setup Fieldconstants addon")
  public void setup() {
    operations.setup();
  }
}