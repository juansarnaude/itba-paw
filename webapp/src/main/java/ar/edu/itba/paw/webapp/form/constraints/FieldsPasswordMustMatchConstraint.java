package ar.edu.itba.paw.webapp.form.constraints;

import ar.edu.itba.paw.webapp.form.RegisterForm;
import ar.edu.itba.paw.webapp.form.annotations.FieldsPasswordMustMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldsPasswordMustMatchConstraint implements ConstraintValidator<FieldsPasswordMustMatch, Object> {

    @Override
    public void initialize(FieldsPasswordMustMatch fieldsPasswordMustMatch){
    }

    @Override
    public boolean isValid(Object aux, ConstraintValidatorContext constraintValidatorContext){
        RegisterForm registerForm = (RegisterForm) aux;
        return registerForm.getPassword().equals(registerForm.getRepeatPassword());
    }
}
