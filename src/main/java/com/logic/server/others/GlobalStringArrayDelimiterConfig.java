package com.logic.server.others;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

@ControllerAdvice
public class GlobalStringArrayDelimiterConfig {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String[].class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isEmpty()) {
                    setValue(new String[0]);
                } else {
                    setValue(text.split(";"));
                }
            }
        });
    }
}