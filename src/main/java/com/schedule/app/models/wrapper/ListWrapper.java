package com.schedule.app.models.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : Thành Lộc
 * @since : 10/5/2022, Wed
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListWrapper<T> {

    private long total;

    private long currentPage;

    private long maxResult;

    private long totalPage;

    private List<T> data;

}
