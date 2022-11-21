package com.bloobloom.assessment.model

import com.fasterxml.jackson.annotation.JsonProperty

enum class Status {
    @JsonProperty("Active")
    ACTIVE,
    @JsonProperty("Inactive")
    INACTIVE
}