package com.oguz.demo.evechargingsessions.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SummaryDto implements Serializable {
    private static final long serialVersionUID = 123L;

    /**
     * Total action count
     */
    private long totalCount;

    /**
     * Started action count
     */
    private long startedCount;

    /**
     * Stopped action count
     */
    private long stoppedCount;

}
