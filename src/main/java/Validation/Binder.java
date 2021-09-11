package Validation;

/**
 * Basic binder class based on chain of responsibility pattern
 * and Binder's vaodin class ( https://vaadin.com/api/platform/14.6.8/com/vaadin/flow/data/binder/Binder.html )
 */
public class Binder {

    Object field = null;

    public Binder(){

    }

    private Binder(Object field){
        this.field = field;
    }

    /**
     * Prepares the field to be processed
     * @param field the field that will be processed by Binder
     * @return a Binder with the field to be validated/transformed
     */
    public Binder forField(String field) {
        return new Binder(field);
    }

    /**
     * @param validator has the condition the field must meet
     * @return a Binder with valid field according to Validator criteria
     * @throws Exception is field is not valid
     */
    public Binder withValidator(FieldValidator validator) throws Exception {
        if (validator.validate(this.field)) return this;

        throw new Exception("Field is not Valid: < " + this.field + " >\n");
    }

    /**
     *
     * @param converter that will modify the field
     * @return a modified field according to Converter definition
     */
    public Binder withConverter(FieldConverter converter){
        return new Binder(converter.transform(this.field));
    }

    /**
     * Runs a command with the field as a parameter of it
     * @param command that will be executed with the field
     */
    public void execute(Command command){
        command.run(field);
    }
}
