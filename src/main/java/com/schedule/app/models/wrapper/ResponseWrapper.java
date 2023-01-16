package com.schedule.app.models.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : Thành Lộc
 * @since : 10/5/2022, Wed
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ResponseWrapper<T> {

    protected int status;

    protected String message;

    protected T data;

}
