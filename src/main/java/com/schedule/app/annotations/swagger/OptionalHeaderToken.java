package com.schedule.app.annotations.swagger;

import com.schedule.app.utils.Constants;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Parameter(
        in = ParameterIn.HEADER,
        name = Constants.HEADER_TOKEN_NAME,
        schema = @Schema(
                type = "string"
        )
)
public @interface OptionalHeaderToken {
}
