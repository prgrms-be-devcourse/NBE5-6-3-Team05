package com.grepp.moodlink.app.controller.api.like.payload;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ToggleResponse {
    private boolean status;

    public ToggleResponse(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }
}
