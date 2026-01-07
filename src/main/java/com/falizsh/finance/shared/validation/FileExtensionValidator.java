package com.falizsh.finance.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileExtensionValidator implements ConstraintValidator<FileExtensions, MultipartFile> {

    private String[] allowedExtensions;

    @Override
    public void initialize(FileExtensions constraintAnnotation) {
        this.allowedExtensions = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        if (file == null || file.isEmpty()) {
            return true;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }

        for (String extension : allowedExtensions) {
            if (originalFilename.toLowerCase().endsWith(extension.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
